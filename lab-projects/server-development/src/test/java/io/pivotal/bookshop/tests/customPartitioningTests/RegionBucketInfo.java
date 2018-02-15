package io.pivotal.bookshop.tests.customPartitioningTests;

import java.util.HashMap;
import java.util.Map;

public class RegionBucketInfo {
	private Map<Integer, String> primaryBucketInfo = new HashMap<Integer,String>();
	private Map<Integer, String> redundantBucketInfo= new HashMap<Integer,String>();
	
	public void addPrimaryBucketInfo (Integer bucketNumber, String memberName) {
		primaryBucketInfo.put(bucketNumber, memberName);
	}

	public void addRedundantBucketInfo (Integer bucketNumber, String memberName) {
		redundantBucketInfo.put(bucketNumber, memberName);
	}
	
	public Map<Integer, String> getPrimaryBucketInfo() {
		return primaryBucketInfo;
	}

	public Map<Integer,String> getRedundantBucketInfo() {
		return redundantBucketInfo;
	}
}
