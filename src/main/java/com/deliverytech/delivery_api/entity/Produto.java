package com.deliverytech.delivery_api.entity;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "produtos")
@Builder
@Schema(name = "Produto", description = "Entidade que representa um produto vinculado a um restaurante.")
public class Produto {



    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(description = "ID único do produto", example = "10")
    private Long id;

    @Schema(description = "Nome do produto", example = "Pizza de Calabresa")
    private String nome;

    @Schema(description = "Descrição detalhada do produto", example = "Pizza grande com borda recheada e queijo extra")
    private String descricao;

    @Schema(description = "Preço do produto", example = "49.90")
    private Double preco;

    @Schema(description = "Indica se o produto está ativo ou inativo", example = "true")
    private Boolean ativo = true;

    @Schema(description = "Categoria do produto", example = "Pizzas")
    private String categoria;

    @ManyToOne
    @JoinColumn(name = "restaurante_id")
    @Schema(description = "Restaurante dono do produto")
    private Restaurante restaurante;

    public void inativar() {
        this.ativo = false;
    }
}
