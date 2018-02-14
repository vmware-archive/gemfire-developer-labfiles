package io.pivotal.bookshop.dao;

public class QueryUncheckedException extends RuntimeException {
	public QueryUncheckedException() { }
	public QueryUncheckedException(Throwable cause) {
		super(cause);
	}
	
	public QueryUncheckedException(String message) {
		super(message);
	}
	
	public QueryUncheckedException(String message, Throwable cause) {
		super (message, cause);
	}
	

}
