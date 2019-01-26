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

// TODO-07: Run this test 
public class PdxVersionedClientTests {
	private static final int versionedCustomerKey = 9999;
	private ClientCache clientCache;
	private Region<Integer, Customer> customers;

	@Before
	public void setUp() throws Exception {
		this.clientCache = GemFireClientCacheHelper.createPdxEnabled(false);
		customers = clientCache.getRegion("Customer");
		customers.removeAll(customers.keySet());
		populateCustomers();
	}

	@After
	public void cleanUp() {
		clientCache.close();
	}
		
	@Test
	public void testVersionedExistingCustomer() {
		CustomerDAO dao = new CustomerDAO(clientCache);
		Customer lula = dao.doGet(5542);
		assertNull("Expected existing customer 'Lula Wax' to have a null telephone number",lula.getTelephoneNumber());
	}

	@Test
	public void testVersionedNewCustomer() {
		populateVersionedCustomer();
		CustomerDAO dao = new CustomerDAO(clientCache);
		Customer versionedCustomer = dao.doGet(versionedCustomerKey);
		assertNotNull("Expected versioned customer to have a non-null telephone number.",versionedCustomer.getTelephoneNumber());
		cleanupVersionedCustomer();
	}
	
	private void populateVersionedCustomer() {

		Customer cust1 = new Customer(9999, "Modified", "Customer", ""+44444);
		cust1.addOrder(17699);
		cust1.setTelephoneNumber("5035551212");
		customers.put(versionedCustomerKey, cust1);
	}
	
	private void cleanupVersionedCustomer() {
		customers.remove(versionedCustomerKey);
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
