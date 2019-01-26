package io.pivotal.bookshop.events;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.apache.geode.internal.logging.LogService;
import org.apache.logging.log4j.Logger;

import io.pivotal.bookshop.domain.BookMaster;

/**
 * A basic DAO class to assist in managing interactions with the H2 Database.
 * This presumes that the database has been created and is running locally at
 * the given H2_CONNECTION_URL specified. Prior to using this class, be sure to
 * start the application in the database-server project. It can either be run by
 * right-mouse clicking on the project and selecting Run As -> Spring Boot App
 * or by running mvn package from the command line and then java -jar
 * target/database-server-1.0.0.RELEASE.jar
 * 
 * When deploying this and the other event handling components to the GemFire
 * server, ensure that a copy of the h2-latest.jar file is deployed to the
 * server before deploying any jar files you create that depend on H2. This
 * h2-latest.jar file will be found in the server-cluster folder.
 *
 */
public class JdbcBookDAO {
	private static String H2_CONNECTION_URL = "jdbc:h2:tcp://localhost:9096/~/testdb";
	private static final Logger logger = LogService.getLogger(JdbcBookDAO.class.getName());

	public JdbcBookDAO() {
		try {
			logger.info("Initializing JdbcBookDAO");
			Class.forName("org.h2.Driver");

		} catch (ClassNotFoundException e) {
			logger.error("Failed initialization", e);
		}
	}

	/**
	 * Attempts to return a BookMaster item from the configured database given
	 * the itemId, which should map to the primary key in the database.
	 * 
	 * @param itemId
	 *            The primary key for the desired row in the database
	 * 
	 * @return BookMaster for the given itemId or null if no entry is found
	 */
	public BookMaster getBook(Integer itemId) {
		logger.info("Attempting to load BookMaster for key: " + itemId);
		PreparedStatement statement = null;
		try {
			Connection connection = getConnenction();
			statement = connection.prepareStatement("select * from BOOKSHOP.BOOKS where ITEM_NUMBER=?");
			statement.setInt(1, itemId);
			ResultSet rs = statement.executeQuery();
			if (!rs.wasNull() && rs.next()) {

				BookMaster book = new BookMaster(itemId, rs.getString("description"), rs.getFloat("retail_cost"),
						rs.getInt("publication_date"), rs.getString("author"), rs.getString("title"));
				logger.info("Book loaded from Database: " + book);
				return book;
			} else {
				logger.warn("No book found for key: " + itemId);
				return null;
			}
		} catch (SQLException e) {
			logger.error(e.getMessage());
			return null;
		} finally {
			if (statement != null) {
				try {
					statement.close();
				} catch (SQLException e) {
					logger.error(e.getMessage());
				}
			}
		}
	}

	/**
	 * Attempts to persist the given book, either by inserting it if there
	 * currently isn't a row with the given itemId or by updating if the row
	 * already exists in the database
	 * 
	 * @param book
	 *            The BookMaster data to persist in the relational database
	 * 
	 * @return true if the persist succeeds, false otherwise
	 */
	public boolean persistBook(BookMaster book) {
		logger.info("Persisting Book: " + book);
		try {
			if (checkExists(book)) {
				return updateBook(book);
			} else {
				return insertBook(book);
			}
		} catch (Exception e) {
			logger.error("Failed to persist", e);
			return false;
		}
	}

	/**
	 * Attempts to delete the row from the database having the primary key of
	 * the value of the book's itemNumber field
	 * 
	 * @param book
	 *            The book to remove from the relational database
	 */
	public void deleteBook(BookMaster book) {
		String query = "delete from bookshop.books where item_number=?";
		logger.info("Deleting Book with itemId: " + book.getItemNumber());
		try {
			Connection connection = getConnenction();
			PreparedStatement preparedStmt = connection.prepareStatement(query);
			preparedStmt.setInt(1, book.getItemNumber());
			// execute the preparedstatement
			preparedStmt.execute();

			connection.close();

		} catch (SQLException e) {
			logger.error("Failed to delete book", e);
		}

	}

	/**
	 * Verifies whether the the book in question is in the relational database -
	 * based on the itemNumber
	 * 
	 * @param book
	 *            The BookMaster containing the itemNumber that will be
	 *            validated
	 * @return True if a row is found in the database for the given itemNumber,
	 *         false otherwise
	 * @throws Exception
	 *             if an error exists on the query or closing the query
	 */
	public boolean checkExists(BookMaster book) throws Exception {
		String query = "select count(*) from bookshop.books where item_number=?";
		boolean result = false;
		PreparedStatement preparedStmt = null;
		try {
			Connection connection = getConnenction();
			preparedStmt = connection.prepareStatement(query);
			preparedStmt.setInt(1, book.getItemNumber());
			// execute the preparedstatement
			ResultSet rs = preparedStmt.executeQuery();
			if (!rs.wasNull() && rs.next())
				if (rs.getInt(1) > 0)
					result = true;
		} catch (SQLException e) {
			logger.error("Error while executing query '%s'", e, query);
			throw new Exception(e);
		} finally {
			if (preparedStmt != null) {
				try {
					preparedStmt.close();
				} catch (SQLException e) {
					logger.error("Error while closing the prepared statement", e);
					throw new Exception(e);
				}
			}
		}
		return result;
	}

	private Connection getConnenction() throws SQLException {
		return DriverManager.getConnection(H2_CONNECTION_URL, "sa", "");
	}

	private boolean updateBook(BookMaster book) {
		String query = " update bookshop.books  set title=?, author=?, description=?, retail_cost=?, publication_date=? where item_number=?";
		PreparedStatement preparedStmt = null;
		logger.info("Persisting with update");
		try {
			Connection connection = getConnenction();
			preparedStmt = connection.prepareStatement(query);
			preparedStmt.setInt(6, book.getItemNumber());
			preparedStmt.setString(1, book.getTitle());
			preparedStmt.setString(2, book.getAuthor());
			preparedStmt.setString(3, book.getDescription());
			preparedStmt.setFloat(4, book.getRetailCost());
			preparedStmt.setInt(5, book.getYearPublished());

			// execute the preparedstatement
			preparedStmt.execute();
			return true;
		} catch (SQLException e) {
			logger.error("Error while executing query '{}'", e, query);
			return false;
		} finally {
			if (preparedStmt != null) {
				try {
					preparedStmt.close();
				} catch (SQLException e) {
					logger.error("Error while closing prepared statement", e);
				}
			}
		}

	}

	private boolean insertBook(BookMaster book) {
		String query = " insert into bookshop.books (item_number, title, author, description, retail_cost, publication_date)"
				+ " values (?, ?, ?, ?, ?, ?)";
		PreparedStatement preparedStmt = null;
		logger.info("Persisting with insert");
		try {
			Connection connection = getConnenction();
			preparedStmt = connection.prepareStatement(query);
			preparedStmt.setInt(1, book.getItemNumber());
			preparedStmt.setString(2, book.getTitle());
			preparedStmt.setString(3, book.getAuthor());
			preparedStmt.setString(4, book.getDescription());
			preparedStmt.setFloat(5, book.getRetailCost());
			preparedStmt.setInt(6, book.getYearPublished());

			// execute the preparedstatement
			preparedStmt.execute();
			return true;
		} catch (SQLException e) {
			logger.error("Error while executing query '{}'", e, query);
			return false;
		} finally {
			if (preparedStmt != null) {
				try {
					preparedStmt.close();
				} catch (SQLException e) {
					logger.error("Error closing prepared statement", e);
				}
			}
		}

	}
}
