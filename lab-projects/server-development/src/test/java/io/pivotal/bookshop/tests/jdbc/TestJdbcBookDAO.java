package io.pivotal.bookshop.tests.jdbc;

import static org.junit.Assert.*;

import org.junit.Ignore;
import org.junit.Test;

import io.pivotal.bookshop.domain.BookMaster;
import io.pivotal.bookshop.events.JdbcBookDAO;

public class TestJdbcBookDAO {


	@Test 
	public void testBookRead() {
		JdbcBookDAO dao = new JdbcBookDAO();
		BookMaster book = dao.getBook(9876);
		assertNotNull("Failed to get Book", book);
		assertEquals("Got wrong book", "Felipe Gutierrez", book.getAuthor());
	}
	
	@Test
	public void testNewBookWrite() {
		BookMaster book = new BookMaster(8775,"Some Desc",(float) 12.50,2015,"Mark","A new book");
		JdbcBookDAO dao = new JdbcBookDAO();
		dao.persistBook(book);
		
		book = dao.getBook(8775);
		assertEquals("Insert failed", "Some Desc", book.getDescription());
	}
	
	@Test
	public void testExistingBookWrite() {
		JdbcBookDAO dao = new JdbcBookDAO();
		BookMaster book = dao.getBook(9876);
		book.setDescription("Changed Desc");
		dao.persistBook(book);
		
		book = dao.getBook(9876);
		assertEquals("Updated failed", "Changed Desc", book.getDescription());
	}

}
