package com.deliverytech.delivery_api.entity;
import com.deliverytech.delivery_api.enums.StatusPedido;


import jakarta.persistence.*;
import lombok.*;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "pedidos")
public class Pedido {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String descricao;
    private Double total;
    @Column(nullable = false)
    private Boolean ativo = true;

    @Enumerated(EnumType.STRING)
    private StatusPedido status = StatusPedido.PENDENTE;


    // 🔹 Relacionamento com Cliente
    @ManyToOne
    @JoinColumn(name = "cliente_id", nullable = false)
    private Cliente cliente;

    // 🔹 Relacionamento com Restaurante (se já tiver essa entity criada)
    @ManyToOne
    @JoinColumn(name = "restaurante_id", nullable = true)
    private Restaurante restaurante;

    // 🔹 Endereço de entrega simples
    private String enderecoEntrega;

    private String numeroPedido;
}
