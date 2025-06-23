package com.silver.demo.serviceImpl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.silver.demo.model.DetallePedido;
import com.silver.demo.model.Videojuego;
import com.silver.demo.model.Pedido;

import com.silver.demo.repository.DetallePedidoRepository;
import com.silver.demo.repository.VideojuegoRepository;
import com.silver.demo.repository.PedidoRepository;

import com.silver.demo.service.DetallePedidoService;

@Service
public class DetallePedidoServiceImpl implements DetallePedidoService {

	@Autowired
	private DetallePedidoRepository repoDetalle;

	@Autowired
	private PedidoRepository repoPedido;

	@Autowired
	private VideojuegoRepository repoVideojuego;

	// 1. Listar todos
	@Override
	public ResponseEntity<Map<String, Object>> listarDetalles() {
		Map<String, Object> r = new HashMap<>();
		List<DetallePedido> lista = repoDetalle.findAll();
		if (!lista.isEmpty()) {
			r.put("mensaje", "Lista de detalles");
			r.put("detalles", lista);
			r.put("status", HttpStatus.OK);
			return ResponseEntity.ok(r);
		}
		r.put("mensaje", "Sin registros");
		r.put("status", HttpStatus.NOT_FOUND);
		return ResponseEntity.status(HttpStatus.NOT_FOUND).body(r);
	}

	@Override
	public ResponseEntity<Map<String, Object>> listarDetallePorId(Long id) {
		Map<String, Object> r = new HashMap<>();
		return repoDetalle.findById(id).map(det -> {
			r.put("detalle", det);
			r.put("mensaje", "Detalle encontrado");
			r.put("status", HttpStatus.OK);
			return ResponseEntity.ok(r);
		}).orElseGet(() -> {
			r.put("mensaje", "Sin registros con ID: " + id);
			r.put("status", HttpStatus.NOT_FOUND);
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(r);
		});
	}

	// 3. Listar por Pedido
	@Override
	public ResponseEntity<Map<String, Object>> listarPorPedido(Long pedidoId) {
		Map<String, Object> r = new HashMap<>();
		List<DetallePedido> lista = repoDetalle.findByPedidoId(pedidoId);
		if (!lista.isEmpty()) {
			r.put("mensaje", "Detalles del pedido " + pedidoId);
			r.put("detalles", lista);
			r.put("status", HttpStatus.OK);
			return ResponseEntity.ok(r);
		}
		r.put("mensaje", "El pedido " + pedidoId + " no tiene detalles");
		r.put("status", HttpStatus.NOT_FOUND);
		return ResponseEntity.status(HttpStatus.NOT_FOUND).body(r);
	}

	// 4. Agregar
	@Override
	public ResponseEntity<Map<String, Object>> agregarDetalle(DetallePedido d) {
		Map<String, Object> r = new HashMap<>();
		try {
			// Validar pedido y videojuego
			Optional<Pedido> pedidoOpt = repoPedido.findById(d.getPedido().getId());
			Optional<Videojuego> juegoOpt = repoVideojuego.findById(d.getVideojuego().getId());
			if (pedidoOpt.isEmpty() || juegoOpt.isEmpty()) {
				r.put("mensaje", "Pedido o videojuego inexistente");
				r.put("status", HttpStatus.NOT_FOUND);
				return ResponseEntity.status(HttpStatus.NOT_FOUND).body(r);
			}

			Videojuego juego = juegoOpt.get();
			if (juego.getStock() < d.getCantidad()) {
				r.put("mensaje", "Stock insuficiente");
				r.put("status", HttpStatus.BAD_REQUEST);
				return ResponseEntity.badRequest().body(r);
			}

			// Precio por unidad = precio actual del juego si no viene en JSON
			if (d.getPrecioUnitario() == null)
				d.setPrecioUnitario(juego.getPrecio());

			// Guardar detalle
			DetallePedido guardado = repoDetalle.save(d);

			// Descontar stock y actualizar pedido.total
			juego.setStock(juego.getStock() - d.getCantidad());
			repoVideojuego.save(juego);

			Pedido pedido = pedidoOpt.get();
			double nuevoTotal = pedido.getTotal() + d.getCantidad() * d.getPrecioUnitario();
			pedido.setTotal(nuevoTotal);
			repoPedido.save(pedido);

			r.put("detalle", guardado);
			r.put("pedidoTotal", nuevoTotal);
			r.put("mensaje", "Detalle añadido");
			r.put("status", HttpStatus.CREATED);
			return ResponseEntity.status(HttpStatus.CREATED).body(r);

		} catch (Exception e) {
			r.put("mensaje", "Error al agregar detalle");
			r.put("error", e.getMessage());
			r.put("status", HttpStatus.INTERNAL_SERVER_ERROR);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(r);
		}
	}

	@Override
	public ResponseEntity<Map<String, Object>> editarDetalle(DetallePedido nuevo, Long id) {
		Map<String, Object> r = new HashMap<>();
		var actual = repoDetalle.findById(id).orElse(null);

		if (actual == null) {
			r.put("mensaje", "Detalle no encontrado");
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(r);
		}

		var juego = actual.getVideojuego();
		var pedido = actual.getPedido();

		// Revertimos el stock y total viejo
		juego.setStock(juego.getStock() + actual.getCantidad());
		pedido.setTotal(pedido.getTotal() - actual.getCantidad() * actual.getPrecioUnitario());

		// Nuevos valores
		actual.setCantidad(nuevo.getCantidad());
		actual.setPrecioUnitario(nuevo.getPrecioUnitario());

		// Aplicamos cambios nuevos
		juego.setStock(juego.getStock() - actual.getCantidad());
		pedido.setTotal(pedido.getTotal() + actual.getCantidad() * actual.getPrecioUnitario());

		repoVideojuego.save(juego);
		repoPedido.save(pedido);
		repoDetalle.save(actual);

		r.put("mensaje", "Detalle actualizado");
		return ResponseEntity.ok(r);
	}
	
	@Override
	public ResponseEntity<Map<String, Object>> eliminarDetalle(Long id) {
	    Map<String, Object> r = new HashMap<>();
	    var detalle = repoDetalle.findById(id).orElse(null);

	    if (detalle == null) {
	        r.put("mensaje", "Detalle no encontrado");
	        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(r);
	    }

	    var juego = detalle.getVideojuego();
	    var pedido = detalle.getPedido();

	    juego.setStock(juego.getStock() + detalle.getCantidad());
	    pedido.setTotal(pedido.getTotal() - detalle.getCantidad() * detalle.getPrecioUnitario());

	    repoVideojuego.save(juego);
	    repoPedido.save(pedido);
	    repoDetalle.delete(detalle);

	    r.put("mensaje", "Detalle eliminado");
	    return ResponseEntity.ok(r);
	}

}
