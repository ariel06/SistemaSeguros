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
public class Persona {
	private Integer id;
	private String nombre;
	private String apellido;
	private Double altura;
	private String email;
	private Integer edad;
	
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public String getNombre() {
		return nombre;
	}
	public void setNombre(String nombre) {
		this.nombre = nombre;
	}
	public String getApellido() {
		return apellido;
	}
	public void setApellido(String apellido) {
		this.apellido = apellido;
	}
	public Double getAltura() {
		return altura;
	}
	public void setAltura(Double altura) {
		this.altura = altura;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public Integer getEdad() {
		return edad;
	}
	public void setEdad(Integer edad) {
		this.edad = edad;
	}	
}