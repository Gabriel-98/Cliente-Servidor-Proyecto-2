package com.votaciones.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.votaciones.dto.EleccionDTO;
import com.votaciones.service.EleccionService;

@RestController
@RequestMapping("elecciones")
public class EleccionController {

	@Autowired
	EleccionService eleccionService;
	
	@PostMapping
	public EleccionDTO crear(@RequestBody EleccionDTO eleccionDTO){
		return eleccionService.crear(eleccionDTO);
	}
	
	@PutMapping
	public EleccionDTO editar(@RequestBody EleccionDTO eleccionDTO){
		return eleccionService.editar(eleccionDTO);
	}
	
	@PutMapping("/{codigo}/finalizar")
	public boolean finalizar(@PathVariable("codigo") String codigo){
		return eleccionService.finalizar(codigo);
	}
	
	@PutMapping("/{codigo}/cancelar")
	public boolean cancelar(@PathVariable("codigo") String codigo) {
		return eleccionService.cancelar(codigo);
	}
	
	/*@GetMapping("/{codigo}/resultado")
	public List<ResultadoCandidatoDTO> resultadoEleccion(@PathVariable("codigo") String codigo){
		return eleccionService.resultadoEleccion(codigo);
	}*/
	
	@GetMapping("/*/filtro/{filtro}/zona-horaria/{zona}")
	public List<EleccionDTO> listar(@PathVariable("filtro") String filtro, @PathVariable("zona") String zona){
		return eleccionService.listar(filtro, zona);
	}
}
