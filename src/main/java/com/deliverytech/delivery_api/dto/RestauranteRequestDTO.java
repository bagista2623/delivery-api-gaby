package com.deliverytech.delivery_api.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class RestauranteRequestDTO {

    @NotBlank(message = "O nome é obrigatório")
    private String nome;

    @NotBlank(message = "O endereço é obrigatório")
    private String endereco;

    private String especialidade;
}
