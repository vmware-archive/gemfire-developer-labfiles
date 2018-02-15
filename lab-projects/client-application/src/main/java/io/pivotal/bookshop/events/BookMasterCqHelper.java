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
	private static String cqQueryString = "SELECT * FROM /BookMaster b WHERE b.retailCost > 50.00";

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
		Pool myPool = PoolManager.find("DEFAULT");

		QueryService queryService = myPool.getQueryService();

		CqAttributesFactory cqAf = new CqAttributesFactory();
		cqAf.addCqListener(new BookMasterCqListener(regionToUpdate));
		CqAttributes cqa = cqAf.create();

		CqQuery myCq = queryService.newCq("myCQ", cqQueryString, cqa);

		myCq.execute();
	}
}
