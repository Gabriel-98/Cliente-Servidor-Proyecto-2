package com.votaciones.dto;

import lombok.Data;

@Data
public class ResultadoCandidatoDTO {
	private String codigoEleccion;
	private String cedulaCandidato;
	private Integer numeroVotos;
}
