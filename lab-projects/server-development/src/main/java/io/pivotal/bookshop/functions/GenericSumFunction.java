package io.pivotal.bookshop.functions;

import java.math.BigDecimal;

import org.apache.geode.cache.Region;
import org.apache.geode.cache.execute.Function;
import org.apache.geode.cache.execute.FunctionContext;
import org.apache.geode.cache.execute.FunctionException;
import org.apache.geode.cache.execute.RegionFunctionContext;
import org.apache.geode.cache.partition.PartitionRegionHelper;
import org.apache.geode.pdx.PdxInstance;

public class GenericSumFunction implements Function {

	@Override
	public void execute(FunctionContext context) {
		if (context instanceof RegionFunctionContext) {
			RegionFunctionContext rfc = (RegionFunctionContext) context;
			String fieldString = (String) rfc.getArguments();

			Region<Object, PdxInstance> localRegion = PartitionRegionHelper.getLocalDataForContext(rfc);
			BigDecimal summable = BigDecimal.ZERO;

			for (PdxInstance item : localRegion.values()) {
				Object field = item.getField(fieldString);
				if ( field instanceof Float) {
					summable = summable.add(BigDecimal.valueOf((Float) field));
				} else {
					System.out.println("Field : " + fieldString + " is NOT a Float. Value= " + field);
				}
			}
			System.out.println("Returning: " + summable);
			rfc.getResultSender().lastResult(summable);
		} else {
			throw new FunctionException("Function must be called as onRegion()");
		}

	}

	@Override
	public String getId() {
		return getClass().getSimpleName();
	}
	
	@Override
	public boolean optimizeForWrite() {
		return true;
	}

}
