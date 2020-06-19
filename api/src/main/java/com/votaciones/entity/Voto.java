package com.votaciones.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import lombok.Data;

@Data
@Entity
@Table(name="votos")
public class Voto {
	
	@Id
	@GeneratedValue(strategy=GenerationType.SEQUENCE)
	@Column(name="id", nullable=false, updatable=false)
	private Integer id;
	
	@ManyToOne
	@JoinColumn(name="codigo_eleccion", referencedColumnName="codigo", nullable=false, updatable=false)
	private Eleccion eleccion;
	
	@ManyToOne
	@JoinColumn(name="cedula_votante", referencedColumnName="cedula", nullable=false, updatable=false)
	private Votante votante;
	
	@Column(name="cedulaCandidato", nullable=true, updatable=true)
	private String cedulaCandidato;
}
