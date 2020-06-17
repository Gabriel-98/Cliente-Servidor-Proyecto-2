package com.votaciones.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.votaciones.entity.Candidato;
import com.votaciones.entity.Eleccion;
import com.votaciones.entity.ResultadoCandidato;

public interface ResultadoCandidatoRepository extends JpaRepository<ResultadoCandidato,Integer> {
	Optional<ResultadoCandidato> findByEleccionAndCandidato(Eleccion eleccion, Candidato candidato);
	List<ResultadoCandidato> findAllByEleccionOrderByCandidato(Eleccion eleccion);
	List<ResultadoCandidato> findAllByEleccionOrderByNumeroVotosDesc(Eleccion eleccion);
}
