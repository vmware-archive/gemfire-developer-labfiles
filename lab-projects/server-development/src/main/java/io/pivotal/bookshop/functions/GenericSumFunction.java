package io.pivotal.bookshop.functions;

import java.math.BigDecimal;

import org.apache.geode.cache.Region;
import org.apache.geode.cache.execute.Function;
import org.apache.geode.cache.execute.FunctionContext;
import org.apache.geode.cache.execute.FunctionException;
import org.apache.geode.cache.execute.RegionFunctionContext;
import org.apache.geode.cache.partition.PartitionRegionHelper;
import org.apache.geode.pdx.PdxInstance;

//TODO-01: Make this class implement the GemFire Function interface
public class GenericSumFunction implements Function {

	@Override
	public void execute(FunctionContext context) {
		// TODO-02: Ensure the FunctionContext is a RegionFunctionContext
		if (context instanceof RegionFunctionContext) {
			RegionFunctionContext rfc = (RegionFunctionContext) context;
			// TODO-03: Get the argument from the FunctionContext representing the field to perform sum on
			String fieldString = (String) rfc.getArguments();
			// TODO-04: Use the PartitionRegionHelper to get all the local region data
			Region<Object, PdxInstance> localRegion = PartitionRegionHelper.getLocalDataForContext(rfc);
			BigDecimal summable = BigDecimal.ZERO;
			// TODO-05: Iterate over the values in the local region data
			for (PdxInstance item : localRegion.values()) {
             // TODO-06: Get the requested field, assert it is a Numeric type, cast it and add it to
             //          the summable variable defined above
				Object field = item.getField(fieldString);
				if ( field instanceof Float) {
					summable = summable.add(BigDecimal.valueOf((Float) field));
				} else {
					System.out.println("Field : " + fieldString + " is NOT a Float. Value= " + field);
				}
			}
			// TODO-07: Return the final sum
			System.out.println("Returning: " + summable);
			rfc.getResultSender().lastResult(summable);
		} else {
			throw new FunctionException("Function must be called as onRegion()");
		}

	}

	// TODO-08: Have this method return the name of the class as the simple name (i.e. just the class name)
	@Override
	public String getId() {
		return getClass().getSimpleName();
	}
	
	// TODO-09: Implement the method to force only primary buckets to be considered
	@Override
	public boolean optimizeForWrite() {
		return true;
	}

}
