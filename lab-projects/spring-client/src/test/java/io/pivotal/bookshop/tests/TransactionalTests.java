package io.pivotal.bookshop.tests;

import static org.junit.Assert.*;

import org.apache.geode.cache.client.ClientCache;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

import io.pivotal.bookshop.config.GemFireRepositoryConfig;
import io.pivotal.bookshop.domain.BookMaster;
import io.pivotal.bookshop.domain.InventoryItem;
import io.pivotal.bookshop.repositories.BookMasterRepository;
import io.pivotal.bookshop.repositories.InventoryItemRepository;
import io.pivotal.bookshop.services.AddBookService;

@RunWith(SpringRunner.class)
@ContextConfiguration(classes=GemFireRepositoryConfig.class)
public class TransactionalTests {
	@Autowired
	private InventoryItemRepository inventoryRepo;
	@Autowired
	private BookMasterRepository bookRepo;
	@Autowired
	private AddBookService service; 
	@Autowired
	private ClientCache cache;
	
	@Before
	public void setup() {
		cache.setCopyOnRead(true);
		bookRepo.deleteAll();
		inventoryRepo.deleteAll();
		populateBooks();
		populateInventory();
	}
	
	@Test
	public void successfulCommitTest() {
		BookMaster book = new BookMaster(124, "An basic book to test validate", (float) 34.99, 2011, "You Student", "Transaction");
		service.addBook(book, 10, 15.50f);
		assertEquals(book, bookRepo.findById(book.getItemNumber()).get());
		assertTrue(inventoryRepo.findById(book.getItemNumber()).isPresent());
	}
	
	@Test
	public void failedCommitTest() {
		BookMaster book = new BookMaster(789, "An basic book to test validate", (float) 34.99, 2011, "You Student", "Duplicate Test");
		try {
			service.addBook(book, 10, 15.50f);
			fail("Should have thrown an exception");
		} catch (RuntimeException e) {
			assertTrue("InventoryItem should not have persisted",!inventoryRepo.existsById(book.getItemNumber()));
		}
		
	}
	
	private void populateBooks() {
		BookMaster book = new BookMaster(123, "Run on sentences and drivel on all things mundane",
				(float) 34.99, 2011, "Daisy Mae West", "A Treatise of Treatises");
		bookRepo.save(book);
		BookMaster book2 = new BookMaster(456, "A book about a dog",
				(float) 11.99, 1971, "Clarence Meeks", "Clifford the Big Red Dog");
		bookRepo.save(book2);
		BookMaster book3 = new BookMaster(789, "Theoretical information about the structure of Operating Systems",
				(float) 59.99, 2011, "Jim Heavisides", "Operating Systems: An Introduction");
		bookRepo.save(book3);
		
	}
	
	private void populateInventory() {

		InventoryItem item1 = new InventoryItem(123, (float) 12.50, (float) 34.99,  12, "BookRUs", "Seattle");
		inventoryRepo.save(item1);

		InventoryItem item2 = new InventoryItem(456, (float) 12.50, (float) 11.99, 1, "BookRUs", "Seattle");
		inventoryRepo.save(item2);

	
	}	
}
