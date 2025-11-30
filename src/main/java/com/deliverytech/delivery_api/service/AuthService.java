package com.deliverytech.delivery_api.service;

import com.deliverytech.delivery_api.dto.*;
import com.deliverytech.delivery_api.entity.Usuario;
import com.deliverytech.delivery_api.enums.Role;
import com.deliverytech.delivery_api.repository.UsuarioRepository;
import com.deliverytech.delivery_api.security.JwtUtil;

import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final AuthenticationManager authenticationManager;
    private final UsuarioRepository usuarioRepository;
    private final UsuarioService usuarioService;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;

    // LOGIN
    public LoginResponse login(LoginRequest request) {

        Authentication auth = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getEmail(), request.getSenha()
                )
        );

        Usuario usuario = (Usuario) auth.getPrincipal();
        String token = jwtUtil.generateToken(usuario);

        return new LoginResponse(
                token,
                "Bearer",
                jwtUtil.getExpirationFromToken(token),
                usuarioService.mapToResponse(usuario)
        );
    }

    // REGISTRO
    public UserResponse registrar(RegisterRequest request) {

        Usuario novo = Usuario.builder()
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

        usuarioRepository.save(novo);
        return usuarioService.mapToResponse(novo);
    }
}
