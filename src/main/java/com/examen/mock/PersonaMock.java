package com.examen.mock;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

import org.springframework.stereotype.Repository;

import com.examen.entity.Persona;
import com.examen.exception.DuplicateKeyException;

@Repository
public class PersonaMock {
	private List<Persona> datos;
	private int id;
		
	public PersonaMock() {
		datos = new ArrayList<>();
		id = 1;
	}
	
	public List<Persona> listar() {
		return datos;
	}

	public Persona crear(Persona dato) throws DuplicateKeyException {
		if(registroYaExiste(dato)) {
			throw new DuplicateKeyException();
		}
		else {
			dato.setId(id);
			datos.add(dato);
			id++;
			
			return dato;
		}
	}
	
	private boolean registroYaExiste(Persona dato) {
		return buscarPorId(dato.getId()) != null || buscarPorEmail(dato.getEmail()) != null;
	}
	
	public Persona buscarPorEmail(String email) {
		Persona dato = null;
		
		for (int i = 0; i < datos.size(); i++) {
			if(datos.get(i).getEmail().compareToIgnoreCase(email) == 0) {
				dato = datos.get(i);
			}				
		}
		
		return dato;
	}

	public void borrar(Integer id) {
		Persona dato = buscarPorId(id);
		
		if(dato != null) {
			datos.remove(dato);			
		}
		else {
			throw new NoSuchElementException();
		}
	}
	
	public Persona buscarPorId(Integer id) {
		for (int i = 0; datos != null && i < datos.size(); i++) {
			if(datos.get(i).getId().equals(id)) {
				return datos.get(i);
			}				
		}
				
		return null;
	}

	public void modificar(Persona dato) {
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

	public Persona listarPorId(Integer id) {
		Persona persona = null;
		
		for (int i = 0; i < datos.size(); i++) {
			if(datos.get(i).getId().equals(id)) {
				persona = datos.get(i);
			}				
		}
		
		return persona;
	}
	
}