package com.deliverytech.delivery_api.controller;

import com.deliverytech.delivery_api.config.TestSecurityConfig;
import com.deliverytech.delivery_api.dto.ClienteRequestDTO;
import com.deliverytech.delivery_api.repository.ClienteRepository;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;

import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@Import(TestSecurityConfig.class)   // ← DESABILITA A SEGURANÇA NOS TESTES
class ClienteControllerIT {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ClienteRepository clienteRepository;

    @Autowired
    private ObjectMapper objectMapper;

    @BeforeEach
    void limparBanco() {
        clienteRepository.deleteAll();
    }

    @Test
    @DisplayName("Deve criar cliente com sucesso")
    void deveCriarCliente() throws Exception {

        ClienteRequestDTO dto = new ClienteRequestDTO(
                "Gaby Test",
                "gaby@test.com",
                "51999999999",
                "Rua Teste 123"
        );

        mockMvc.perform(
                        post("/api/clientes")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(dto))
                )
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.nome").value("Gaby Test"))
                .andExpect(jsonPath("$.email").value("gaby@test.com"));
    }

    @Test
    @DisplayName("Deve retornar erro ao criar cliente inválido")
    void erroCriarClienteInvalido() throws Exception {

        ClienteRequestDTO dto = new ClienteRequestDTO(
                "",               // nome inválido
                "email_invalido", // email inválido
                "123",            // telefone inválido
                ""                // endereço inválido
        );

        mockMvc.perform(
                        post("/api/clientes")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(dto))
                )
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("Deve buscar cliente por ID")
    void deveBuscarClientePorId() throws Exception {

        var cliente = clienteRepository.save(
                new com.deliverytech.delivery_api.entity.Cliente(
                        null,
                        "Maria Silva",
                        "maria@test.com",
                        "51988888888",
                        "Rua X",
                        null,
                        true
                )
        );

        mockMvc.perform(get("/api/clientes/" + cliente.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nome").value("Maria Silva"))
                .andExpect(jsonPath("$.email").value("maria@test.com"));
    }

    @Test
    @DisplayName("Deve retornar 404 ao buscar cliente inexistente")
    void deveRetornar404() throws Exception {
        mockMvc.perform(get("/api/clientes/999999"))
                .andExpect(status().isNotFound());
    }
}
