package com.gestion400.facturacion.acciones;

import org.openxava.actions.*;

/**
 * Acción para el modo detalle
 */
public class VerNombreClienteEnFacturas extends ViewBaseAction{

	@Override
	public void execute() throws Exception {
		String cliente = getView().getValueString("cliente.nombre");
		addMessage("VerNombreClienteEnFacturas.ver", cliente);	// i18n 
	}

}
