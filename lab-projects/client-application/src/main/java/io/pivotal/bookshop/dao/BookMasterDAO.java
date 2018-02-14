package io.pivotal.bookshop.dao;

import org.apache.geode.cache.Region;
import org.apache.geode.cache.client.ClientCache;

import io.pivotal.bookshop.domain.BookMaster;

public class BookMasterDAO extends DAOCommon<Integer,BookMaster>
{
		// The region object that stores BookMaster objects
		private Region<Integer, BookMaster> books;

		public BookMasterDAO(ClientCache cache) {
			super(cache);
			this.books = cache.getRegion("BookMaster");
		}
		
		@Override
		public void doInsert(Integer key, BookMaster book) {
			// TODO-06: Write code to insert book with the given key. Use the method that assumes
            //          the entry doesn't already exist
					
		}
		
		@Override
		public BookMaster doGet(Integer key)
		{
			// TODO-07: Write code to get & return a book for the specified key
			return null;
		}
		
		@Override
		public void doUpdate(Integer key, BookMaster book) {
	    	// TODO-08: Write code to update book for specified key
	    }

		@Override
		public void doDelete(Integer key) 
	    {
	        // TODO-09: Implement delete functionality for specified key
	    }

		

}
