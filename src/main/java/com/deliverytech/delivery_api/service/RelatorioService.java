package com.deliverytech.delivery_api.service;

import com.deliverytech.delivery_api.dto.*;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;

@Service
public class RelatorioService {

    public List<RelatorioVendasRestauranteDTO> gerarVendasPorRestaurante() {
        return Arrays.asList(
                new RelatorioVendasRestauranteDTO("Pizza Express", 3250.00),
                new RelatorioVendasRestauranteDTO("Sushi House", 4890.00),
                new RelatorioVendasRestauranteDTO("Burger Master", 2100.00)
        );
    }

    public List<RelatorioProdutoMaisVendidoDTO> gerarProdutosMaisVendidos() {
        return Arrays.asList(
                new RelatorioProdutoMaisVendidoDTO("X-Burger", 120L),
                new RelatorioProdutoMaisVendidoDTO("Pizza Calabresa", 95L),
                new RelatorioProdutoMaisVendidoDTO("Sushi Hot Roll", 75L)
        );
    }

    public List<RelatorioClientesAtivosDTO> gerarClientesAtivos() {
        return Arrays.asList(
                new RelatorioClientesAtivosDTO("Gaby", 14L),
                new RelatorioClientesAtivosDTO("João Pedro", 10L),
                new RelatorioClientesAtivosDTO("Helena", 9L)
        );
    }

    public RelatorioPedidosPeriodoDTO gerarPedidosPorPeriodo(String inicio, String fim) {
        return new RelatorioPedidosPeriodoDTO(inicio, fim, 42L);
    }
}
