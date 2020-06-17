package com.votaciones.entity;

import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import lombok.Data;

@Data
@Entity
@Table(name="partidos")
public class Partido {

	@Id
	@Column(name="nit", nullable=false, updatable=false)
	private String nit;
	
	@Column(name="nombre", nullable=false, updatable=true)
	private String nombre;
	
	@Column(name="direccion", nullable=false, updatable=true)
	private String direccion;
	
	@Column(name="telefono", nullable=false, updatable=true)
	private String telefono;
	
	@Column(name="administracion", nullable=false, updatable=true)
	private String administracion;
	
	@Column(name="foto", nullable=false, updatable=true, columnDefinition="TEXT")
	private String foto;

	@OneToMany(cascade=CascadeType.ALL, mappedBy="partido")
	private List<Candidato> candidatos;
}
