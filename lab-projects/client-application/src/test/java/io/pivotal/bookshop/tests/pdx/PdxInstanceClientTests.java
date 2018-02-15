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

// TODO-10: Run this test to verify correct configuration and implementation of the CustomerDAO
public class PdxInstanceClientTests {
	private ClientCache clientCache;
	private Region<Integer, Customer> customers;

	@Before
	public void setUp() throws Exception {
		this.clientCache = GemFireClientCacheHelper.createPdxEnabled(true);
		customers = clientCache.getRegion("Customer");
		customers.removeAll(customers.keySetOnServer());
		populateCustomers();
	}

	@After
	public void cleanUp() {
		clientCache.close();
	}
		
	@Test
	public void testPdxInstanceConfiguration() {
		CustomerDAO dao = new CustomerDAO(clientCache);
		String customerName = dao.getCustomerName(5542);
		assertNotNull("Failed PdxInstance configuration - PdxInstance not returned", customerName);
		assertEquals("Improper construction of customer name", "Wax, Lula", customerName);
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
