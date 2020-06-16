package com.votaciones.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.votaciones.entity.Eleccion;

@Repository
public interface EleccionRepository extends JpaRepository<Eleccion,String> {

}
