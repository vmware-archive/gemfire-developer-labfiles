package io.pivotal.bookshop.dao;

import java.util.ArrayList;
import java.util.List;

import org.apache.geode.cache.Region;
import org.apache.geode.cache.client.ClientCache;
import org.apache.geode.cache.query.SelectResults;
import org.apache.geode.cache.query.Struct;
import org.apache.geode.pdx.PdxInstance;

import io.pivotal.bookshop.domain.Customer;

public class CustomerDAO extends DAOCommon<Integer, Customer> {
	private Region<Integer, Customer> customers;

	public CustomerDAO(ClientCache clientCache) {
		super(clientCache, "Customer");
		this.customers = clientCache.getRegion("Customer");
	}

	/**
	 * Constructs a query string returning all entries in the /Customer region
	 * 
	 * @return A list of all Customer entries
	 */
	public List<Customer> getAll() {
		String queryString1 = "SELECT * FROM /Customer";
		return ((SelectResults<Customer>) doQuery(queryString1)).asList();
	}

	/**
	 * Executes a Projection List query having only the customerNumber,
	 * firstName and lastName fields
	 * 
	 * @return A list of all Customer entries with only the customerNumber,
	 *         firstName and lastName fields set
	 */
	public List<Customer> getAllSummary() {
		List<Customer> custList = new ArrayList<Customer>();

		String queryString1 = "SELECT c.customerNumber, c.firstName, c.lastName FROM /Customer c ";
		for (Struct s : (SelectResults<Struct>) doQuery(queryString1)) {
			Customer c = new Customer((Integer) s.get("customerNumber"), (String) s.get("firstName"),
					(String) s.get("lastName"));
			custList.add(c);
		}
		return custList;

	}

	public List<Customer> findCustomersInZip(String zip) {
		String queryString1 = "SELECT * FROM /Customer where primaryAddress.postalCode = '" + zip + "'";
		return ((SelectResults<Customer>) doQuery(queryString1)).asList();

	}

	/**
	 * Constructs an equi-join query string that returns Customers where the
	 * associated BookOrder has a totalPrice > $45.00
	 * 
	 * @return List of Customer entries having associated BookOrder > $45.00
	 */
	public List<Customer> findHighPricedCustomerOrders() {
		String queryString1 = "select distinct c " + "from /Customer c, /BookOrder o "
				+ "where c.customerNumber = o.customerNumber and  o.totalPrice > 45.00";
		return ((SelectResults<Customer>) doQuery(queryString1)).asList();
	}

	/**
	 * Produces a string representation of the customer in the form 'lastName,
	 * firstName'. This uses PdxInstance so if for some reason the PdxInstance
	 * is not returned, it's likely because the client wasn't configures with
	 * readSerialized set to true.
	 * 
	 * @param key
	 *            Key for customer to fetch
	 * @return String that is the concatenation of last name and first name as
	 *         'lastName, firstName' or null if object is either not in the
	 *         cache or is not a PdxInstance
	 */
	public String getCustomerName(Integer key) {
		String customerName = null;
		Object customerEntry = this.doGet(key);
		// TODO-09: Write necessary code to test to see if the customerEntry is
		// a PDX instance, extract first name and last name and create a string concatenation as specified.
		
		
		return customerName;
	}
}
