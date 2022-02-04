package com.examen.response;

import com.examen.entity.Loan;
import com.examen.pagging.PagedResponseDto;

public class BaseResponse<T> {	// La T indica que el atributo payload puede ser cualquier clase.
	private Integer code;		// Código de respuesta HTTP
	private T payload;			// payload es del tipo genérico (puede ser cualquier clase). En nuestra API REST, se cargará null, 1 o N personas/películas.
	private String message;		// Mensaje corto que describe el resultado.
	private String description; // Mensaje que vé el usuario por pantalla.
	
	public Integer getCode() {
		return code;
	}
	public void setCode(Integer code) {
		this.code = code;
	}
	public T getPayload() {
		return payload;
	}
	public void setPayload(T payload) {
		this.payload = payload;
	}	
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	

}