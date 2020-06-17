package com.votaciones.controller;

import java.util.List;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.votaciones.dto.CandidatoDTO;
import com.votaciones.service.CandidatoService;

@RestController
@RequestMapping("candidatos")
public class CandidatoController {

	@Autowired
	CandidatoService candidatoService;
	
	@Autowired
	ModelMapper modelMapper;
	
	@PostMapping()
	public CandidatoDTO crear(@RequestBody CandidatoDTO candidatoDTO){
		return candidatoService.crear(candidatoDTO);
	}
	
	@PutMapping()
	public CandidatoDTO editar(@RequestBody CandidatoDTO candidatoDTO){
		return candidatoService.editar(candidatoDTO);
	}
	
	@GetMapping("/*")
	public List<CandidatoDTO> listar(){
		return candidatoService.listar();
	}
	
	@GetMapping("/*/partido/{nit-partido}")
	public List<CandidatoDTO> listarPorPartido(@PathVariable("nit-partido") String codigoPartido){
		return candidatoService.listarPorPartido(codigoPartido);
	}
	
	@GetMapping("/*/eleccion/{codigo-eleccion}")
	public List<CandidatoDTO> listarPorEleccion(@PathVariable("codigo-eleccion") String codigoEleccion){
		return candidatoService.listarPorEleccion(codigoEleccion);
	}
	
	@PostMapping("/{cedula}/eleccion/{codigo-eleccion}")
	public Boolean inscribir(@PathVariable("cedula") String cedula, @PathVariable("codigo-eleccion") String codigoEleccion){
		return candidatoService.inscribir(cedula, codigoEleccion);
	}
	
	@DeleteMapping("/{cedula}/eleccion/{codigo-eleccion}")
	public Boolean cancelarInscripcion(@PathVariable("cedula") String cedula, @PathVariable("codigo-eleccion") String codigoEleccion) {
		return candidatoService.cancelarInscripcion(cedula, codigoEleccion);
	}
}
