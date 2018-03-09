package io.pivotal.bookshop.repositories;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import io.pivotal.bookshop.domain.InventoryItem;

public interface InventoryItemRepository  extends CrudRepository<InventoryItem, Integer> {
	// TODO-16: Define a similar 'findBy' method that looks for items in stock that fall below a threshold
	List<InventoryItem> findByQuantityInStockLessThan(Integer stockThreshold);
}
