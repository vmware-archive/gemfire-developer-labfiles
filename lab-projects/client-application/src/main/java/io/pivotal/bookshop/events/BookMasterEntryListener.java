package io.pivotal.bookshop.events;

import org.apache.geode.cache.EntryEvent;
import org.apache.geode.cache.util.CacheListenerAdapter;

import io.pivotal.bookshop.domain.BookMaster;

public class BookMasterEntryListener extends CacheListenerAdapter<Integer, BookMaster> {

	@Override
	public void afterCreate(EntryEvent<Integer, BookMaster> event) {
		Integer key = event.getKey();
		event.getRegion().registerInterest(key);

	}

	@Override
	public void afterDestroy(EntryEvent<Integer, BookMaster> event) {
		Integer key = event.getKey();
		event.getRegion().unregisterInterest(key);

	}
}
