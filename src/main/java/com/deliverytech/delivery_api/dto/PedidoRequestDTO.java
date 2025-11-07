package com.deliverytech.delivery_api.dto;

import lombok.Data;
import jakarta.validation.constraints.NotNull;

import java.util.List;

@Data
public class PedidoRequestDTO {

    @NotNull(message = "O ID do cliente é obrigatório")
    private Long clienteId;

    @NotNull(message = "O ID do restaurante é obrigatório")
    private Long restauranteId;

    @NotNull(message = "O endereço de entrega é obrigatório")
    private String enderecoEntrega;

    @NotNull(message = "A lista de itens não pode ser nula")
    private List<ItemPedidoDTO> itens;
}
