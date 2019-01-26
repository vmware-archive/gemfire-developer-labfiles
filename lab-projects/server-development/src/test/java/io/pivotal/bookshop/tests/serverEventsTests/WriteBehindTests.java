package io.pivotal.bookshop.tests.serverEventsTests;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import org.apache.geode.cache.Region;
import org.apache.geode.cache.client.ClientCache;
import org.apache.geode.cache.client.ClientCacheFactory;
import org.apache.geode.cache.client.ClientRegionFactory;
import org.apache.geode.cache.client.ClientRegionShortcut;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import io.pivotal.bookshop.domain.BookMaster;
import io.pivotal.bookshop.events.JdbcBookDAO;

public class WriteBehindTests {
	static Integer sleepMillis = 2000;
	ClientCache clientCache;
	Region<Integer, BookMaster> books;
	JdbcBookDAO dao = new JdbcBookDAO();
	BookMaster book1 = new BookMaster(444, "book1", 0.0f, 2000, "a", "book1");
	BookMaster book2 = new BookMaster(555, "book2", 0.0f, 2000, "b", "book2");

	@Before
	public void initCache() {
		this.clientCache = new ClientCacheFactory()
				.set("name", "DataOperations Client")
				.addPoolLocator("localhost", 10334)
				.create();

		ClientRegionFactory<Integer, BookMaster> customerRegionFactory = this.clientCache
				.createClientRegionFactory(ClientRegionShortcut.PROXY);
		books = customerRegionFactory.create("BookMaster");
		// Ensure these entries don't exist in the database
		dao.deleteBook(book1);
		dao.deleteBook(book2);
	}

	@After
	public void closeCache() {
		clientCache.close();
	}

	@Test
	  public void testInsert() throws Exception {
		  this.books.put(book1.getItemNumber(), book1);
		  this.books.put(book2.getItemNumber(), book2);
		  Thread.sleep(sleepMillis);

		  if (! dao.checkExists(book1) || ! dao.checkExists(book2)) 
			  fail("Book: " + book1 + " and/or Book: " + book2 + " were not written to the databasse");
	  }


	@Test
	public void testUpdate() throws Exception {
		this.books.put(book1.getItemNumber(), book1);
		Thread.sleep(sleepMillis);
		BookMaster bookMod = dao.getBook(book1.getItemNumber());
		assertNotNull("Failed to fetch book from the database", bookMod);
		bookMod.setAuthor("new author");
		this.books.put(bookMod.getItemNumber(), bookMod);
		Thread.sleep(sleepMillis);
		assertEquals("Failed to verify update to database", "new author", dao.getBook(bookMod.getItemNumber()).getAuthor());

	}

	@Test
	public void testDelete() throws Exception {
		this.books.put(book1.getItemNumber(), book1);
		Thread.sleep(sleepMillis);
		assertTrue("Update not written to the database", dao.checkExists(book1));
		int itemId = book1.getItemNumber();
		this.books.destroy(book1.getItemNumber());
		Thread.sleep(sleepMillis);
		assertFalse("Failed to delete from the database", dao.checkExists(new BookMaster(itemId,"",0.0f,1900,"","")));
	}

}
