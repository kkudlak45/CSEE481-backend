package com.backend.server.exception;

public class DataServiceException extends Exception {

	private static final long serialVersionUID = 1L;
	
	public DataServiceException(Exception e) {
		super(e);
	}

}
