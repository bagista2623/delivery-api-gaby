package com.deliverytech.delivery_api.service;

import com.deliverytech.delivery_api.dto.ClienteRequestDTO;
import com.deliverytech.delivery_api.entity.Cliente;
import com.deliverytech.delivery_api.repository.ClienteRepository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ClienteServiceTest {

    @Mock
    private ClienteRepository clienteRepository;

    @InjectMocks
    private ClienteService clienteService;

    private Cliente cliente;
    private ClienteRequestDTO dto;

    @BeforeEach
    void setup() {
        cliente = new Cliente();
        cliente.setId(1L);
        cliente.setNome("Gaby");
        cliente.setEmail("gaby@test.com");
        cliente.setTelefone("51999999999");
        cliente.setEndereco("Rua Teste, 123");

        dto = new ClienteRequestDTO();
        dto.setNome("Gaby");
        dto.setEmail("gaby@test.com");
        dto.setTelefone("51999999999");
        dto.setEndereco("Rua Teste, 123");
    }

    // -----------------------------------------
    // TESTE 1 - salvar cliente com sucesso
    // -----------------------------------------
    @Test
    void deveSalvarClienteComSucesso() {
        when(clienteRepository.save(any(Cliente.class))).thenReturn(cliente);

        var resultado = clienteService.cadastrar(dto);

        assertNotNull(resultado);
        assertEquals("Gaby", resultado.getNome());
        verify(clienteRepository, times(1)).save(any());
    }

    // -----------------------------------------
    // TESTE 2 - email duplicado
    // -----------------------------------------
    @Test
    void deveLancarErroQuandoEmailDuplicado() {
        when(clienteRepository.findByEmail("gaby@test.com")).thenReturn(Optional.of(cliente));

        IllegalArgumentException ex = assertThrows(
                IllegalArgumentException.class,
                () -> clienteService.cadastrar(dto)
        );

        assertEquals("E-mail já cadastrado!", ex.getMessage());
        verify(clienteRepository, never()).save(any());
    }

    // -----------------------------------------
    // TESTE 3 - buscar por ID existente
    // -----------------------------------------
    @Test
    void deveBuscarClientePorId() {
        when(clienteRepository.findById(1L)).thenReturn(Optional.of(cliente));

        var resultado = clienteService.buscarPorId(1L);

        assertTrue(resultado.isPresent());
        assertEquals("Gaby", resultado.get().getNome());
    }

    // -----------------------------------------
    // TESTE 4 - buscar por ID inexistente
    // -----------------------------------------
    @Test
    void deveRetornarVazioQuandoNaoEncontrarId() {
        when(clienteRepository.findById(99L)).thenReturn(Optional.empty());

        var resultado = clienteService.buscarPorId(99L);

        assertTrue(resultado.isEmpty());
    }
}
