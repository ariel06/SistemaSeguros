package com.examen.business;

import java.util.List;
import java.util.NoSuchElementException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.examen.dao.PersonaDAO;
import com.examen.entity.Persona;
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
public class PersonaBusiness {
	
	@Autowired
	private PersonaDAO personaDAO;
	
	public List<Persona> listar() {		
		return personaDAO.listar();
	}

	public Persona crear(Persona persona) throws DuplicateKeyException, InvalidFieldException {		
		try {
			validarCrear(persona);
		
			return personaDAO.crear(persona);
			
		} catch (InvalidFieldException e) {
			throw new InvalidFieldException("Verificar los datos de la persona > " + e.getMessage());
		}
	}

	private void validarCrear(Persona persona) throws InvalidFieldException {
	
		if(persona.getEmail() == null  || persona.getEmail().trim().isEmpty()) {
			throw new InvalidFieldException("El campo Email es obligatorio");
		}
		
		if(persona.getNombre() == null  || persona.getNombre().trim().isEmpty()) {
			throw new InvalidFieldException("El campo Nombre es obligatorio");
		}
		
		if(persona.getApellido() == null  || persona.getApellido().trim().isEmpty()) {
			throw new InvalidFieldException("El campo Apellido es obligatorio");
		}
		
		if(persona.getEdad() == null || persona.getEdad().intValue() < 0 || persona.getEdad().intValue() > 120) {
			throw new InvalidFieldException("El campo Edad es obligatorio y debe ser mayor a 0 y menor 121");
		}
		
		if(persona.getAltura() == null || persona.getAltura().doubleValue() < 0 || persona.getAltura().doubleValue() > 3) {
			throw new InvalidFieldException("El campo Altura es obligatorio y debe ser mayor a 0 y menor 3");
		}
	}
		

	public void borrar(Integer id) throws NoSuchElementException, InvalidFieldException {
		try {
			validarBorrar(id);
		
			personaDAO.borrar(id);
			
		} catch (InvalidFieldException e) {
			throw new InvalidFieldException("Verificar los datos de la persona > " + e.getMessage());
		}
	}

	private void validarBorrar(Integer id) throws InvalidFieldException {
		if(id == null || id < 1) {
			throw new InvalidFieldException("El campo Id es obligatorio y debe ser mayor a 0");
		}
	}

	public void modificar(Integer id, Persona persona) throws NoSuchElementException, InvalidFieldException {
		try {
			validarModificar(id, persona);
		
			persona.setId(id);
			
			personaDAO.modificar(persona);
			
		} catch (InvalidFieldException e) {
			throw new InvalidFieldException("Verificar los datos de la persona > " + e.getMessage());
		}
	}

	private void validarModificar(Integer id, Persona persona) throws InvalidFieldException {
		if(id == null || id < 1) {
			throw new InvalidFieldException("El campo Id es obligatorio y debe ser mayor a 0");
		}
		
		if(persona.getEmail() == null  || persona.getEmail().trim().isEmpty()) {
			throw new InvalidFieldException("El campo Email es obligatorio");
		}
		
		if(persona.getNombre() == null  || persona.getNombre().trim().isEmpty()) {
			throw new InvalidFieldException("El campo Nombre es obligatorio");
		}
		
		if(persona.getApellido() == null  || persona.getApellido().trim().isEmpty()) {
			throw new InvalidFieldException("El campo Apellido es obligatorio");
		}
		
		if(persona.getEdad() == null || persona.getEdad().intValue() < 0 || persona.getEdad().intValue() > 120) {
			throw new InvalidFieldException("El campo Edad es obligatorio y debe ser mayor a 0 y menor 121");
		}
		
		if(persona.getAltura() == null || persona.getAltura().doubleValue() < 0 || persona.getAltura().doubleValue() > 3) {
			throw new InvalidFieldException("El campo Altura es obligatorio y debe ser mayor a 0 y menor 3");
		}
	}
}
