package io.pivotal.bookshop.dao;

import org.apache.geode.cache.client.ClientCache;

import io.pivotal.bookshop.domain.BookOrder;
import io.pivotal.bookshop.keys.OrderKey;

public class BookOrderDAO extends DAOCommon<OrderKey, BookOrder> {

	public BookOrderDAO(ClientCache clientCache) {
		super(clientCache, "BookOrder");
	}


}
