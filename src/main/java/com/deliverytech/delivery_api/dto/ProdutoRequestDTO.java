package com.deliverytech.delivery_api.dto;

import com.deliverytech.delivery_api.validation.ValidCategoria;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;
import lombok.Data;

@Data
@Schema(name = "ProdutoRequestDTO",
        description = "Modelo de dados enviado para cadastrar ou atualizar um produto.")
public class ProdutoRequestDTO {

    @NotBlank(message = "O nome é obrigatório")
    @Size(min = 2, max = 50)
    @Schema(description = "Nome do produto", example = "Pizza Calabresa")
    private String nome;

    @NotNull(message = "O preço é obrigatório")
    @DecimalMin(value = "0.01", message = "O preço deve ser maior que zero")
    @DecimalMax(value = "500.00", message = "O preço não pode ultrapassar R$ 500,00")
    @Schema(description = "Preço unitário do produto", example = "39.90")
    private Double preco;

    @NotBlank(message = "A categoria é obrigatória")
    @ValidCategoria
    @Schema(description = "Categoria do produto", example = "Pizza")
    private String categoria;

    @NotBlank(message = "A descrição é obrigatória")
    @Size(min = 10, message = "A descrição deve ter pelo menos 10 caracteres")
    @Schema(description = "Descrição detalhada do produto",
            example = "Pizza tradicional de calabresa com queijo mussarela.")
    private String descricao;

    @NotNull(message = "O ID do restaurante é obrigatório")
    @Schema(description = "ID do restaurante dono do produto", example = "12")
    private Long restauranteId;
}
