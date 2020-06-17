package com.votaciones.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.votaciones.entity.Candidato;
import com.votaciones.entity.Partido;

@Repository
public interface CandidatoRepository extends JpaRepository<Candidato,String> {
	List<Candidato> findAllByPartidoOrderByCedulaAsc(Partido partido);
}
