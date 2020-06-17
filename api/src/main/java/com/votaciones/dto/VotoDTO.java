package com.votaciones.dto;

import lombok.Data;

@Data
public class VotoDTO {
	private String codigoEleccion;
	private String cedulaVotante;
	private String cedulaCandidato;
}
