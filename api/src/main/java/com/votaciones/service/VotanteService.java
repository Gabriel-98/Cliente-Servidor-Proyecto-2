package com.votaciones.service;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Optional;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.utilities.Time;
import com.utilities.validations;
import com.votaciones.dto.VotanteDTO;
import com.votaciones.entity.Votante;
import com.votaciones.repository.VotanteRepository;

@Service
public class VotanteService {

	@Autowired
	VotanteRepository votanteRepository;
	
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
		
		validations.isoLocalDateTimeValidationResponse(votanteDTO.getFechaNacimiento(), false, "Error! La fecha de nacimiento tiene formato incorrecto");
		
		LocalDateTime fechaHoraActual = LocalDateTime.now(ZoneId.of("UTC"));
		LocalDateTime fechaNacimiento = LocalDateTime.parse(votanteDTO.getFechaNacimiento(), DateTimeFormatter.ISO_LOCAL_DATE_TIME);
		int difYears = Time.differenceInYears(fechaHoraActual, fechaNacimiento);
		if(difYears < 18)
		throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Error! Debe tener minimo 18 años");
		if(difYears > 100)
		throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Error! Debe tener maximo 100 años");
		
		votanteDTO.setTelefono(validations.selectDefaultValue(votanteDTO.getTelefono(), ""));
		votanteDTO.setFoto(validations.selectDefaultValue(votanteDTO.getFoto(), ""));
		
		if(votanteRepository.existsById(votanteDTO.getCedula()))
		throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Error! Ya existe un usuario con esa cedula");
	
		Votante votante = modelMapper.map(votanteDTO, Votante.class);
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
			
		votanteRepository.save(votante);
		return true;
	}
	
}
