package io.pivotal.bookshop.tests.expiration;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.apache.geode.cache.Region;
import org.apache.geode.cache.client.ClientCache;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import io.pivotal.bookshop.dao.CustomerDAO;
import io.pivotal.bookshop.dao.GemFireClientCacheHelper;
import io.pivotal.bookshop.domain.Customer;

// TODO-01: Open this test and examine the setup and the test itself
// TODO-04: Run the test
public class ExpirationTests {
	static Integer sleepMillis = 10000;
	private ClientCache clientCache;
	private Region<Integer, Customer> customers;
	private static Integer key = 12345;

	/**
	 * 
	 * @throws Exception
	 */
	@Before
	public void setUp() throws Exception {
		this.clientCache = GemFireClientCacheHelper.createPdxEnabledCachingProxy(false);
		clientCache.setCopyOnRead(true);
		customers = clientCache.getRegion("Customer");
		customers.removeAll(customers.keySetOnServer());
	}

	@After
	public void cleanUp() {
		clientCache.close();
	}

	@Test
	public void testExpiration() throws Exception {
		CustomerDAO dao = new CustomerDAO(clientCache);
		Customer customer = new Customer(key,"Test","Customer");
		
		// Call helper class to enable eviction per student configuration
		GemFireClientCacheHelper.enableEviction(customers, sleepMillis -1000);

		// Insert entry and ensure it exists in client and server regions
		dao.doInsert(key, customer);
		assertTrue("Failed to write entry",customers.containsKey(key));
		assertTrue("Failed to write entry",customers.containsKeyOnServer(key));

		// After sleeping, entry should no longer exist on client but should still be on the server
		Thread.sleep(sleepMillis);
		assertFalse("Failed to expire entry",customers.containsKey(key));
		assertTrue("Should NOT have removed entry from server",customers.containsKeyOnServer(key));

	}
	
}
