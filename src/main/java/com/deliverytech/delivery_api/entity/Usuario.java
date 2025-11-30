package com.deliverytech.delivery_api.entity;

import com.deliverytech.delivery_api.enums.Role;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;

@Entity
@Table(name = "usuario")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Schema(name = "Usuario", description = "Usuário do sistema que realiza autenticação e autorização.")
public class Usuario implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(description = "ID único do usuário", example = "1")
    private Long id;

    @Column(nullable = false)
    @Schema(description = "Nome do usuário", example = "Gaby Fraga")
    private String nome;

    @Column(nullable = false, unique = true)
    @Schema(description = "Email utilizado para login", example = "gaby@delivery.com")
    private String email;

    @Column(nullable = false)
    @Schema(description = "Senha criptografada do usuário", example = "$2a$10$XyZ123...")
    private String senha;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    @Schema(description = "Papel do usuário no sistema (CLIENTE, ADMIN, RESTAURANTE)", example = "CLIENTE")
    private Role role;

    @Builder.Default
    @Schema(description = "Indica se o usuário está ativo", example = "true")
    private Boolean ativo = true;

    @Builder.Default
    @Schema(description = "Data de criação do usuário", example = "2025-01-10T11:45:00")
    private LocalDateTime dataCriacao = LocalDateTime.now();

    @Schema(description = "ID do restaurante associado (somente para ROLE RESTAURANTE)", example = "15")
    private Long restauranteId;

    // ===========================
    // SPRING SECURITY
    // ===========================

    @Override
    @Schema(hidden = true)
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(() -> "ROLE_" + this.role.name());
    }

    @Override
    @Schema(hidden = true)
    public String getPassword() {
        return this.senha;
    }

    @Override
    @Schema(hidden = true)
    public String getUsername() {
        return this.email;
    }

    @Override
    @Schema(hidden = true)
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    @Schema(hidden = true)
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    @Schema(hidden = true)
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    @Schema(hidden = true)
    public boolean isEnabled() {
        return this.ativo != null ? this.ativo : true;
    }
}
