package com.votaciones.dto;

import lombok.Data;

@Data
public class VotanteDTO {
	private String cedula;
	private String email;
	private String nombre;
	private String apellidos;
	private String fechaNacimiento;
	private String telefono;
	private String password;
	private String foto;
}
