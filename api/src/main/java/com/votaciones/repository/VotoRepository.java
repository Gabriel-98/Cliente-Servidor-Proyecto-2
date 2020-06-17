package com.votaciones.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.votaciones.entity.Voto;

public interface VotoRepository extends JpaRepository<Voto,Integer> {

}
