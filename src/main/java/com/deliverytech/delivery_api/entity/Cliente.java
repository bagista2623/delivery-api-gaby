package com.deliverytech.delivery_api.entity;

import java.time.LocalDateTime;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "clientes")
@Builder
@Schema(name = "Cliente", description = "Entidade que representa um cliente do sistema.")
public class Cliente {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(description = "ID único do cliente", example = "1")
    private Long id;

    @Schema(description = "Nome completo do cliente", example = "Gabrielle Cavalheiro")
    private String nome;

    @Schema(description = "Email do cliente", example = "gaby@example.com")
    private String email;

    @Schema(description = "Telefone do cliente no formato brasileiro", example = "51999999999")
    private String telefone;

    @Schema(description = "Endereço principal do cliente", example = "Rua das Flores, 123 - Porto Alegre/RS")
    private String endereco;

    @Column(name = "data_cadastro")
    @Schema(description = "Data e hora em que o cliente foi cadastrado", example = "2025-05-12T14:30:00")
    private LocalDateTime dataCadastro;

    @Column(nullable = true)
    @Schema(description = "Indica se o cliente está ativo no sistema", example = "true")
    private Boolean ativo;

    public void inativar() {
        this.ativo = false;
    }
}
