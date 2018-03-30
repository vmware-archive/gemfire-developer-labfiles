package io.pivotal.bookshop.dao;

import org.apache.geode.cache.client.ClientCache;
import org.apache.geode.cache.client.ClientCacheFactory;
import org.apache.geode.cache.client.ClientRegionFactory;
import org.apache.geode.cache.client.ClientRegionShortcut;
import org.apache.geode.pdx.ReflectionBasedAutoSerializer;

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

		clientFactory.setPdxSerializer(new ReflectionBasedAutoSerializer(true, "io.pivotal.bookshop.domain.*"));
		
		clientFactory.setPdxReadSerialized(readSerialized);
		
		ClientCache cache = clientFactory.create();
		createProxyRegion(cache,"BookMaster", false);
		createProxyRegion(cache,"Customer", false);
		createProxyRegion(cache,"BookOrder", false);
		
		return cache;
	}

	public static ClientCache createPdxEnabledCachingProxy(boolean readSerialized) {
		// Initial configuration of the ClientCacheFactory
		ClientCacheFactory clientFactory = new ClientCacheFactory()
				.set("name", "Client App")
				.addPoolLocator("localhost", 10334)
				.setPoolSubscriptionEnabled(true);

		clientFactory.setPdxSerializer(new ReflectionBasedAutoSerializer(true, "io.pivotal.bookshop.domain.*"));

		clientFactory.setPdxReadSerialized(readSerialized);

		ClientCache cache = clientFactory.create();
		createProxyRegion(cache,"BookMaster", true);
		createProxyRegion(cache,"Customer", true);
		createProxyRegion(cache,"BookOrder", true);

		return cache;
	}

	/**
	 * Modifies the region definition to specify an expiration (passed as milliseconds) with the pesumtpion that the
	 * configured expiration type will be Time to Live and the action will be LOCAL_DESTROY.
	 *
	 * @param region Region to add eviction to
	 * @param timeMillis Total time to live for entries (in milliseconds)
	 */
	public static void enableEviction(Region region, int timeMillis) {
		// TODO-02: Alter the specified region to set a total time to live with an action to only destroy locally

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
			// TODO-03: Add a call to the ClientRegionFactory to enable statistics
			regionFactory = cache.createClientRegionFactory(ClientRegionShortcut.CACHING_PROXY);
		else
			regionFactory = cache.createClientRegionFactory(ClientRegionShortcut.PROXY);

		regionFactory.create(regionName);
	}


}
