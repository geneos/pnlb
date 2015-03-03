package org.eevolution.tools;

import java.math.BigDecimal;

public class ListadoCobranzasUtil {

	BigDecimal monto;
	String tipo;
	String descripcion;
	
	public ListadoCobranzasUtil(String t, String d, BigDecimal m) {
		tipo = t;
		descripcion = d;
		monto = m;
	}
	
	public String getDescripcion() {
		return descripcion;
	}
	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}
	public BigDecimal getMonto() {
		return monto;
	}
	public void setMonto(BigDecimal monto) {
		this.monto = monto;
	}
	public String getTipo() {
		return tipo;
	}
	public void setTipo(String tipo) {
		this.tipo = tipo;
	}
	
	
}
