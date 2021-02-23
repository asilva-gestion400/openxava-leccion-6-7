package com.ayto.multas.modelo;

import java.util.*;

import javax.persistence.*;

import org.openxava.annotations.*;
import org.openxava.util.*;

import com.ayto.multas.calculadores.*;
import com.ayto.multas.validadores.*;

@View(members="matricula, informacionVehiculo; marca, modelo; tipo; multas;")
@View(name="VehiculoSimple", members="matricula; marca, modelo;")
@View(name="VehiculoCompleta", extendsView = "VehiculoSimple", members="multas;")
@Entity
@RemoveValidator(ValidadorBorrarVehiculo.class)
public class Vehiculo {
	
	@Id @Column(length = 10)
	private String matricula;
	
	private String marca;
	
	private String modelo;

	@OneToMany(mappedBy = "vehiculoImplicado")
	@ListProperties("estado,anyo,fecha,importe")
	@ReadOnly 
	private Collection<Multa> multas;
	
	@DefaultValueCalculator(DefaultTipoVehiculo.class)
	private TipoVehiculo tipo;
	
	/* propiedad calculada */
	@Depends("marca,modelo,tipo")
	public String getInformacionVehiculo() {
		return (getTipo() != null ? Labels.get(getTipo().name()) : "") + " " + getMarca() + " " + getModelo();
	}
	
	/* */

	public String getMatricula() {
		return matricula;
	}

	public void setMatricula(String matricula) {
		this.matricula = matricula;
	}

	public String getMarca() {
		return marca == null ? "" : marca;
	}

	public void setMarca(String marca) {
		this.marca = marca;
	}

	public String getModelo() {
		return modelo;
	}

	public void setModelo(String modelo) {
		this.modelo = modelo;
	}

	public Collection<Multa> getMultas() {
		return multas;
	}

	public void setMultas(Collection<Multa> multas) {
		this.multas = multas;
	}

	public TipoVehiculo getTipo() {
		return tipo;
	}

	public void setTipo(TipoVehiculo tipo) {
		this.tipo = tipo;
	}

}
