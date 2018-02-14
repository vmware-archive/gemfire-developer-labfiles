package io.pivotal.bookshop.tests.clientCacheTests;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import org.apache.geode.cache.Region;
import org.apache.geode.cache.client.ClientCache;
import org.junit.Before;
import org.junit.Test;

import io.pivotal.bookshop.dao.GemFireClientCacheHelper;
import io.pivotal.bookshop.domain.BookMaster;
import io.pivotal.bookshop.domain.Customer;

// TODO-04: Run the test suite and verify the test passes
// TODO-05: Open the gemfire.properties file and modify the log-level
public class ClientCacheTests {

	private Region<Integer, BookMaster> books;
	private Region <Integer, Customer> customers;
	private ClientCache clientCache;

	@Before
	public void setUp() throws Exception {		
		this.clientCache =  GemFireClientCacheHelper.create();
		books = clientCache.getRegion("BookMaster");
		customers = clientCache.getRegion("Customer");
		
		assertNotNull("Failed to fetch BookMaster region", books);
		assertNotNull("Failed to fetch Customer region", customers);
	}

	@Test
	public void testFetchFromProxyRegions() {
		Customer customer = customers.get(5598);
		assertNotNull("Customer shouldn't be null", customer);
		assertEquals("Failed to fetch the correct customer object", "Kari", customer.getFirstName());

		BookMaster book = books.get(456);
		assertNotNull("Book shouldn't be null", book);
		assertEquals("Failed to fetch the correct book", "Clifford the Big Red Dog", book.getTitle());
	}
	

}
