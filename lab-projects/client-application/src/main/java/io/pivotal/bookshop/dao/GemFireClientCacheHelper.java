package io.pivotal.bookshop.dao;

import org.apache.geode.cache.client.ClientCache;
import org.apache.geode.cache.client.ClientCacheFactory;
import org.apache.geode.cache.client.ClientRegionFactory;
import org.apache.geode.cache.client.ClientRegionShortcut;

import io.pivotal.bookshop.domain.BookMaster;
import io.pivotal.bookshop.domain.Customer;
import io.pivotal.bookshop.keys.OrderKey;

public class GemFireClientCacheHelper {

	/**
	 * Initializes the ClientCache. It also indirectly initializes the
	 * BookMaster and Customer regions by calling the associated static methods.
	 * 
	 * @return
	 */
	// TODO-03: Modify the client cache creation code to also enable subscriptions
	public static ClientCache create() {
		ClientCache cache = new ClientCacheFactory()
				.set("name", "Client App")
				.addPoolLocator("localhost", 10334)
				.create();

		createBookMasterClientRegion(cache);
		createCustomerClientRegion(cache);
		createBookOrderClientRegion(cache);
		return cache;
	}

	/**
	 * Initializes the BookMaster region to act as a caching proxy to the server
	 * region by the same name AND caches a copy locally. Once created, it can
	 * later be fetched using the getRegion() method call on the ClientCache.
	 * 
	 * @param cache
	 */
	private static void createBookMasterClientRegion(ClientCache cache) {
		ClientRegionFactory<Integer, BookMaster> booksRegionFactory = cache
				.createClientRegionFactory(ClientRegionShortcut.CACHING_PROXY);
		booksRegionFactory.create("BookMaster");

	}

	/**
	 * Initializes the Customer region to act as a caching proxy to the server
	 * region by the same name AND caches a copy locally. Once created, it can
	 * later be fetched using the getRegion() method call on the ClientCache.
	 * 
	 * @param cache
	 */
	public static void createCustomerClientRegion(ClientCache cache) {
		ClientRegionFactory<Integer, Customer> customerRegionFactory = cache
				.createClientRegionFactory(ClientRegionShortcut.CACHING_PROXY);
		customerRegionFactory.create("Customer");

	}

	/**
	 * Initializes the BookOrder region to act as a proxy to the server region
	 * by the same name. Once created, it can later be
	 * fetched using the getRegion() method call on the ClientCache.
	 * 
	 * @param cache
	 */
	public static void createBookOrderClientRegion(ClientCache cache) {
		ClientRegionFactory<OrderKey, Customer> customerRegionFactory = cache
				.createClientRegionFactory(ClientRegionShortcut.PROXY);
		customerRegionFactory.create("BookOrder");

	}



}
