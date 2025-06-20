package com.silver.demo.serviceImpl;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.silver.demo.model.Categoria;
import com.silver.demo.repository.CategoriaRepository;
import com.silver.demo.service.CategoriaService;

@Service
public class CategoriaServiceImpl implements CategoriaService {

	@Autowired
	private CategoriaRepository cateRepo;

	@Override
	public ResponseEntity<Map<String, Object>> listaCategorias() {
		Map<String, Object> response = new HashMap<>();
		try {
			// traer la lista de categorias
			List<Categoria> categorias = cateRepo.findAll();

			if (!categorias.isEmpty()) {
				response.put("mensaje", "Lista de Categorias");
				response.put("categorias", categorias);
				response.put("status", HttpStatus.OK);
				response.put("fecha", new Date());
				return ResponseEntity.status(HttpStatus.OK).body(response);
			} else {
				response.put("mensaje", "No hay Resgistros para mostrar");
				response.put("status", HttpStatus.NOT_FOUND);
				response.put("fecha", new Date());
				return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);

			}

		} catch (Exception e) {

			response.put("mensaje", "Error al obtener las categorías: " + e.getMessage());
			response.put("status", HttpStatus.INTERNAL_SERVER_ERROR);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);

		}

	}

	@Override
	public ResponseEntity<Map<String, Object>> listaCategoriasId(Long id) {

		Map<String, Object> response = new HashMap<>();

		try {

			Optional<Categoria> categoria = cateRepo.findById(id);

			if (categoria.isPresent()) {
				response.put("mensaje", "Categoria Encontrada");
				response.put("categoria", categoria.get());
				response.put("status", HttpStatus.OK);
				response.put("fecha", new Date());
				return ResponseEntity.status(HttpStatus.OK).body(response);
			} else {
				response.put("mensaje", "No se ha encontrado la categoria con el ID " + id);
				response.put("status", HttpStatus.NOT_FOUND);
				response.put("fecha", new Date());
				return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);

			}

		} catch (Exception e) {

			response.put("mensaje", "Error al obtener la categoría: " + e.getMessage());
			response.put("status", HttpStatus.INTERNAL_SERVER_ERROR);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
		}

	}

	@Override
	public ResponseEntity<Map<String, Object>> agregarCategorias(Categoria categoria) {
		Map<String, Object> response = new HashMap<>();

		try {

			Categoria categoriaGuardado = cateRepo.save(categoria);

			response.put("mensaje", "Categoria creada exitosamente.");
			response.put("fecha", new Date());
			response.put("categoria", categoriaGuardado);
			return ResponseEntity.ok(response);

		} catch (Exception e) {

			response.put("mensaje", "Error al crear La categoria.");
			response.put("fecha", new Date());
			response.put("error", e.getMessage());
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
		}
	}

	@Override
	public ResponseEntity<Map<String, Object>> editarCategorias(Categoria categoria, Long id) {

		Map<String, Object> response = new HashMap<>();

		try {
			Optional<Categoria> categoriaEncontrada = cateRepo.findById(id);

			if (categoriaEncontrada.isPresent()) {

				Categoria cate = categoriaEncontrada.get();
				cate.setNombre(categoria.getNombre());
				cateRepo.save(cate);
				response.put("mensaje", "Categoria actualizada exitosamente.");
				response.put("fecha", new Date());
				response.put("categoria", cate);
				return ResponseEntity.ok(response);
			} else {
				response.put("mensaje", "No se ha encontrado la categoria con el ID " + id + " para Actualizar");
				response.put("status", HttpStatus.NOT_FOUND);
				response.put("fecha", new Date());
				return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);

			}

		} catch (Exception e) {

			response.put("mensaje", "Error al Actualizar La categoria.");
			response.put("fecha", new Date());
			response.put("error", e.getMessage());
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
		}

	}

	@Override
	public ResponseEntity<Map<String, Object>> eliminarCategorias(Long id) {

		Map<String, Object> response = new HashMap<>();

		try {

			Optional<Categoria> catencontrada = cateRepo.findById(id);

			if (catencontrada.isPresent()) {
				Categoria cat = catencontrada.get();
				cateRepo.delete(cat);
				response.put("mensaje", "Categoria Eliminada exitosamente.");
				response.put("fecha", new Date());
				return ResponseEntity.ok(response);

			} else {
				response.put("mensaje", "No se ha encontrado la categoria con el ID " + id + " para Eliminar");
				response.put("status", HttpStatus.NOT_FOUND);
				response.put("fecha", new Date());
				return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);

			}

		} catch (Exception e) {

			response.put("mensaje", "Error al Eliminar La categoria.");
			response.put("fecha", new Date());
			response.put("error", e.getMessage());
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
		}

	}

}
