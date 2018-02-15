package io.pivotal.bookshop.tests.customPartitioningTests;

import org.apache.geode.cache.execute.FunctionException;
import org.apache.geode.cache.execute.ResultCollector;
import org.apache.geode.distributed.DistributedMember;

import java.util.*;
import java.util.concurrent.TimeUnit;

public class BucketAlignmentResultCollector implements ResultCollector<Object[], RegionBucketInfo> {

	final Map<DistributedMember, Object[]> fullResult = new HashMap<>();

	public void addResult(DistributedMember memberID, Object[] resultOfSingleExecution) {
		this.fullResult.put(memberID, resultOfSingleExecution);
	}

	public RegionBucketInfo getResult() throws FunctionException {
		return processResults();
	}

	public RegionBucketInfo getResult(long timeout, TimeUnit unit) throws FunctionException, InterruptedException {
		return processResults();
	}

	/**
	 * Process results by returning a RegionBucketInfo object that reports member hosting bucket for both primary and redundant 
	 * buckets. The bucket number is the key so the member hosting can be easily accessed.
	 * 
	 * @return
	 */
	private RegionBucketInfo processResults() {
		RegionBucketInfo regionInfo = new RegionBucketInfo();
		for (Map.Entry<DistributedMember, Object[]> entry : this.fullResult.entrySet()) {
			String memberName = entry.getKey().getName();
			List primaryBucketInfo = (List) entry.getValue()[0];
			List redundantBucketInfo = (List) entry.getValue()[1];

			// Process Primary Bucket Info
			for (Iterator i = sort(primaryBucketInfo).iterator(); i.hasNext();) {
				Map map = (Map) i.next();
				if ((Integer) map.get("Size") > 0) {
					regionInfo.addPrimaryBucketInfo((Integer) map.get("BucketId"), memberName);
				}
			}
			// Process Redundant Bucket Info
			for (Iterator i = sort(redundantBucketInfo).iterator(); i.hasNext();) {
				Map map = (Map) i.next();
				if ((Integer) map.get("Size") > 0) {
					regionInfo.addRedundantBucketInfo((Integer) map.get("BucketId"), memberName);
				}
			}
		}
		return regionInfo;
	}

	private List sort(List bucketInfo) {
		Map sortedBuckets = new TreeMap();
		for (int i = 0; i < bucketInfo.size(); i++) {
			Map map = (Map) bucketInfo.get(i);
			sortedBuckets.put(map.get("BucketId"), map);
		}
		return new ArrayList(sortedBuckets.values());
	}

	public void clearResults() {
		fullResult.clear();
	}

	public void endResults() {
	}
}
