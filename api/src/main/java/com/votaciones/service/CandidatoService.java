package com.votaciones.service;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;
import java.util.Optional;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.utilities.FileBytes;
import com.utilities.validations;
import com.votaciones.dto.CandidatoDTO;
import com.votaciones.entity.Candidato;
import com.votaciones.entity.Eleccion;
import com.votaciones.entity.Partido;
import com.votaciones.entity.ResultadoCandidato;
import com.votaciones.repository.CandidatoRepository;
import com.votaciones.repository.EleccionRepository;
import com.votaciones.repository.PartidoRepository;
import com.votaciones.repository.ResultadoCandidatoRepository;

@Service
public class CandidatoService {
	
	@Autowired
	PartidoRepository partidoRepository;
	
	@Autowired
	CandidatoRepository candidatoRepository;
	
	@Autowired
	EleccionRepository eleccionRepository;
	
	@Autowired
	ResultadoCandidatoRepository resultadoCandidatoRepository;
	
	@Autowired
	ModelMapper modelMapper;
	
	public CandidatoDTO crear(CandidatoDTO candidatoDTO){
		// Validar que algunos datos no sean nulos
		validations.nonNullValidationResponse(candidatoDTO, "Error! El candidato no puede ser nulo");
		validations.nonNullValidationResponse(candidatoDTO.getCedula(), "Error! La cedula no puede ser nula");
		validations.nonNullValidationResponse(candidatoDTO.getNombre(), "Error! El nombre no puede ser nulo");
		validations.nonNullValidationResponse(candidatoDTO.getApellidos(), "Error! El apellido no puede ser nulo");
		
		// Asignar valores por defecto a valores nulos
		candidatoDTO.setTelefono(validations.selectDefaultValue(candidatoDTO.getTelefono(), ""));
		candidatoDTO.setFoto(validations.selectDefaultValue(candidatoDTO.getFoto(), ""));
		
		
		if(candidatoRepository.existsById(candidatoDTO.getCedula()))
		throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Error! Ya existe un candidato con esa cedula");
		
		if(candidatoDTO.getNitPartido() != null){
			if(!partidoRepository.existsById(candidatoDTO.getNitPartido()))
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Error! No existe un partido con ese nit");
		}
		
		Candidato candidato = modelMapper.map(candidatoDTO, Candidato.class);
		Candidato candidatoRespuesta = candidatoRepository.save(candidato);
		CandidatoDTO candidatoRespuestaDTO = modelMapper.map(candidatoRespuesta, CandidatoDTO.class);
		return candidatoRespuestaDTO;
	}
	
	public CandidatoDTO editar(CandidatoDTO candidatoDTO){
		validations.nonNullValidationResponse(candidatoDTO, "Error! El candidato no puede ser nulo");
		validations.nonNullValidationResponse(candidatoDTO.getCedula(), "Error! La cedula no debe ser nula");
		
		Optional<Candidato> optionalCandidato = candidatoRepository.findById(candidatoDTO.getCedula());
		if(!optionalCandidato.isPresent())
		throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Error! No existe un candidato con esa cedula");
		
		// Validacion de claves foraneas
		if(candidatoDTO.getNitPartido() != null && !candidatoDTO.getNitPartido().equals("")){
			if(!partidoRepository.existsById(candidatoDTO.getNitPartido()))
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Error! No existe un partido con ese nit");
		}
				
		// Modificar los datos anteriores
		Candidato candidatoPasado = optionalCandidato.get();
		Candidato candidato = modelMapper.map(candidatoDTO, Candidato.class);
		if(candidato.getNombre() != null)
		candidatoPasado.setNombre(candidato.getNombre());
		if(candidato.getApellidos() != null)
		candidatoPasado.setApellidos(candidato.getApellidos());
		if(candidato.getTelefono() != null)
		candidatoPasado.setTelefono(candidato.getTelefono());
		if(candidato.getFoto() != null)
		candidatoPasado.setFoto(candidato.getFoto());
		if(candidato.getPartido() != null){
			if(candidato.getPartido().getNit().equals(""))
			candidatoPasado.setPartido(null);
			else
			candidatoPasado.setPartido(candidato.getPartido());
		}
		
		Candidato candidatoRespuesta = candidatoRepository.save(candidatoPasado);
		CandidatoDTO candidatoRespuestaDTO = modelMapper.map(candidatoRespuesta, CandidatoDTO.class);
		
		return candidatoRespuestaDTO;
	}
	
	public List<CandidatoDTO> listar(){
		List<Candidato> candidatos = candidatoRepository.findAll();
		List<CandidatoDTO> candidatosDTO = new LinkedList<CandidatoDTO>();
		ListIterator<Candidato> iterator = candidatos.listIterator();
		while(iterator.hasNext()){
			Candidato candidato = iterator.next();
			candidato.setFoto(null);
			candidatosDTO.add(modelMapper.map(candidato, CandidatoDTO.class));
		}
		return candidatosDTO;
	}
	
	public List<CandidatoDTO> listarPorPartido(String nitPartido){
		validations.nonNullValidationResponse(nitPartido, "Error! El nit del partido no puede ser nulo");
		
		Optional<Partido> optionalPartido = partidoRepository.findById(nitPartido);
		if(!optionalPartido.isPresent())
		throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Error! No existe un partido con ese nit");
			
		Partido partido = optionalPartido.get();
		
		List<Candidato> candidatos = candidatoRepository.findAllByPartidoOrderByCedulaAsc(partido);
		List<CandidatoDTO> candidatosDTO = new LinkedList<CandidatoDTO>();
		ListIterator<Candidato> iterator = candidatos.listIterator();
		while(iterator.hasNext()){
			Candidato candidato = iterator.next();
			candidato.setFoto(null);
			candidatosDTO.add(modelMapper.map(candidato, CandidatoDTO.class));
		}
		return candidatosDTO;
	}
	
	public List<CandidatoDTO> listarPorEleccion(String codigoEleccion){
		validations.nonNullValidationResponse(codigoEleccion, "Error! El codigo de la eleccion no puede ser nulo");	
		
		Optional<Eleccion> optionalEleccion = eleccionRepository.findById(codigoEleccion);
		if(!optionalEleccion.isPresent())
		throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Error! No existe una eleccion con ese codigo");
		
		Eleccion eleccion = optionalEleccion.get();
		
		List<ResultadoCandidato> resultadoCandidatos = resultadoCandidatoRepository.findAllByEleccionOrderByCandidato(eleccion);
		List<CandidatoDTO> candidatosDTO = new LinkedList<CandidatoDTO>();
		ListIterator<ResultadoCandidato> iterator = resultadoCandidatos.listIterator();
		while(iterator.hasNext()){
			ResultadoCandidato resultadoCandidato = iterator.next();
			Candidato candidato = resultadoCandidato.getCandidato();
			candidato.setFoto(null);
			CandidatoDTO candidatoDTO = modelMapper.map(candidato, CandidatoDTO.class);
			candidatosDTO.add(candidatoDTO);
		}
		return candidatosDTO;
	}
	
	public Boolean inscribir(String cedula, String codigoEleccion){
		validations.nonNullValidationResponse(cedula, "Error! La cedula no puede ser nula");
		validations.nonNullValidationResponse(codigoEleccion, "Error! El codigo no puede ser nulo");
		
		if(!candidatoRepository.existsById(cedula))
		throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Error! No existe un candidato con esa cedula");
		if(!eleccionRepository.existsById(codigoEleccion))
		throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Error! No existe una eleccion con ese codigo");
		
		Eleccion eleccion = eleccionRepository.findById(codigoEleccion).get();
		Candidato candidato = candidatoRepository.findById(cedula).get();
		
		Optional<ResultadoCandidato> optionalResultadoCandidato = resultadoCandidatoRepository.findByEleccionAndCandidato(eleccion, candidato);
		if(optionalResultadoCandidato.isPresent())
		throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Error! El candidato ya estaba registrado para esa eleccion");
		
		LocalDateTime fechaHoraActual = LocalDateTime.now(ZoneId.of("UTC"));
		if(fechaHoraActual.plusSeconds(10).isAfter(eleccion.getFechaInicio()))
		throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Error! No puede inscribir candidatos a esta eleccion");
		
		ResultadoCandidato resultadoCandidato = new ResultadoCandidato();
		resultadoCandidato.setCandidato(candidato);
		resultadoCandidato.setEleccion(eleccion);
		resultadoCandidato.setNumeroVotos(0);
		
		resultadoCandidatoRepository.save(resultadoCandidato);
		return true;
	}
	
	public Boolean cancelarInscripcion(String cedula, String codigoEleccion){
		validations.nonNullValidationResponse(cedula, "Error! La cedula no puede ser nula");
		validations.nonNullValidationResponse(codigoEleccion, "Error! El codigo no puede ser nulo");
	
		if(!candidatoRepository.existsById(cedula))
		throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Error! No existe un candidato con esa cedula");
		if(!eleccionRepository.existsById(codigoEleccion))
		throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Error! No existe una eleccion con ese codigo");
			
		Eleccion eleccion = eleccionRepository.findById(codigoEleccion).get();
		Candidato candidato = candidatoRepository.findById(cedula).get();
		
		Optional<ResultadoCandidato> optionalResultadoCandidato = resultadoCandidatoRepository.findByEleccionAndCandidato(eleccion, candidato);
		if(!optionalResultadoCandidato.isPresent())
		throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Error! El candidato no estaba registrado para esa eleccion");
		
		LocalDateTime fechaHoraActual = LocalDateTime.now(ZoneId.of("UTC"));
		if(fechaHoraActual.plusSeconds(10).isAfter(eleccion.getFechaInicio()))
		throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Error! No puede cancelar la inscripcion de candidatos a esta eleccion");
		
		ResultadoCandidato resultadoCandidato = optionalResultadoCandidato.get();
		resultadoCandidatoRepository.delete(resultadoCandidato);
		return true;
	}
	
	public byte[] mostrarFoto(String cedula){
		validations.nonNullValidationResponse(cedula, "Error! La cedula no puede ser nula");
			
		Optional<Candidato> optionalCandidato = candidatoRepository.findById(cedula);
		if(!optionalCandidato.isPresent())
		throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Error! No existe un candidato con esa cedula");
	
		Candidato candidato = optionalCandidato.get();
		String foto = candidato.getFoto();
		
		FileBytes fotoBytes = new FileBytes(foto);
		return fotoBytes.getByteArray();
	}
}
