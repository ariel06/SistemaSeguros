package com.examen.mock;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

import org.springframework.stereotype.Repository;

import com.examen.entity.Loan;
import com.examen.exception.DuplicateKeyException;

@Repository
public class LoanMock {
	private List<Loan> datos;
	private int id;
		
	public LoanMock() {
		datos = new ArrayList<>();
		Loan l1=new Loan();
		l1.setId(1);
		l1.setTotal(2500.00);
		l1.setUserId(1);

		Loan l2=new Loan();
		l2.setId(2);
		l2.setTotal(65120.75);
		l2.setUserId(1);
		
		datos.add(l1);
		datos.add(l2);
			
	}
	
	public List<Loan> obtenerListaUserId(Integer userId) {
		List<Loan> listLoansUser =  new ArrayList<>();
		
	/*	for (int i = 0; datos != null && i < datos.size(); i++) {
			if(datos.get(i).getUserId().equals(userId)) {
				listLoansUser.add(datos.get(i));
			}				
		}*/
		
	    for(Loan loan :datos) {
	    	if(loan.getUserId().equals(userId)) {
				listLoansUser.add(loan);
			}	
	      }
				
		return listLoansUser;
	}
	
	
	public List<Loan> listar() {
		return datos;
	}

	public Loan crear(Loan dato) throws DuplicateKeyException {
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
	
	private boolean registroYaExiste(Loan dato) {
		return buscarPorId(dato.getId()) != null ;
	}
	


	public void borrar(Integer id) {
		Loan dato = buscarPorId(id);
		
		if(dato != null) {
			datos.remove(dato);			
		}
		else {
			throw new NoSuchElementException();
		}
	}
	
	public Loan buscarPorId(Integer id) {
		for (int i = 0; datos != null && i < datos.size(); i++) {
			if(datos.get(i).getId().equals(id)) {
				return datos.get(i);
			}				
		}
				
		return null;
	}

	public void modificar(Loan dato) {
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

	public Loan listarPorId(Integer id) {
		Loan Loan = null;
		
		for (int i = 0; i < datos.size(); i++) {
			if(datos.get(i).getId().equals(id)) {
				Loan = datos.get(i);
			}				
		}
		
		return Loan;
	}

	public void deleteLoans(Integer userId) {
	    for(Loan loan :datos) {
	    	if(loan.getUserId().equals(userId)) {
	    		datos.remove(loan);
			}	
	      }
	
	}
	
}