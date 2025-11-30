package com.deliverytech.delivery_api.dto;

import com.deliverytech.delivery_api.enums.Role;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Schema(name = "RegisterRequest", description = "Modelo para registro de novos usuários.")
public class RegisterRequest {

    @NotBlank(message = "O nome é obrigatório")
    @Schema(description = "Nome completo do usuário", example = "Gabrielle Fraga")
    private String nome;

    @NotBlank
    @Email
    @Schema(description = "Email do usuário", example = "gaby@delivery.com")
    private String email;

    @NotBlank(message = "A senha é obrigatória")
    @Size(min = 6, message = "A senha deve ter no mínimo 6 caracteres")
    @Schema(description = "Senha para login", example = "123456")
    private String senha;

    @Schema(description = "Tipo de usuário (CLIENTE, RESTAURANTE, ADMIN, ENTREGADOR)", example = "CLIENTE")
    private Role role;

    @Schema(description = "ID do restaurante (obrigatório se role = RESTAURANTE)", example = "1")
    private Long restauranteId;
}
