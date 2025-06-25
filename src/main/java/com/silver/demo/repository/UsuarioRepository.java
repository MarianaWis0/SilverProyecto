package com.silver.demo.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.silver.demo.model.Usuario;

@Repository
public interface UsuarioRepository extends JpaRepository<Usuario, Long>{

	Boolean existsByEmailIgnoreCase(String email);
	
	Optional<Usuario> findByEmailIgnoreCase(String email);
	
	List<Usuario> findAllByEstado(String estado);
	
	@Query("SELECT u FROM Usuario u JOIN u.rol r WHERE u.estado = :estado AND r.nombre = :nombreRol")
	List<Usuario> findByEstadoAndRolNombre(String estado, String nombreRol);
}
