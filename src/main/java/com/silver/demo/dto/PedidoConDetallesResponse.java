package com.silver.demo.dto;

import com.silver.demo.model.Pedido;

import java.util.List;

import com.silver.demo.model.DetallePedido;
import lombok.Data;


@Data
public class PedidoConDetallesResponse {

    private Pedido pedido;
    private List<DetallePedido> detalles;

    public PedidoConDetallesResponse(Pedido pedido, List<DetallePedido> detalles) {
        this.pedido = pedido;
        this.detalles = detalles;
    }

}
