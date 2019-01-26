package io.pivotal.bookshop.dao;

import org.apache.geode.cache.Region;
import org.apache.geode.cache.client.ClientCache;

import io.pivotal.bookshop.domain.Customer;

// TODO-11: Implement the appropriate methods of the CustomerDAO class as you did with BookMasterDAO
public class CustomerDAO extends DAOCommon<Integer,Customer> {
	private Region<Integer, Customer> customers;

	public CustomerDAO(ClientCache clientCache) {
		super(clientCache);
		this.customers = clientCache.getRegion("Customer");
	}

	@Override
	public void doInsert(Integer key, Customer entry) {
		customers.create(key, entry);
		
	}

	@Override
	public Customer doGet(Integer key) {
		return customers.get(key);	}

	@Override
	public void doUpdate(Integer key, Customer entry) {
		customers.put(key, entry);
	}

	@Override
	public void doDelete(Integer key) {
		customers.destroy(key);
	}

	


}
