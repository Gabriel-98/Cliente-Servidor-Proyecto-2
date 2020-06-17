package com.votaciones.dto;

import lombok.Data;

@Data
public class CandidatoDTO{
	private String cedula;
	private String nombre;
	private String apellidos;
	private String foto;
	private String telefono;
	private String nitPartido;
}
