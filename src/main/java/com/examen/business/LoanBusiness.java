package com.examen.business;

import java.util.List;
import java.util.NoSuchElementException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.examen.dao.LoanDAO;
import com.examen.entity.Loan;
import com.examen.exception.DuplicateKeyException;
import com.examen.exception.InvalidFieldException;

/**
 * Toda clase Business se encarga de:
 * 
 * 		1) Escuchar los mensajes provenientes del Controller
 * 
 * 		2) Hacer las validaciones:
 * 			a) de los parámetros pasados desde el Controller
 * 			b) de negocio
 * 
 * 		3) Hacer la lógica de negocio
 * 
 * 		4) Comunicarse con el DAO  		
 * 
 *  	5) Responderle al Controller que lo invocó 
 * 
 * */

@Service // Indica que esta clase es un Business/Service
public class LoanBusiness {
	
	@Autowired
	private LoanDAO loanDAO;
	
	public List<Loan> listar(Integer page, Integer size, Integer user_id) {		
		return loanDAO.listar(user_id);
	}

	public Loan crear(Loan loan) throws DuplicateKeyException, InvalidFieldException {		
		try {
			validarCrear(loan);
		
			return loanDAO.crear(loan);
			
		} catch (InvalidFieldException e) {
			throw new InvalidFieldException("Verificar los datos de la loan > " + e.getMessage());
		}
	}

	private void validarCrear(Loan loan) throws InvalidFieldException {
					
		if(loan.getTotal()== null  ) {
			throw new InvalidFieldException("El campo  es obligatorio");
		}
		
		if(loan.getUserId() == null  ) {
			throw new InvalidFieldException("El campo es obligatorio");
		}
			
	}
		

	public void borrar(Integer id) throws NoSuchElementException, InvalidFieldException {
		try {
			validarBorrar(id);
		
			loanDAO.borrar(id);
			
		} catch (InvalidFieldException e) {
			throw new InvalidFieldException("Verificar los datos de la loan > " + e.getMessage());
		}
	}

	private void validarBorrar(Integer id) throws InvalidFieldException {
		if(id == null || id < 1) {
			throw new InvalidFieldException("El campo Id es obligatorio y debe ser mayor a 0");
		}
	}

	public void modificar(Integer id, Loan loan) throws NoSuchElementException, InvalidFieldException {
		try {
			validarModificar(id, loan);
		
			loan.setId(id);
			
			loanDAO.modificar(loan);
			
		} catch (InvalidFieldException e) {
			throw new InvalidFieldException("Verificar los datos de la loan > " + e.getMessage());
		}
	}

	private void validarModificar(Integer id, Loan loan) throws InvalidFieldException {
		if(id == null || id < 1) {
			throw new InvalidFieldException("El campo Id es obligatorio y debe ser mayor a 0");
		}
		
		if(loan.getTotal()== null  ) {
			throw new InvalidFieldException("El campo  es obligatorio");
		}
		
		if(loan.getUserId() == null  ) {
			throw new InvalidFieldException("El campo es obligatorio");
		}
		
	}
}
