package com.deliverytech.delivery_api.dto;

import com.deliverytech.delivery_api.entity.Produto;
import lombok.Data;

@Data
public class ProdutoResponseDTO {

    private Long id;
    private String nome;
    private String descricao;
    private Double preco;
    private Boolean ativo;

    public ProdutoResponseDTO(Produto produto) {
        this.id = produto.getId();
        this.nome = produto.getNome();
        this.descricao = produto.getDescricao();
        this.preco = produto.getPreco();
        this.ativo = produto.getAtivo();
    }
}
