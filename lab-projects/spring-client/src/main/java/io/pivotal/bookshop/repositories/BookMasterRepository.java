package io.pivotal.bookshop.repositories;

import java.util.List;


import org.springframework.data.gemfire.repository.Query;
import org.springframework.data.repository.CrudRepository;

import io.pivotal.bookshop.domain.BookMaster;

// TODO-11: Open and inspect this Interface and note it extends CrudRepository. What methods does that add?
public interface BookMasterRepository extends CrudRepository<BookMaster, Integer>{
	// TODO-15: Add a 'findBy' method that returns all BookMaster entries having a retailCost of more than the value passed in.
	//          HINT: You might have a look at the unit test (SpringRepositoryTests) to see how the method is used.


	// TODO-19: Add a method called findLowStockBooks that returns a list of BookMaster entries and takes a single
	//          argument as an integer for the inventory in stock threshold. Also add the necessary @Query annotation
	//          to tell Spring GemFire how to perform the query
	//          HINT: You can use the initial query used in the query lab but now you can parameterize the comparison value

	
}
