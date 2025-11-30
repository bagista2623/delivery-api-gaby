package com.deliverytech.delivery_api.dto;

import com.deliverytech.delivery_api.validation.ValidCategoria;
import com.deliverytech.delivery_api.validation.ValidCep;
import com.deliverytech.delivery_api.validation.ValidTelefone;
import com.deliverytech.delivery_api.validation.ValidHorario;

import io.swagger.v3.oas.annotations.media.Schema;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
@Schema(name = "RestauranteRequestDTO",
        description = "Dados enviados para cadastro ou atualização de um restaurante.")
public class RestauranteRequestDTO {

    @NotBlank(message = "O nome do restaurante é obrigatório")
    @Schema(description = "Nome do restaurante", example = "Pizzaria da Gaby")
    private String nome;

    @ValidCategoria
    @NotBlank(message = "A categoria é obrigatória")
    @Schema(description = "Categoria do restaurante", example = "Pizzaria")
    private String categoria;

    @NotBlank(message = "O endereço é obrigatório")
    @Schema(description = "Endereço completo do restaurante", example = "Av. Central, 500 - Centro")
    private String endereco;

    @ValidTelefone
    @Schema(description = "Telefone do restaurante", example = "51999887766")
    private String telefone;

    @Schema(description = "CNPJ do restaurante", example = "12.345.678/0001-90")
    private String cnpj;

    @ValidHorario
    @Schema(description = "Horário de funcionamento do restaurante",
            example = "08:00 - 22:00")
    private String horarioFuncionamento;

    @Schema(description = "Faixa de preço do restaurante",
            example = "$$, $$$")
    private String faixaPreco;

    @Schema(description = "Descrição breve sobre o restaurante",
            example = "Especializado em pizzas artesanais e massas caseiras.")
    private String descricao;

    @ValidCep
    @Schema(description = "CEP do endereço do restaurante", example = "92410200")
    private String cep;
}
