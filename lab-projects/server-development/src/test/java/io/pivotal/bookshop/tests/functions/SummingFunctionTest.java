package io.pivotal.bookshop.tests.functions;

import static org.junit.Assert.assertEquals;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;

import org.apache.geode.cache.Region;
import org.apache.geode.cache.client.ClientCache;
import org.apache.geode.cache.client.ClientCacheFactory;
import org.apache.geode.cache.client.ClientRegionFactory;
import org.apache.geode.cache.client.ClientRegionShortcut;
import org.apache.geode.cache.execute.Execution;
import org.apache.geode.cache.execute.FunctionService;
import org.apache.geode.cache.execute.ResultCollector;
import org.apache.geode.pdx.ReflectionBasedAutoSerializer;
import org.junit.Before;
import org.junit.Test;

import io.pivotal.bookshop.domain.BookOrder;
import io.pivotal.bookshop.domain.BookOrderItem;
import io.pivotal.bookshop.domain.OrderKey;

public class SummingFunctionTest {
	ClientCache clientCache;
	Region<OrderKey, BookOrder> bookOrders;

	@Before
	public void initCache() {
		this.clientCache = new ClientCacheFactory().set("name", "DataOperations Client")
				.addPoolLocator("localhost", 10334)
				.setPdxSerializer(new ReflectionBasedAutoSerializer(true, "io.pivotal.bookshop.domain.*"))
				.create();

		ClientRegionFactory<OrderKey, BookOrder> regionFactory = this.clientCache
				.createClientRegionFactory(ClientRegionShortcut.PROXY);
		bookOrders = regionFactory.create("BookOrder");

		bookOrders.removeAll(bookOrders.keySetOnServer());
		populateBookOrders();
	}

	@Test
	// TODO-10: From a terminal/command shell, package files to a JAR file
	// TODO-11: Deploy the jar file from gfsh
	// TODO-12: Re-start the cluster
	// TODO-13: Run the test verifying the function performs as expected
	public void testSummingFunction() {
		Execution execution = FunctionService.onRegion(bookOrders).withArgs("totalPrice")
				.withCollector(new SummingResultCollector());

		ResultCollector rc = execution.execute("GenericSumFunction");

		BigDecimal result = (BigDecimal) rc.getResult();
		assertEquals(new BigDecimal("93.95"), result); // 40.98 + 52.97
	}
	
	private void populateBookOrders()
	{
		OrderKey key1  = new OrderKey(5598, 17699);
		// Order for Kari Powell for book: A Treatise of Treatises
		BookOrder order1 = new BookOrder(17699, new Date(), (float)5.99, new Date(), new ArrayList(), 5598, (float)40.98);
		order1.addOrderItem(new BookOrderItem (1, 123, (float)1, (float)0));
		bookOrders.put(key1, order1);
		
		// Order for Lula Wax   book: A Treatise of Treatises & Clifford the Big Red Dog
		BookOrder order2 = new BookOrder(17700, new Date(), (float)5.99, new Date(), new ArrayList(), 5543, (float)52.97);
		OrderKey key2  = new OrderKey(6024, 17700);
		order2.addOrderItem(new BookOrderItem (1, 123, (float)1, (float)0));
		order2.addOrderItem(new BookOrderItem(2,456, (float)1,(float)0));
		bookOrders.put(key2, order2);
	}

}
