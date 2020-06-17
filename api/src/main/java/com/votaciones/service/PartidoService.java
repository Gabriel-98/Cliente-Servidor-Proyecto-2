package com.votaciones.service;

import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;
import java.util.Optional;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.utilities.FileBytes;
import com.utilities.validations;
import com.votaciones.dto.PartidoDTO;
import com.votaciones.entity.Candidato;
import com.votaciones.entity.Partido;
import com.votaciones.repository.CandidatoRepository;
import com.votaciones.repository.PartidoRepository;

@Service
public class PartidoService{

	@Autowired
	PartidoRepository partidoRepository;
	
	@Autowired
	CandidatoRepository candidatoRepository;
	
	@Autowired
	ModelMapper modelMapper;
	
	public PartidoDTO crear(PartidoDTO partidoDTO){
		validations.nonNullValidationResponse(partidoDTO, "Error! El partido no puede ser nulo");
		validations.nonNullValidationResponse(partidoDTO.getNit(), "Error! El nit no puede ser nulo");
		validations.nonNullValidationResponse(partidoDTO.getNombre(), "Error! El nombre no puede ser nulo");
		
		partidoDTO.setDireccion(validations.selectDefaultValue(partidoDTO.getDireccion(), ""));
		partidoDTO.setTelefono(validations.selectDefaultValue(partidoDTO.getTelefono(), ""));
		partidoDTO.setAdministracion(validations.selectDefaultValue(partidoDTO.getAdministracion(), ""));
		partidoDTO.setFoto(validations.selectDefaultValue(partidoDTO.getFoto(), ""));
		
		if(partidoRepository.existsById(partidoDTO.getNit()))
		throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Error! Ya existe un partido con ese nit");
		if(partidoRepository.existsByNombre(partidoDTO.getNombre()))
		throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Error! Ya existe un partido con ese nombre");
		
		
		
		Partido partidoRespuesta = partidoRepository.save(modelMapper.map(partidoDTO, Partido.class));
		return modelMapper.map(partidoRespuesta, PartidoDTO.class);
	}
	
	public PartidoDTO editar(PartidoDTO partidoDTO){
		validations.nonNullValidationResponse(partidoDTO, "Error! El partido no debe ser nulo");
		validations.nonNullValidationResponse(partidoDTO.getNit(), "Error! El nit no puede ser nulo");
		
		Optional<Partido> optionalPartido = partidoRepository.findById(partidoDTO.getNit());
		if(!optionalPartido.isPresent())
		throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Error! No existe un partido con ese nit");
		
		Partido partidoPasado = optionalPartido.get();
		Partido partido = modelMapper.map(partidoDTO, Partido.class);
		
		if(partido.getNombre() != null && !partido.getNombre().equals(partidoPasado.getNombre())){
			if(partidoRepository.existsByNombre(partido.getNombre()))
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Error! Ya existe un partido con ese nit");
		}
		
		if(partido.getNombre() != null)
		partidoPasado.setNombre(partido.getNombre());
		if(partido.getDireccion() != null)
		partidoPasado.setDireccion(partido.getDireccion());
		if(partido.getTelefono() != null)
		partidoPasado.setTelefono(partido.getTelefono());
		if(partido.getAdministracion() != null)
		partidoPasado.setAdministracion(partido.getAdministracion());
		if(partido.getFoto() != null)
		partidoPasado.setFoto(partido.getFoto());
		
		Partido partidoRespuesta = partidoRepository.save(partidoPasado);
		PartidoDTO partidoRespuestaDTO = modelMapper.map(partidoRespuesta, PartidoDTO.class);
		return partidoRespuestaDTO;
	}
	
	public Boolean eliminar(String nit){
		validations.nonNullValidationResponse(nit, "Error! El nit no puede ser nulo");
		
		Optional<Partido> optionalPartido = partidoRepository.findById(nit);
		if(!optionalPartido.isPresent())
		throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Error! El partido no existe");
		
		Partido partido = optionalPartido.get();
		List<Candidato> candidatos = partido.getCandidatos();
		ListIterator<Candidato> iterator = candidatos.listIterator();
		while(iterator.hasNext()) {
			Candidato candidato = iterator.next();
			candidato.setPartido(null);
		}
		candidatoRepository.saveAll(candidatos);
		candidatos.clear();
		partidoRepository.save(partido);
		partidoRepository.delete(partido);
		return true;
	}
	
	public List<PartidoDTO> listar(){
		List<Partido> partidos = partidoRepository.findAll(Sort.by("nit"));
		List<PartidoDTO> partidosDTO = new LinkedList<PartidoDTO>();
		ListIterator<Partido> iterator = partidos.listIterator();
		while(iterator.hasNext())
		partidosDTO.add(modelMapper.map(iterator.next(), PartidoDTO.class));
		return partidosDTO;
	}
	
	public byte[] mostrarFoto(String nit){
		validations.nonNullValidationResponse(nit, "Error! El nit no puede ser nulo");
			
		Optional<Partido> optionalPartido = partidoRepository.findById(nit);
		if(!optionalPartido.isPresent())
		throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Error! No existe un partido con ese nit");
	
		Partido partido = optionalPartido.get();
		String foto = partido.getFoto();
		
		FileBytes fotoBytes = new FileBytes(foto);
		return fotoBytes.getByteArray();
	}
}
