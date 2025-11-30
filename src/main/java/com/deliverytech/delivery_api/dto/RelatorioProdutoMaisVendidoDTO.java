package com.deliverytech.delivery_api.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class RelatorioProdutoMaisVendidoDTO {
    private String produto;
    private Long quantidade;
}
