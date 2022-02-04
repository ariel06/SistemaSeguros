package com.examen.exception;

public class DuplicateKeyException extends Exception{
	
	public DuplicateKeyException() {
		
	}
	
	public DuplicateKeyException(String message) {
        super(message);
    }
}
