package io.pivotal.bookshop.tests.clientEventsTests;

import static org.junit.Assert.assertEquals;

import org.apache.geode.GemFireCheckedException;
import org.apache.geode.GemFireException;
import org.apache.geode.cache.Region;
import org.apache.geode.cache.client.ClientCache;
import org.apache.geode.cache.client.ClientRegionFactory;
import org.apache.geode.cache.client.ClientRegionShortcut;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import io.pivotal.bookshop.dao.GemFireClientCacheHelper;
import io.pivotal.bookshop.domain.BookMaster;
import io.pivotal.bookshop.events.BookMasterCqHelper;

public class ContinuousQueryTests {
	private ClientCache clientCache;
	private Region<Integer, BookMaster> books;
	private Region<Integer, BookMaster> localRegion;
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
		// Create a local region to contain matching Cq events
		ClientRegionFactory customerRegionFactory = clientCache.createClientRegionFactory(ClientRegionShortcut.LOCAL);
		localRegion = customerRegionFactory.create("CqMatches");
	}

	@After
	public void cleanUp() {
		cleanupBook();
		clientCache.close();
	}

	@Test
	public void testCqNoMatches() throws GemFireException, GemFireCheckedException, InterruptedException {
		BookMasterCqHelper.registerCq(localRegion);
		BookMaster book = new BookMaster(key, "New test book", (float) 28.77, 2014, "Some Author", "All About GemFire");
		books.put(key, book);
		// Allow time for update to happen via CQ
		Thread.sleep(1000);
		assertEquals("Failed non-matching CQ evaluation ", 0, this.localRegion.keySet().size());
	}

	@Test
	public void testCqSuccess() throws InterruptedException, GemFireException, GemFireCheckedException {
		BookMasterCqHelper.registerCq(localRegion);
		BookMaster book = new BookMaster(key, "New test book", (float) 55.88, 2014, "Some Author", "All About GemFire");
		books.put(key, book);
		// Allow time for update to happen via CQ
		Thread.sleep(1000);
		assertEquals("Failed successful CQ evaluation", 1, this.localRegion.keySet().size());
		BookMaster matchingBooks = localRegion.get(localRegion.keySet().iterator().next());
		assertEquals("Failed successful CQ evaluation", book.getItemNumber(), matchingBooks.getItemNumber());
	}
	
	/**
	 * Ensure book entry is not in GemFire BookMaster region
	 */
	private void cleanupBook() {
		books.remove(key);
	}
	
}
