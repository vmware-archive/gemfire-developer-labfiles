package io.pivotal.bookshop.dao;

import org.apache.geode.cache.client.ClientCache;
import org.apache.geode.cache.client.ClientCacheFactory;
import org.apache.geode.cache.client.ClientRegionFactory;
import org.apache.geode.cache.client.ClientRegionShortcut;
import org.apache.geode.pdx.ReflectionBasedAutoSerializer;

import io.pivotal.bookshop.domain.BookMaster;
import io.pivotal.bookshop.domain.Customer;
import io.pivotal.bookshop.keys.OrderKey;

public class GemFireClientCacheHelper {

	/**
	 * Initializes the ClientCache. It also indirectly initializes the
	 * BookMaster, Customer and BookOrder regions by calling the associated
	 * static methods.
	 * 
	 * @returnInitialized ClientCache and regions initialized as well.
	 */
	public static ClientCache create() {
		ClientCache cache = new ClientCacheFactory()
				.set("name", "Client App")
				.addPoolLocator("localhost", 10334)
				.setPoolSubscriptionEnabled(true)
				.create();

		createProxyRegion(cache,"BookMaster", true);
		createProxyRegion(cache,"Customer", true);
		createProxyRegion(cache,"BookOrder", true);
		return cache;
	}

	/**
	 * Initializes the ClientCache, including enabling auto-serialization and
	 * optionally setting read-serialized to true. It also indirectly
	 * initializes the BookMaster, Customer and BookOrder regions by calling the
	 * associated static methods.
	 * 
	 * @param readSerialized
	 *            Boolean indicating whether the client cache should be
	 *            initialized with read-serialized set to true
	 * @return Initialized ClientCache and regions initialized as well.
	 */
	public static ClientCache createPdxEnabled(boolean readSerialized) {
		// Initial configuration of the ClientCacheFactory
		ClientCacheFactory clientFactory = new ClientCacheFactory()
				.set("name", "Client App")
				.addPoolLocator("localhost", 10334)
				.setPoolSubscriptionEnabled(true);

		// TODO-02: Add configuration to enable auto-serialization on
		// the client side (and enforce check portability)
		
		// TODO-08: Set read-serialized to true

		
		ClientCache cache = clientFactory.create();
		createProxyRegion(cache,"BookMaster", false);
		createProxyRegion(cache,"Customer", false);
		createProxyRegion(cache,"BookOrder", false);
		
		return cache;
	}

	/**
	 * Initializes a region by name and creates as either caching proxy or proxy
	 * depending on the boolean flag supplied. Once created, it can later be
	 * fetched using the getRegion() method call on the ClientCache.
	 * 
	 * @param cache
	 *            ClientCache to use for creating the ClientRegionFactory
	 * @param regionName
	 *            Name of the region to initialize
	 * @param createAsCachingProxy
	 *            Boolean indicating wither region should be a caching proxy
	 */
	private static void createProxyRegion(ClientCache cache, String regionName, boolean createAsCachingProxy) {
		ClientRegionFactory regionFactory;
		if (createAsCachingProxy)
			regionFactory = cache.createClientRegionFactory(ClientRegionShortcut.CACHING_PROXY);
		else
			regionFactory = cache.createClientRegionFactory(ClientRegionShortcut.PROXY);

		regionFactory.create(regionName);
	}


}
