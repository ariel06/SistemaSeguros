package com.examen.dao;

import java.util.List;
import java.util.NoSuchElementException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.examen.entity.Loan;
import com.examen.exception.DuplicateKeyException;
import com.examen.mock.LoanMock;


/**
 * Toda clase DAO se encarga de:
 * 
 * 		1) Escuchar los mensajes provenientes del Business
 * 
 * 		2) Comunicarse con la Base de Datos/Mock
 *  
 *  	3) Responderle al Business que lo invocó 
 * 
 * */

@Repository // Indica que esta clase es un DAO
public class LoanDAO {

	@Autowired
	private LoanMock mock;
	
	public List<Loan> listar( Integer user_id) {
		if(user_id.intValue()>0){
		    return mock.obtenerListaUserId(user_id);
		}else {
			return mock.listar();
		}
	}

	public Loan crear(Loan Loan) throws DuplicateKeyException {
		try {
			return mock.crear(Loan);
		} 
		catch (DuplicateKeyException e) {
			throw new DuplicateKeyException("Ya existe una Loan con ese email y/o ID");
		}
	}

	public void borrar(Integer id) throws NoSuchElementException {
		try {
			mock.borrar(id);
		} 
		catch (NoSuchElementException e) {
			throw new NoSuchElementException("La Loan a Borrar no existe");
		}
	}

	public void modificar(Loan Loan) throws NoSuchElementException {
		try {
			mock.modificar(Loan);
		} 
		catch (NoSuchElementException e) {
			throw new NoSuchElementException("La Loan a Modificar no existe");
		}
	}
}
