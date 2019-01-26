package io.pivotal.bookshop.services;

import org.apache.geode.cache.CacheTransactionManager;
import org.apache.geode.cache.client.ClientCache;

import io.pivotal.bookshop.dao.BookOrderDAO;
import io.pivotal.bookshop.dao.CustomerDAO;
import io.pivotal.bookshop.domain.BookOrder;
import io.pivotal.bookshop.domain.Customer;
import io.pivotal.bookshop.keys.OrderKey;

public class TransactionalService {

	private CacheTransactionManager tx;
	private CustomerDAO customerDao;
	private BookOrderDAO bookOrderDao;

	public TransactionalService(ClientCache cache) {
		customerDao = new CustomerDAO(cache);
		bookOrderDao = new BookOrderDAO(cache);

		tx = cache.getCacheTransactionManager();

	}

	public void addOrder(OrderKey key, BookOrder order) {
		try {
			tx.begin();

			Customer cust = customerDao.doGet(order.getCustomerNumber());
			cust.addOrder(order.getOrderNumber());

			customerDao.doUpdate(key.getCustomerNumber(), cust);
			bookOrderDao.doInsert(key, order);

			tx.commit();
		}
			
		catch (Exception e) {
			tx.rollback();
			throw e;
		}
	}

}
