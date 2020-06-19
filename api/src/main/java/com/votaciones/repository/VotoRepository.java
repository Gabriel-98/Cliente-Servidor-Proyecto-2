package com.votaciones.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.votaciones.entity.Eleccion;
import com.votaciones.entity.Votante;
import com.votaciones.entity.Voto;

public interface VotoRepository extends JpaRepository<Voto,Integer> {
	Optional<Voto> findByEleccionAndVotante(Eleccion eleccion, Votante votante);
	List<Voto> findAllByEleccionOrderById(Eleccion eleccion);
	List<Voto> findAllByVotanteOrderById(Votante votante);
}
