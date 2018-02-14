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

import io.pivotal.bookshop.dao.CustomerDAO;
import io.pivotal.bookshop.dao.GemFireClientCacheHelper;
import io.pivotal.bookshop.domain.Customer;

// TODO-12: When you have completed implementing the CustomerDAO functionality, run this JUnit test to validate correct implementation
public class CustomerDAOTests {
	private ClientCache clientCache;
	private Region<Integer, Customer> customers;
	private static Integer key = 12345;

	/**
	 * Make sure this is run once
	 * 
	 * @throws Exception
	 */
	@Before
	public void setUp() throws Exception {
		this.clientCache = GemFireClientCacheHelper.create();
		customers = clientCache.getRegion("Customer");
		
	}

	@After
	public void cleanUp() {
		cleanupCustomer();
		clientCache.close();
	}

	@Test
	public void testInsertCustomer() {
		// Ensure no entry exists before attempting to put the value
		cleanupCustomer();

		CustomerDAO dao = new CustomerDAO(clientCache);
		Customer customer = new Customer(key,"Test","Customer");
		dao.doInsert(key, customer);

		verify(key, customer);

	}

	@Test
	public void test_GetCustomer() {
		setupCustomer();
		CustomerDAO dao = new CustomerDAO(clientCache);
		Customer cust = dao.doGet(key);
		verify(key, cust);
	}

	@Test
	public void testUpdateCustomer() {
		setupCustomer();

		CustomerDAO dao = new CustomerDAO(clientCache);
		Customer cust = dao.doGet(key);
		assertNotNull(cust);
		cust.setFirstName("New name");
		dao.doUpdate(key, cust);
		verify(key, cust);
	}

	@Test
	public void testDeleteCustomer() {
		setupCustomer();

		CustomerDAO dao = new CustomerDAO(clientCache);
		dao.doDelete(key);
		assertNull("Customer entry should have been removed for key: " + key, dao.doGet(key));
	}

	@Test(expected = EntryExistsException.class)
	public void testCantInsertSameKey() {
		setupCustomer();
		CustomerDAO dao = new CustomerDAO(clientCache);
		Customer cust2 = new Customer(key,"New","Customer");
		dao.doInsert(key, cust2);

	}
	
	/**
	 * Verify entry with given key matches the book object passed in
	 * 
	 * @param key Key to validate entry for
	 * @param book Book expected to match entry with given key
	 */
	private void verify(Integer key, Customer cust) {
		clientCache.setCopyOnRead(true);
		Customer storedCustomer = customers.get(key);
		assertNotNull("Failed to fetch customer for key: " + key, storedCustomer);
		assertEquals("Stored customer does not match original customer", cust, storedCustomer);
	}

	/**
	 * Ensure book entry is not in GemFire BookMaster region
	 */
	private void cleanupCustomer() {
		customers.remove(key);
	}

	/**
	 * Ensure book entry is in GemFire BookMaster region
	 */
	private void setupCustomer() {
		Customer customer = new Customer(key,"Test","Customer");
		customers.put(key, customer);

	}

}
