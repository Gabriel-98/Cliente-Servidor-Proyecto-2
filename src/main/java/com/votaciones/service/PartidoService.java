package com.votaciones.service;

import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;
import java.util.Optional;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.votaciones.dto.PartidoDTO;
import com.votaciones.entity.Partido;
import com.votaciones.repository.PartidoRepository;

@Service
public class PartidoService{

	@Autowired
	PartidoRepository partidoRepository;
	
	@Autowired
	ModelMapper modelMapper;
	
	public PartidoDTO crear(PartidoDTO partidoDTO){
		System.out.println(partidoDTO);
		if(partidoDTO.getNit() == null)
		throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Error! El nit no puede ser nulo");
			
		String cad = partidoDTO.getAdministracion();
		for(int i=0; i<cad.length(); i++)
		System.out.print(cad.charAt(i));
		System.out.println();
		
		
		String nombre = partidoDTO.getNombre();
		String direccion = partidoDTO.getDireccion();
		String telefono = partidoDTO.getTelefono();
		String administracion = partidoDTO.getAdministracion();
		if(nombre == null || direccion == null || telefono == null || administracion == null)
		throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Error! Ningun campo debe ser nulo");
			
		if(partidoRepository.existsById(partidoDTO.getNit()))
		throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Error! Ya existe un partido con ese nit");
		
		Partido partidoRespuesta = partidoRepository.save(modelMapper.map(partidoDTO, Partido.class));		
		return modelMapper.map(partidoRespuesta, PartidoDTO.class);
	}
	
	public PartidoDTO editar(PartidoDTO partidoDTO){
		if(partidoDTO == null)
		throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Error! El partido no debe ser nulo");
		
		Optional<Partido> optionalPartido = partidoRepository.findById(partidoDTO.getNit());
		if(!optionalPartido.isPresent())
		throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Error! No existe un partido con ese nit");
		
		Partido partido = modelMapper.map(partidoDTO, Partido.class);
		Partido partidoPasado = optionalPartido.get();
		
		if(partido.getNombre() != null)
		partidoPasado.setNombre(partido.getNombre());
		if(partido.getDireccion() != null)
		partidoPasado.setDireccion(partido.getDireccion());
		if(partido.getTelefono() != null)
		partidoPasado.setTelefono(partido.getTelefono());
		if(partido.getAdministracion() != null)
		partidoPasado.setAdministracion(partido.getAdministracion());
		
		Partido partidoRespuesta = partidoRepository.save(partidoPasado);
		PartidoDTO partidoRespuestaDTO = modelMapper.map(partidoRespuesta, PartidoDTO.class);
		return partidoRespuestaDTO;
	}
	
	public Boolean eliminar(String nit){
		System.out.println(nit);
		if(nit == null)
		throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Error! El nit no puede ser nulo");
		System.out.println(nit);
		if(!partidoRepository.existsById(nit)){
			System.out.println(".");
			throw new ResponseStatusException(HttpStatus.ACCEPTED, "Error! El partido no existe");	
		}
		System.out.println(nit);
		partidoRepository.deleteById(nit);
		return true;
		//return new ResponseEntity<Boolean>(true, HttpStatus.ACCEPTED);
	}
	
	public List<PartidoDTO> listar(){
		List<Partido> partidos = partidoRepository.findAll();
		List<PartidoDTO> partidosDTO = new LinkedList<PartidoDTO>();
		ListIterator<Partido> iterator = partidos.listIterator();
		while(iterator.hasNext())
		partidosDTO.add(modelMapper.map(iterator.next(), PartidoDTO.class));
		return partidosDTO;
	}
}
