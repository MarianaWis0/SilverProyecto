package com.silver.demo.serviceImpl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.silver.demo.model.Pedido;
import com.silver.demo.repository.PedidoRepository;
import com.silver.demo.service.PedidoService;

@Service
public class PedidoServiceImpl implements PedidoService {
	
	@Autowired
	private PedidoRepository repoPedido;

	@Override
    public ResponseEntity<Map<String, Object>> listarPedidos() {
        Map<String, Object> response = new HashMap<>();
        try {
            List<Pedido> pedidos = repoPedido.findAll();
            if (!pedidos.isEmpty()) {
            	response.put("mensaje", "Lista de pedidos");
            	response.put("pedidos", pedidos);
            	response.put("status", HttpStatus.OK);
                return ResponseEntity.status(HttpStatus.OK).body(response);
            } else {
            	response.put("mensaje", "No existen pedidos");
            	response.put("status", HttpStatus.NOT_FOUND);
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
            }
        } catch (Exception e) {
        	response.put("mensaje", "Error al listar pedidos");
        	response.put("error", e.getMessage());
        	response.put("status", HttpStatus.INTERNAL_SERVER_ERROR);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

	@Override
    public ResponseEntity<Map<String, Object>> listarPedidoPorId(Long id) {
        Map<String, Object> response = new HashMap<>();
        try {
            Optional<Pedido> pedido = repoPedido.findById(id);
            if (pedido.isPresent()) {
            	response.put("pedido", pedido.get());
            	response.put("mensaje", "Búsqueda correcta");
            	response.put("status", HttpStatus.OK);
                return ResponseEntity.ok().body(response);
            } else {
            	response.put("mensaje", "Sin registros con el ID: " + id);
            	response.put("status", HttpStatus.NOT_FOUND);
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
            }
        } catch (Exception e) {
        	response.put("mensaje", "Error al buscar pedido");
        	response.put("error", e.getMessage());
        	response.put("status", HttpStatus.INTERNAL_SERVER_ERROR);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

	@Override
    public ResponseEntity<Map<String, Object>> agregarPedido(Pedido pedido) {
        Map<String, Object> response = new HashMap<>();
        try {
            repoPedido.save(pedido);
            response.put("pedido", pedido);
            response.put("mensaje", "Se agregó correctamente el pedido");
            response.put("status", HttpStatus.CREATED);
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        } catch (Exception e) {
        	response.put("mensaje", "Error al agregar pedido");
        	response.put("error", e.getMessage());
        	response.put("status", HttpStatus.INTERNAL_SERVER_ERROR);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

	@Override
    public ResponseEntity<Map<String, Object>> editarPedido(Pedido pedido, Long id) {
        Map<String, Object> response = new HashMap<>();
        try {
            Optional<Pedido> pedidoExis = repoPedido.findById(id);
            if (pedidoExis.isPresent()) {
                Pedido pedidoEditado = pedidoExis.get();
                pedidoEditado.setNombreUsuario(pedido.getNombreUsuario());
                pedidoEditado.setFecha(pedido.getFecha());
                pedidoEditado.setTotal(pedido.getTotal());
                repoPedido.save(pedidoEditado);

                response.put("pedido", pedidoEditado);
                response.put("mensaje", "Datos del pedido actualizados");
                response.put("status", HttpStatus.CREATED);
                return ResponseEntity.status(HttpStatus.CREATED).body(response);
            } else {
            	response.put("mensaje", "Sin registros con el ID: " + id);
            	response.put("status", HttpStatus.NOT_FOUND);
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
            }
        } catch (Exception e) {
        	response.put("mensaje", "Error al actualizar pedido");
        	response.put("error", e.getMessage());
        	response.put("status", HttpStatus.INTERNAL_SERVER_ERROR);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

	@Override
    public ResponseEntity<Map<String, Object>> eliminarPedido(Long id) {
        Map<String, Object> response = new HashMap<>();
        try {
            Optional<Pedido> pedidoExis = repoPedido.findById(id);
            if (pedidoExis.isPresent()) {
                repoPedido.delete(pedidoExis.get());
                response.put("mensaje", "Pedido eliminado correctamente");
                response.put("status", HttpStatus.NO_CONTENT);
                return ResponseEntity.status(HttpStatus.NO_CONTENT).body(response);
            } else {
            	response.put("mensaje", "Sin registros con el ID: " + id);
            	response.put("status", HttpStatus.NOT_FOUND);
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
            }
        } catch (Exception e) {
        	response.put("mensaje", "Error al eliminar pedido");
        	response.put("error", e.getMessage());
        	response.put("status", HttpStatus.INTERNAL_SERVER_ERROR);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }
	
	
	
}