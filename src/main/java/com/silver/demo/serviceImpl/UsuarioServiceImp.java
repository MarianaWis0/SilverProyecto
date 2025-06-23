package com.silver.demo.serviceImpl;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.silver.demo.dto.UsuarioDTO;
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
	public ResponseEntity<Map<String, Object>> listarUsuariosPorEstado(String estado) {
		Map<String, Object> respuesta = new HashMap<>();
		List<Usuario> usuarios = usuarioRepo.findAllByEstado(estado);
		
		if (!usuarios.isEmpty()) {
			respuesta.put("mensaje", "Usuarios encontrados");
			respuesta.put("usuarios", usuarios);
			respuesta.put("fecha", new Date());
			respuesta.put("estado", HttpStatus.OK);
			
			return ResponseEntity.status(HttpStatus.OK).body(respuesta);
		} else {
			respuesta.put("mensaje", "No se encontraron usuarios con el estado seleccionado");
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
			
			if (!passwordEncoder.matches(dto.getPassword(), usuarioExistente.getPassword())) {
				usuarioExistente.setPassword(passwordEncoder.encode(dto.getPassword()));
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
			
			if ("I".equalsIgnoreCase(usuarioExistente.getEstado())) {
				respuesta.put("mensaje", "El usuario seleccionado ya está inactivo");
				respuesta.put("fecha", new Date());
				
				return ResponseEntity.status(HttpStatus.CONFLICT).body(respuesta);
			}
			
			usuarioExistente.setEstado("I");
			
			usuarioRepo.save(usuarioExistente);
			
			respuesta.put("mensaje", "Usuario desactivado exitosamente");
			respuesta.put("usuario", usuarioExistente);
			respuesta.put("fecha", new Date());
			
			return ResponseEntity.status(HttpStatus.OK).body(respuesta);
		} catch (Exception e) {
			respuesta.put("mensaje", "Error al desactivar el usuario: " + e.getMessage());
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
			
			if ("A".equalsIgnoreCase(usuarioExistente.getEstado())) {
				respuesta.put("mensaje", "El usuario seleccionado ya está activo");
				respuesta.put("fecha", new Date());
				
				return ResponseEntity.status(HttpStatus.CONFLICT).body(respuesta);
			}
			
			usuarioExistente.setEstado("A");
			
			usuarioRepo.save(usuarioExistente);
			
			respuesta.put("mensaje", "Usuario reactivado exitosamente");
			respuesta.put("usuario", usuarioExistente);
			respuesta.put("fecha", new Date());
			
			return ResponseEntity.status(HttpStatus.OK).body(respuesta);
		} catch (Exception e) {
			respuesta.put("mensaje", "Error al reactivar el usuario: " + e.getMessage());
			respuesta.put("fecha", new Date());
			
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(respuesta);
		}
	}

}
