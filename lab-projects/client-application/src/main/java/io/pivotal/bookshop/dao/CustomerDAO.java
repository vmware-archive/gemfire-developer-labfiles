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
		String queryString1 = "SELECT * FROM /Customer";
		return ((SelectResults<Customer>) doQuery(queryString1)).asList();
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
		String queryString1 = "SELECT c.customerNumber, c.firstName, c.lastName FROM /Customer c ";
		for (Struct s : (SelectResults<Struct>) doQuery(queryString1)) {
			Customer c = new Customer((Integer) s.get("customerNumber"), (String) s.get("firstName"),
					(String) s.get("lastName"));
			custList.add(c);
		}
		
		return custList;

	}

	/**
	 * Constructs an equi-join query string that returns Customers where the associated BookOrder has a
	 * totalPrice > $45.00. This is a demo query that is designed to fail due to the fact that both regions
	 * are partitioned regions.
	 * 
	 * @return List of Customer entries having associated BookOrder > $45.00
	 */
	public List<Customer> findHighPricedCustomerOrders() {
		String queryString1 = "select distinct c " + "from /Customer c, /BookOrder o "
				+ "where c.customerNumber = o.customerNumber and  o.totalPrice > 45.00";
		return ((SelectResults<Customer>) doQuery(queryString1)).asList();
	}	

}
