package io.pivotal.bookshop.dao;

import org.apache.geode.cache.client.ClientCache;
import org.apache.geode.cache.client.ClientCacheFactory;
import org.apache.geode.cache.client.ClientRegionFactory;
import org.apache.geode.cache.client.ClientRegionShortcut;

import io.pivotal.bookshop.domain.BookMaster;
import io.pivotal.bookshop.domain.Customer;

public class GemFireClientCacheHelper {
	
	/**
	 * Initializes the ClientCache. It also indirectly initializes the BookMaster and Customer regions by calling the associated
	 * static methods.
	 * 
	 * @return
	 */
	public static ClientCache create() {
		// TODO-01: Create a Client Cache and configure it with a pool definition
		ClientCache cache = null;
		
		createBookMasterClientRegion(cache);
		createCustomerClientRegion(cache);
		return cache;
	}
	
	/**
	 * Initializes the BookMaster region to act as a proxy to the server region by the same name AND caches a copy locally. 
	 * Once created, it can later be fetched using the getRegion() method call on the ClientCache.
	 * 
	 * @param cache
	 */
	private static void createBookMasterClientRegion(ClientCache cache) {
        // TODO-02: Use the client cache ClientRegionFactory to create a region named 'BookMaster' of a type that acts
		//  as a proxy to the server BUT also keeps a copy locally.


	}
	
	/**
	 * Initializes the Customer region to act as a proxy to the server region by the same name AND caches a copy locally. 
	 * Once created, it can later be fetched using the getRegion() method call on the ClientCache.
	 * 
	 * @param cache
	 */
	public static void createCustomerClientRegion(ClientCache cache) {
        // TODO-03: Use the client cache ClientRegionFactory to create a region named 'Customer' of a type that acts
		//	as a proxy to the server BUT also keeps a copy locally.
		
	}

}
