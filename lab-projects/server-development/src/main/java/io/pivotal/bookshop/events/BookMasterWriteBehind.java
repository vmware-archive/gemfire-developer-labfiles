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
	private final static Logger logger = LogService.getLogger(BookMasterWriteBehind.class.getName());
	
	@Override
	public void close() { }

	/**
	 *
	 * @param events The list of events to be processed
	 * @return Boolean True if processing all events success, false otherwise
	 */
	@Override
	public boolean processEvents(List<AsyncEvent> events) {
		JdbcBookDAO dao = new JdbcBookDAO();
		boolean success = true; // Indicate whether the processEvents() succeeds or not
        // TODO-01b: Log a message regarding what's happening
		logger.info("In BookMasterWriteBehind.processEvents()");
		
		// TODO-02: Implement processEvents utilizing the JdbcBookDAO to help
		// with persisting data to a DB. Be sure to utilize the operation type to 
		// determine the correct action to take with the database. Be sure to return a
		// boolean indicator of success
		for (AsyncEvent<Integer, BookMaster> event : events) {
			BookMaster book = event.getDeserializedValue();
			Operation operation = event.getOperation();
			logger.info("Performing operation: {} on book: {}, key: {}",operation,book,event.getKey());
			if (operation.isCreate() || operation.isUpdate()) {
				logger.info("Persisting book to database");
				if (!dao.persistBook(book)) {
					result = false;
					logger.warn("Persist failed for book: " + book);
				}
			} else if (operation.isDestroy() ) {
				logger.info("Deleting book from database");
				dao.deleteBook(new BookMaster(event.getKey(),"",0.0f,0001,"",""));
			}
		}
		return success;

	}

}
