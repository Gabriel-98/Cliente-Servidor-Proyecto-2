package com.votaciones.repository;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.votaciones.entity.Eleccion;

@Repository
public interface EleccionRepository extends JpaRepository<Eleccion,String> {
	boolean existsByNombre(String nombre);
	List<Eleccion> findAllByCanceladaOrderByFechaInicioAscFechaFinAsc(Boolean cancelada);
	
	List<Eleccion> findAllByCanceladaAndFechaFinBeforeOrderByFechaInicioAscFechaFinAsc(Boolean cancelada, LocalDateTime ldt);
	List<Eleccion> findAllByCanceladaAndFechaInicioBeforeAndFechaFinAfterOrderByFechaInicioAscFechaFinAsc(Boolean cancelada, LocalDateTime ldt1, LocalDateTime ldt2);
	List<Eleccion> findAllByCanceladaAndFechaInicioAfterOrderByFechaInicioAscFechaFinAsc(Boolean cancelada, LocalDateTime ldt);
}
