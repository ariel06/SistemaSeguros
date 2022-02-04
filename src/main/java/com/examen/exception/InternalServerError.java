package com.examen.exception;

public class InternalServerError extends Exception{
	
	public InternalServerError() {
		
	}
	
	public InternalServerError(String message) {
        super(message);
    }
}
