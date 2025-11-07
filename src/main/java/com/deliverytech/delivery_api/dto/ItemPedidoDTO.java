package com.deliverytech.delivery_api.dto;
import jakarta.validation.constraints.NotNull;


import lombok.Data;

@Data
public class ItemPedidoDTO {

    @NotNull(message = "O ID do produto é obrigatório")
    private Long produtoId;

    @NotNull(message = "A quantidade é obrigatória")
    private Integer quantidade;
}
