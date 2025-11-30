package com.deliverytech.delivery_api.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
@Schema(name = "LoginResponse", description = "Retorno enviado após login bem-sucedido.")
public class LoginResponse {

    @Schema(description = "Token JWT de autenticação", example = "eyJhbGciOiJIUzI1NiJ9...")
    private String token;

    @Schema(description = "Tipo do token", example = "Bearer")
    private String tipo;

    @Schema(description = "Timestamp de expiração do token", example = "1764529151")
    private Long expiracao;

    @Schema(description = "Dados públicos do usuário autenticado")
    private UserResponse usuario;
}
