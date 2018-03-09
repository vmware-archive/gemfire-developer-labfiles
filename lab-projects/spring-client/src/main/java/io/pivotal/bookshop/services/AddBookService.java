package io.pivotal.bookshop.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import io.pivotal.bookshop.domain.BookMaster;
import io.pivotal.bookshop.domain.InventoryItem;
import io.pivotal.bookshop.repositories.BookMasterRepository;
import io.pivotal.bookshop.repositories.InventoryItemRepository;

@Service
public class AddBookService {
	@Autowired
	private InventoryItemRepository inventoryRepo;
	@Autowired
	private BookMasterRepository bookRepo;
	
	// TODO-22: Add the Spring annotation to mark this as a transactional operation

	public void addBook(BookMaster book,int inventoryQty, float costToCust) {
		if (!bookRepo.existsById(book.getItemNumber()) ) {
				bookRepo.save(book);
				if (! inventoryRepo.existsById(book.getItemNumber())) {
					inventoryRepo.save(new InventoryItem(book.getItemNumber(),(float) 12.50, costToCust, inventoryQty, "BookRUs", "Seattle"));
				} else {
					throw new RuntimeException("InventoryItem Shouldn't exist for id: " + book.getItemNumber());
					
				}
		} else {
			throw new RuntimeException("Book Shouldn't exist: " + book);
		}
	}
}
