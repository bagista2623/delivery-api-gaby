package com.deliverytech.delivery_api.entity;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "restaurantes")
@Builder
@Schema(description = "Entidade que representa um restaurante cadastrado na plataforma")
public class Restaurante {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(description = "ID do restaurante", example = "10")
    private Long id;

    @Schema(description = "Nome do restaurante", example = "Pizzaria Sabor & Forno")
    private String nome;

    @Schema(description = "Categoria culinária do restaurante", example = "Pizza, Japonesa, Brasileira")
    private String categoria;

    @Schema(description = "Endereço do restaurante", example = "Rua das Flores, 123 - Porto Alegre")
    private String endereco;

    @Schema(description = "Telefone de contato", example = "(51) 99999-9999")
    private String telefone;

    @Schema(description = "CNPJ do restaurante", example = "12.345.678/0001-99")
    private String cnpj;

    @Schema(description = "Horário de funcionamento", example = "08:00 - 18:00")
    private String horarioFuncionamento;

    @Schema(description = "Faixa de preço do restaurante", example = "$$, $$, $$$")
    private String faixaPreco;

    @Schema(description = "Descrição breve do restaurante", example = "Restaurante especializado em comida artesanal.")
    private String descricao;

    @Column(name = "data_cadastro")
    @Schema(description = "Data em que o restaurante foi cadastrado")
    private LocalDateTime dataCadastro;

    @Column(nullable = true)
    @Schema(description = "Indica se o restaurante está ativo", example = "true")
    private Boolean ativo;

    public void inativar() {
        this.ativo = false;
    }
}
