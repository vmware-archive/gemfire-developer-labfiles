package io.pivotal.bookshop.function;

import java.math.BigDecimal;

import org.apache.geode.cache.Region;
import org.apache.geode.cache.execute.Execution;
import org.apache.geode.cache.execute.FunctionService;
import org.apache.geode.cache.execute.ResultCollector;

public class FunctionExecutor {

	public static BigDecimal callSumFunction(Region region, String fieldName) {
		Execution execution = FunctionService.onRegion(region).setArguments(fieldName)
				.withCollector(new SummingResultCollector());

		ResultCollector rc = execution.execute("GenericSumFunction");

		return (BigDecimal) rc.getResult();
	
	}
}
