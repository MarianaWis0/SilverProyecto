package com.silver.demo.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.silver.demo.model.Categoria;

@Repository
public interface CategoriaRepository extends JpaRepository<Categoria , Long> {
	
	

}
