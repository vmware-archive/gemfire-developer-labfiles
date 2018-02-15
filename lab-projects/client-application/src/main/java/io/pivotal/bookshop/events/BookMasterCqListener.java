package io.pivotal.bookshop.events;

import org.apache.geode.cache.Region;
import org.apache.geode.cache.query.CqEvent;
import org.apache.geode.cache.query.CqListener;
import org.apache.geode.internal.logging.LogService;
import org.apache.logging.log4j.Logger;

public class BookMasterCqListener implements CqListener {
	private Region region;
	private final Logger logger = LogService.getLogger();
	
	/**
	 * Constructs the listener with a local region to write matching events to
	 * 
	 * @param r Local region to update
	 */
	public BookMasterCqListener(Region r) {
		this.region = r;
	}

	@Override
	public void close() { }

	@Override
	public void onEvent(CqEvent event) {
		// TODO-09: Implement the functionality to capture the new value from the event as well as the key and store in the region
		// NOTE that in some cases the newValue may be null (in the case of a delete) so make sure to test for that be for inserting
		if (event.getNewValue() != null)
			region.put(event.getKey(), event.getNewValue());

	}

	@Override
	public void onError(CqEvent event) {
		logger.error("Error occured on CQEvent: " + event.getQueryOperation().toString() );
	}

}
