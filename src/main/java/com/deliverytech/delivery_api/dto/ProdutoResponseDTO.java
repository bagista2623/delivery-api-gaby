package com.deliverytech.delivery_api.dto;

import com.deliverytech.delivery_api.entity.Produto;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Schema(name = "ProdutoResponseDTO",
        description = "Dados retornados após criação, listagem ou atualização de um produto.")
public class ProdutoResponseDTO {

    @Schema(description = "ID do produto", example = "22")
    private Long id;

    @Schema(description = "Nome do produto", example = "Hambúrguer Artesanal")
    private String nome;

    @Schema(description = "Descrição do produto",
            example = "Hambúrguer 150g com queijo cheddar e pão brioche.")
    private String descricao;

    @Schema(description = "Preço do produto", example = "24.90")
    private Double preco;

    @Schema(description = "Produto está ativo no sistema?", example = "true")
    private Boolean ativo;

    @Schema(description = "Categoria do produto", example = "Lanche")
    private String categoria;

    @Schema(description = "ID do restaurante proprietário do produto", example = "5")
    private Long restauranteId;

    public ProdutoResponseDTO(Produto produto) {
        this.id = produto.getId();
        this.nome = produto.getNome();
        this.descricao = produto.getDescricao();
        this.preco = produto.getPreco();
        this.ativo = produto.getAtivo();
        this.categoria = produto.getCategoria();
        this.restauranteId = produto.getRestaurante() != null
                ? produto.getRestaurante().getId()
                : null;
    }
}
