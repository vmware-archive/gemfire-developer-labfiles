package io.pivotal.bookshop.tests;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.List;
import java.util.Optional;

import org.junit.Before;
import org.junit.Ignore;
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

@RunWith(SpringRunner.class)
@ContextConfiguration(classes=GemFireRepositoryConfig.class)
public class SpringRepositoryTests {
	@Autowired
	private InventoryItemRepository inventoryRepo;
	
	@Autowired
	private BookMasterRepository bookRepo;
	
	@Before
	public void setup() {
		inventoryRepo.deleteAll();
		bookRepo.deleteAll();
		populateBooks();
		populateInventory();
	}
	
	@Test
	public void bookTests() {
		Optional<BookMaster> book = bookRepo.findById(123);
		assertTrue("Failed to fetch a book for key 123",book.isPresent());
		BookMaster theBook = book.get();
		assertEquals("Fetched incorrect book", "Daisy Mae West",theBook.getAuthor());
	}
	
	@Test
	public void basicBookMasterQueryTest() {
		List<BookMaster> results = bookRepo.findByRetailCostGreaterThan((float) 50.00);
		assertEquals("Query should return only one book", 1, results.size());
		assertEquals("Join query should have return book with author 'Jim Heavisides'", "Jim Heavisides", results.get(0).getAuthor());
	}
	
	@Test 
	public void basicInventoryItemQueryTest() {
		List<InventoryItem> results = inventoryRepo.findByQuantityInStockLessThan(2);
		assertEquals("Query should return only one Item",1, results.size());
	
	}

	@Test
	public void joinQueryTest() {
		List<BookMaster> results = bookRepo.findLowStockBooks(2);
		// Assert that only one customer item is returned
		assertEquals("Join query should return only one Book",1, results.size());
		// Assert that the customer returned has last name = 'Wax'
		assertEquals("Join query should have returned Book with author 'Clarence Meeks'","Clarence Meeks",results.get(0).getAuthor());
	
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
