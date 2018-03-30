package io.pivotal.bookshop.repositories;

import java.util.List;


import org.springframework.data.gemfire.repository.Query;
import org.springframework.data.repository.CrudRepository;

import io.pivotal.bookshop.domain.BookMaster;

public interface BookMasterRepository extends CrudRepository<BookMaster, Integer>{
	List<BookMaster> findByRetailCostGreaterThan(float f);

	@Query("select b from  /BookMaster b, /InventoryItem i where b.itemNumber = i.itemNumber and  i.quantityInStock < $1")
	List<BookMaster> findLowStockBooks(Integer stockThreshold);
	
}
