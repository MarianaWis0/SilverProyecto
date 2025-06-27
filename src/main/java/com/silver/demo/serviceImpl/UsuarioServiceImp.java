package com.silver.demo.serviceImpl;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.silver.demo.dto.UsuarioDTO;
import com.silver.demo.dto.UsuarioRequestDTO;
import com.silver.demo.dto.UsuarioResponseDTO;
import com.silver.demo.model.Rol;
import com.silver.demo.model.Usuario;
import com.silver.demo.repository.RolRepository;
import com.silver.demo.repository.UsuarioRepository;
import com.silver.demo.service.UsuarioService;
import com.silver.demo.utils.TextoUtils;

@Service
public class UsuarioServiceImp implements UsuarioService{

	private final UsuarioRepository usuarioRepo;
	private final RolRepository rolRepo;
	private final PasswordEncoder passwordEncoder;
	
	public UsuarioServiceImp(UsuarioRepository usuarioRepo, RolRepository rolRepo, PasswordEncoder passwordEncoder) {
		this.usuarioRepo = usuarioRepo;
		this.rolRepo = rolRepo;
		this.passwordEncoder = passwordEncoder;
	}

	@Override
	@Transactional(readOnly = true)
	public ResponseEntity<Map<String, Object>> listarUsuariosPorEstado(String estado, String rol, int page, int size,
			String textoBusqueda) {
		Map<String, Object> respuesta = new HashMap<>();
		Pageable pageable = PageRequest.of(page, size);
		Page<Usuario> usuariosPage;

		boolean hayRol = rol != null && !rol.trim().isEmpty();
		boolean hayBusqueda = textoBusqueda != null && !textoBusqueda.trim().isEmpty();

		if (hayBusqueda) {
			String termino = "%" + textoBusqueda.toLowerCase() + "%";

			if (hayRol) {
				usuariosPage = usuarioRepo.buscarPorEstadoRolYNombreOEmail(estado, rol, termino, pageable);
			} else {
				usuariosPage = usuarioRepo.buscarPorEstadoYNombreOEmail(estado, termino, pageable);
			}

		} else {
			if (hayRol) {
				usuariosPage = usuarioRepo.findByEstadoAndRolNombre(estado, rol, pageable);
			} else {
				usuariosPage = usuarioRepo.findAllByEstado(estado, pageable);
			}
		}

		List<UsuarioResponseDTO> uDTOs = usuariosPage.stream().map(usuario -> {
			UsuarioResponseDTO dto = new UsuarioResponseDTO();
			dto.setId(usuario.getId());
			dto.setNombre(usuario.getNombre());
			dto.setEmail(usuario.getEmail());
			dto.setEstado(usuario.getEstado());
			dto.setRol(usuario.getRol().getNombre());
			return dto;
		}).toList();

		if (!uDTOs.isEmpty()) {
			respuesta.put("mensaje", "Usuarios encontrados");
			respuesta.put("usuarios", uDTOs);
			respuesta.put("currentPage", usuariosPage.getNumber());
			respuesta.put("totalItems", usuariosPage.getTotalElements());
			respuesta.put("totalPages", usuariosPage.getTotalPages());
			respuesta.put("fecha", new Date());
			respuesta.put("estado", HttpStatus.OK);

			return ResponseEntity.status(HttpStatus.OK).body(respuesta);
		} else {
			respuesta.put("mensaje", "No se encontraron usuarios con los criterios seleccionados");
			respuesta.put("fecha", new Date());
			respuesta.put("estado", HttpStatus.NOT_FOUND);
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(respuesta);
		}
	}

	@Override
	@Transactional
	public ResponseEntity<Map<String, Object>> crearUsuario(UsuarioDTO dto) {
		Map<String, Object> respuesta = new HashMap<>();
		
		try {
			if (usuarioRepo.existsByEmailIgnoreCase(dto.getEmail())) {
				respuesta.put("mensaje", "Email ya registrado por otro usuario");
				respuesta.put("fecha", new Date());
				
				return ResponseEntity.status(HttpStatus.CONFLICT).body(respuesta);
			}
			
			Rol rol = rolRepo.findByNombre("USER")
					.orElseThrow(() -> new RuntimeException("Rol no encontrado"));
			
			Usuario u = new Usuario();
			u.setNombre(TextoUtils.formatoPrimeraLetraMayuscula(dto.getNombre()));
			u.setEmail(TextoUtils.formatoTodoMinuscula(dto.getEmail()));
			u.setPassword(passwordEncoder.encode(dto.getPassword()));
			u.setEstado("A");
			u.setRol(rol);
			
			usuarioRepo.save(u);

			respuesta.put("mensaje", "Usuario creado exitosamente");
			respuesta.put("usuario", u);
			respuesta.put("fecha", new Date());
			
			return ResponseEntity.status(HttpStatus.OK).body(respuesta);
		} catch (Exception e) {
			respuesta.put("mensaje", "Error al crear el usuario: " + e.getMessage());
			respuesta.put("fecha", new Date());
			
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(respuesta);
		}
	}

	@Override
	@Transactional
	public ResponseEntity<Map<String, Object>> actualizarUsuario(UsuarioDTO dto, Long id) {
		Map<String, Object> respuesta = new HashMap<>();
		
		try {
			Usuario usuarioExistente = usuarioRepo.findById(id)
										.orElseThrow(() -> new RuntimeException("Usuario con ID " + id + " no encontrado"));
			
			Optional<Usuario> usuarioConMismoEmail = usuarioRepo.findByEmailIgnoreCase(dto.getEmail());
			
			if (usuarioConMismoEmail.isPresent() && !usuarioConMismoEmail.get().getId().equals(id)) {
				respuesta.put("mensaje", "Email ya registrado por otro usuario");
				respuesta.put("fecha", new Date());
				
				return ResponseEntity.status(HttpStatus.CONFLICT).body(respuesta);
			}
			
			if (dto.getPassword() != null && !dto.getPassword().isBlank()) {
			    if (!passwordEncoder.matches(dto.getPassword(), usuarioExistente.getPassword())) {
			        usuarioExistente.setPassword(passwordEncoder.encode(dto.getPassword()));
			    }
			}
			
			usuarioExistente.setNombre(TextoUtils.formatoPrimeraLetraMayuscula(dto.getNombre()));
			usuarioExistente.setEmail(TextoUtils.formatoTodoMinuscula(dto.getEmail()));
			
			usuarioRepo.save(usuarioExistente);
			
			respuesta.put("mensaje", "Usuario actualizado exitosamente");
			respuesta.put("usuario", usuarioExistente);
			respuesta.put("fecha", new Date());
			
			return ResponseEntity.status(HttpStatus.OK).body(respuesta);
		} catch (Exception e) {
			respuesta.put("mensaje", "Error al actualizar el usuario: " + e.getMessage());
			respuesta.put("fecha", new Date());
			
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(respuesta);
		}
	}

	@Override
	@Transactional
	public ResponseEntity<Map<String, Object>> desactivarUsuario(Long id) {
		Map<String, Object> respuesta = new HashMap<>();
		
		try {
			Usuario usuarioExistente = usuarioRepo.findById(id)
										.orElseThrow(() -> new RuntimeException("Usuario con ID " + id + " no encontrado"));
			
			if (usuarioExistente.getId() == 1) {
				respuesta.put("mensaje", "Está prohibido modificar el administrador principal!");
				respuesta.put("fecha", new Date());

				return ResponseEntity.status(HttpStatus.FORBIDDEN).body(respuesta);
			}
			
			if ("I".equalsIgnoreCase(usuarioExistente.getEstado())) {
				respuesta.put("mensaje", "El usuario seleccionado ya está inactivo");
				respuesta.put("fecha", new Date());
				
				return ResponseEntity.status(HttpStatus.CONFLICT).body(respuesta);
			}
			
			usuarioExistente.setEstado("I");
			
			usuarioRepo.save(usuarioExistente);
			
			respuesta.put("mensaje", "Usuario eliminado exitosamente");
			respuesta.put("usuario", usuarioExistente);
			respuesta.put("fecha", new Date());
			
			return ResponseEntity.status(HttpStatus.OK).body(respuesta);
		} catch (Exception e) {
			respuesta.put("mensaje", "Error al eliminar el usuario: " + e.getMessage());
			respuesta.put("fecha", new Date());
			
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(respuesta);
		}
	}

	@Override
	@Transactional
	public ResponseEntity<Map<String, Object>> reactivarUsuario(Long id) {
		Map<String, Object> respuesta = new HashMap<>();
		
		try {
			Usuario usuarioExistente = usuarioRepo.findById(id)
										.orElseThrow(() -> new RuntimeException("Usuario con ID " + id + " no encontrado"));
			
			if (usuarioExistente.getId() == 1) {
				respuesta.put("mensaje", "Está prohibido modificar el administrador principal!");
				respuesta.put("fecha", new Date());

				return ResponseEntity.status(HttpStatus.FORBIDDEN).body(respuesta);
			}
			
			if ("A".equalsIgnoreCase(usuarioExistente.getEstado())) {
				respuesta.put("mensaje", "El usuario seleccionado ya está activo");
				respuesta.put("fecha", new Date());
				
				return ResponseEntity.status(HttpStatus.CONFLICT).body(respuesta);
			}
			
			usuarioExistente.setEstado("A");
			
			usuarioRepo.save(usuarioExistente);
			
			respuesta.put("mensaje", "Usuario recuperado exitosamente");
			respuesta.put("usuario", usuarioExistente);
			respuesta.put("fecha", new Date());
			
			return ResponseEntity.status(HttpStatus.OK).body(respuesta);
		} catch (Exception e) {
			respuesta.put("mensaje", "Error al recuperar el usuario: " + e.getMessage());
			respuesta.put("fecha", new Date());
			
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(respuesta);
		}
	}

	@Override
	@Transactional
	public ResponseEntity<Map<String, Object>> actualizarUsuarioAdmin(UsuarioRequestDTO dto, Long id) {
		Map<String, Object> respuesta = new HashMap<>();

		try {
			Usuario usuarioExistente = usuarioRepo.findById(id)
					.orElseThrow(() -> new RuntimeException("Usuario con ID " + id + " no encontrado"));

			Optional<Usuario> usuarioConMismoEmail = usuarioRepo.findByEmailIgnoreCase(dto.getEmail());

			if (usuarioExistente.getId() == 1) {
				respuesta.put("mensaje", "Está prohibido modificar el administrador principal!");
				respuesta.put("fecha", new Date());
				
				System.out.println("Se intentó modificar al administrador principal");

				return ResponseEntity.status(HttpStatus.FORBIDDEN).body(respuesta);
			}
			
			if (usuarioConMismoEmail.isPresent() && !usuarioConMismoEmail.get().getId().equals(id)) {
				respuesta.put("mensaje", "Email ya registrado por otro usuario");
				respuesta.put("fecha", new Date());

				return ResponseEntity.status(HttpStatus.CONFLICT).body(respuesta);
			}

			if (dto.getPassword() != null && !dto.getPassword().isBlank()) {
				if (!passwordEncoder.matches(dto.getPassword(), usuarioExistente.getPassword())) {
					usuarioExistente.setPassword(passwordEncoder.encode(dto.getPassword()));
				}
			}
			
			Rol rol = rolRepo.findByNombre(dto.getRol())
					.orElseThrow(() -> new RuntimeException("Rol no encontrado"));

			usuarioExistente.setNombre(TextoUtils.formatoPrimeraLetraMayuscula(dto.getNombre()));
			usuarioExistente.setEmail(TextoUtils.formatoTodoMinuscula(dto.getEmail()));
			usuarioExistente.setRol(rol);

			usuarioRepo.save(usuarioExistente);

			respuesta.put("mensaje", "Usuario actualizado exitosamente");
			respuesta.put("usuario", usuarioExistente);
			respuesta.put("fecha", new Date());

			return ResponseEntity.status(HttpStatus.OK).body(respuesta);
		} catch (Exception e) {
			respuesta.put("mensaje", "Error al actualizar el usuario: " + e.getMessage());
			respuesta.put("fecha", new Date());

			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(respuesta);
		}
	}

}
