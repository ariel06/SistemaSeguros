package com.examen.entity;

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
public class Loan {
	
	private Integer id;
	private Double total;
	private Integer userId;
		
	
	/*   “id”: 1,
      “total”: 2500.00
      “userId”: 1
	*/
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public Double getTotal() {
		return total;
	}
	public void setTotal(Double total) {
		this.total = total;
	}
	public Integer getUserId() {
		return userId;
	}
	public void setUserId(Integer userId) {
		this.userId = userId;
	}
		
}