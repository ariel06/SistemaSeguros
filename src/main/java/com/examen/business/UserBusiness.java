package com.examen.business;

import java.util.List;
import java.util.NoSuchElementException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.examen.dao.UserDAO;
import com.examen.entity.User;
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
public class UserBusiness {
	
	@Autowired
	private UserDAO userDAO;
	
	public List<User> listar() {		
		return userDAO.listar();
	}

	public User crear(User user) throws DuplicateKeyException, InvalidFieldException {		
		try {
			validarCrear(user);
		
			return userDAO.crear(user);
			
		} catch (InvalidFieldException e) {
			throw new InvalidFieldException("Verificar los datos del user  " + e.getMessage());
		}
	}

	private void validarCrear(User user) throws InvalidFieldException {
	
		if(user.getEmail() == null  || user.getEmail().trim().isEmpty()) {
			throw new InvalidFieldException("El campo Email es obligatorio");
		}
		
		if(user.getEmail() == null  || user.getEmail().trim().isEmpty()) {
			throw new InvalidFieldException("El campo Email es obligatorio");
		}
		
		if(user.getFirstName() == null  || user.getFirstName().trim().isEmpty()) {
			throw new InvalidFieldException("El campo Nombre es obligatorio");
		}
		
		if(user.getLastName() == null  || user.getLastName().trim().isEmpty()) {
			throw new InvalidFieldException("El campo Apellido es obligatorio");
		}
	}
		

	public void borrar(Integer id) throws NoSuchElementException, InvalidFieldException {
		try {
			validarBorrar(id);
		
			userDAO.borrar(id);
			
		} catch (InvalidFieldException e) {
			throw new InvalidFieldException("Verificar los datos de la user > " + e.getMessage());
		}
	}

	private void validarBorrar(Integer id) throws InvalidFieldException {
		if(id == null || id < 1) {
			throw new InvalidFieldException("El campo Id es obligatorio y debe ser mayor a 0");
		}
	}

	public void modificar(Integer id, User user) throws NoSuchElementException, InvalidFieldException {
		try {
			validarModificar(id, user);
		
			user.setId(id);
			
			userDAO.modificar(user);
			
		} catch (InvalidFieldException e) {
			throw new InvalidFieldException("Verificar los datos de la user > " + e.getMessage());
		}
	}

	private void validarModificar(Integer id, User user) throws InvalidFieldException {
		if(id == null || id < 1) {
			throw new InvalidFieldException("El campo Id es obligatorio y debe ser mayor a 0");
		}
		
		if(user.getEmail() == null  || user.getEmail().trim().isEmpty()) {
			throw new InvalidFieldException("El campo Email es obligatorio");
		}
		
		if(user.getFirstName() == null  || user.getFirstName().trim().isEmpty()) {
			throw new InvalidFieldException("El campo Nombre es obligatorio");
		}
		
		if(user.getLastName() == null  || user.getLastName().trim().isEmpty()) {
			throw new InvalidFieldException("El campo Apellido es obligatorio");
		}
		
	}

	public User getUser(Integer id) {
		
		return userDAO.getUser(id);
	}
}
