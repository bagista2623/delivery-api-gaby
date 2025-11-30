package com.deliverytech.delivery_api.dto;

import com.deliverytech.delivery_api.entity.Cliente;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(name = "ClienteResponseDTO", description = "Dados retornados pela API sobre um cliente cadastrado.")
public class ClienteResponseDTO {

    @Schema(description = "ID único do cliente", example = "12")
    private Long id;

    @Schema(description = "Nome completo do cliente", example = "Gabrielle Fraga")
    private String nome;

    @Schema(description = "Email do cliente", example = "gabrielle@email.com")
    private String email;

    @Schema(description = "Telefone do cliente", example = "51987654321")
    private String telefone;

    @Schema(description = "Endereço completo do cliente", example = "Rua das Flores, 123 - Centro")
    private String endereco;

    @Schema(description = "Indica se o cliente está ativo", example = "true")
    private Boolean ativo;

    public ClienteResponseDTO(Cliente save) {
        this.id = save.getId();
        this.nome = save.getNome();
        this.email = save.getEmail();
        this.telefone = save.getTelefone();
        this.endereco = save.getEndereco();
        this.ativo = save.getAtivo();
    }
}
