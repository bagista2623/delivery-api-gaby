package com.deliverytech.delivery_api.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
@Schema(name = "ItemPedidoDTO",
        description = "Item que compõe um pedido, contendo o produto e a quantidade.")
public class ItemPedidoDTO {

    @NotNull(message = "O ID do produto é obrigatório")
    @Schema(description = "ID do produto escolhido no pedido", example = "7")
    private Long produtoId;

    @NotNull(message = "A quantidade é obrigatória")
    @Min(value = 1, message = "A quantidade deve ser no mínimo 1")
    @Schema(description = "Quantidade do produto solicitado", example = "2")
    private Integer quantidade;
}
