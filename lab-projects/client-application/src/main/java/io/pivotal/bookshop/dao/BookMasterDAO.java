package io.pivotal.bookshop.dao;

import java.util.List;

import org.apache.geode.cache.client.ClientCache;
import org.apache.geode.cache.query.SelectResults;

import io.pivotal.bookshop.domain.BookMaster;

public class BookMasterDAO extends DAOCommon<Integer, BookMaster> {
	// Note that there is a generic region now defined in the DAOCommon class that you can use called 'region'.
	// Notice also that type type (key and value) are defined by the types specified above

	public BookMasterDAO(ClientCache cache) {
		super(cache, "BookMaster");
	}

	/**
	 * Perform a equi-join query that will identify books in BookMaster region having an inventory
	 * balance of < 2 items using InventoryItem objects.
	 * 
	 * @return List of BookMaster having quantityInStock < 2
	 */
	public List<BookMaster> findLowQuantityBooks() {
		String queryString1 = "select  b " + "from /BookMaster b, /InventoryItem i "
				+ "where b.itemNumber = i.itemNumber and  i.quantityInStock < 2";
		return ((SelectResults<BookMaster>) doQuery(queryString1)).asList();
	}

}
