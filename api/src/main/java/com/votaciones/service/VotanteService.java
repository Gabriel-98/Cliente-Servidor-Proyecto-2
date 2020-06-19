package com.votaciones.service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;
import java.util.Optional;
import java.util.Random;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.utilities.FileBytes;
import com.utilities.Time;
import com.utilities.validations;
import com.votaciones.dto.VotanteDTO;
import com.votaciones.dto.VotoDTO;
import com.votaciones.entity.Candidato;
import com.votaciones.entity.Eleccion;
import com.votaciones.entity.Partido;
import com.votaciones.entity.ResultadoCandidato;
import com.votaciones.entity.Votante;
import com.votaciones.entity.Voto;
import com.votaciones.repository.CandidatoRepository;
import com.votaciones.repository.EleccionRepository;
import com.votaciones.repository.ResultadoCandidatoRepository;
import com.votaciones.repository.VotanteRepository;
import com.votaciones.repository.VotoRepository;

@Service
public class VotanteService {

	@Autowired
	EleccionRepository eleccionRepository;
	
	@Autowired
	VotanteRepository votanteRepository;
	
	@Autowired
	CandidatoRepository candidatoRepository;
	
	@Autowired
	VotoRepository votoRepository;
	
	@Autowired
	ResultadoCandidatoRepository resultadoCandidatoRepository;
	
	@Autowired
	ModelMapper modelMapper;
	
	public VotanteDTO crear(VotanteDTO votanteDTO) {
		validations.nonNullValidationResponse(votanteDTO, "Error! El votante no puede ser nulo");
		validations.nonNullValidationResponse(votanteDTO.getCedula(), "Error! La cedula no puede ser nula");;
		validations.nonNullValidationResponse(votanteDTO.getEmail(), "Error! El email no puede ser nulo");
		validations.nonNullValidationResponse(votanteDTO.getNombre(), "Error! El nombre no puede ser nulo");
		validations.nonNullValidationResponse(votanteDTO.getApellidos(), "Error! El apellido no puede ser nulo");
		validations.nonNullValidationResponse(votanteDTO.getFechaNacimiento(), "Error! La fecha de nacimiento no puede ser nula");
		
		if(votanteDTO.getPassword() != null)
		throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Error! No debe ingresar una contraseña");
		
		validations.isoLocalDateValidationResponse(votanteDTO.getFechaNacimiento(), false, "Error! La fecha de nacimiento tiene formato incorrecto");
		
		LocalDate fechaActual = LocalDate.now(ZoneId.of("UTC"));
		LocalDate fechaNacimiento = LocalDate.parse(votanteDTO.getFechaNacimiento(), DateTimeFormatter.ISO_LOCAL_DATE);
		int difYears = Time.differenceInYears(fechaActual, fechaNacimiento);
		if(difYears < 18)
		throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Error! Debe tener minimo 18 años");
		if(difYears > 100)
		throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Error! Debe tener maximo 100 años");
		
		votanteDTO.setTelefono(validations.selectDefaultValue(votanteDTO.getTelefono(), ""));
		votanteDTO.setFoto(validations.selectDefaultValue(votanteDTO.getFoto(), ""));
		
		Random random = new Random();
		String password = "";
		for(int i=0; i<10; i++){
			int r = random.nextInt(62);
			char c;
			if(r < 10)
			c = (char)('0' + r);
			else if(r < 36)
			c = (char)('A' + r - 10);
			else
			c = (char)('a' + r - 36);
			password += c;
		}
		votanteDTO.setPassword(password);
		
		if(votanteRepository.existsById(votanteDTO.getCedula()))
		throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Error! Ya existe un votante con esa cedula");
	
		Votante votante = modelMapper.map(votanteDTO, Votante.class);
		votante.setActivo(true);	
		Votante votanteRespuesta = votanteRepository.save(votante);
		VotanteDTO votanteRespuestaDTO = modelMapper.map(votanteRespuesta, VotanteDTO.class);
		return votanteRespuestaDTO;
	}
	
	public Boolean bloquear(String cedula){
		validations.nonNullValidationResponse(cedula, "Error! La cedula no puede ser nula");
		
		Optional<Votante> optionalVotante = votanteRepository.findById(cedula);
		if(!optionalVotante.isPresent())
		throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Error! No existe un votante con esa cedula");
			
		Votante votante = optionalVotante.get();
		if(!votante.getActivo())
		throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Error! El votante ya estaba bloqueado");
		
		votante.setActivo(false);
		votanteRepository.save(votante);
		return true;
	}
	
	public Boolean desbloquear(String cedula){
		validations.nonNullValidationResponse(cedula, "Error! La cedula no puede ser nula");
		
		Optional<Votante> optionalVotante = votanteRepository.findById(cedula);
		if(!optionalVotante.isPresent())
		throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Error! No existe un votante con esa cedula");
			
		Votante votante = optionalVotante.get();
		if(votante.getActivo())
		throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Error! El votante ya estaba activo");
			
		votante.setActivo(true);
		votanteRepository.save(votante);
		return true;
	}
	
	public Boolean votar(String cedulaVotante, String codigoEleccion, String cedulaCandidato){
		validations.nonNullValidationResponse(cedulaVotante, "Error! La cedula del votante no puede ser nula");
		validations.nonNullValidationResponse(codigoEleccion, "Error! El codigo de la eleccion no puede ser nulo");
		validations.nonNullValidationResponse(cedulaCandidato, "Error! La cedula del candidato no puede ser nula");
		
		Optional<Votante> optionalVotante = votanteRepository.findById(cedulaVotante);
		if(!optionalVotante.isPresent())
		throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Error! No existe un votante con esa cedula");
		Optional<Eleccion> optionalEleccion = eleccionRepository.findById(codigoEleccion);
		if(!optionalEleccion.isPresent())
		throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Error! No existe una eleccion con ese codigo");
		Optional<Candidato> optionalCandidato = candidatoRepository.findById(cedulaCandidato);
		if(!optionalCandidato.isPresent())
		throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Error! No existe un candidato con esa cedula");
		
		Votante votante = optionalVotante.get();
		Eleccion eleccion = optionalEleccion.get();
		Candidato candidato = optionalCandidato.get();
		
		if(!votante.getActivo())
		throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Error! El votante esta bloqueado");
			
		LocalDateTime fechaHoraActual = LocalDateTime.now(ZoneId.of("UTC"));
		if(eleccion.getCancelada())
		throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Error! La eleccion fue cancelada");
		if(fechaHoraActual.isBefore(eleccion.getFechaInicio()))
		throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Error! La eleccion no ha iniciado");
		if(fechaHoraActual.isAfter(eleccion.getFechaFin()))
		throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Error! La eleccion ya finalizo");
		
		Optional<Voto> optionalVoto = votoRepository.findByEleccionAndVotante(eleccion, votante);
		if(!optionalVoto.isPresent())
		throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Error! El votante no esta registrado en esa eleccion");
			
		Voto voto = optionalVoto.get();
		if(voto.getCedulaCandidato() != null)
		throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Error! El usuario ya habia votado en esa eleccion");
		
		Optional<ResultadoCandidato> optionalResultadoCandidato = resultadoCandidatoRepository.findByEleccionAndCandidato(eleccion, candidato);
		if(!optionalResultadoCandidato.isPresent())
		throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Error! El candidato no esta registrado para esta eleccion");
		ResultadoCandidato resultadoCandidato = optionalResultadoCandidato.get();
		
		voto.setCedulaCandidato(candidato.getCedula());
		resultadoCandidato.setNumeroVotos(resultadoCandidato.getNumeroVotos() + 1);
		
		votoRepository.save(voto);
		return true;
	}
	
	public List<VotanteDTO> listar(String filtro){
		validations.nonNullValidationResponse(filtro, "Error! El filtro no puede ser nulo");
		
		List<Votante> votantes;
		if(filtro.equals("ACTIVOS"))
		votantes = votanteRepository.findAllByActivoOrderByCedula(true);
		else if(filtro.equals("BLOQUEADOS"))
		votantes = votanteRepository.findAllByActivoOrderByCedula(false);
		else
		throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Error! El filtro es incorrecto");
		
		List<VotanteDTO> votantesDTO = new LinkedList<VotanteDTO>();
		ListIterator<Votante> iterator = votantes.listIterator();
		while(iterator.hasNext())
		votantesDTO.add(modelMapper.map(iterator.next(), VotanteDTO.class));
		return votantesDTO;
	}
	
	public List<VotanteDTO> listarPorEleccion(String codigoEleccion){
		validations.nonNullValidationResponse(codigoEleccion, "Error! El codigo de la eleccion no puede ser nulo");
		
		Optional<Eleccion> optionalEleccion = eleccionRepository.findById(codigoEleccion);
		if(!optionalEleccion.isPresent())
		throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Error! No existe una eleccion con ese codigo");
			
		Eleccion eleccion = optionalEleccion.get();
		List<Voto> votos = votoRepository.findAllByEleccionOrderById(eleccion);
		List<VotanteDTO> votantesDTO = new LinkedList<VotanteDTO>();
		ListIterator<Voto> iterator = votos.listIterator();
		while(iterator.hasNext()){
			Voto voto = iterator.next();
			Votante votante = voto.getVotante();
			votantesDTO.add(modelMapper.map(votante, VotanteDTO.class));
		}
		return votantesDTO;
	}
	
	public List<VotoDTO> listarVotos(String cedula){
		validations.nonNullValidationResponse(cedula, "Error! La cedula no puede ser nula");
		
		Optional<Votante> optionalVotante = votanteRepository.findById(cedula);
		if(!optionalVotante.isPresent())
		throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Error! No existe un votante con esa cedula");
	
		Votante votante = optionalVotante.get();
		List<Voto> votos = votoRepository.findAllByVotanteOrderById(votante);
		List<VotoDTO> votosDTO = new LinkedList<VotoDTO>();
		ListIterator<Voto> iterator = votos.listIterator();
		while(iterator.hasNext())
		votosDTO.add(modelMapper.map(iterator.next(), VotoDTO.class));
		return votosDTO;
	}
	
	public byte[] mostrarFoto(String cedula){
		validations.nonNullValidationResponse(cedula, "Error! La cedula no puede ser nula");
			
		Optional<Votante> optionalVotante = votanteRepository.findById(cedula);
		if(!optionalVotante.isPresent())
		throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Error! No existe un votante con esa cedula");
	
		Votante votante = optionalVotante.get();
		String foto = votante.getFoto();
		
		FileBytes fotoBytes = new FileBytes(foto);
		return fotoBytes.getByteArray();
	}
}
