package com.silver.demo.serviceImpl;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;



import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.silver.demo.dto.PedidoRequestDTO;
import com.silver.demo.dto.DetallePedidoRequestDTO;
import com.silver.demo.model.DetallePedido;
import com.silver.demo.model.Pedido;
import com.silver.demo.model.Usuario;
import com.silver.demo.model.Videojuego;
import com.silver.demo.repository.DetallePedidoRepository;
import com.silver.demo.repository.PedidoRepository;
import com.silver.demo.repository.UsuarioRepository;
import com.silver.demo.repository.VideojuegoRepository;
import com.silver.demo.security.UserDetailsImp;
import com.silver.demo.service.PedidoService;

import jakarta.transaction.Transactional;

@Service
public class PedidoServiceImpl implements PedidoService {
	
	@Autowired
	private PedidoRepository repoPedido;
	
	@Autowired
	private VideojuegoRepository videoRepo;
	
	@Autowired
	private DetallePedidoRepository detaRepo;
	
	@Autowired
	private UsuarioRepository usuRepo;
	

	@Override
	@Transactional
	public ResponseEntity<Map<String, Object>> listarPedidos() {
		Map<String, Object> response = new LinkedHashMap<>();
		try {
			List<Pedido> pedidos = repoPedido.findAll();

			if (!pedidos.isEmpty()) {
				// Mapeamos cada pedido
				List<Map<String, Object>> pedidosResponse = pedidos.stream().map(pedido -> {

					Map<String, Object> pedidoMap = new LinkedHashMap<>();

					// Datos básicos del pedido
					pedidoMap.put("id", pedido.getId());
					pedidoMap.put("fecha", pedido.getFecha());
					pedidoMap.put("total", pedido.getTotal());

					// Usar LinkedHashMap para el usuario
					Map<String, Object> usuarioMap = new LinkedHashMap<>();
					usuarioMap.put("id", pedido.getUsuarios().getId());
					usuarioMap.put("nombre", pedido.getUsuarios().getNombre());
					usuarioMap.put("email", pedido.getUsuarios().getEmail());
					pedidoMap.put("usuario", usuarioMap);

					// Obtenemos y mapeamos los detalles
					List<DetallePedido> detalles = detaRepo.findByPedidoId(pedido.getId());
					List<Map<String, Object>> detallesMap = detalles.stream().map(detalle -> {
						// Usar LinkedHashMap para el detalle
						Map<String, Object> detalleMap = new LinkedHashMap<>();
						detalleMap.put("id", detalle.getId());
						detalleMap.put("cantidad", detalle.getCantidad());
						detalleMap.put("precioUnitario", detalle.getPrecioUnitario());

						// Usar LinkedHashMap para el videojuego
						Map<String, Object> videojuegoMap = new LinkedHashMap<>();
						videojuegoMap.put("id", detalle.getVideojuego().getId());
						videojuegoMap.put("titulo", detalle.getVideojuego().getTitulo());
						detalleMap.put("videojuego", videojuegoMap);

						return detalleMap;
					}).collect(Collectors.toList());

					pedidoMap.put("detalles", detallesMap);
					return pedidoMap;
				}).collect(Collectors.toList());

				response.put("status", HttpStatus.OK.value());
				response.put("mensaje", "Lista de pedidos obtenidos correctamente");
				response.put("pedidos", pedidosResponse);

				return ResponseEntity.ok(response);

			} else {
				response.put("status", HttpStatus.NOT_FOUND.value());
				response.put("mensaje", "No se encontraron pedidos");
				return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
			}
		} catch (Exception e) {
			response.put("status", HttpStatus.INTERNAL_SERVER_ERROR.value());
			response.put("mensaje", "Error al obtener los pedidos");
			response.put("error", e.getMessage());
			return ResponseEntity.internalServerError().body(response);
		}
	}
	
	@Override
	@Transactional
	public ResponseEntity<Map<String, Object>> listarPedidoPorId(Long id) {
		Map<String, Object> response = new LinkedHashMap<>();
		try {
			Optional<Pedido> pedidoOpt = repoPedido.findById(id);

			if (pedidoOpt.isPresent()) {
				Pedido pedido = pedidoOpt.get();
				
				Map<String, Object> pedidoMap = new LinkedHashMap<>();
				pedidoMap.put("id", pedido.getId());
				pedidoMap.put("fecha", pedido.getFecha());
				pedidoMap.put("total", pedido.getTotal());

				
				Map<String, Object> usuarioMap = new LinkedHashMap<>();
				usuarioMap.put("id", pedido.getUsuarios().getId());
				usuarioMap.put("nombre", pedido.getUsuarios().getNombre());
				usuarioMap.put("email", pedido.getUsuarios().getEmail());
				pedidoMap.put("usuario", usuarioMap);

				List<DetallePedido> detalles = detaRepo.findByPedidoId(pedido.getId());
				List<Map<String, Object>> detallesMap = detalles.stream().map(detalle -> {
					Map<String, Object> detalleMap = new LinkedHashMap<>();
					detalleMap.put("id", detalle.getId());
					detalleMap.put("cantidad", detalle.getCantidad());
					detalleMap.put("precioUnitario", detalle.getPrecioUnitario());

					// Videojuego
					Map<String, Object> videojuegoMap = new LinkedHashMap<>();
					videojuegoMap.put("id", detalle.getVideojuego().getId());
					videojuegoMap.put("titulo", detalle.getVideojuego().getTitulo());
					detalleMap.put("videojuego", videojuegoMap);

					return detalleMap;
				}).collect(Collectors.toList());

				pedidoMap.put("detalles", detallesMap);

				// Respuesta final ordenada
				response.put("mensaje", "Pedido Encontrado");
				response.put("pedido", pedidoMap);
				response.put("status", HttpStatus.OK);
				return ResponseEntity.ok().body(response);

			} else {
				response.put("mensaje", "Sin registros con el ID: " + id);
				response.put("status", HttpStatus.NOT_FOUND.value());
				return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
			}
		} catch (Exception e) {
			
			response.put("mensaje", "Error al buscar pedido");
			response.put("error", e.getMessage());
			response.put("status", HttpStatus.INTERNAL_SERVER_ERROR.value());
			return ResponseEntity.internalServerError().body(response);
		}
	}
	

	@Override
	@Transactional
	public ResponseEntity<Map<String, Object>> agregarPedido(PedidoRequestDTO pedidoRequest) {
	    Map<String, Object> response = new HashMap<>();

	    try {
	        // Obtener usuario autenticado
	        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
	        UserDetailsImp userDetails = (UserDetailsImp) auth.getPrincipal();
	        Usuario usuario = userDetails.getUsuario();

	        // Crear pedido
	        Pedido pedido = new Pedido();
	        pedido.setUsuarios(usuario);
	        pedido.setFecha(LocalDateTime.now());

	        List<DetallePedido> listaDetalles = new ArrayList<>();
	        double total = 0;

	        for (DetallePedidoRequestDTO detalleReq : pedidoRequest.getDetalles()) {
	            Videojuego videojuego = videoRepo.findById(detalleReq.getVideojuegoId())
	                .orElseThrow(() -> new RuntimeException("Videojuego no encontrado"));

	            if (videojuego.getStock() < detalleReq.getCantidad() || videojuego.getEstado().equalsIgnoreCase("I")) {
	                throw new RuntimeException("Stock insuficiente o videojuego inactivo: " + videojuego.getTitulo());
	            }

	            DetallePedido detalle = new DetallePedido();
	            detalle.setPedido(pedido);
	            detalle.setVideojuego(videojuego);
	            detalle.setCantidad(detalleReq.getCantidad());
	            detalle.setPrecioUnitario(videojuego.getPrecio());

	            listaDetalles.add(detalle);

	            videojuego.setStock(videojuego.getStock() - detalleReq.getCantidad());
	            videoRepo.save(videojuego);

	            total += detalle.getPrecioUnitario() * detalle.getCantidad();
	        }

	        pedido.setDetallePedido(listaDetalles);
	        pedido.setTotal(total);

	        Pedido pedidoGuardado = repoPedido.save(pedido);

	        response.put("pedido", pedidoGuardado);
	        response.put("mensaje", "Pedido creado con éxito");
	        response.put("status", HttpStatus.CREATED);
	        return ResponseEntity.status(HttpStatus.CREATED).body(response);

	    } catch (RuntimeException ex) {
	        response.put("mensaje", ex.getMessage());
	        response.put("status", HttpStatus.BAD_REQUEST.value());
	        return ResponseEntity.badRequest().body(response);

	    } catch (Exception ex) {
	        response.put("mensaje", "Error al agregar pedido");
	        response.put("error", ex.getMessage());
	        response.put("status", HttpStatus.INTERNAL_SERVER_ERROR.value());
	        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
	    }
	}



	@Override
	@Transactional
	public ResponseEntity<Map<String, Object>> editarPedido(PedidoRequestDTO pedidoRequest, Long id) {
	    Map<String, Object> response = new HashMap<>();

	    try {
	        // Obtener el usuario autenticado
	        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
	        UserDetailsImp userDetails = (UserDetailsImp) auth.getPrincipal();
	        Usuario usuario = userDetails.getUsuario();

	        // Buscar el pedido del usuario autenticado
	        Pedido pedido = repoPedido.findByIdAndUsuarioId(id, usuario.getId())
	                .orElseThrow(() -> new RuntimeException("Pedido no encontrado o no pertenece al usuario"));

	        // Validar que haya detalles
	        if (pedidoRequest.getDetalles() == null || pedidoRequest.getDetalles().isEmpty()) {
	            throw new RuntimeException("El pedido debe tener al menos un detalle");
	        }

	        List<DetallePedido> detallesActuales = detaRepo.findByPedidoId(id);
	        double total = 0;

	        for (DetallePedidoRequestDTO detalleReq : pedidoRequest.getDetalles()) {
	            Videojuego videojuego = videoRepo.findById(detalleReq.getVideojuegoId())
	                    .orElseThrow(() -> new RuntimeException("Videojuego no encontrado"));

	            Optional<DetallePedido> detalleExistente = detallesActuales.stream()
	                    .filter(d -> d.getVideojuego().getId().equals(detalleReq.getVideojuegoId()))
	                    .findFirst();

	            if (detalleExistente.isPresent()) {
	                DetallePedido detalle = detalleExistente.get();
	                ajustarStock(detalle, detalleReq.getCantidad());
	                detalle.setCantidad(detalleReq.getCantidad());
	                detaRepo.save(detalle);
	            } else {
	                if (videojuego.getStock() < detalleReq.getCantidad()) {
	                    throw new RuntimeException("Stock insuficiente para: " + videojuego.getTitulo());
	                }

	                DetallePedido nuevoDetalle = new DetallePedido();
	                nuevoDetalle.setPedido(pedido);
	                nuevoDetalle.setVideojuego(videojuego);
	                nuevoDetalle.setCantidad(detalleReq.getCantidad());
	                nuevoDetalle.setPrecioUnitario(videojuego.getPrecio());

	                detaRepo.save(nuevoDetalle);

	                videojuego.setStock(videojuego.getStock() - detalleReq.getCantidad());
	                videoRepo.save(videojuego);
	            }

	            total += videojuego.getPrecio() * detalleReq.getCantidad();
	        }

	        List<Long> idsEnRequest = pedidoRequest.getDetalles().stream()
	                .map(DetallePedidoRequestDTO::getVideojuegoId)
	                .collect(Collectors.toList());

	        for (DetallePedido d : detallesActuales) {
	            if (!idsEnRequest.contains(d.getVideojuego().getId())) {
	                devolverStockAlCancelar(d);
	                detaRepo.delete(d);
	            }
	        }

	        pedido.setTotal(total);
	        pedido.setFecha(LocalDateTime.now());
	        Pedido pedidoActualizado = repoPedido.save(pedido);

	        response.put("mensaje", "Pedido actualizado exitosamente.");
	        response.put("pedido", pedidoActualizado);
	        response.put("fecha", new Date());
	        response.put("status", HttpStatus.OK);
	        return ResponseEntity.ok(response);

	    } catch (Exception e) {
	        response.put("mensaje", "Error al actualizar pedido");
	        response.put("error", e.getMessage());
	        response.put("status", HttpStatus.INTERNAL_SERVER_ERROR.value());
	        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
	    }
	}



	@Override
	@Transactional
	public ResponseEntity<Map<String, Object>> eliminarPedido(Long pedidoId) {
	    Map<String, Object> response = new HashMap<>();

	    try {
	        // Obtener usuario autenticado
	        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
	        UserDetailsImp userDetails = (UserDetailsImp) auth.getPrincipal();
	        Usuario usuario = userDetails.getUsuario();

	        // Buscar el pedido por ID y usuario
	        Pedido pedido = repoPedido.findByIdAndUsuarioId(pedidoId, usuario.getId())
	            .orElseThrow(() -> new RuntimeException("No existe pedido con ID: " + pedidoId + " para este usuario"));

	        // Devolver stock de cada videojuego
	        for (DetallePedido detalle : pedido.getDetallePedido()) {
	            Videojuego videojuego = detalle.getVideojuego();
	            videojuego.setStock(videojuego.getStock() + detalle.getCantidad());
	            videoRepo.save(videojuego);
	        }

	        // Eliminar el pedido (por cascade también borra los detalles)
	        repoPedido.delete(pedido);

	        response.put("mensaje", "Pedido con ID " + pedidoId + " eliminado correctamente");
	        return ResponseEntity.ok(response);

	    } catch (RuntimeException e) {
	        response.put("mensaje", e.getMessage());
	        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);

	    } catch (Exception e) {
	        response.put("mensaje", "Error al eliminar pedido");
	        response.put("error", e.getMessage());
	        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
	    }
	}



	
	private void ajustarStock(DetallePedido detalleExistente, int nuevaCantidad) {
	    Videojuego videojuego = detalleExistente.getVideojuego();
	    int cantidadAnterior = detalleExistente.getCantidad();
	    int diferencia = nuevaCantidad - cantidadAnterior;

	    // Si se quiere aumentar la cantidad
	    if (diferencia > 0) {
	        if (videojuego.getStock() < diferencia) {
	            throw new RuntimeException("Stock insuficiente para aumentar cantidad de: " + videojuego.getTitulo());
	        }
	        videojuego.setStock(videojuego.getStock() - diferencia); // quitar unidades adicionales
	    }
	    // Si se quiere reducir la cantidad
	    else if (diferencia < 0) {
	        videojuego.setStock(videojuego.getStock() + Math.abs(diferencia)); // devolver unidades sobrantes
	    }

	    videoRepo.save(videojuego);
	}



	private void devolverStockAlCancelar(DetallePedido detalle) {
	    Videojuego videojuego = detalle.getVideojuego();
	    videojuego.setStock(videojuego.getStock() + detalle.getCantidad());
	    videoRepo.save(videojuego);
	}
	
}