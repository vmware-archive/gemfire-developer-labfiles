package io.pivotal.bookshop.dao;

import java.util.ArrayList;
import java.util.List;

import org.apache.geode.cache.Region;
import org.apache.geode.cache.client.ClientCache;
import org.apache.geode.cache.query.SelectResults;
import org.apache.geode.cache.query.Struct;

import io.pivotal.bookshop.domain.Customer;

public class CustomerDAO extends DAOCommon<Integer, Customer> {
	// Note that there is a generic region now defined in the DAOCommon class that you can use called 'region'.
	// Notice also that type type (key and value) are defined by the types specified above

	public CustomerDAO(ClientCache clientCache) {
		super(clientCache, "Customer");
	}

	/**
	 * Constructs a query string returning all entries in the /Customer region
	 * 
	 * @return A list of all Customer entries
	 */
	public List<Customer> getAll() {
		// TODO-02: Implement the getAll method by by 1) creating the query string and 2) return the results of calling doQuery but convert
		// to a java.util.List
		String queryString1 = "";
		return null;
	}

	/**
	 * Executes a Projection List query having only the customerNumber, firstName and lastName fields
	 * 
	 * @return A list of all Customer entries with only the customerNumber, firstName and lastName fields set
	 */
	public List<Customer> getAllSummary() {
		List<Customer> custList = new ArrayList<Customer>();

		// TODO-04: implement the method to select only the customerNumber, firstName and lastName as a projection list, process
		// the results and return as a list of Customer
		String queryString1 = "";
		
		return custList;

	}

	

}
