package io.pivotal.bookshop.partitions;

import java.io.Serializable;

import org.apache.geode.cache.EntryOperation;
import org.apache.geode.cache.PartitionResolver;

import io.pivotal.bookshop.keys.OrderKey;

public class CustomerPartitionResolver implements PartitionResolver<OrderKey,Object> {
	private static final long serialVersionUID = 1L;

	@Override
	public void close()
	{
		//nothing to do when the Region closes
	}
	
	@Override
	public String getName()
	{
		return this.getClass().getName() + "PartitionResolver";
	}
	
	@Override
	public Serializable getRoutingObject(EntryOperation<OrderKey,Object> eo)
	{
		// TODO-02: Implement getRoutingObject to obtain the customerId portion of the key and return it
		return null;
	}


}
