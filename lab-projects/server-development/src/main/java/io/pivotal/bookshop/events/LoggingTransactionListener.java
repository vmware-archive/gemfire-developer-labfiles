package io.pivotal.bookshop.events;

import org.apache.geode.cache.CacheEvent;
import org.apache.geode.cache.EntryEvent;
import org.apache.geode.cache.TransactionEvent;
import org.apache.geode.cache.util.TransactionListenerAdapter;
import org.apache.geode.internal.logging.LogService;
import org.apache.logging.log4j.Logger;

// TODO-05: Make this class extend from TransactionListenerAdapter
// TODO-08: Open the serverCache.xml file in the xml folder (in the server-cluster project) and add configuration 
//         to register this transaction listener
public class LoggingTransactionListener  extends TransactionListenerAdapter {

	private Logger logger = LogService.getLogger(LoggingTransactionListener.class.getName());


	// TODO-06: Add an afterCommit method that overrides the adapter method. Write a logger message that notes when a transaction is committed.
	//          Write code that will get the list of events and print the key, old value and new value for each (you can use the code from the 
	//          LoggingCacheListener as a guide).
	@Override
	public void afterCommit(TransactionEvent event) {
		logger.info("afterCommit: TxId= " + event.getTransactionId());
		for (CacheEvent ce : event.getEvents()) {
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

	
	// TODO-07: Add an afterRollback method and use the method to log that the rollback occurred.
	@Override
	public void afterRollback(TransactionEvent event) {
		logger.info("afterRollback: TxId= " + event.getTransactionId());
		for (CacheEvent ce : event.getEvents()) {			
				logger.info("   Cache event received with operation: " + ce.getOperation());
		}
	}


}
