package com.votaciones.entity;

import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import lombok.Data;

@Data
@Entity
@Table(name="candidatos")
public class Candidato {

	@Id
	@Column(name="cedula", nullable=false, updatable=false)
	private String cedula;
	
	@Column(name="nombre", nullable=false, updatable=true)
	private String nombre;
	
	@Column(name="apellidos", nullable=false, updatable=true)
	private String apellidos;
	
	@Column(name="telefono", nullable=false, updatable=true)
	private String telefono;
	
	@Column(name="foto", nullable=false, updatable=true, columnDefinition="TEXT")
	private String foto;
	
	@ManyToOne
	@JoinColumn(name="nit_partido", referencedColumnName="nit", nullable=true, updatable=true)
	private Partido partido;

	@OneToMany(cascade=CascadeType.ALL, mappedBy="candidato")
	private List<ResultadoCandidato> resultadoCandidatos;
}
