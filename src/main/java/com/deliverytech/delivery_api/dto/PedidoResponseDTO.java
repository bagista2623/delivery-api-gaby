package com.deliverytech.delivery_api.dto;

import com.deliverytech.delivery_api.entity.Pedido;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(name = "PedidoResponseDTO",
        description = "Dados retornados após a criação ou consulta de um pedido.")
public class PedidoResponseDTO {

    @Schema(description = "ID único do pedido", example = "52")
    private Long id;

    @Schema(description = "Descrição do pedido", example = "2x Pizza Calabresa, 1x Coca-Cola 2L")
    private String descricao;

    @Schema(description = "Valor total do pedido", example = "89.90")
    private Double total;

    @Schema(description = "Indica se o pedido está ativo", example = "true")
    private Boolean ativo;

    public PedidoResponseDTO(Pedido pedido) {
        this.id = pedido.getId();
        this.descricao = pedido.getDescricao();
        this.total = pedido.getTotal();
        this.ativo = pedido.getAtivo();
    }
}
