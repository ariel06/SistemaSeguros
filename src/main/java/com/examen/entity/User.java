package com.examen.entity;

import java.util.List;

/*
 	Clase del tipo Entidad 
 	
 		* no hay tipo de datos nativos (int, char, double, etc..) xq el dato podría ser null y no lo permiten.
 		* en su lugar se pone la Clase equivalente: 
 		* 	int 	-> Integer
 		* 	double 	-> Double
 		* 	boolean -> Boolean
 		* 	etc
 		*  
 	se crea 
 	
*/ 
public class User {
	
	private Integer id;
	private String firstName;
	private String lastName;
	private String email;
	private List<Loan> loans;
	
	public List<Loan> getLoans() {
		return loans;
	}
	public void setLoans(List<Loan> loans) {
		this.loans = loans;
	}
	
	
	public String getFirstName() {
		return firstName;
	}
	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}
	public String getLastName() {
		return lastName;
	}
	public void setLastName(String lastName) {
		this.lastName = lastName;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}

}