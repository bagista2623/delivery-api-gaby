package com.deliverytech.delivery_api.dto;

import lombok.Data;
import jakarta.validation.constraints.NotBlank;

@Data
public class RestauranteRequestDTO {

    @NotBlank(message = "O nome do restaurante é obrigatório")
    private String nome;

    @NotBlank(message = "A categoria é obrigatória")
    private String categoria;

    @NotBlank(message = "O endereço é obrigatório")
    private String endereco;

    private String telefone;

    private String cnpj;

    private String horarioFuncionamento;

    private String faixaPreco;

    private String descricao;
}
