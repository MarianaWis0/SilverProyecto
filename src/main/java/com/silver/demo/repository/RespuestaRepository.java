package com.silver.demo.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.silver.demo.model.Respuesta;

@Repository
public interface RespuestaRepository extends JpaRepository<Respuesta, Long>{
	

}
