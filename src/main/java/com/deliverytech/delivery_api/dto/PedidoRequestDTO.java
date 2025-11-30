package com.deliverytech.delivery_api.dto;

import com.deliverytech.delivery_api.validation.TelefoneValidator;
import io.swagger.v3.oas.annotations.media.Schema;

import jakarta.validation.constraints.*;
import lombok.Data;

import java.util.List;

@Data
@Schema(name = "PedidoRequestDTO",
        description = "Dados enviados pelo cliente para criação de um pedido.")
public class PedidoRequestDTO {

    @NotNull(message = "O ID do cliente é obrigatório")
    @Schema(description = "ID do cliente que está realizando o pedido", example = "3")
    private Long clienteId;

    @NotNull(message = "O ID do restaurante é obrigatório")
    @Schema(description = "ID do restaurante responsável pelo pedido", example = "10")
    private Long restauranteId;

    @NotEmpty(message = "O pedido deve ter pelo menos 1 item")
    @Schema(description = "Lista de itens que compõem o pedido")
    private List<ItemPedidoDTO> itens;

    @NotBlank(message = "O endereço de entrega é obrigatório")
    @Schema(description = "Endereço completo para entrega",
            example = "Rua Flores da Cunha, 1220 - Bairro Centro")
    private String enderecoEntrega;

    @TelefoneValidator.ValidCEP
    @Schema(description = "CEP do endereço do cliente", example = "92410200")
    private String cep;
}
