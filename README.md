# Delivery API — Documentação Completa (Roteiros 1 ao 10)

Este repositório reúne todo o desenvolvimento da Delivery API, seguindo os 10 roteiros da disciplina, desde a criação do projeto até a execução via Docker.

# Sumário

Roteiro 1 – Preparação do Ambiente

Roteiro 2 – Criação do Projeto

Roteiro 3 – Health Check e H2

Roteiro 4 – Estrutura MVC e DTOs

Roteiro 5 – CRUD Cliente

Roteiro 6 – Produto, Restaurante e Pedido

Roteiro 7 – Endpoints Extras e Regras

Roteiro 8 – Swagger

Roteiro 9 – Padronização

Roteiro 10 – Docker

Como rodar

Console H2

Tecnologias

### Roteiro 1 – Preparação do Ambiente

Instalação do JDK 21

Instalação do IntelliJ

Configuração do Maven

Instalação do Git e criação do repositório

Primeiro commit

### Roteiro 2 – Criação do Projeto

Geração do projeto no Spring Initializr

Dependências principais: Web, JPA, H2, Lombok, Validation

Criação do pacote base

Teste inicial da aplicação

### Roteiro 3 – Health Check e H2

Criação do endpoint /health

Configuração do H2 no application.properties

Acesso ao console H2

Geração automática das tabelas

### Roteiro 4 – Estrutura MVC e DTOs

Criação das camadas: controller, service, repository, entity, dto

DTOs de Cliente

Entidade Cliente

ClienteRepository

### Roteiro 5 – CRUD Cliente

Cadastro

Listagem

Busca por ID

Atualização

Inativação (soft delete)

Validações no Service

### Roteiro 6 – Produto, Restaurante e Pedido

Criação das entidades

DTOs de request e response

Services e Controllers

Relacionamentos (ex.: Restaurante → Produtos)

Regras básicas de negócio

### Roteiro 7 – Endpoints Extras e Regras

Busca de produtos por nome

Listagem por restaurante

Busca de clientes por nome

Filtro de pedidos

Cálculo de valores

Soft delete aplicado em todas as entidades

### Roteiro 8 – Swagger

Integração com SpringDoc OpenAPI

Acesso: http://localhost:8080/swagger-ui.html

Organização dos endpoints

### Roteiro 9 – Padronização e Ajustes

Refatoração geral

Padronização das respostas HTTP

Melhoria dos DTOs

Ajustes nos services e controllers

Criação do README

### Roteiro 10 – Docker

Criação do Dockerfile

Criação do docker-compose.yml

Build e execução dos containers

Testes com a API rodando via Docker

###  Como rodar o projeto (sem Docker)
Windows
mvnw.cmd spring-boot:run

Linux/WSL
./mvnw spring-boot:run

### Como rodar o projeto (com Docker)
Build da imagem
docker build -t delivery-api-gaby .

Executar com Docker Compose
docker compose up --build


Aplicação:

http://localhost:8080


Swagger:

http://localhost:8080/swagger-ui.html

🗄 Console H2

JDBC URL: jdbc:h2:mem:deliverydb

Usuário: sa

Senha: (vazio)

Acesso:

http://localhost:8080/h2-console

### 🛠 Tecnologias utilizadas

Java 21

Spring Boot

Spring Data JPA

H2 Database

Maven

Lombok

Swagger / SpringDoc

Docker

Git e GitHub