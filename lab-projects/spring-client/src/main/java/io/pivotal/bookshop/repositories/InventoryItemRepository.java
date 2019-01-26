package io.pivotal.bookshop.repositories;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import io.pivotal.bookshop.domain.InventoryItem;

public interface InventoryItemRepository  extends CrudRepository<InventoryItem, Integer> {
	List<InventoryItem> findByQuantityInStockLessThan(Integer stockThreshold);
}
