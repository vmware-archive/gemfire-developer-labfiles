package io.pivotal.bookshop.dao;

import org.apache.geode.cache.Region;
import org.apache.geode.cache.client.ClientCache;
import org.apache.geode.cache.query.Query;
import org.apache.geode.cache.query.QueryService;
import org.apache.geode.cache.query.SelectResults;

public abstract class DAOCommon<K,T> {
	private ClientCache clientCache;
	private Region<K, T> region;
  

   public DAOCommon(ClientCache clientCache, String regionName) {
		this.clientCache = clientCache;
		this.region = this.clientCache.getRegion(regionName);
	}

	public void doInsert(K key,T entry) {
		region.create(key, entry);
	}
    
    public T doGet(K key) {
    	return region.get(key);
    }
    
    public void doUpdate(K key, T entry) {
    	region.put(key, entry);
    }
    
    public void doDelete(K key) {
    	region.remove(key);
    }

    /**
     * Uses the supplied queryString to execute a GemFire query using OQL and the GemFire QueryService. This is a very basic 
     * implementation that does not take advantage of query parameters or anything like that.
     * 
     * @param queryString The string to be passed to the query service
     * @return SelectResults is a collection of any number of types depending on the nature of the query
     */
	public SelectResults<?> doQuery(String queryString) {
		// TODO-01: Implement the doQuery method to use the supplied query string to execute, returning the SelectResults
		// Catch any exceptions and re-throw as a QueryUncheckedException, which is an unchecked exception provided in this package.
		return null;
	}    
}
