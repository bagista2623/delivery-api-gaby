package com.deliverytech.delivery_api.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
@Schema(name = "ClienteRequestDTO", description = "Dados enviados para cadastro ou atualização de um cliente.")
public class ClienteRequestDTO {

    @NotBlank(message = "O nome é obrigatório")
    @Size(min = 2, max = 100, message = "O nome deve ter entre 2 e 100 caracteres")
    @Schema(description = "Nome completo do cliente", example = "Gabrielle Fraga")
    private String nome;

    @NotBlank(message = "O email é obrigatório")
    @Email(message = "Formato de email inválido")
    @Schema(description = "Email do cliente", example = "gabrielle@email.com")
    private String email;

    @NotBlank(message = "O telefone é obrigatório")
    @Pattern(regexp = "\\d{10,11}", message = "Telefone deve ter 10 ou 11 dígitos")
    @Schema(description = "Telefone do cliente no formato apenas números", example = "51987654321")
    private String telefone;

    @NotBlank(message = "O endereço é obrigatório")
    @Schema(description = "Endereço para entrega", example = "Rua das Flores, 123 - Centro")
    private String endereco;
}
