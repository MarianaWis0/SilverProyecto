package com.silver.demo.repository.serviceImpl;

import java.util.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.silver.demo.model.Videojuego;
import com.silver.demo.repository.VideojuegoRepository;
import com.silver.demo.repository.service.VideojuegoService;

@Service
public class VideojuegoServiceImpl implements VideojuegoService {

    @Autowired
    private VideojuegoRepository dao;

    @Override
    public ResponseEntity<Map<String, Object>> listarVideojuego() {
        Map<String, Object> respuesta = new HashMap<>();
        List<Videojuego> videojuegos = dao.findAll();

        if (!videojuegos.isEmpty()) {
            respuesta.put("mensaje", "Lista de videojuegos");
            respuesta.put("videojuegos", videojuegos);
            respuesta.put("status", HttpStatus.OK);
            respuesta.put("fecha", new Date());
            return ResponseEntity.status(HttpStatus.OK).body(respuesta);
        } else {
            respuesta.put("mensaje", "No existen registros");
            respuesta.put("status", HttpStatus.NOT_FOUND);
            respuesta.put("fecha", new Date());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(respuesta);
        }
    }

    @Override
    public ResponseEntity<Map<String, Object>> listarVideojuegoId(Long id) {
        Map<String, Object> respuesta = new HashMap<>();
        Optional<Videojuego> videojuego = dao.findById(id);

        if (videojuego.isPresent()) {
            respuesta.put("videojuego", videojuego.get());
            respuesta.put("mensaje", "Videojuego encontrado");
            respuesta.put("status", HttpStatus.OK);
            respuesta.put("fecha", new Date());
            return ResponseEntity.ok().body(respuesta);
        } else {
            respuesta.put("mensaje", "Sin registros con ID: " + id);
            respuesta.put("status", HttpStatus.NOT_FOUND);
            respuesta.put("fecha", new Date());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(respuesta);
        }
    }

    @Override
    public ResponseEntity<Map<String, Object>> agregarVideojeugo(Videojuego videojuego) {
        Map<String, Object> respuesta = new HashMap<>();

        if (videojuego.getImg() == null || videojuego.getImg().isBlank()) {
            videojuego.setImg("sinimagen.jpg");
        }

        if (videojuego.getStock() != null && videojuego.getStock() == 0) {
            videojuego.setEstado("F"); 
        }

        dao.save(videojuego);

        respuesta.put("videojuego", videojuego);
        respuesta.put("mensaje", "Se añadió correctamente el videojuego");
        respuesta.put("status", HttpStatus.CREATED);
        respuesta.put("fecha", new Date());
        return ResponseEntity.status(HttpStatus.CREATED).body(respuesta);
    }

    @Override
    public ResponseEntity<Map<String, Object>> editarVideojuego(Videojuego vj, Long id) {
        Map<String, Object> respuesta = new HashMap<>();
        Optional<Videojuego> existe = dao.findById(id);

        if (existe.isPresent()) {
            Videojuego videojuego = existe.get();
            videojuego.setTitulo(vj.getTitulo());
            videojuego.setDescripcion(vj.getDescripcion());
            videojuego.setPrecio(vj.getPrecio());
            videojuego.setStock(vj.getStock());
            videojuego.setImg(vj.getImg());
            videojuego.setCategoria(vj.getCategoria());

            if (vj.getStock() == 0) {
                videojuego.setEstado("F"); 
            } else {
                videojuego.setEstado(vj.getEstado()); 
            }

            dao.save(videojuego);

            respuesta.put("videojuego", videojuego);
            respuesta.put("mensaje", "Datos del videojuego modificados");
            respuesta.put("status", HttpStatus.OK);
            respuesta.put("fecha", new Date());
            return ResponseEntity.ok(respuesta);
        } else {
            respuesta.put("mensaje", "No se encontró el videojuego con ID: " + id);
            respuesta.put("status", HttpStatus.NOT_FOUND);
            respuesta.put("fecha", new Date());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(respuesta);
        }
    }


    @Override
    public ResponseEntity<Map<String, Object>> eliminarLogicoVideojuego(Long id) {
        Map<String, Object> respuesta = new HashMap<>();
        Optional<Videojuego> existe = dao.findById(id);

        if (existe.isPresent()) {
            Videojuego videojuego = existe.get();
            videojuego.setEstado("I"); 
            dao.save(videojuego);

            respuesta.put("mensaje", "Videojuego eliminado lógicamente");
            respuesta.put("status", HttpStatus.OK);
            respuesta.put("fecha", new Date());
            return ResponseEntity.ok().body(respuesta);
        } else {
            respuesta.put("mensaje", "No se encontró el videojuego con ID: " + id);
            respuesta.put("status", HttpStatus.NOT_FOUND);
            respuesta.put("fecha", new Date());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(respuesta);
        }
    }

    @Override
    public ResponseEntity<Map<String, Object>> EstadoVideojuego(List<String> estado) {
        Map<String, Object> respuesta = new HashMap<>();
        List<Videojuego> videojuegos = dao.findByEstadoIn(estado);

        if (!videojuegos.isEmpty()) {
            respuesta.put("mensaje", "Videojuegos con estado: " + estado);
            respuesta.put("videojuegos", videojuegos);
            respuesta.put("status", HttpStatus.OK);
            respuesta.put("fecha", new Date());
            return ResponseEntity.ok().body(respuesta);
        } else {
            respuesta.put("mensaje", "No se encontraron videojuegos con estado: " + estado);
            respuesta.put("status", HttpStatus.NOT_FOUND);
            respuesta.put("fecha", new Date());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(respuesta);
        }
    }
}
