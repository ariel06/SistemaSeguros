package com.examen.controller;

import java.util.List;
import java.util.NoSuchElementException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.examen.business.PersonaBusiness;
import com.examen.entity.Persona;
import com.examen.exception.DuplicateKeyException;
import com.examen.exception.InternalServerError;
import com.examen.exception.InvalidFieldException;
import com.examen.response.BaseResponse;



/**
 * Toda clase Controller se encarga de:
 * 
 * 		1) Escuchar los mensajes provenientes del Cliente.
 * 
 * 		2) Enviar los mensajes provenientes el Cliente al Business y luego escuchar su respuesta.
 * 			NOTA: No hay que validar los mensajes, los mismos serán validados en el Business
 * 
 * 		3) Armar la respuesta para enviarle al Cliente que lo invocó.  		
 * 
 *  	4) Responderle al Cliente que lo invocó. 
 * 
 * */

@RestController  // Indica que esta clase es un Controller/Rest
@RequestMapping("/v0/personas") // v0: Versión de la API | personas: nombre del servicio en plural
public class PersonaController {
	
	@Autowired
	private PersonaBusiness personaBusiness;
	
	@GetMapping(value="")
	public ResponseEntity<BaseResponse> listar() throws Exception {
		
		BaseResponse<List<Persona>> response = new BaseResponse<List<Persona>>();
	
		try {
			List<Persona> personas = personaBusiness.listar();
			
			response.setCode(HttpStatus.OK.value());
			
			if(personas != null && !personas.isEmpty()) {
				response.setPayload(personas);
				
				response.setMessage("PERSONA_ENCONTRADA");
				response.setDescription("Se encontró " + personas.size() + " Persona/s");
			}		
			else {				
				response.setMessage("NO_HAY_PERSONAS");
				response.setDescription("No se encontraron Personas");
			}
			
			return ResponseEntity.ok(response);
		} catch (Exception e) {
			throw new InternalServerError(e.getMessage());
		}
	}
	
	@PostMapping(value="")
	public ResponseEntity<BaseResponse> crear(@RequestBody Persona persona) throws Exception {
		
		BaseResponse<Persona> response = new BaseResponse<Persona>();
				
		try {
			Persona personaCreada = personaBusiness.crear(persona);
								
			response.setPayload(personaCreada);
			response.setCode(HttpStatus.CREATED.value());   // Valores posibles: 200 (o sea: OK), 201 (o sea: CREATED)
			response.setMessage("PERSONA_CREADA");
			response.setDescription("Operación exitosa");
								
			return ResponseEntity.ok(response);
		} 
		catch (DuplicateKeyException e) {
			throw new DuplicateKeyException(e.getMessage());
		} 
		catch(InvalidFieldException e) {
			throw new InvalidFieldException(e.getMessage());
		}
		catch (Exception e) {
			throw new InternalServerError(e.getMessage());
		}
	}
	
	@DeleteMapping(value="/{id}")
	public ResponseEntity<BaseResponse> borrar(@PathVariable Integer id) throws Exception {
		
		BaseResponse<Persona> response = new BaseResponse<Persona>();
		
		try {
			personaBusiness.borrar(id);
			
			response.setCode(HttpStatus.OK.value());
			response.setMessage("PERSONA_BORRADA");
			response.setDescription("Operación exitosa");
			
			return ResponseEntity.ok(response);
		} 
		catch(InvalidFieldException e) {
			throw new InvalidFieldException(e.getMessage());
		}
		catch (NoSuchElementException e) {
			throw new NoSuchElementException(e.getMessage());
		} 
		catch (Exception e) {
			throw new InternalServerError(e.getMessage());
		}
	}

	@PutMapping(value="/{id}")
	public ResponseEntity<BaseResponse> modificar(@PathVariable Integer id, @RequestBody Persona persona) throws Exception {

		BaseResponse<Persona> response = new BaseResponse<Persona>();
				
		try {
			personaBusiness.modificar(id, persona);
			
			response.setCode(HttpStatus.OK.value());
			response.setMessage("PERSONA_MODIFICADA");
			response.setDescription("Operacion exitosa");
			
			return ResponseEntity.ok(response);
		}
		catch(InvalidFieldException e) {
			throw new InvalidFieldException(e.getMessage());
		}
		catch (NoSuchElementException e) {
			throw new NoSuchElementException(e.getMessage());
		} 
		catch (Exception e) {
			throw new InternalServerError(e.getMessage());
		}
	}
}
