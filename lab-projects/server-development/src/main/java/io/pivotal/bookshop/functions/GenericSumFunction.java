package io.pivotal.bookshop.functions;

import java.math.BigDecimal;

import org.apache.geode.cache.Region;
import org.apache.geode.cache.execute.Function;
import org.apache.geode.cache.execute.FunctionContext;
import org.apache.geode.cache.execute.FunctionException;
import org.apache.geode.cache.execute.RegionFunctionContext;
import org.apache.geode.cache.partition.PartitionRegionHelper;
import org.apache.geode.pdx.PdxInstance;

// TODO-01: Make this class implement the GemFire Function interface
public class GenericSumFunction  {

		// TODO-02: Ensure the FunctionContext is a RegionFunctionContext
			// TODO-03: Get the argument from the FunctionContext representing the field to perform sum on

			// TODO-04: Use the PartitionRegionHelper to get all the local region data

			// TODO-05: Iterate over the values in the local region data

                // TODO-06: Get the requested field, assert it is a Numeric type, cast it and add it to
                //          the summable variable defined above

			// TODO-07: Return the final sum

	

	// TODO-08: Have this method return the name of the class as the simple name (i.e. just the class name)
	
	// TODO-09: Implement the method to force only primary buckets to be considered
	


}
