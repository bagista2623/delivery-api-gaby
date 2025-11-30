package com.deliverytech.delivery_api.service;

import com.deliverytech.delivery_api.dto.RegisterRequest;
import com.deliverytech.delivery_api.dto.UserResponse;
import com.deliverytech.delivery_api.entity.Usuario;
import com.deliverytech.delivery_api.enums.Role;
import com.deliverytech.delivery_api.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UsuarioService {

    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;

    public Usuario registrarUsuario(RegisterRequest request) {

        if (usuarioRepository.findByEmail(request.getEmail()).isPresent()) {
            throw new IllegalArgumentException("E-mail já está cadastrado");
        }

        Usuario usuario = Usuario.builder()
                .nome(request.getNome())
                .email(request.getEmail())
                .senha(passwordEncoder.encode(request.getSenha()))
                .role(request.getRole())
                .ativo(true)
                .dataCriacao(java.time.LocalDateTime.now())
                .restauranteId(
                        request.getRole() == Role.RESTAURANTE ? request.getRestauranteId() : null
                )
                .build();

        return usuarioRepository.save(usuario);
    }

    public UserResponse mapToResponse(Usuario usuario) {
        UserResponse dto = new UserResponse();
        dto.setId(usuario.getId());
        dto.setNome(usuario.getNome());
        dto.setEmail(usuario.getEmail());
        dto.setRole(usuario.getRole());
        dto.setRestauranteId(usuario.getRestauranteId());
        return dto;
    }

    public Usuario buscarPorEmail(String email) {
        return usuarioRepository.findByEmail(email).orElse(null);
    }

    public Usuario buscarPorId(Long id) {
        return usuarioRepository.findById(id).orElse(null);
    }
}
