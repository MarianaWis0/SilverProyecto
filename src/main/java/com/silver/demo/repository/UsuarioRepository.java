package com.silver.demo.repository;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.silver.demo.model.Usuario;

@Repository
public interface UsuarioRepository extends JpaRepository<Usuario, Long>{

	Boolean existsByEmailIgnoreCase(String email);
	
	Optional<Usuario> findByEmailIgnoreCase(String email);
	
	Page<Usuario> findAllByEstado(String estado, Pageable pageable);
	
	@Query("SELECT u FROM Usuario u JOIN u.rol r WHERE u.estado = :estado AND r.nombre = :nombreRol")
	Page<Usuario> findByEstadoAndRolNombre(@Param("estado") String estado, @Param("nombreRol") String nombreRol, Pageable pageable);
	
	@Query("SELECT u FROM Usuario u WHERE u.estado = :estado AND u.rol.nombre = :rol AND "
			+ "(LOWER(u.nombre) LIKE LOWER(:busqueda) OR LOWER(u.email) LIKE LOWER(:busqueda))")
	Page<Usuario> buscarPorEstadoRolYNombreOEmail(@Param("estado") String estado, @Param("rol") String rol,
			@Param("busqueda") String textoBusqueda, Pageable pageable);

	@Query("SELECT u FROM Usuario u WHERE u.estado = :estado AND "
			+ "(LOWER(u.nombre) LIKE LOWER(:busqueda) OR LOWER(u.email) LIKE LOWER(:busqueda))")
	Page<Usuario> buscarPorEstadoYNombreOEmail(@Param("estado") String estado, @Param("busqueda") String textoBusqueda,
			Pageable pageable);
}
