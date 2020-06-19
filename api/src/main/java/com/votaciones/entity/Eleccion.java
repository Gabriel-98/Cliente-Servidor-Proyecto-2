package com.votaciones.entity;

import java.time.LocalDateTime;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.Data;

@Data
@Entity
@Table(name="elecciones")
public class Eleccion {
	
	@Id
	@Column(name="codigo", nullable=false, updatable=false)
	private String codigo;
	
	@Column(name="nombre", nullable=false, updatable=true)
	private String nombre;
	
	@Column(name="descripcion", nullable=false, updatable=true)
	private String descripcion;
		
	@Column(name="fecha_inicio", nullable=false, updatable=true)
	@JsonFormat(pattern="yyyy-MM-dd'T'HH:mm:ss")
	private LocalDateTime fechaInicio;
	
	@Column(name="fecha_fin", nullable=false, updatable=true)
	@JsonFormat(pattern="yyyy-MM-dd'T'HH:mm:ss")
	private LocalDateTime fechaFin;
	
	@Column(name="cancelada", nullable=false, updatable=true)
	private Boolean cancelada;
	
	@OneToMany(cascade=CascadeType.ALL, mappedBy="eleccion")
	private List<ResultadoCandidato> resultadoCandidatos;
	
	@OneToMany(cascade=CascadeType.ALL, mappedBy="eleccion")
	private List<Voto> votos;
}
