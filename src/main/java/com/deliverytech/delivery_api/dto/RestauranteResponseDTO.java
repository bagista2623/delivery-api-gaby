package com.deliverytech.delivery_api.dto;

import com.deliverytech.delivery_api.entity.Restaurante;
import lombok.Data;

@Data
public class RestauranteResponseDTO {

    private Long id;
    private String nome;
    private String endereco;
    private String especialidade;
    private Boolean ativo;

    public RestauranteResponseDTO(Restaurante restaurante) {
        this.id = restaurante.getId();
        this.nome = restaurante.getNome();
        this.endereco = restaurante.getEndereco();
        this.especialidade = restaurante.getEspecialidade();
        this.ativo = restaurante.getAtivo();
    }
}
