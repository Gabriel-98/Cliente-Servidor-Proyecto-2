package com.votaciones.dto;

import lombok.Data;

@Data
public class EleccionDTO{
	private String codigo;
	private String nombre;
	private String descripcion;
	private String zonaHoraria;
	private String fechaInicio;
	private String fechaFin;
}
