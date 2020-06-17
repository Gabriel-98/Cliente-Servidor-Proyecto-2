package com.votaciones.controller;

import java.util.List;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.votaciones.dto.PartidoDTO;
import com.votaciones.service.PartidoService;

@RestController
@RequestMapping("partidos")
public class PartidoController{

	@Autowired
	PartidoService partidoService;
	
	@Autowired
	ModelMapper modelMapper;
	
	@PostMapping()
	public PartidoDTO crear(@RequestBody PartidoDTO partidoDTO){
		return partidoService.crear(partidoDTO);
	}
	
	@PutMapping()
	public PartidoDTO editar(@RequestBody PartidoDTO partidoDTO) {
		return partidoService.editar(partidoDTO);
	}
	
	@DeleteMapping("/{nitPartido}")
	public Boolean eliminar(@PathVariable("nitPartido") String nitPartido){
		return partidoService.eliminar(nitPartido);
	}
	
	@GetMapping("/*")
	public List<PartidoDTO> listar(){
		return partidoService.listar();
	}
	
	@GetMapping(value="/{nit}/foto", produces=MediaType.IMAGE_JPEG_VALUE)
	public byte[] mostrarFoto(@PathVariable("nit") String nit){
		return partidoService.mostrarFoto(nit);
	}
}
