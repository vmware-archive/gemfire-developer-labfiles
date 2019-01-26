package io.pivotal.bookshop.dao;

import org.apache.geode.cache.client.ClientCache;

public abstract class DAOCommon<K,T> {
	private ClientCache clientCache;
  

   public DAOCommon(ClientCache clientCache) {
		this.clientCache = clientCache;
	}

	public abstract void doInsert(K key,T entry) ;
    
    public abstract T doGet(K key);
    
    public abstract void doUpdate(K key, T entry);
    
    public abstract void doDelete(K key);
    

   

}
