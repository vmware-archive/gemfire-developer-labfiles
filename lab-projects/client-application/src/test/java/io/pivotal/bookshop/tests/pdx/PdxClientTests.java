package io.pivotal.bookshop.tests.pdx;

import static org.junit.Assert.*;

import java.util.List;

import org.apache.geode.cache.Region;
import org.apache.geode.cache.client.ClientCache;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import io.pivotal.bookshop.dao.CustomerDAO;
import io.pivotal.bookshop.dao.GemFireClientCacheHelper;
import io.pivotal.bookshop.domain.Customer;

// TODO-03: Run this set of tests to verify correct client configuration
public class PdxClientTests {
	private ClientCache clientCache;
	private Region<Integer, Customer> customers;

	@Before
	public void setUp() throws Exception {
		this.clientCache = GemFireClientCacheHelper.createPdxEnabled(false);
		customers = clientCache.getRegion("Customer");
		customers.removeAll(customers.keySetOnServer());
		populateCustomers();
	}

	@After
	public void cleanUp() {
		clientCache.close();
	}
	
	@Test
	public void testPdxSerializeation() {
		CustomerDAO dao = new CustomerDAO(clientCache);
		List<Customer> customerList = dao.getAll();
		assertEquals("Failed to query for all customers", 3, customerList.size());
	}
	
	@Test
	// TODO-05: Change the server configuration to 1) remove the domain class jar file and 2) enable PDX Read Serialized true
	public void testServerPdxReadSerialized() {
		CustomerDAO dao = new CustomerDAO(clientCache);
		List<Customer> customers = dao.findCustomersInZip("44444");
		assertEquals("Failed PDX Serialization configuration on the server side", 1, customers.size());
	}

	public void populateCustomers()
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
