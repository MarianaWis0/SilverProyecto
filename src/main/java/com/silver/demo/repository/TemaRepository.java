package com.silver.demo.repository;

import java.util.List;

import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.silver.demo.model.Tema;

@Repository
public interface TemaRepository extends JpaRepository<Tema, Long> {
	
	List<Tema> findAll(Sort sort);
}
