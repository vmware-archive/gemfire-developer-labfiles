package io.pivotal.bookshop.tests.transactions;


import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import org.apache.geode.cache.EntryExistsException;
import org.apache.geode.cache.Region;
import org.apache.geode.cache.TransactionDataNotColocatedException;
import org.apache.geode.cache.client.ClientCache;
import org.apache.geode.cache.client.ClientCacheFactory;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import io.pivotal.bookshop.dao.GemFireClientCacheHelper;
import io.pivotal.bookshop.domain.BookOrder;
import io.pivotal.bookshop.domain.BookOrderItem;
import io.pivotal.bookshop.domain.Customer;
import io.pivotal.bookshop.keys.OrderKey;
import io.pivotal.bookshop.services.TransactionalService;

// TODO-01: Open the test harness and observe the two transaction tests to be run
public class TransactionalTests {
	private ClientCache cache;
	private Region<Integer, BookOrder> order_region;
	private Region<Integer, Customer> customer_region;

	@Before
	public void setUp() throws Exception {
		this.cache = GemFireClientCacheHelper.createPdxEnabled(false);

		order_region = cache.getRegion("BookOrder");
		customer_region = cache.getRegion("Customer");

		// Force the region data to be removed so log will only show true
		// updates. The region.clear() method doesn't
		// work for clients calling clear() on partitioned regions.
		order_region.removeAll(order_region.keySetOnServer());
		customer_region.removeAll(customer_region.keySetOnServer());

		populateCustomers();
		populateBookOrders();
	}

	@After
	public void cleanup() {
		cache.close();
	}
	
	@Test
	public void testTransactionCommit() {
		TransactionalService svc = new TransactionalService(cache);

		// Parameterized data for test
		Integer customerNumber = 5598;
		Integer orderNumber = 17701;
		OrderKey orderKey = new OrderKey(customerNumber, orderNumber);

		BookOrder order1 = new BookOrder(orderNumber, new Date(), (float)5.99, new Date(), new ArrayList(), 5598, (float)40.98);
		order1.addOrderItem(new BookOrderItem (1, 123, 1, (float)0));
		svc.addOrder(orderKey, order1);

		// Verify that changes were made in both regions
		Customer cust = customer_region.get(customerNumber);
		BookOrder order = order_region.get(orderKey);

		assertNotNull("Failed to insert order",order);
		assertTrue("Customer was not successfully updated with new Order Number",cust.getMyBookOrders().contains(order.getOrderNumber()));

	}

	@Test()
	public void testTransactionRollback() {
		TransactionalService svc = new TransactionalService(cache);

		// Parameterized data for test
		Integer customerNumber = 5598;
		Integer orderNumber = 17699;
		OrderKey orderKey = new OrderKey(customerNumber, orderNumber);

		// Attempt to insert a duplicate order and key
		BookOrder order1 = new BookOrder(orderNumber, new Date(), (float)5.99, new Date(), new ArrayList(), 5598, (float)40.98);
		order1.addOrderItem(new BookOrderItem (1, 123, 1, (float)0));
		try {
			svc.addOrder(orderKey, order1);
			fail("Should have thrown an EntryExistsException");
		} catch (EntryExistsException e) {
			Customer cust = customer_region.get(customerNumber);
			assertNotNull("Failed to fetch Customer (should never happen)", cust);
			assertEquals("Incorrect number of orders in 'myBookOrders'- transaction may not have rolled back", 1, cust.getMyBookOrders().size());
			
		}
	}


	public void populateCustomers()
	{
		Region<Integer, Customer> customers = cache.getRegion("Customer");
		
		Customer cust1 = new Customer(5598, "Kari", "Powell", ""+44444);
		cust1.addOrder(17699);
		customers.put(cust1.getCustomerNumber(), cust1);
		
		Customer cust2 = new Customer (5542, "Lula", "Wax", ""+12345);
		customers.put(cust2.getCustomerNumber(), cust2);
		
		Customer cust3 = new Customer (6024, "Trenton", "Garcia", ""+88888);
		cust3.addOrder(17700);
		customers.put(cust3.getCustomerNumber(), cust3);

	}
	public void populateBookOrders()
	{
		Region<OrderKey, BookOrder> orders = cache.getRegion("BookOrder");
		// Order for Kari Powell for book: A Treatise of Treatises
		OrderKey key1  = new OrderKey(5598, 17699);
		BookOrder order1 = new BookOrder(17699, new Date(), (float)5.99, new Date(), new ArrayList(), 5598, (float)40.98);
		order1.addOrderItem(new BookOrderItem (1, 123, 1, (float)0));
		orders.put(key1, order1);
		
		// Order for Lula Wax   book: A Treatise of Treatises & Clifford the Big Red Dog
		OrderKey key2  = new OrderKey(6024, 17700);
		BookOrder order2 = new BookOrder(17700, new Date(), (float)5.99, new Date(), new ArrayList(), 5543, (float)52.97);
		order2.addOrderItem(new BookOrderItem (1, 123, 1, (float)0));
		order2.addOrderItem(new BookOrderItem(2,456, 1,(float)0));
		orders.put(key2, order2);
	}
	
}
