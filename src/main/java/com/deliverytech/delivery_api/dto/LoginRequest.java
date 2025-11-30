package com.deliverytech.delivery_api.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
@Schema(name = "LoginRequest", description = "Dados necessários para realizar login.")
public class LoginRequest {

    @NotBlank(message = "O email é obrigatório")
    @Email(message = "Email inválido")
    @Schema(description = "Email cadastrado do usuário", example = "gaby_teste@delivery.com")
    private String email;

    @NotBlank(message = "A senha é obrigatória")
    @Schema(description = "Senha do usuário", example = "123456")
    private String senha;
}
