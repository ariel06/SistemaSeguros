package com.examen.controller;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpInputMessage;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MissingPathVariableException;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import com.examen.entity.Persona;
import com.examen.response.BaseResponse;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.exc.InvalidFormatException;

@SpringBootTest // Esta anotación es para que inicialice las instancias correspondientes a los @Autowired de la clase de Test
public class ExceptionControllerTest {
	/**
	 * 1. Identificación de los métodos a probar de la clase ExceptionController:
	 * 		
	 * 		ResponseEntity<BaseResponse> handleException(Exception ex, WebRequest request) throws Exception ;
	 * 
	 * */
	
	/**
	 * 2. Identificación del dominio de prueba de c/método
	 * 
	
	 * 		
	 * 		ResponseEntity<BaseResponse> handleException(Exception ex, WebRequest request) throws Exception
	 * 			* ex puede ser:
	 * 				MissingPathVariableException		> El RequestPath es obligatorio 
	 * 
	 * 				MethodArgumentTypeMismatchException	> RequestPath > El parámetro pasado no tiene un formato válido
	 * 					
	 * 				HttpMessageNotReadableException		
	 * 					ex.cause() == null				> El RequestBody es obligatorio
	 * 					ex.cause() != null 				> El Formato del RequestBody debe ser JSON	
	 * 	
	 * 				InvalidFormatException				> RequestBody > el valor del atributo no tiene un formato válido
	 * 				
	 * 
	 * 		Para todos los casos puede ocurrir un error inesperado en el Servidor (Ejemplo: se cae la conexión a la base de datos) 
	 * 			=> debe saltar un error genérico (sin mostrar el trace, ni headers)
	 *  
	 * */
	

	@Autowired
	private ExceptionsController exceptionsController; 

	
	@Test
	public void testNoSePasaRequestPathCuandoEsObligatorio() throws Exception {
		
		MissingPathVariableException ex = new MissingPathVariableException(null, null);
		
		ResponseEntity<BaseResponse> response = exceptionsController.handleException(ex, null);
		
		HttpStatus status = HttpStatus.BAD_REQUEST;
		
		assertTrue(response.getBody().getCode().equals(status.value()));
		assertTrue(response.getBody().getMessage().equals(status.name()));
		assertTrue(response.getBody().getDescription().equals("El RequestPath es obligatorio"));
		assertTrue(response.getBody().getPayload() == null);
		
		assertDoesNotThrow(() -> exceptionsController.handleException(ex, null)); // Se verifica q el método no devuelve ninguna exception
	}
	
	@Test
	public void testRequestPathObligatorioFormatoInvalido() throws Exception {
		
		MethodArgumentTypeMismatchException ex = new MethodArgumentTypeMismatchException(null, null, null, null, null);
		
		ResponseEntity<BaseResponse> response = exceptionsController.handleException(ex, null);
		
		HttpStatus status = HttpStatus.BAD_REQUEST;
		
		assertTrue(response.getBody().getCode().equals(status.value()));
		assertTrue(response.getBody().getMessage().equals(status.name()));
		assertTrue(response.getBody().getDescription().equals("RequestPath > El parámetro pasado no tiene un formato válido"));
		assertTrue(response.getBody().getPayload() == null);
		
		assertDoesNotThrow(() -> exceptionsController.handleException(ex, null)); // Se verifica q el método no devuelve ninguna exception
	}
	
	@Test
	public void testRequestBodyNuloCuandoEsObligatorio() throws Exception {		
		HttpInputMessage httpInputMessage = null;
		HttpMessageNotReadableException exception = new HttpMessageNotReadableException(null, httpInputMessage);
		
		ResponseEntity<BaseResponse> response = exceptionsController.handleException(exception, null);
		
		HttpStatus status = HttpStatus.BAD_REQUEST;
		
		assertTrue(response.getBody().getCode().equals(status.value()));
		assertTrue(response.getBody().getMessage().equals(status.name()));
		assertTrue(response.getBody().getDescription().equals("El RequestBody es obligatorio"));
		assertTrue(response.getBody().getPayload() == null);
		
		assertDoesNotThrow(() -> exceptionsController.handleException(exception, null)); // Se verifica q el método no devuelve ninguna exception
	}
	
	@Test
	public void testRequestBodyObligatorioFormatoInvalido() throws Exception {
		Exception jsonException = null;
		String personaJsonFormatoInvalido = "asdasd";
		
		ObjectMapper objectMapper = new ObjectMapper();
		try {
			objectMapper.readValue(personaJsonFormatoInvalido, Persona.class);
		} catch (JsonMappingException e) {
			jsonException = e;	
		} catch (JsonProcessingException e) {
			jsonException = e;	
		};
		
		HttpInputMessage httpInputMessage = null;
		HttpMessageNotReadableException exception = new HttpMessageNotReadableException(jsonException.getMessage(), jsonException, httpInputMessage);
				
		
		ResponseEntity<BaseResponse> response = exceptionsController.handleException(exception, null);
		
		HttpStatus status = HttpStatus.BAD_REQUEST;
		
		assertTrue(response.getBody().getCode().equals(status.value()));
		assertTrue(response.getBody().getMessage().equals(status.name()));
		assertTrue(response.getBody().getDescription().equals("El Formato del RequestBody debe ser JSON"));
		assertTrue(response.getBody().getPayload() == null);
		
		assertDoesNotThrow(() -> exceptionsController.handleException(exception, null)); // Se verifica q el método no devuelve ninguna exception
	}
	
	@Test
	public void testRequestBodyObligatorioJSONOkFormatoDelValorAsociadoAlAtributoNoEsValido() throws Exception {		
		HttpInputMessage httpInputMessage = null;
		JsonParser p = null;
		InvalidFormatException exception = new InvalidFormatException(p, null, null, null);
		
		ResponseEntity<BaseResponse> response = exceptionsController.handleException(exception, null);
		
		HttpStatus status = HttpStatus.BAD_REQUEST;
		
		assertTrue(response.getBody().getCode().equals(status.value()));
		assertTrue(response.getBody().getMessage().equals(status.name()));
		assertTrue(response.getBody().getDescription().equals("RequestBody > el valor del atributo no tiene un formato válido"));
		assertTrue(response.getBody().getPayload() == null);
		
		assertDoesNotThrow(() -> exceptionsController.handleException(exception, null)); // Se verifica q el método no devuelve ninguna exception
	}
}
