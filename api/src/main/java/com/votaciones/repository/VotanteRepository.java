package com.votaciones.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.votaciones.entity.Votante;

@Repository
public interface VotanteRepository extends JpaRepository<Votante,String> {

}
