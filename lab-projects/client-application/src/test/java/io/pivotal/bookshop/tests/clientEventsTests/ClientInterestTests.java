package io.pivotal.bookshop.tests.clientEventsTests;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import org.apache.geode.cache.Region;
import org.apache.geode.cache.client.ClientCache;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import io.pivotal.bookshop.dao.BookMasterDAO;
import io.pivotal.bookshop.dao.GemFireClientCacheHelper;
import io.pivotal.bookshop.domain.BookMaster;
import io.pivotal.bookshop.events.BookMasterEntryListener;

public class ClientInterestTests {

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
		books = clientCache.getRegion("BookMaster");
		books.getAttributesMutator().addCacheListener(new BookMasterEntryListener());
	}

	@After
	public void cleanUp() {
		cleanupBook();
		clientCache.close();
	}
	
	@Test
	public void testRegisterInterest() {
		setupBook();
		assertEquals("Failed to register interest on book creation", 1,books.getInterestList().size());
		assertEquals("Failed to properly register interest on key " + key, key,books.getInterestList().get(0));
	}
	
	@Test
	public void testRegisterInterestUpdate() throws InterruptedException {
		setupBook();
		BookMasterDAO dao = new BookMasterDAO(clientCache);
		System.out.println("Run the ClientInterestDriver Java - test will timeout in the next 20 seconds");
		// Depends on running the ClientInterestDriver class
		boolean success = false;
		for (int i=1;i<20;i++) {
		   if (dao.doGet(key).getRetailCost()  > 30.00f) {
			   success = true;
			   break;
		   }
		   Thread.sleep(1000);
		}
		if (! success)
			fail("Test failed - expecting updated value to be pushed to this client.");
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
