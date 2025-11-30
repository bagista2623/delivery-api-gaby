package com.deliverytech.delivery_api.entity;

import com.deliverytech.delivery_api.enums.StatusPedido;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "pedidos")
@Schema(name = "Pedido", description = "Entidade que representa um pedido realizado por um cliente.")
public class Pedido {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(description = "ID único do pedido", example = "45")
    private Long id;

    @Schema(description = "Descrição do pedido", example = "Pedido contendo 2 pizzas e 1 refrigerante")
    private String descricao;

    @Schema(description = "Valor total do pedido", example = "89.50")
    private Double total;

    @Builder.Default
    @Column(nullable = false)
    @Schema(description = "Indica se o pedido está ativo", example = "true")
    private Boolean ativo = true;

    @Builder.Default
    @Enumerated(EnumType.STRING)
    @Schema(description = "Status atual do pedido",
            example = "PENDENTE",
            allowableValues = {"PENDENTE", "CONFIRMADO", "PREPARANDO", "SAIU_PARA_ENTREGA", "ENTREGUE", "CANCELADO"})
    private StatusPedido status = StatusPedido.PENDENTE;

    @Builder.Default
    @Column(name = "data_criacao", nullable = false)
    @Schema(description = "Data e hora da criação do pedido", example = "2025-11-30T14:00:00")
    private LocalDateTime dataCriacao = LocalDateTime.now();

    @ManyToOne
    @JoinColumn(name = "cliente_id", nullable = false)
    @Schema(description = "Cliente que realizou o pedido")
    private Cliente cliente;

    @ManyToOne
    @JoinColumn(name = "restaurante_id")
    @Schema(description = "Restaurante responsável pelo pedido")
    private Restaurante restaurante;

    @Schema(description = "Endereço de entrega", example = "Rua das Flores, 123 - Centro")
    private String enderecoEntrega;

    @Schema(description = "Número único do pedido", example = "PED-2025-00123")
    private String numeroPedido;
}
