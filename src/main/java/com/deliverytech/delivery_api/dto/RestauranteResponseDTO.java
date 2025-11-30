package com.deliverytech.delivery_api.dto;

import com.deliverytech.delivery_api.entity.Restaurante;

import io.swagger.v3.oas.annotations.media.Schema;

import lombok.Data;

@Data
@Schema(name = "RestauranteResponseDTO",
        description = "Dados retornados sobre um restaurante cadastrado.")
public class RestauranteResponseDTO {

    @Schema(description = "ID único do restaurante", example = "15")
    private Long id;

    @Schema(description = "Nome do restaurante", example = "Pizzaria da Gaby")
    private String nome;

    @Schema(description = "Endereço completo", example = "Av. Central, 500 - Centro")
    private String endereco;

    @Schema(description = "Categoria do restaurante", example = "Pizzaria")
    private String categoria;

    @Schema(description = "Indica se o restaurante está ativo", example = "true")
    private Boolean ativo;

    public RestauranteResponseDTO(Restaurante restaurante) {
        this.id = restaurante.getId();
        this.nome = restaurante.getNome();
        this.endereco = restaurante.getEndereco();
        this.categoria = restaurante.getCategoria();
        this.ativo = restaurante.getAtivo();
    }
}
