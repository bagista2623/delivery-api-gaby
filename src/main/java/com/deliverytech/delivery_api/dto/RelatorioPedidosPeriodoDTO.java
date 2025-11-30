package com.deliverytech.delivery_api.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class RelatorioPedidosPeriodoDTO {
    private String dataInicio;
    private String dataFim;
    private Long totalPedidos;
}
