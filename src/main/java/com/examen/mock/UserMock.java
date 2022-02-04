package com.examen.mock;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

import org.springframework.stereotype.Repository;

import com.examen.entity.Loan;
import com.examen.entity.Persona;
import com.examen.entity.User;
import com.examen.exception.DuplicateKeyException;

@Repository
public class UserMock {
	private List<User> datos;
	private int id=1;

		
	public UserMock() {
		datos = new ArrayList<>();
		User us1=new User();
		us1.setId(1);
		us1.setFirstName("Pepe");
		us1.setLastName("Argento");
		us1.setEmail("test@app.com.ar");
		datos.add(us1);
	
	}
	
	public List<User> listar() {
		return datos;
	}

	public User crear(User dato) throws DuplicateKeyException {
		if(registroYaExiste(dato)) {
			throw new DuplicateKeyException();
		}
		else {
			id++;
			dato.setId(id);
			datos.add(dato);
			
			
			return dato;
		}
	}
	
	private boolean registroYaExiste(User dato) {
		return buscarPorId(dato.getId()) != null || buscarPorEmail(dato.getEmail()) != null;
	}
	
	public User buscarPorEmail(String email) {
		User dato = null;
		
		for (int i = 0; i < datos.size(); i++) {
			if(datos.get(i).getEmail().compareToIgnoreCase(email) == 0) {
				dato = datos.get(i);
			}				
		}
		
		return dato;
	}

	public void borrar(Integer id) {
		User dato = buscarPorId(id);
		
		if(dato != null) {
			datos.remove(dato);			
		}
		else {
			throw new NoSuchElementException();
		}
	}
	
	public User buscarPorId(Integer id) {
		for (int i = 0; datos != null && i < datos.size(); i++) {
			if(datos.get(i).getId().equals(id)) {
				return datos.get(i);
			}				
		}
				
		return null;
	}

	public void modificar(User dato) {
		boolean registroModificado = false;
		for (int i = 0; !registroModificado && i < datos.size(); i++) {
			registroModificado = datos.get(i).getId().equals(dato.getId());			
			if(registroModificado) {
				datos.set(i, dato);
			}				
		}
		
		if(!registroModificado) {
			throw new NoSuchElementException();
		}
	}

	public User listarPorId(Integer id) {
		User user = null;
		
		for (int i = 0; i < datos.size(); i++) {
			if(datos.get(i).getId().equals(id)) {
				user = datos.get(i);
			}				
		}
		
		return user;
	}
	
	
}