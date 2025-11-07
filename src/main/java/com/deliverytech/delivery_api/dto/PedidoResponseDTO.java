package com.deliverytech.delivery_api.dto;

import com.deliverytech.delivery_api.entity.Pedido;
import lombok.Data;

@Data
public class PedidoResponseDTO {

    private Long id;
    private String descricao;
    private Double total;
    private Boolean ativo;

    public PedidoResponseDTO(Pedido pedido) {
        this.id = pedido.getId();
        this.descricao = pedido.getDescricao();
        this.total = pedido.getTotal();
        this.ativo = pedido.getAtivo();
    }
}
