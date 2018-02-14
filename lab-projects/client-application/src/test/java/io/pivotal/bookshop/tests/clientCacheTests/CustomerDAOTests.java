package io.pivotal.bookshop.tests.clientCacheTests;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.fail;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.geode.cache.EntryExistsException;
import org.apache.geode.cache.Region;
import org.apache.geode.cache.client.ClientCache;
import org.apache.geode.cache.client.ClientRegionFactory;
import org.apache.geode.cache.client.ClientRegionShortcut;
import org.junit.After;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import io.pivotal.bookshop.dao.CustomerDAO;
import io.pivotal.bookshop.dao.GemFireClientCacheHelper;
import io.pivotal.bookshop.domain.BookOrder;
import io.pivotal.bookshop.domain.BookOrderItem;
import io.pivotal.bookshop.domain.Customer;

public class CustomerDAOTests {
	private ClientCache clientCache;
	private Region<Integer, Customer> customers;
	private Region<Integer, BookOrder> orders;
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
		customers = clientCache.getRegion("Customer");
		customers.removeAll(customers.keySetOnServer());
		populateCustomers();
		
		ClientRegionFactory<Integer, BookOrder> orderRegionFactory = clientCache.createClientRegionFactory(ClientRegionShortcut.PROXY);
		orders = orderRegionFactory.create("BookOrder");
		orders.removeAll(orders.keySetOnServer());
		populateBookOrders();
		
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
	public void test_GetBook() {
		setupCustomer();
		CustomerDAO dao = new CustomerDAO(clientCache);
		Customer cust = dao.doGet(key);
		verify(key, cust);
	}

	@Test
	public void testUpdateBook() {
		setupCustomer();

		CustomerDAO dao = new CustomerDAO(clientCache);
		Customer cust = dao.doGet(key);
		assertNotNull(cust);
		cust.setFirstName("New name");
		dao.doUpdate(key, cust);
		verify(key, cust);
	}

	@Test
	public void testDeleteBook() {
		setupCustomer();

		CustomerDAO dao = new CustomerDAO(clientCache);
		dao.doDelete(key);
		assertNull("BookMaster entry should have been removed for key: " + key, dao.doGet(key));
	}

	@Test(expected = EntryExistsException.class)
	public void testCantInsertSameKey() {
		setupCustomer();
		CustomerDAO dao = new CustomerDAO(clientCache);
		Customer cust2 = new Customer(key,"New","Customer");
		dao.doInsert(key, cust2);

	}

	@Test
	// TODO-03: Run this test to verify the correct implementation of the 'getAll()' method
	public void testGetAll() {
		CustomerDAO dao = new CustomerDAO(clientCache);
		List<Customer> results = dao.getAll();
		assertEquals("Failed basic query: ", 3, results.size());
		// Can't guarantee order but can guarantee that there is an entry with a customer first name of 'Lula'
		boolean found = false;
		for (Customer item : results) {
			if ("Lula".equals(item.getFirstName())) {
				found = true;
				break;
			}
		}
		if (! found) 
			fail("No entry found for Customer First Name = 'Lula'");
	}
	
	@Test 
	// TODO-05: Run this test to verify the correct implementation of the 'getAllSummary()'
	public void testSummaryGetAll() {
		CustomerDAO dao = new CustomerDAO(clientCache);
		List<Customer> results = dao.getAllSummary();
		assertEquals("Failed struct query: ", 3, results.size());
		// Can't guarantee order but can guarantee that there is an entry with a customer first name of 'Lula'
		boolean found = false;
		for (Customer item : results) {
			if ("Lula".equals(item.getFirstName())) {
				found = true;
				break;
			}	
		}
		if (! found) 
			fail("No entry found for Customer First Name = 'Lula'");
	}
	
	@Test @Ignore
	// This will be a demo query that is expected to fail. Demo this ahead of the PartitionedRegion presentation
	// (maybe right after the Query lab)
	public void testJoinQueryOnPartitionedRegion() {
		CustomerDAO dao = new CustomerDAO(clientCache);
		List<Customer> results = dao.findHighPricedCustomerOrders();
		assertEquals("Should only have one result", 1, results.size());
		
	}
	
	/**
	 * Verify entry with given key matches the book object passed in
	 * 
	 * @param key Key to validate entry for
	 * @param book Book expected to match entry with given key
	 */
	private void verify(Integer key, Customer cust) {
		Customer storedCustomer = customers.get(key);
		assertNotNull("Failed to fetch book for key: " + key, storedCustomer);
		assertEquals("Stored book does not match original book", cust, storedCustomer);
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

	private void populateCustomers()
	{		
		Customer cust1 = new Customer(5598, "Kari", "Powell", ""+44444);
		cust1.addOrder(17600);
		customers.put(cust1.getCustomerNumber(), cust1);
		
		Customer cust2 = new Customer (5542, "Lula", "Wax", ""+12345);
		customers.put(cust2.getCustomerNumber(), cust2);
		
		Customer cust3 = new Customer (6024, "Trenton", "Garcia", ""+88888);
		cust3.addOrder(17700);
		customers.put(cust3.getCustomerNumber(), cust3);

	}

	private void populateBookOrders()
	{
		// Order for Kari Powell for book: A Treatise of Treatises
		BookOrder order1 = new BookOrder(17699, new Date(), (float)5.99, new Date(), new ArrayList(), 5598, (float)40.98);
		order1.addOrderItem(new BookOrderItem (1, 123, 1, (float)0));
		orders.put(17699, order1);
		
		// Order for Lula Wax   book: A Treatise of Treatises & Clifford the Big Red Dog
		BookOrder order2 = new BookOrder(17700, new Date(), (float)5.99, new Date(), new ArrayList(), 5543, (float)52.97);
		order2.addOrderItem(new BookOrderItem (1, 123, 1, (float)0));
		order2.addOrderItem(new BookOrderItem(2,456, 1,(float)0));
		orders.put(17700, order2);

	
	}

}
