package com.examen.dao;

import java.util.List;
import java.util.NoSuchElementException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.examen.entity.User;
import com.examen.exception.DuplicateKeyException;
import com.examen.mock.LoanMock;
import com.examen.mock.UserMock;


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
public class UserDAO {

	@Autowired
	private UserMock mock;
	
	@Autowired
	private LoanMock mockLoan;
	
	public List<User> listar() {
		
		return mock.listar();
	}

	public User crear(User persona) throws DuplicateKeyException {
		try {
			return mock.crear(persona);
		} 
		catch (DuplicateKeyException e) {
			throw new DuplicateKeyException("Ya existe una persona con ese email y/o ID");
		}
	}

	public void borrar(Integer id) throws NoSuchElementException {
		try {
			mock.borrar(id);
			mockLoan.deleteLoans(id);
		} 
		catch (NoSuchElementException e) {
			throw new NoSuchElementException("La persona a Borrar no existe");
		}
	}

	public void modificar(User persona) throws NoSuchElementException {
		try {
			mock.modificar(persona);
		} 
		catch (NoSuchElementException e) {
			throw new NoSuchElementException("La persona a Modificar no existe");
		}
	}

	public User getUser(Integer id) {
		User user=null;
		try {
		user= mock.listarPorId(id);
		user.setLoans(mockLoan.obtenerListaUserId(user.getId()));
		}catch(Exception ex) {
			System.out.println("Ha habido un error Error");
			ex.printStackTrace();
		}
		return user;
	}
}
