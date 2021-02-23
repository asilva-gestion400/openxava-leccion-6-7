package com.gestion400.facturacion.acciones;

import java.math.*;
import java.util.*;

import org.openxava.actions.*;
import org.openxava.model.*;

import com.gestion400.facturacion.modelo.*;

/**
 * Acción para el modo lista de las FacturasPagadas
 */
public class VerTotal extends TabBaseAction{

	@Override
	public void execute() throws Exception {
		BigDecimal total = BigDecimal.ZERO;
		
		if (getSelectedKeys() == null || getSelectedKeys().length == 0) {
			// si no hay ninguna seleccionada visualizamos el total de todas las pagadas
			Collection<Factura> pagadas = Factura.findByEstado(EstadoFactura.PAGADA);
			for(Factura factura : pagadas) {
				total = total.add(factura.getImporteTotal());
			}
		}
		else {
			// si hay alguna seleccionada visualizamos solo el total de estas
			for (Map key: getSelectedKeys()) {
				Factura factura = (Factura)MapFacade.findEntity(getModelName(), key);
				total = total.add(factura.getImporteTotal());
			}
		}
		
		addInfo("VerTotal.total", total);	// i18n
	}
	
}
