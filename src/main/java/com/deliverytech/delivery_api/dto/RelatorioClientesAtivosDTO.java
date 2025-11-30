package com.deliverytech.delivery_api.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class RelatorioClientesAtivosDTO {
    private String nomeCliente;
    private Long quantidadePedidos;
}
