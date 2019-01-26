package io.pivotal.bookshop.tests.clientEventsTests;

import org.apache.geode.cache.client.ClientCache;

import io.pivotal.bookshop.dao.BookMasterDAO;
import io.pivotal.bookshop.dao.GemFireClientCacheHelper;
import io.pivotal.bookshop.domain.BookMaster;

// TODO-07: When prompted, run this driver class to put an update into the server BookMaster region
public class ClientInterestDriver {
	private static Integer key = 12345;

	public static void main(String[] args) throws Exception {
		ClientCache clientCache = GemFireClientCacheHelper.create();
		BookMasterDAO dao = new BookMasterDAO(clientCache);

		// Update value to trigger threshold for test
		BookMaster book = dao.doGet(key);
		if (book != null) {
			book.setRetailCost(33f);
			dao.doUpdate(key, book);
		}
	}

}
