package io.pivotal.bookshop.function;

import org.apache.geode.cache.execute.FunctionException;
import org.apache.geode.cache.execute.ResultCollector;
import org.apache.geode.distributed.DistributedMember;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.concurrent.TimeUnit;

public class SummingResultCollector implements ResultCollector<Serializable, Serializable> {

	  // TODO-01: Determine how you will contain results
	  //    HINT: You only need to keep a final sum
	  BigDecimal total = BigDecimal.ZERO;

	  @Override
	  public void addResult(DistributedMember memberID,
	                        Serializable resultOfSingleExecution) {
	    // TODO-02: Implement the addResult method
	    //    HINT: Keep in mind what was sent from the function in the prior lab.
	    total = total.add((BigDecimal) resultOfSingleExecution);
	  }

	  @Override
	  public void clearResults() {
	    // TODO-03: Implement clearResults method
	    total = BigDecimal.ZERO;

	  }

	  @Override
	  public void endResults() {
	    // No special processing required.

	  }

	  @Override
	  public Serializable getResult() throws FunctionException {
	    // TODO-04: Implement getResult method
	    return total.setScale(2, BigDecimal.ROUND_HALF_UP);
	  }

	  @Override
	  public Serializable getResult(long timeout, TimeUnit unit)
	      throws FunctionException, InterruptedException {
	    // This method will do whatever the other getResult() method does.
	    return this.getResult();
	  }

	}

