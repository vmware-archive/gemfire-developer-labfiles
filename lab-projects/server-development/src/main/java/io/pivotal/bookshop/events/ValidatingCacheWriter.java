package io.pivotal.bookshop.events;

import org.apache.geode.cache.CacheWriterException;
import org.apache.geode.cache.EntryEvent;
import org.apache.geode.cache.Region;
import org.apache.geode.cache.query.Query;
import org.apache.geode.cache.query.QueryException;
import org.apache.geode.cache.query.QueryService;
import org.apache.geode.cache.query.SelectResults;
import org.apache.geode.cache.util.CacheWriterAdapter;
import org.apache.geode.internal.logging.LogService;
import org.apache.logging.log4j.Logger;

import io.pivotal.bookshop.domain.BookMaster;

/**
 * This implementation provides a specific function for all new BookMaster entries. It validates that there are
 * no other entries having the same itemNumber. This in theory would ensure someone doesn't mistakenly try to create
 * two books with the same item number.
 * 
 * This implementation has factored out the validation part from the handling of invalid entries. It's the job of 
 * the CacheWriter method(s) to extract the value and region to validate and to handle an invalid case. It's the
 * job of validateNewValue to handle the logic of determining if the object in question is valid.
 *
 */
public class ValidatingCacheWriter extends CacheWriterAdapter<Integer, BookMaster>  {
	private static final Logger logger = LogService.getLogger();

	@Override
	public void beforeCreate(EntryEvent<Integer, BookMaster> event) throws CacheWriterException {
		BookMaster b = event.getNewValue();
		Region<Integer, BookMaster> books = event.getRegion();
		logger.info("Validating value: {} for region: {}",b,books.getFullPath());
		try {
			if (! validateNewValue(b, books)) {
				throw new CacheWriterException("Entry with itemNumber = " + b.getItemNumber() + " can't already exist");
			}
		} catch (QueryException e) {
			throw new CacheWriterException("Failed to execute query: " + e);
		}
		
	}

	/**
	 * New value is valid as long as the itemNumber doesn't exist
	 * 
	 * @param book New book value to validate
	 * @param books BookMaster region reference used to create a query
	 * @return True if new value is valid (i.e no other entry has the same itemNumber)
	 * @throws QueryException If this query fails for some reason
	 */
	private boolean validateNewValue(BookMaster book, Region<Integer,BookMaster> books) throws QueryException {
		System.out.println("Validating Item: " + book);
		Object[] queryParams = new Object[1];
		queryParams[0] = book.getItemNumber();
		String queryString = "Select * from /BookMaster where itemNumber = $1";
		QueryService queryService = books.getRegionService().getQueryService();
		Query query = queryService.newQuery(queryString);
		SelectResults<BookMaster> results = (SelectResults<BookMaster>) query.execute(queryParams);
		
		return results.size() == 0;
	}
	

}
