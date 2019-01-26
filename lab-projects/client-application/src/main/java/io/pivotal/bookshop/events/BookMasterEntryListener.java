package io.pivotal.bookshop.events;

import org.apache.geode.cache.EntryEvent;
import org.apache.geode.cache.util.CacheListenerAdapter;

import io.pivotal.bookshop.domain.BookMaster;

// TODO-01: Implement the listener to extend the adapter but only implement for the create and destroy events
public class BookMasterEntryListener extends CacheListenerAdapter<Integer, BookMaster> {

	// TODO-02: Implement necessary code to register and unregister interest on create and destroy events
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
