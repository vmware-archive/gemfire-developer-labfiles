package io.pivotal.bookshop.events;

import java.util.List;

import org.apache.geode.cache.Operation;
import org.apache.geode.cache.asyncqueue.AsyncEvent;
import org.apache.geode.cache.asyncqueue.AsyncEventListener;
import org.apache.geode.internal.logging.LogService;
import org.apache.logging.log4j.Logger;

import io.pivotal.bookshop.domain.BookMaster;

public class BookMasterWriteBehind implements AsyncEventListener {
	private final static Logger logger = LogService.getLogger(BookMasterWriteBehind.class.getName());
	
	@Override
	public void close() { }

	@Override
	public boolean processEvents(List<AsyncEvent> events) {
		JdbcBookDAO dao = new JdbcBookDAO();
		logger.info("In BookMasterWriteBehind.processEvents()");
		
		boolean result = true;
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
		return result;

	}

}
