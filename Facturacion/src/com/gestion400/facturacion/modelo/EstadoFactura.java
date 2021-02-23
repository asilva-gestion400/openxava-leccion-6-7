package com.gestion400.facturacion.modelo;

import org.openxava.util.*;

public enum EstadoFactura {
	PENDIENTE, PAGADA;
	
	@Override
	public String toString() {
		return Labels.get(this.name());
	}
}
