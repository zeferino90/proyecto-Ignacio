package com.index.facturapp.clasesextra;

import java.util.Date;

public class Factura {
	private int numFact;
	private Date data;
	private String estado;
	private Cliente cliente;
	private String notas;
	public int getNumFact() {
		return numFact;
	}
	public void setNumFact(int numFact) {
		this.numFact = numFact;
	}
	public String getEstado() {
		return estado;
	}
	public void setEstado(String estado) {
		this.estado = estado;
	}
	public Cliente getCliente() {
		return cliente;
	}
	public void setCliente(Cliente cliente) {
		this.cliente = cliente;
	}
	public Date getData() {
		return data;
	}
	public void setData(Date data) {
		this.data = data;
	}
	public String getNotas() {
		return notas;
	}
	public void setNotas(String notas) {
		this.notas = notas;
	}
}
