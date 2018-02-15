package io.pivotal.bookshop.tests.serverEventsTests;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import org.apache.geode.cache.CacheWriterException;
import org.apache.geode.cache.Region;
import org.apache.geode.cache.client.ClientCache;
import org.apache.geode.cache.client.ClientCacheFactory;
import org.apache.geode.cache.client.ClientRegionFactory;
import org.apache.geode.cache.client.ClientRegionShortcut;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import io.pivotal.bookshop.domain.BookMaster;

public class ServerEventsTests {
  ClientCache clientCache;
  Region<Integer, BookMaster> books;
  Integer keyUnderTest = 9876;

  @Before
  public void initCache() {
		this.clientCache = new ClientCacheFactory()
				.set("name", "DataOperations Client")
				.addPoolLocator("localhost", 10334)
				.create();

		ClientRegionFactory<Integer, BookMaster> customerRegionFactory = this.clientCache
				.createClientRegionFactory(ClientRegionShortcut.PROXY);
		books = customerRegionFactory.create("BookMaster");

		// Ensure the KeyUnderTest does not currently exist in the BookMaster region
		if (this.books !=null) {
			books.remove(keyUnderTest);
		}
  }

  @After
  public void closeCache() {
    clientCache.close();
  }

  /**
   * Asserts that when the `keyUnderTest` is requested that a new one is created by the cache loader.
   * First asserts that there is no entry with `keyUnderTest`, then performs a get operation, which should
   * trigger the CacheLoader behavior. Then, asserts that a non-null value was returned AND that
   * there is now a key = 'keyUnderTest' on the server AND the author and item number are as expected.
   * Note: These last two tests are probably unnecessary but were at least interesting to validate.
   *
   */
  @Test
  public void testCacheLoader() {
    assertFalse("Region is not properly initialized", books.containsKeyOnServer(keyUnderTest));
    BookMaster newBook = books.get(keyUnderTest);
    assertNotNull("BookMasterCacheLoader failed to create book for key: " + keyUnderTest, newBook);
    assertTrue("Key " + keyUnderTest + " not found on server", books.containsKeyOnServer(keyUnderTest));
    assertEquals("BookMasterCacheLoader created the incorrect book", "Felipe Gutierrez", newBook.getAuthor());
    assertEquals("BookMasterCacheLoader created the incorrect book", keyUnderTest, Integer.valueOf(newBook.getItemNumber()));
  }

  /**
   * Asserts that when inserting a valid entry that no errors occur
   */
  @Test
  public void testValidatingCacheWriterSuccess() {
    books.put(124, new BookMaster(124, "A spy fiction thriller about a retrograde amnesiac who must discover who he is ", (float) 34.99, 2011, "Robert Ludlum", "Bourne Identity"));
  }

  @Test()
  /**
   * Asserts that when attempting to insert two entries with the same itemNumber, the second one fails with an expected
   * CacheWriterException.
   */
  public void testValidatingCacheWriterFailure() {
    books.put(124, new BookMaster(124, "An basic book to test validate", (float) 34.99, 2011, "You Student", "Duplicate Test"));
    try {
      books.put(125, new BookMaster(124, "An basic book to test validate ", (float) 34.99, 2011, "You Student", "Duplicate Test"));
      fail("Failed to throw an exception");
    } catch (Exception e) {
      assert e.getCause() instanceof CacheWriterException;
    } finally {
    	books.remove(124);
    	books.remove(125);
    }
  }
}
