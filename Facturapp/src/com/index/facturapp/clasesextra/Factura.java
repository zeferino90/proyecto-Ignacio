package com.index.facturapp.clasesextra;

public class Factura {
	private int numFact;
	//private Date data;
	private String estado;
	private Cliente cliente;
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
}