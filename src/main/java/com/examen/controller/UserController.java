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

import com.examen.business.UserBusiness;
import com.examen.entity.User;
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
@RequestMapping("/users") // v0: Versión de la API | users: nombre del servicio en plural
public class UserController {
	
	@Autowired
	private UserBusiness userBusiness;
	
	@GetMapping(value="")
	public ResponseEntity<BaseResponse> listar() throws Exception {
		
		BaseResponse<List<User>> response = new BaseResponse<List<User>>();
	
		try {
			List<User> users = userBusiness.listar();
			
			response.setCode(HttpStatus.OK.value());
			
			if(users != null && !users.isEmpty()) {
				response.setPayload(users);
				
				response.setMessage("PERSONA_ENCONTRADA");
				response.setDescription("Se encontró " + users.size() + " User/s");
			}		
			else {				
				response.setMessage("NO_HAY_PERSONAS");
				response.setDescription("No se encontraron Users");
			}
			
			return ResponseEntity.ok(response);
		} catch (Exception e) {
			throw new InternalServerError(e.getMessage());
		}
	}
	
	
	@GetMapping(value="/{id}")
	public ResponseEntity<BaseResponse> get(@PathVariable Integer id) throws Exception {
		
		BaseResponse<User> response = new BaseResponse<User>();
	
		try {
			User user = userBusiness.getUser(id);
			
			response.setCode(HttpStatus.OK.value());
			
			if(user != null ) {
				response.setPayload(user);
				
				response.setMessage("PERSONA_ENCONTRADA");
				response.setDescription("Se encontró " + user.getId() + " User/s");
			}		
			else {				
				response.setMessage("NO_EXISTE_PERSONA");
				response.setDescription("No se encontraron Users");
			}
			
			return ResponseEntity.ok(response);
		} catch (Exception e) {
			throw new InternalServerError(e.getMessage());
		}
	}
	
	@PostMapping(value="")
	public ResponseEntity<BaseResponse> crear(@RequestBody User user) throws Exception {
		
		BaseResponse<User> response = new BaseResponse<User>();
				
		try {
			User userCreada = userBusiness.crear(user);
								
			response.setPayload(userCreada);
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
		
		BaseResponse<User> response = new BaseResponse<User>();
		
		try {
			userBusiness.borrar(id);
			
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
	public ResponseEntity<BaseResponse> modificar(@PathVariable Integer id, @RequestBody User user) throws Exception {

		BaseResponse<User> response = new BaseResponse<User>();
				
		try {
			userBusiness.modificar(id, user);
			
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
