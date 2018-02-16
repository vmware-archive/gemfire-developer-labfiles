package io.pivotal.bookshop.function;

import java.math.BigDecimal;

import org.apache.geode.cache.Region;
import org.apache.geode.cache.execute.Execution;
import org.apache.geode.cache.execute.FunctionService;
import org.apache.geode.cache.execute.ResultCollector;

public class FunctionExecutor {

	// TODO-05: Inspect the signature of this method
	public static BigDecimal callSumFunction(Region region, String fieldName) {
		// TODO-06: execute the function using the supplied 'fieldName' for the field and the supplied region to execute the function on
		Execution execution = FunctionService.onRegion(region).setArguments(fieldName)
				.withCollector(new SummingResultCollector());

		ResultCollector rc = execution.execute("GenericSumFunction");

		// TODO-07: Get result and assert that the two orders total $93.95
		return (BigDecimal) rc.getResult();
	
	}
}
