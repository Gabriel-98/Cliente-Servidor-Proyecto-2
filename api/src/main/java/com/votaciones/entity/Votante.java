package com.votaciones.entity;

import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.Data;

@Data
@Entity
@Table(name="votantes")
public class Votante {
	
	@Id
	@Column(name="cedula", nullable=false, updatable=false)
	private String cedula;
	
	@Column(name="email", nullable=false, updatable=true)
	private String email;
	
	@Column(name="nombre", nullable=false, updatable=true)
	private String nombre;
	
	@Column(name="apellidos", nullable=false, updatable=true)
	private String apellidos;
	
	@Column(name="fecha_nacimiento", nullable=false, updatable=true)
	@JsonFormat(pattern="yyyy/MM/dd'T'HH:mm:ss")
	private String fechaNacimiento;
	
	@Column(name="telefono", nullable=false, updatable=true)
	private String telefono;
	
	@Column(name="password", nullable=false, updatable=true)
	private String password;
	
	@Column(name="activo", nullable=false, updatable=true)
	private Boolean activo;
	
//	@Column(name="foto", nullable=true, updatable=true)
//	private String foto;
}
