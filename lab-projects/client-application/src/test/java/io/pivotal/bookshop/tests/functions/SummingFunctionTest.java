package io.pivotal.bookshop.tests.functions;

import static org.junit.Assert.assertEquals;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;

import org.apache.geode.cache.Region;
import org.apache.geode.cache.client.ClientCache;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import io.pivotal.bookshop.dao.GemFireClientCacheHelper;
import io.pivotal.bookshop.domain.BookOrder;
import io.pivotal.bookshop.domain.BookOrderItem;
import io.pivotal.bookshop.function.FunctionExecutor;
import io.pivotal.bookshop.keys.OrderKey;

public class SummingFunctionTest {
	ClientCache clientCache;
	Region<OrderKey, BookOrder> bookOrders;

	@Before
	public void initCache() {
		this.clientCache = GemFireClientCacheHelper.createPdxEnabled(false);

		bookOrders = clientCache.getRegion("BookOrder");

		bookOrders.removeAll(bookOrders.keySetOnServer());
		populateBookOrders();
	}
	
	@After
	public void cleanup() {
		clientCache.close();
	}

	@Test
	public void testSummingFunction() {
		
		assertEquals("Possible incorrect implementation of the ResultCollector - ",new BigDecimal("93.95"), FunctionExecutor.callSumFunction(bookOrders,"totalPrice")); // 40.98 + 52.97
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

}
