package com.examen.controller;

import java.util.NoSuchElementException;

import org.springframework.beans.TypeMismatchException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.BindException;
import org.springframework.web.HttpMediaTypeNotAcceptableException;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingPathVariableException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.ServletRequestBindingException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.context.request.async.AsyncRequestTimeoutException;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.multipart.support.MissingServletRequestPartException;
import org.springframework.web.servlet.NoHandlerFoundException;

import com.examen.exception.DuplicateKeyException;
import com.examen.exception.InternalServerError;
import com.examen.exception.InvalidFieldException;
import com.examen.response.BaseResponse;
//import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.exc.InvalidFormatException;


@ControllerAdvice
public class ExceptionsController {
	// Endpoint no encontrado > 404: no lo estoy capturando
	
	@ExceptionHandler(Exception.class)
	public ResponseEntity<BaseResponse> handleException(Exception ex, WebRequest request) throws Exception {
		BaseResponse<Void> response = new BaseResponse<Void>();
						
		HttpStatus status = getStatus(ex);	
		
		response.setCode(status.value());
		response.setMessage(status.name());
		response.setDescription(getMessageError(ex));			
		
		return ResponseEntity.status(status).body(response);
	}
	

	private HttpStatus getStatus(Exception ex) {
		HttpStatus status;
		
		if(isBadRequest(ex)) {
			status = HttpStatus.BAD_REQUEST;	
		}
		else if(isNotFound(ex)) {
			status = HttpStatus.NOT_FOUND;
		}
		else if(isInternalServerError(ex)) {
			status = HttpStatus.INTERNAL_SERVER_ERROR;
		}
		else if(isMethodNotAllowed(ex)) {
			status = HttpStatus.METHOD_NOT_ALLOWED;
		}
		else if(isUnsupportedMediaType(ex)) {
			status = HttpStatus.UNSUPPORTED_MEDIA_TYPE;
		}
		else if(isNotAcceptable(ex)) {
			status = HttpStatus.NOT_ACCEPTABLE;
		}
		else if(isServiceUnavaible(ex)) {
			status = HttpStatus.SERVICE_UNAVAILABLE;
		}
		else {
			status = HttpStatus.INTERNAL_SERVER_ERROR;	
		}
		
		return status;
	}

	private boolean isBadRequest(Exception ex) {		
		return ex instanceof DuplicateKeyException 
			|| ex instanceof MissingPathVariableException 
			|| ex instanceof MethodArgumentTypeMismatchException 
			|| ex instanceof HttpMessageNotReadableException
			|| ex instanceof InvalidFormatException	
			|| ex instanceof InvalidFieldException
			|| ex instanceof NoSuchElementException		
			|| ex instanceof MissingServletRequestParameterException	
			|| ex instanceof ServletRequestBindingException	
			|| ex instanceof TypeMismatchException		
			|| ex instanceof MethodArgumentNotValidException	
			|| ex instanceof MissingServletRequestPartException			
			|| ex instanceof BindException;
			//|| ex instanceof JsonParseException;		
	}
	
	private boolean isNotFound(Exception ex) {		
		return ex instanceof NoSuchElementException || ex instanceof NoHandlerFoundException;
	}
	
	
	private boolean isInternalServerError(Exception ex) {		
		return ex instanceof InternalServerError;
	}
	
	private boolean isMethodNotAllowed(Exception ex) {		
		return ex instanceof HttpRequestMethodNotSupportedException;
	}
	
	private boolean isUnsupportedMediaType(Exception ex) {
		return ex instanceof HttpMediaTypeNotSupportedException;
	}
	
	private boolean isNotAcceptable(Exception ex) {
		return ex instanceof HttpMediaTypeNotAcceptableException;
	}
	
	private boolean isServiceUnavaible(Exception ex) {
		return ex instanceof AsyncRequestTimeoutException;
	}
	
	private String getMessageError(Exception ex) {
		String mensajeError = "";		
				
		if (ex instanceof MissingPathVariableException) {
			mensajeError = "El RequestPath es obligatorio";
		}
		else if (ex instanceof MethodArgumentTypeMismatchException) {
			mensajeError = "RequestPath > El parámetro pasado no tiene un formato válido";
		}
		else if (ex instanceof HttpMessageNotReadableException) {
			if(ex.getCause() == null) {
				mensajeError = "El RequestBody es obligatorio";	
			}
			else {
				mensajeError = "El Formato del RequestBody debe ser JSON";
			}
			
		}
		/*else if(ex instanceof JsonParseException) {
			mensajeError = "El Formato del RequestBody debe ser JSON";
		}*/
		else if (ex instanceof InvalidFormatException) {
			mensajeError = "RequestBody > el valor del atributo no tiene un formato válido";
		}
		else {
			mensajeError = ex.getMessage();	
		}
		
		return mensajeError;
	}
}
