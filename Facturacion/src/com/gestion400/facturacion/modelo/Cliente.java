package com.gestion400.facturacion.modelo;

import javax.persistence.*;

import org.openxava.annotations.*;
import org.openxava.jpa.*;


@View(members = "numero,nombre;direccion")
@View(name = "referencia", members = "numero,nombre")
@Entity
public class Cliente {
	
    @Id  // La propiedad numero es la clave.  Las claves son obligatorias (required) por defecto
    @Column(length=6)  // La longitud de columna se usa a nivel UI y a nivel DB
    private int numero;
 
    @Column(length=50) // La longitud de columna se usa a nivel UI y a nivel DB
    @Required  // Se mostrar� un error de validaci�n si la propiedad nombre se deja en blanco
    private String nombre;
    
    @NoFrame
    @Embedded // As� para referenciar a una clase incrustable
    private Direccion direccion; // Una referencia Java convencional
    
    public static Cliente findByNumero(Number numero) {
    	if (numero == null) return null;
    	try {
        	String sentencia = "from Cliente where numero = :numero";
        	Query query = XPersistence.getManager().createQuery(sentencia);
        	query.setParameter("numero", numero);
        	return (Cliente) query.getSingleResult();
    	}
		catch(NoResultException ex) {
			return null;
		}
    }
    
    /* */
 
    public int getNumero() {
        return numero;
    }
 
    public void setNumero(int numero) {
        this.numero = numero;
    }
 
    public String getNombre() {
        return nombre;
    }
 
    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

	public Direccion getDireccion() {
		return direccion;
	}

	public void setDireccion(Direccion direccion) {
		this.direccion = direccion;
	}
}
