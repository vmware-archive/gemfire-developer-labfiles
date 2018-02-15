package io.pivotal.bookshop.events;

import org.apache.geode.GemFireCheckedException;
import org.apache.geode.GemFireException;
import org.apache.geode.cache.Region;
import org.apache.geode.cache.client.Pool;
import org.apache.geode.cache.client.PoolManager;
import org.apache.geode.cache.query.CqAttributes;
import org.apache.geode.cache.query.CqAttributesFactory;
import org.apache.geode.cache.query.CqQuery;
import org.apache.geode.cache.query.QueryService;

public class BookMasterCqHelper {
	// TODO-10: Write the proper query string to monitor entries from
	// /BookMaster region where the retailCost is more than $50
	private static String cqQueryString = "";

	/**
	 * Registers a continuous query request with the server. This query utilized
	 * a pre-defined query string defined as a static member.
	 * 
	 * @param regionToUpdate
	 *            The region where entries provided in the CqEvent are stored.
	 *            This will be provided as an argument to the CqListener
	 * @throws GemFireException
	 *             Can be thrown during the creation or execution of the
	 *             Continuous Query
	 * @throws GemFireCheckedException
	 *             Can be thrown during the creation or execution of the
	 *             Continuous Query
	 */
	public static void registerCq(Region regionToUpdate) throws GemFireException, GemFireCheckedException {
		// TODO-11a: Get a reference to the pool

		// TODL-11b: Get the query service for the Pool

		// TODO-11c: Create CQ Attributes, registering the BookMasterCqListener
		// implementation class

		// TODO-11d: Create the continuous query and execute it, providing the
		// Continuous Query Attributes defined earlier.

		// TODO-11e: Execute the query - this will send the query to the server
		// and return
		
	}
}
