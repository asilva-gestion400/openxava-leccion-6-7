package com.gestion400.facturacion.modelo;

import java.math.*;
import java.util.*;

import javax.persistence.*;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.validation.constraints.*;

import org.hibernate.annotations.*;
import org.openxava.annotations.*;
import org.openxava.calculators.*;
import org.openxava.jpa.*;

import com.gestion400.facturacion.acciones.*;
import com.gestion400.facturacion.calculadores.*;
import com.gestion400.facturacion.validadores.*;

@Tab(properties = "numero, fecha, estado, cliente.numero, cliente.nombre, importeTotal",
	 defaultOrder = "${numero} DESC")

@Tab(name = "facturasPendientes",
	 properties = "numero, fecha, estado, cliente.numero, cliente.nombre, importeTotal",
	 defaultOrder = "${numero} DESC",
	 baseCondition = "${estado} = 'PENDIENTE'")

@Tab(name = "facturasPagadas",
	 properties = "numero, fecha, estado, cliente.numero, cliente.nombre, importeTotal",
	 defaultOrder = "${numero} DESC",
	 baseCondition = "${estado} = 'PAGADA'")

@View(members = 
	" anyo, numero, fecha;" +
	" importeTotal;" + 
// 	- nombreControlador.accion() -> visualizará la acción en la vista: añadir oculta="true" a la acción en el controlador
//	" cliente, Factura.verNombreCliente();" +	 
	" cliente;" + 
	" estado;" + 
	" detallesFactura;" + 
	" observaciones")
@View(name="Referencia", members="anyo, numero, fecha;")
@RemoveValidator(ValidadorBorrarFactura.class)
@Entity
public class Factura {
    
    /* BeanValidator: Antes de grabar confirma que el método devuelve true, si no lanza una excepción*/
	@AssertTrue(
		message="sin_detalles"	// i18n
	)
    private boolean isPosiblePagar() {
    	return !(EstadoFactura.PAGADA.equals(getEstado()) && getDetallesFactura().isEmpty());
    }

    /* */
    
	@Id
    @GeneratedValue(generator="system-uuid")
    @Hidden
    @GenericGenerator(name="system-uuid", strategy="uuid")
    @Column(length=32)
    private String oid;
 
    @Column(length=4)
    @DefaultValueCalculator(CurrentYearCalculator.class) // Año actual
    private int anyo;
 
    @Column(length=6)
    private int numero;
 
    @Required
    @DefaultValueCalculator(CurrentDateCalculator.class) // Fecha actual
    private Date fecha;
    
    @Required
    @DefaultValueCalculator(DefaultEstadoFacturaCalculator.class)
    @Enumerated(EnumType.STRING)
    private EstadoFactura estado;
 
    @Stereotype("MEMO")
    private String observaciones;
    
    @ManyToOne(fetch=FetchType.LAZY, optional=false) 
    @NoFrame @NoCreate @NoModify
    @ReferenceView("referencia")
    @OnChange(AlCambiarClienteEnFactura.class)
    private Cliente cliente;
    
    @OneToMany(mappedBy="factura", cascade = CascadeType.REMOVE)
    @ListProperties("producto.numero, producto.descripcion, cantidad, importe")
    @CollectionView("Factura") 
    private Collection<DetalleFactura> detallesFactura;

    @ReadOnly
    private BigDecimal importe;
    
    @Stereotype("MONEY") 
    public BigDecimal getImporteTotal() {
    	BigDecimal total = BigDecimal.ZERO;
    	if (!getDetallesFactura().isEmpty()) {
    		for(DetalleFactura detalle : getDetallesFactura()) {
    			total = total.add(detalle.getImporte());
    		}
    	}
    	return total;
    }
     
    public static Collection<Factura> findByEstado(EstadoFactura estado){
    	String sentencia = "from Factura where estado = :estado";
    	Query query = XPersistence.getManager().createQuery(sentencia);
    	query.setParameter("estado", estado);
    	return query.getResultList();
    }
    
    /* */
	public String getOid() {
		return oid;
	}

	public void setOid(String oid) {
		this.oid = oid;
	}

	public int getAnyo() {
		return anyo;
	}

	public void setAnyo(int anyo) {
		this.anyo = anyo;
	}

	public int getNumero() {
		return numero;
	}

	public void setNumero(int numero) {
		this.numero = numero;
	}

	public Date getFecha() {
		return fecha;
	}

	public void setFecha(Date fecha) {
		this.fecha = fecha;
	}

	public String getObservaciones() {
		return observaciones;
	}

	public void setObservaciones(String observaciones) {
		this.observaciones = observaciones;
	}

	public Cliente getCliente() {
		return cliente;
	}

	public void setCliente(Cliente cliente) {
		this.cliente = cliente;
	}

	public Collection<DetalleFactura> getDetallesFactura() {
		return detallesFactura;
	}

	public void setDetallesFactura(Collection<DetalleFactura> detallesFactura) {
		this.detallesFactura = detallesFactura;
	}

	public EstadoFactura getEstado() {
		return estado;
	}

	public void setEstado(EstadoFactura estado) {
		this.estado = estado;
	}

	public BigDecimal getImporte() {
		return importe;
	}

	public void setImporte(BigDecimal importe) {
		this.importe = importe;
	}

}
