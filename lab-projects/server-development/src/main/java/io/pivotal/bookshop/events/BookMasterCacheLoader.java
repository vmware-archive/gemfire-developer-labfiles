package io.pivotal.bookshop.events;

import org.apache.geode.cache.CacheLoader;
import org.apache.geode.cache.LoaderHelper;
import org.apache.geode.internal.logging.LogService;
import org.apache.logging.log4j.Logger;

import io.pivotal.bookshop.domain.BookMaster;

public class BookMasterCacheLoader implements CacheLoader<Integer, BookMaster> {

	public BookMaster load(LoaderHelper<Integer, BookMaster> helper) {
		JdbcBookDAO dao = new JdbcBookDAO();
		

		// TODO-01: Implement the necessary logic to get the key and use the JdbcBookDAO to fetch the book using that key
		return null;
	}

	public void close() {
		// do nothing
	}

}