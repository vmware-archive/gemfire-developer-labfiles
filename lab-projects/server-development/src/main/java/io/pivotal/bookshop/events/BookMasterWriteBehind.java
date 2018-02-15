package io.pivotal.bookshop.events;

import java.util.List;

import org.apache.geode.cache.Operation;
import org.apache.geode.cache.asyncqueue.AsyncEvent;
import org.apache.geode.cache.asyncqueue.AsyncEventListener;
import org.apache.geode.internal.logging.LogService;
import org.apache.logging.log4j.Logger;

import io.pivotal.bookshop.domain.BookMaster;

public class BookMasterWriteBehind implements AsyncEventListener {
	// TODO-01a: Configure a Log4J logger as a class level private field
	
	@Override
	public void close() { }

	@Override
	public boolean processEvents(List<AsyncEvent> events) {
		JdbcBookDAO dao = new JdbcBookDAO();
        // TODO-01b: Log a message regarding what's happening

		// TODO-02: Implement processEvents utilizing the JdbcBookDAO to help
		// with persisting data to a DB. Be sure to utilize the operation type to 
		// determine the correct action to take with the database. Be sure to return a
		// boolean indicator of success

		return false;
	}

}
