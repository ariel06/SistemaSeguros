package com.examen.controller;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.examen.business.LoanBusiness;
import com.examen.entity.Loan;
import com.examen.exception.DuplicateKeyException;
import com.examen.exception.InternalServerError;
import com.examen.exception.InvalidFieldException;
import com.examen.pagging.PageDto;
import com.examen.pagging.PagedResponseDto;
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
@RequestMapping("/loans") // v0: Versión de la API | loans: nombre del servicio en plural
public class LoanController {
	
	@Autowired
	private LoanBusiness loanBusiness;
	
	
	@GetMapping(value="")
	public ResponseEntity<BaseResponse> listar(
			@RequestParam(value = "page") Integer page,
			@RequestParam(value = "page") Integer size,
			@RequestParam(value = "user_id", defaultValue = "0") Integer user_id
			
		/*	@PathVariable Integer page,
			@PathVariable Integer size,
			@PathVariable("user_id") Optional<Integer> user_id*/
			) throws Exception {
		
		BaseResponse<PagedResponseDto<Loan>> response = new BaseResponse<PagedResponseDto<Loan>>();
	
		try {
			List<Loan> loans  = loanBusiness.listar(page,size,user_id);
			
			response.setCode(HttpStatus.OK.value());
			
			
			
			if(loans != null && !loans.isEmpty()) {
				
				PagedResponseDto<Loan> pagegResp = new PagedResponseDto<Loan>();
				
				pagegResp.setItems(loans);
				
				pagegResp.setPageDto(new PageDto());
				pagegResp.getPageDto().setPage(1);
				pagegResp.getPageDto().setSize(50);
				pagegResp.getPageDto().setTotal(1500);
				
				
				response.setPayload(pagegResp);
				
				
				
				response.setMessage("Creditos encontrados");
				response.setDescription("Se encontró " + loans.size() + " Loan/s");
			}		
			else {				
				response.setMessage("NO_HAY_CREDITOS");
				response.setDescription("No se encontraron Loans");
			}
			
			return ResponseEntity.ok(response);
		} catch (Exception e) {
			throw new InternalServerError(e.getMessage());
		}
	}
	
	@PostMapping(value="")
	public ResponseEntity<BaseResponse> crear(@RequestBody Loan loan) throws Exception {
		
		BaseResponse<Loan> response = new BaseResponse<Loan>();
				
		try {
			Loan loanCreada = loanBusiness.crear(loan);
								
			response.setPayload(loanCreada);
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
		
		BaseResponse<Loan> response = new BaseResponse<Loan>();
		
		try {
			loanBusiness.borrar(id);
			
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
	public ResponseEntity<BaseResponse> modificar(@PathVariable Integer id, @RequestBody Loan loan) throws Exception {

		BaseResponse<Loan> response = new BaseResponse<Loan>();
				
		try {
			loanBusiness.modificar(id, loan);
			
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
