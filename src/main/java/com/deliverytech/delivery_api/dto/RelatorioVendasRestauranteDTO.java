package com.deliverytech.delivery_api.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class RelatorioVendasRestauranteDTO {
    private String restaurante;
    private Double totalVendas;
}
