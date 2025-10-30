# Delivery API

Este projeto é uma API REST desenvolvida com Spring Boot para gerenciar o sistema de uma empresa fictícia de entregas, chamada **DeliveryTech**.

## Funcionalidades principais

A API permite o cadastro, busca, atualização e inativação (soft delete) das seguintes entidades:

- **Cliente**
- **Produto**
- **Pedido**
- **Restaurante**

## Tecnologias utilizadas

- Java 21
- Spring Boot
- Spring Data JPA
- Banco H2 (memória)
- Maven
- Postman para testes

## Endpoints principais

- `/clientes` → CRUD de clientes
- `/produtos` → CRUD de produtos
- `/pedidos` → CRUD de pedidos
- `/restaurantes` → CRUD de restaurantes

Cada módulo possui também endpoints extras como **busca por nome**, **listagem de ativos**, etc.

# Rode o Projeto com:
- /mvnw spring-boot:run

# Acesse o console H2:

- URL: http://localhost:8080/h2-console

- JDBC URL padrão: jdbc:h2:mem:testdb

- Usuário: sa

- Senha: (deixe em branco)


## Como rodar o projeto

1. **Clone o repositório:**

```bash
git clone https://github.com/bagista2623/delivery-api-gaby.git
cd delivery-api-gaby
