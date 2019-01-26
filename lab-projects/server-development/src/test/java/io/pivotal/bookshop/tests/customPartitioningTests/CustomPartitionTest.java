package io.pivotal.bookshop.tests.customPartitioningTests;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.util.ArrayList;
import java.util.Date;
import java.util.Map.Entry;

import org.apache.geode.cache.Region;
import org.apache.geode.cache.client.ClientCache;
import org.apache.geode.cache.client.ClientCacheFactory;
import org.apache.geode.cache.client.ClientRegionFactory;
import org.apache.geode.cache.client.ClientRegionShortcut;
import org.apache.geode.cache.execute.Execution;
import org.apache.geode.cache.execute.FunctionService;
import org.apache.geode.cache.execute.ResultCollector;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import io.pivotal.bookshop.domain.BookOrder;
import io.pivotal.bookshop.domain.BookOrderItem;
import io.pivotal.bookshop.domain.Customer;
import io.pivotal.bookshop.keys.OrderKey;

// TODO-03: Run this test to verify correct implementation and deployment of the CustomerParitionResolver
public class CustomPartitionTest {

	private ClientCache cache;
	private Region<Integer, Customer> customers;
	private Region<OrderKey, BookOrder> bookOrders;

	@Before
	public void setup() {
		this.cache = new ClientCacheFactory()
				.set("name", "DataOperations Client")
				.addPoolLocator("localhost", 10334)
				.create();

		ClientRegionFactory<Integer, Customer> customerRegionFactory = cache
				.createClientRegionFactory(ClientRegionShortcut.PROXY);
		customers = customerRegionFactory.create("Customer");
		ClientRegionFactory<OrderKey, BookOrder> bookOrderRegionFactory = cache
				.createClientRegionFactory(ClientRegionShortcut.PROXY);
		bookOrders = bookOrderRegionFactory.create("BookOrder");
		
		customers.removeAll(customers.keySetOnServer());
		populateCustomers();

		bookOrders.removeAll(bookOrders.keySetOnServer());
		populateBookOrders();

	}
	
	@After
	public void tearDown() {
		cache.close();
	}
	
	@Test
	public void partitionTest() {		
		RegionBucketInfo customerBucketInfo = executePRB(customers);
		RegionBucketInfo bookOrderBucketInfo = executePRB(bookOrders);

		// For every BookOrder bucket, find corresponding bucket on Customer and verify it's on the same member
		for (Entry<Integer, String> entry : bookOrderBucketInfo.getPrimaryBucketInfo().entrySet()) {
			String customerBucketNumber = customerBucketInfo.getPrimaryBucketInfo().get(entry.getKey());
			assertNotNull("It appears the custom PartitionResolver has not been set up or has not been implemented properly", customerBucketNumber);
			assertEquals("Bucket for BookOrder is not co-located with Bucket for Customer", entry.getValue(), customerBucketNumber);
		}
	}
	
	@SuppressWarnings("unchecked")
	private RegionBucketInfo executePRB(Region r) { 
		Execution<?, Object[], RegionBucketInfo> execution = FunctionService.onRegion(r).withCollector(new BucketAlignmentResultCollector());
		ResultCollector<Object[], RegionBucketInfo> collector = execution.execute("PRBFunction");
		return  collector.getResult();
	}
	
	private void populateBookOrders()
	{
		OrderKey key1  = new OrderKey(5598, 17699);
		// Order for Kari Powell for book: A Treatise of Treatises
		BookOrder order1 = new BookOrder(17699, new Date(), (float)5.99, new Date(), new ArrayList(), 5598, (float)40.98);
		order1.addOrderItem(new BookOrderItem (1, 123, 1, (float)0));
		bookOrders.put(key1, order1);
		
		// Order for Lula Wax   book: A Treatise of Treatises & Clifford the Big Red Dog
		BookOrder order2 = new BookOrder(17700, new Date(), (float)5.99, new Date(), new ArrayList(), 5543, (float)52.97);
		OrderKey key2  = new OrderKey(6024, 17700);
		order2.addOrderItem(new BookOrderItem (1, 123, 1, (float)0));
		order2.addOrderItem(new BookOrderItem(2,456, 1,(float)0));
		bookOrders.put(key2, order2);
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

}
