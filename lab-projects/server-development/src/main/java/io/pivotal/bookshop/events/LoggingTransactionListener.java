package io.pivotal.bookshop.events;

import org.apache.geode.cache.CacheEvent;
import org.apache.geode.cache.EntryEvent;
import org.apache.geode.cache.TransactionEvent;
import org.apache.geode.cache.util.TransactionListenerAdapter;
import org.apache.geode.internal.logging.LogService;
import org.apache.logging.log4j.Logger;

import java.util.List;

public class LoggingTransactionListener  extends TransactionListenerAdapter {

	private Logger logger = LogService.getLogger(LoggingTransactionListener.class.getName());


	@Override
	public void afterCommit(TransactionEvent event) {
		logger.info("afterCommit: TxId= " + event.getTransactionId());
		for (CacheEvent ce :  event.getEvents()) {
			if (ce instanceof EntryEvent) {
				EntryEvent ee = (EntryEvent) ce;
				logger.info("   Entry updated for key: " + ee.getKey()
						+ "\n          Old value: " + ee.getOldValue()
						+ "\n          New Value: " + ee.getNewValue());
			} else {
				logger.info("   Cache event received with operation: " + ce.getOperation());
			}
		}
	}

	
	@Override
	public void afterRollback(TransactionEvent event) {
		logger.info("afterRollback: TxId= " + event.getTransactionId());
		for (CacheEvent ce :  event.getEvents()) {
				logger.info("   Cache event received with operation: " + ce.getOperation());
		}
	}


}
