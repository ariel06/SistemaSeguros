package com.examen.exception;

public class InvalidFieldException extends Exception{
	
	public InvalidFieldException() {
		
	}
	
	public InvalidFieldException(String message) {
        super(message);
    }
}
