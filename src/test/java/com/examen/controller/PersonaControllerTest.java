package com.examen.controller;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.examen.business.PersonaBusiness;
import com.examen.entity.Persona;
import com.examen.exception.DuplicateKeyException;
import com.examen.exception.InternalServerError;
import com.examen.exception.InvalidFieldException;
import com.examen.response.BaseResponse;

@SpringBootTest // Esta anotación es para que inicialice las instancias correspondientes a los @Autowired de la clase de Test
public class PersonaControllerTest {
	// Para saber sobre como armar un test, leer: NotasFundamentalesSobreTests.txt 

	/**
	 * 1. Identificación de los métodos a probar de la clase PersonaController:
	 * 		
	 * 		ResponseEntity<BaseResponse> listar();
	 * 
	 * 		ResponseEntity<BaseResponse> crear(@RequestBody Persona persona)
	 * 
	 * 		ResponseEntity<BaseResponse> borrar(@PathVariable Integer id)
	 * 
	 * 		ResponseEntity<BaseResponse> modificar(@PathVariable Integer id, @RequestBody Persona persona) 
	 * 
	 * */
	
	/**
	 * 2. Identificación del dominio de prueba de c/método
	 * 
	 * 		ResponseEntity<BaseResponse> listar() 
	 * 			* No se le pasan parámetros: 
	 * 				=> OK (CASO FELIZ). Solo hay 2 casos felices (devuelve datos o no).
	 * 		
	 * 		ResponseEntity<BaseResponse> crear(@RequestBody Persona persona) 
	 * 			* la persona pasada en el Body:
	 * 				=> no es un objeto del tipo Persona => debe saltar una excepción
	 * 				=> es objeto Persona (puede ser null o no) 
	 * 					=>  Si == null => debe saltar un error de validacionCrear
	 * 					=>  Si != null 
	 * 						=> puede saltar una error de validacionCrear (validación de Campos)
	 * 						=> caso contrario => OK (CASO FELIZ). Usuario Creado 
	 * 
	 * 		ResponseEntity<BaseResponse> borrar(@PathVariable Integer id)
	 * 			* el id pasado en el Path:
	 * 				=> no es un objeto del tipo Integer => debe saltar una excepción
	 * 				=> es un objeto del tipo Integer (puede ser null o no) 
	 * 					=>  Si == null => debe saltar un error de validacionBorrar
	 * 					=>  Si != null  
	 * 							=> Persona no encontrada => Lanza exception NoSuchElementException (recurso no encontrado)
	 * 							=> caso contrario => OK (CASO FELIZ). Usuario Borrado 
	 * 
	 *  
	 *  	ResponseEntity<BaseResponse> modificar(@PathVariable Integer id, @RequestBody Persona persona)
	 * 			* el id pasado en el Path:
	 * 				=> no es un objeto del tipo Integer => debe saltar una excepción
	 * 				=> es un objeto del tipo Integer (puede ser null o no) 
	 * 					=>  Si == null => debe saltar un error de validacionModificar
	 * 					=>  Si != null
	 * 							=> Persona no encontrada => Lanza exception NoSuchElementException (recurso no encontrado)
	 * 							=> Caso contrario: NECESARIO PARA EL CASO FELIZ
	 * 
	 * 			* la persona pasada en el Body:
	 * 				=> no es un objeto del tipo Persona => debe saltar una excepción
	 * 				=> es objeto Persona (puede ser null o no) 
	 * 					=>  Si == null => debe saltar un error de validacionModificar
	 * 					=>  Si != null 
	 * 						=> puede saltar una error de validacionCrear (validación de Campos)
	 * 						=> caso contrario  => NECESARIO PARA EL CASO FELIZ		
	 * 
	 * 
	 * 		Para todos los casos puede ocurrir un error inesperado en el Servidor (Ejemplo: se cae la conexión a la base de datos) 
	 * 			=> debe saltar un error genérico (sin mostrar el trace, ni headers)
	 *  
	 * */
		
	
	@Autowired
	private PersonaController personaController; // Unica Capa que se desea probar (en este caso es el Controller) 

	@MockBean 
	private PersonaBusiness personaBusinessMock; // Como se desea probar el Controller y este llama al Business => hay q Mockear esas llamadas para desacoplar las capas (Controller-Business) 

	
	private final Integer ID_PERSONA_EXISTENTE = 1;
	private final Integer ID_PERSONA_NO_EXISTENTE = -1;
	
	private List<Persona> getPersonas() {
		List<Persona> personas = new ArrayList<>();
		
		Persona persona = new Persona();
		persona.setId(1);
		persona.setNombre("Armando");
		persona.setApellido("Casas");
		persona.setEdad(33);
		persona.setEmail("armando.casas@gmail.com");
		persona.setAltura(1.77);
			
		personas.add(persona);		
		
		return personas;
	}
	
	private Persona getPersonaNoValida() {
		Persona persona = new Persona();
		persona.setId(1);
		persona.setNombre("Armando");
		persona.setApellido("Casas");
		persona.setEdad(33);
		persona.setEmail(null);
		persona.setAltura(1.77);
		
		return persona;
	}
	
	private Persona getPersonaOK() {
		Persona persona = new Persona();
		persona.setId(1);
		persona.setNombre("Armando");
		persona.setApellido("Casas");
		persona.setEdad(33);
		persona.setEmail("armando.casas@gmail.com");
		persona.setAltura(1.77);
		
		return persona;
	}
	
	
	////////////////////////////////////////////////////////////////////////////////////////////
	/**
	 * Método a probar: 
	 * 
	 * 		ResponseEntity<BaseResponse> listar() 
	 * 			* No se le pasan parámetros: 
	 * 				=> OK (CASO FELIZ). Solo hay 2 casos felices (devuelve datos o no).
	 * 
	 * 			* error inesperado:
	 * 				=> debe saltar un error genérico (sin mostrar el trace, ni headers)
	 * @throws Exception 
	 * */	
	@Test
	public void testListarSinDatos() throws Exception {
		Mockito.when(personaBusinessMock.listar()).thenReturn(null); // Para decirle al interceptor q reemplace 'la llamada listar() del Business desde el controller' por un null. 
		
		ResponseEntity<BaseResponse> response = personaController.listar(); // Método a testear
		 	
		
		//assertTrue(response != null);		
		assertTrue(response.getBody().getCode().equals(HttpStatus.OK.value()));
		assertTrue(response.getBody().getMessage().equals("NO_HAY_PERSONAS"));
		assertTrue(response.getBody().getPayload() == null);
		
		assertDoesNotThrow(() -> personaController.listar()); // Se verifica q el método no devuelve ninguna exception
	}
	
	@Test
	public void testListarConDatos() throws Exception {		 
		Mockito.when(personaBusinessMock.listar()).thenReturn(getPersonas()); // Para decirle al interceptor q reemplace 'la llamada listar() del Business desde el controller' por 'getPersonas()' implementado en este Test. 
		
		ResponseEntity<BaseResponse> response = personaController.listar(); // Método a testear
		
		assertTrue(response.getBody().getCode().equals(HttpStatus.OK.value()));
		assertTrue(response.getBody().getMessage().equals("PERSONA_ENCONTRADA"));
		assertNotNull(response.getBody().getPayload());
		
		assertDoesNotThrow(() -> personaController.listar()); // Se verifica q el método no devuelve ninguna exception
	}
	
	@Test
	public void testListarErrorInesperado() {		
		try {
			Mockito.doThrow(Exception.class).when(personaBusinessMock).listar();
		} catch (Exception e) {
			;
		}
		
		assertThrows(InternalServerError.class, () -> personaController.listar());
	}
	////////////////////////////////////////////////////////////////////////////////////////////
	
		
	////////////////////////////////////////////////////////////////////////////////////////////
	/**
	 * Método a probar: 
	 * 
	 * 		ResponseEntity<BaseResponse> crear(@RequestBody Persona persona) 
	 * 			* la persona pasada en el Body:
	 * 				=> no es un objeto del tipo Persona => debe saltar una excepción
	 * 				=> es objeto Persona (puede ser null o no) 
	 * 					=>  Si == null => debe saltar un error de validacionCrear
	 * 					=>  Si != null 
	 * 						=> puede saltar una error de validacionCrear (validación de Campos o registro duplicado)
	 * 						=> caso contrario => OK (CASO FELIZ). Usuario Creado
	 *  
	 * 			* error inesperado:
	 * 				=> debe saltar un error genérico (sin mostrar el trace, ni headers)
	 **/
	@Test
	public void testCrearPersonaCorrupta() {
		// TODO: Lo dejaremos para mas adelante
	}	
	
	@Test
	public void testCrearPersonaNoCorruptaPeroNull() {
		Persona persona = null;
				
		try {
			Mockito.when(personaBusinessMock.crear(persona)).thenThrow(InvalidFieldException.class);
		} catch (Exception e) {
			;
		}
		
		assertThrows(InvalidFieldException.class, () -> personaController.crear(persona));
	}
		
	@Test
	public void testCrearPersonaNoCorruptaPeroNotNullError() {
		Persona requestBodyPersona = getPersonaNoValida();
		
		try {
			Mockito.when(personaBusinessMock.crear(requestBodyPersona)).thenThrow(InvalidFieldException.class);
		} catch (Exception e) {
			;
		}
		
		assertThrows(InvalidFieldException.class, () -> personaController.crear(requestBodyPersona));
	}
		
	@Test
	public void testCrearPersonaNoCorruptaPeroNotNullDuplicada() {
		Persona requestBodyPersona = getPersonaOK();
		
		try {
			Mockito.when(personaBusinessMock.crear(requestBodyPersona)).thenThrow(DuplicateKeyException.class);
		} catch (Exception e) {
			;
		}
		
		assertThrows(DuplicateKeyException.class, () -> personaController.crear(requestBodyPersona));
	}
	
	@Test
	public void testCrearPersonaNoCorruptaPeroNotNullOK() throws Exception {
		Persona requestBodyPersona = getPersonaOK();
		
		try {
			Mockito.when(personaBusinessMock.crear(requestBodyPersona)).thenReturn(getPersonaOK());
		} catch (Exception e) {
			;
		}
		
		ResponseEntity<BaseResponse> response = personaController.crear(requestBodyPersona);
		
		assertEquals(HttpStatus.CREATED.value(), response.getBody().getCode());
		assertEquals("PERSONA_CREADA", response.getBody().getMessage());
		
		Persona personaCargada = (Persona) response.getBody().getPayload();
				
		assertEquals(requestBodyPersona.getId(), personaCargada.getId());
		assertEquals(requestBodyPersona.getEmail(), personaCargada.getEmail());
		
		assertDoesNotThrow(() -> personaController.crear(requestBodyPersona)); // Se verifica q el método no devuelve ninguna exception
	}
		
	@Test
	public void testCrearErrorInesperado() {
		Persona requestBodyPersona = getPersonaOK();
		
		try {
			Mockito.doThrow(Exception.class).when(personaBusinessMock).crear(requestBodyPersona);
		} catch (Exception e) {
			;
		}
		
		assertThrows(InternalServerError.class, () -> personaController.crear(requestBodyPersona));
	}
	////////////////////////////////////////////////////////////////////////////////////////////
	
	////////////////////////////////////////////////////////////////////////////////////////////
	
	
	////////////////////////////////////////////////////////////////////////////////////////////
	/**
	 * Método a probar: 
	 * 
	 * 		ResponseEntity<BaseResponse> borrar(@PathVariable Integer id)
	 * 			* el id pasado en el Path:
	 * 				=> no es un objeto del tipo Integer => debe saltar una excepción
	 * 				=> es un objeto del tipo Integer (puede ser null o no) 
	 * 					=>  Si == null => debe saltar un error de validacionBorrar
	 * 					=>  Si != null 
	 * 						No Existe => debe saltar un error por no es posible borrar algo que no existe    
	 * 						Existe	  => OK (CASO FELIZ). Usuario Borrado
	 * 
	 * 			* error inesperado:
	 * 				=> debe saltar un error genérico (sin mostrar el trace, ni headers)
	 * */
	@Test
	public void testBorrarPersonaCorrupta() {
		// TODO: Lo dejaremos para mas adelante	
	}
	
	@Test
	public void testBorrarPersonaNoCorruptaNull() {		
		try {
			Mockito.doThrow(InvalidFieldException.class).when(personaBusinessMock).borrar(null);
		} catch (Exception e) {
			;
		}
		
		assertThrows(InvalidFieldException.class, () -> personaController.borrar(null));
	}
	
	@Test
	public void testBorrarPersonaNoCorruptaPeroNotNulNoExistente() {
		try {
			Mockito.doThrow(NoSuchElementException.class).when(personaBusinessMock).borrar(ID_PERSONA_NO_EXISTENTE);
		} catch (Exception e) {
			;
		}
				
		assertThrows(NoSuchElementException.class, () -> personaController.borrar(ID_PERSONA_NO_EXISTENTE));
	}
	
	@Test
	public void testBorrarPersonaNoCorruptaPeroNotNullOK() throws Exception {		
		try {
			Mockito.doNothing().when(personaBusinessMock).borrar(ID_PERSONA_EXISTENTE);
		} catch (Exception e) {
			;
		}
		
		ResponseEntity<BaseResponse> response = personaController.borrar(ID_PERSONA_EXISTENTE);
		
		assertEquals(HttpStatus.OK.value(), response.getBody().getCode());
		assertEquals("PERSONA_BORRADA", response.getBody().getMessage());		
		
		assertDoesNotThrow(() -> personaController.borrar(ID_PERSONA_EXISTENTE)); // Se verifica q el método no devuelve ninguna exception
	}
	
	@Test
	public void testBorrarPersonaNoCorruptaPeroNotNullErrorInesperado() throws Exception {		
		try {
			Mockito.doThrow(Exception.class).when(personaBusinessMock).borrar(ID_PERSONA_EXISTENTE);
		} catch (Exception e) {
			;
		}
		
		assertThrows(InternalServerError.class, () -> personaController.borrar(ID_PERSONA_EXISTENTE));
	}
	////////////////////////////////////////////////////////////////////////////////////////////
	
	////////////////////////////////////////////////////////////////////////////////////////////
	/**
	 *  	ResponseEntity<BaseResponse> modificar(@PathVariable Integer id, @RequestBody Persona persona)
	 * 			* el id pasado en el Path:
	 * 				=> no es un objeto del tipo Integer => debe saltar una excepción
	 * 				=> es un objeto del tipo Integer (puede ser null o no) 
	 * 					=>  Si == null => debe saltar un error de validacionModificar
	 * 					=>  Si != null
	 * 							=> Persona no encontrada => Lanza exception NoSuchElementException (recurso no encontrado)
	 * 							=> Caso contrario: NECESARIO PARA EL CASO FELIZ
	 * 
	 * 			* la persona pasada en el Body:
	 * 				=> no es un objeto del tipo Persona => debe saltar una excepción
	 * 				=> es objeto Persona (puede ser null o no) 
	 * 					=>  Si == null => debe saltar un error de validacionModificar
	 * 					=>  Si != null 
	 * 						=> puede saltar una error de validacionCrear (validación de Campos)
	 * 						=> caso contrario  => NECESARIO PARA EL CASO FELIZ		
	 * 
	 * 
	 * 			* error inesperado:
	 * 				=> debe saltar un error genérico (sin mostrar el trace, ni headers)
	 *  
	 * 
	 * */
	@Test
	public void testModificarPersonaCorrupta() {
		// TODO: Lo dejaremos para mas adelante	
	}
	
	@Test
	public void testModificarIdPersonaNull() {
		Integer idPersona = null;
		Persona datosPersonaNull = null;
		
		try {
			Mockito.doThrow(InvalidFieldException.class).when(personaBusinessMock).modificar(idPersona, datosPersonaNull);
		} catch (Exception e) {
			;
		}
		assertThrows(InvalidFieldException.class, () -> personaController.modificar(idPersona, datosPersonaNull));
		
		
		Persona datosPersonaOk = getPersonaOK();		
		
		try {
			Mockito.doThrow(InvalidFieldException.class).when(personaBusinessMock).modificar(idPersona, datosPersonaOk);
		} catch (Exception e) {
			;
		}
		
		assertThrows(InvalidFieldException.class, () -> personaController.modificar(idPersona, datosPersonaOk));
	}
	
	@Test
	public void testModificarIdPersonaNotNullInexistente() {
		Integer idPersona = ID_PERSONA_NO_EXISTENTE;
		Persona datosPersonaNull = null;
		
		try {
			Mockito.doThrow(InvalidFieldException.class).when(personaBusinessMock).modificar(idPersona, datosPersonaNull);
		} catch (Exception e) {
			;
		}
		assertThrows(InvalidFieldException.class, () -> personaController.modificar(idPersona, datosPersonaNull));
		
		Persona datosPersonaOk = getPersonaOK();
		
		try {
			Mockito.doThrow(NoSuchElementException.class).when(personaBusinessMock).modificar(idPersona, datosPersonaOk);
		} catch (Exception e) {
			;
		}
		
		assertThrows(NoSuchElementException.class, () -> personaController.modificar(idPersona, datosPersonaOk));
	}
	
	@Test
	public void testModificarIdPersonaNotNullIExistente() throws Exception {
		Integer idPersona = ID_PERSONA_EXISTENTE;
		Persona datosPersonaNoValida = getPersonaNoValida();
		
		try {
			Mockito.doThrow(InvalidFieldException.class).when(personaBusinessMock).modificar(idPersona, datosPersonaNoValida);
		} catch (Exception e) {
			;
		}
		assertThrows(InvalidFieldException.class, () -> personaController.modificar(idPersona, datosPersonaNoValida));
		
		
		Persona datosPersonaValida = getPersonaOK();
		
		try {
			Mockito.doNothing().when(personaBusinessMock).modificar(idPersona, datosPersonaValida);
		} catch (NoSuchElementException | InvalidFieldException e) {
			;
		}
		
		ResponseEntity<BaseResponse> response = personaController.modificar(idPersona, datosPersonaValida);
		
		assertEquals(HttpStatus.OK.value(), response.getBody().getCode());
		assertEquals("PERSONA_MODIFICADA", response.getBody().getMessage());
		
		assertDoesNotThrow(() -> personaController.modificar(idPersona, datosPersonaValida)); // Se verifica q el método no devuelve ninguna exception
	}
	
	@Test
	public void testModificarPersonaBodyCorrupto() {
		// TODO: Lo dejaremos para mas adelante
	}
	
	@Test
	public void testModificarPersonaBodyNoCorruptoErrorValidacion() {
		Persona requestBodyPersona = getPersonaNoValida();
		
		try {
			Mockito.doThrow(InvalidFieldException.class).when(personaBusinessMock).modificar(ID_PERSONA_EXISTENTE, requestBodyPersona);
		} catch (Exception e) {
			;
		}
		
		assertThrows(InvalidFieldException.class, () -> personaController.modificar(ID_PERSONA_EXISTENTE, requestBodyPersona));
	}
	
	
	@Test
	public void testModificarPersonaNoCorruptaPeroNotNullOK() throws Exception {		
		Persona datosPersona = getPersonaOK();
				
		try {
			Mockito.doNothing().when(personaBusinessMock).modificar(ID_PERSONA_EXISTENTE, datosPersona);
		} catch (Exception e) {
			;
		}
		
		ResponseEntity<BaseResponse> response = personaController.modificar(ID_PERSONA_EXISTENTE, datosPersona);
		
		assertEquals(HttpStatus.OK.value(), response.getBody().getCode());
		assertEquals("PERSONA_MODIFICADA", response.getBody().getMessage());	
		
		assertDoesNotThrow(() -> personaController.modificar(ID_PERSONA_EXISTENTE, datosPersona)); // Se verifica q el método no devuelve ninguna exception
	}
	
	@Test
	public void testModificarPersonaNoCorruptaPeroNotNullErrorInesperado() {		
		Persona datosPersona = getPersonaOK();
		
		try {
			Mockito.doThrow(Exception.class).when(personaBusinessMock).modificar(ID_PERSONA_EXISTENTE, datosPersona);
		} catch (Exception e) {
			;
		}
		
		assertThrows(InternalServerError.class, () -> personaController.modificar(ID_PERSONA_EXISTENTE, datosPersona));
	}
	
	////////////////////////////////////////////////////////////////////////////////////////////
}
