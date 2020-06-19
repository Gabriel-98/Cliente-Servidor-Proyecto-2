package com.votaciones.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.votaciones.dto.VotanteDTO;
import com.votaciones.dto.VotoDTO;
import com.votaciones.service.VotanteService;

@RestController
@RequestMapping("votantes")
public class VotanteController {

	@Autowired
	VotanteService votanteService;
	
	@PostMapping()
	public VotanteDTO crear(@RequestBody VotanteDTO votanteDTO){
		return votanteService.crear(votanteDTO);
	}
	
	@PutMapping("/{cedula}/bloquear")
	public Boolean bloquear(@PathVariable("cedula") String cedula){
		return votanteService.bloquear(cedula);
	}
	
	@PutMapping("/{cedula}/desbloquear")
	public Boolean desbloquear(@PathVariable("cedula") String cedula) {
		return votanteService.desbloquear(cedula);
	}
	
	@PostMapping("/{cedula}/voto/{codigoEleccion}/{cedulaCandidato}")
	public Boolean votar(@PathVariable("cedula") String cedula, @PathVariable("codigoEleccion") String codigoEleccion, @PathVariable("cedulaCandidato") String cedulaCandidato) {
		return votanteService.votar(cedula, codigoEleccion, cedulaCandidato);
	}
	
	@GetMapping("/*/{filtro}")
	public List<VotanteDTO> listar(@PathVariable("filtro") String filtro){
		return votanteService.listar(filtro);
	}
	
	@GetMapping("/*/eleccion/{codigoEleccion}")
	public List<VotanteDTO> listarPorEleccion(@PathVariable("codigoEleccion") String codigoEleccion){
		return votanteService.listarPorEleccion(codigoEleccion);
	}
	
	@GetMapping("/{cedula}/votos")
	public List<VotoDTO> listarVotos(@PathVariable("") String cedula){
		return votanteService.listarVotos(cedula);
	}
	
	@GetMapping(value="/{cedula}/foto", produces=MediaType.IMAGE_JPEG_VALUE)
	public byte[] mostrarFoto(@PathVariable("cedula") String cedula){
		return votanteService.mostrarFoto(cedula);
	}
}
