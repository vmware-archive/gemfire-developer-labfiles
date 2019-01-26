package io.pivotal.bookshop.tests.clientCacheTests;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import org.apache.geode.cache.EntryExistsException;
import org.apache.geode.cache.Region;
import org.apache.geode.cache.client.ClientCache;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import io.pivotal.bookshop.dao.BookMasterDAO;
import io.pivotal.bookshop.dao.GemFireClientCacheHelper;
import io.pivotal.bookshop.domain.BookMaster;

// TODO-10: When you have completed implementing the BookMasterDAO functionality, run this JUnit test to validate correct implementation
public class BookMasterDAOTests {
	private ClientCache clientCache;
	private Region<Integer, BookMaster> books;
	private static Integer key = 12345;

	/**
	 * Make sure this is run once
	 * 
	 * @throws Exception
	 */
	@Before
	public void setUp() throws Exception {
		this.clientCache = GemFireClientCacheHelper.create();
		this.clientCache.setCopyOnRead(true);
		books = clientCache.getRegion("BookMaster");
		
	}

	@After
	public void cleanUp() {
		cleanupBook();
		clientCache.close();
	}

	@Test
	public void testInsertBook() {
		// Ensure no entry exists before attempting to put the value
		cleanupBook();

		BookMasterDAO dao = new BookMasterDAO(clientCache);
		BookMaster book = new BookMaster(key, "New test book", (float) 28.77, 2014, "Some Author", "All About GemFire");
		dao.doInsert(key, book);

		verify(key, book);

	}

	@Test
	public void test_GetBook() {
		setupBook();
		BookMasterDAO dao = new BookMasterDAO(clientCache);
		BookMaster book = dao.doGet(key);
		verify(key, book);
	}

	@Test
	public void testUpdateBook() {
		setupBook();

		BookMasterDAO dao = new BookMasterDAO(clientCache);
		BookMaster book = dao.doGet(key);
		assertNotNull(book);
		book.setTitle("A new title");
		dao.doUpdate(key, book);
		verify(key, book);
	}

	@Test
	public void testDeleteBook() {
		setupBook();

		BookMasterDAO dao = new BookMasterDAO(clientCache);
		dao.doDelete(key);
		assertNull("BookMaster entry should have been removed for key: " + key, dao.doGet(key));
	}

	@Test(expected = EntryExistsException.class)
	public void testCantInsertSameKey() {
		setupBook();
		BookMasterDAO dao = new BookMasterDAO(clientCache);
		BookMaster book2 = new BookMaster(key, "New test book2", (float) 33.77, 2014, "Some Author2",
				"All About GemFire2");
		dao.doInsert(key, book2);

	}


	/**
	 * Verify entry with given key matches the book object passed in
	 * 
	 * @param key Key to validate entry for
	 * @param book Book expected to match entry with given key
	 */
	private void verify(Integer key, BookMaster book) {
		BookMaster storedBook = books.get(key);
		assertNotNull("Failed to fetch book for key: " + key, storedBook);
		assertEquals("Stored book does not match original book", book, storedBook);
	}

	/**
	 * Ensure book entry is not in GemFire BookMaster region
	 */
	private void cleanupBook() {
		books.remove(key);
	}

	/**
	 * Ensure book entry is in GemFire BookMaster region
	 */
	private void setupBook() {
		BookMaster book = new BookMaster(key, "New test book", (float) 28.77, 2014, "Some Author", "All About GemFire");
		books.put(key, book);

	}

}
