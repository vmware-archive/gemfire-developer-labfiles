package io.pivotal.bookshop.tests;

import static org.junit.Assert.assertEquals;

import javax.annotation.Resource;

import org.apache.geode.cache.Region;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

import io.pivotal.bookshop.config.GemFireConfiguration;
import io.pivotal.bookshop.domain.BookMaster;

// TODO-01: Open and examine the key behavior of this test harness, including the @ContextConfiguration annotation and the
//          one test that is defined.
// TODO-04: Run the tests. Do they pass? Why not?
@RunWith(SpringRunner.class)
@ContextConfiguration(classes=GemFireConfiguration.class)
public class BasicSpringClientTests {
	
	@Resource(name="BookMaster")
	private Region<Integer, BookMaster> bookRegion;
	
	/**
	 * Pre-setup the cluster data for this test
	 */
	@Before
	public void setup() {
		bookRegion.clear();
		populateBooks(bookRegion);
	
	}
	
	@Test
	public void simpleClientTest() {
	    BookMaster book = bookRegion.get(456);
	    assertEquals("Clifford the Big Red Dog", book.getTitle());

	}
	
	private void populateBooks( Region books) {
		BookMaster book = new BookMaster(123, "Run on sentences and drivel on all things mundane",
				(float) 34.99, 2011, "Daisy Mae West", "A Treatise of Treatises");
		books.put(123, book);
		BookMaster book2 = new BookMaster(456, "A book about a dog",
				(float) 11.99, 1971, "Clarence Meeks", "Clifford the Big Red Dog");
		books.put(456, book2);
		BookMaster book3 = new BookMaster(789, "Theoretical information about the structure of Operating Systems",
				(float) 59.99, 2011, "Jim Heavisides", "Operating Systems: An Introduction");
		books.put(789, book3);
		
	}
}
