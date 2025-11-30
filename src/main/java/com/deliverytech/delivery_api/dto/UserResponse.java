package com.deliverytech.delivery_api.dto;

import com.deliverytech.delivery_api.enums.Role;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(name = "UserResponse", description = "Dados públicos retornados sobre o usuário.")
public class UserResponse {

    @Schema(description = "ID do usuário", example = "1")
    private Long id;

    @Schema(description = "Nome do usuário", example = "Gabrielle Fraga")
    private String nome;

    @Schema(description = "Email do usuário", example = "gaby@delivery.com")
    private String email;

    @Schema(description = "Função/perfil do usuário", example = "CLIENTE")
    private Role role;

    @Schema(description = "ID do restaurante associado (se aplicável)", example = "10")
    private Long restauranteId;
}
