package io.pivotal.bookshop.tests.clientCacheTests;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import java.util.List;

import org.apache.geode.cache.EntryExistsException;
import org.apache.geode.cache.Region;
import org.apache.geode.cache.client.ClientCache;
import org.apache.geode.cache.client.ClientRegionFactory;
import org.apache.geode.cache.client.ClientRegionShortcut;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import io.pivotal.bookshop.dao.BookMasterDAO;
import io.pivotal.bookshop.dao.GemFireClientCacheHelper;
import io.pivotal.bookshop.domain.BookMaster;
import io.pivotal.bookshop.domain.InventoryItem;

// TODO-07: When you have completed implementing the doJoinQuery() in the BookMasterDAO, run this JUnit test to validate correct implementation
public class BookMasterDAOTests {
	private ClientCache clientCache;
	private Region<Integer, BookMaster> books;
	private Region <Integer, InventoryItem> inventory;
	private static Integer key = 12345;

	/**
	 * Make sure this is run once
	 * 
	 * @throws Exception
	 */
	@Before
	public void setUp() throws Exception {
		this.clientCache = GemFireClientCacheHelper.create();
		clientCache.setCopyOnRead(true);
		books = clientCache.getRegion("BookMaster");
		ClientRegionFactory<Integer, InventoryItem> inventoryRegionFactory = clientCache.createClientRegionFactory(ClientRegionShortcut.PROXY);
		inventory = inventoryRegionFactory.create("InventoryItem");
		inventory.removeAll(inventory.keySetOnServer());
		populateInventory();
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

	@Test
	public void testJoinQuery() {
		BookMasterDAO dao = new BookMasterDAO(clientCache);
		List<BookMaster> results = dao.findLowQuantityBooks();
		// Assert that only one customer item is returned
		assertEquals("Join query should return only one Book",1, results.size());
		// Assert that the customer returned has last name = 'Wax'
		assertEquals("Join query should have returned Book with author 'Clarence Meeks'","Clarence Meeks",results.get(0).getAuthor());

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
	
	private void populateBooks() {
		BookMaster book = new BookMaster(123, "Run on sentences and drivel on all things mundane",
				(float) 34.99, 2011, "Daisy Mae West", "A Treatise of Treatises");
		books.put(123, book);
		BookMaster book2 = new BookMaster(456, "A book about a dog",
				(float) 11.99, 1971, "Clarence Meeks", "Clifford the Big Red Dog");
		books.put(456, book2);
		BookMaster book3 = new BookMaster(789, "Theoretical information about the structure of Operating Systems",
				(float) 59.99, 2011, "Jim Heavisides", "Operating Systems: An Introduction");
		books.put(789, book3);
	
	}

	private void populateInventory() {

		InventoryItem item1 = new InventoryItem(123, (float) 12.50, (float) 34.99,  12, "BookRUs", "Seattle");
		inventory.put(123, item1);

		InventoryItem item2 = new InventoryItem(456, (float) 12.50, (float) 11.99, 1, "BookRUs", "Seattle");
		inventory.put(456, item2);

	
	}
}
