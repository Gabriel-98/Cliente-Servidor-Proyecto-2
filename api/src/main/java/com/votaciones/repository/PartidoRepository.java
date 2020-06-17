package com.votaciones.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.votaciones.entity.Partido;

@Repository
public interface PartidoRepository extends JpaRepository<Partido,String>{
	boolean existsByNombre(String nombre);
}
