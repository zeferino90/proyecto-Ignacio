package com.index.facturapp.clasesextra;

public class Cliente {
	private String dni;
	private String nombre;
	private String apellido1;
	private String apellido2;
	private String dir;
	private String localidad;
	
	public Cliente(){}
	
	public String getDni() {
		return dni;
	}
	public void setDni(String dni) {
		this.dni = dni;
	}
	public String getNombre() {
		return nombre;
	}
	public void setNombre(String nombre) {
		this.nombre = nombre;
	}
	public String getApellido1() {
		return apellido1;
	}
	public void setApellido1(String apellido1) {
		this.apellido1 = apellido1;
	}
	public String getApellido2() {
		return apellido2;
	}
	public void setApellido2(String apellido2) {
		this.apellido2 = apellido2;
	}
	public String getDir() {
		return dir;
	}
	public void setDir(String dir) {
		this.dir = dir;
	}
	public String getLocalidad() {
		return localidad;
	}
	public void setLocalidad(String localidad) {
		this.localidad = localidad;
	}

	@Override
	public String toString() {
		if(this.nombre != null){
			String aux = this.nombre;
			aux = aux.concat(" ");
			if(this.apellido1 != null){
				aux = aux.concat(this.apellido1);
				aux.concat(" ");
				if(this.apellido2 != null){
					aux = aux.concat(this.apellido2);
					aux.concat(" ");
					return aux;
				}
			}
		}
		
		
		
		return super.toString();
	}
	
	
}
