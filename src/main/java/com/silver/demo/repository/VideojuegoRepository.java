package com.silver.demo.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.silver.demo.model.Videojuego;


@Repository
public interface VideojuegoRepository extends JpaRepository<Videojuego, Long>{

	List<Videojuego> findByEstadoIn(List<String> estado);
	List<Videojuego> findByCategoriaId(Long categoriaId);

}
