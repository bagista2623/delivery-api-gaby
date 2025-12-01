-Delivery API — Documentação Completa (Roteiros 1 ao 10)

Este repositório reúne todo o desenvolvimento da Delivery API, seguindo os 10 roteiros propostos em aula, desde a configuração inicial até o deploy via Docker.

-- Sumário

Roteiro 1 – Preparação do Ambiente

Roteiro 2 – Criação do Projeto

Roteiro 3 – Health Check e H2

Roteiro 4 – Estrutura MVC e DTOs

Roteiro 5 – CRUD de Cliente

Roteiro 6 – Módulos Produto, Restaurante e Pedido

Roteiro 7 – Endpoints Extras e Regras

Roteiro 8 – Documentação com Swagger

Roteiro 9 – Padronização e Ajustes Finais

Roteiro 10 – Dockerfile e Docker Compose

Como rodar

Console H2

Tecnologias utilizadas

📘 Roteiro 1 – Preparação do Ambiente

Objetivo: preparar todas as ferramentas para iniciar o desenvolvimento.

Atividades realizadas:

Instalação do JDK 21

Instalação do IntelliJ IDEA

Configuração do Maven

Instalação do Git e criação do repositório

Teste do primeiro commit

📘 Roteiro 2 – Criação do Projeto

Objetivo: criar o projeto base usando Spring Boot.

Atividades realizadas:

Geração do projeto no Spring Initializr

Adição das dependências:

Spring Web

Spring Data JPA

H2 Database

Lombok

Validation

Criação do pacote base com.deliverytech.delivery_api

📘 Roteiro 3 – Configuração do H2 + Health Check

Objetivo: validar a inicialização da API e configurar o banco em memória.

Atividades realizadas:

Criação do endpoint /health

Configuração completa do H2

Teste do console H2

Ajustes no application.properties

Verificação da criação automática de tabelas

📘 Roteiro 4 – Estrutura MVC e Criação dos DTOs

Objetivo: organizar o projeto seguindo boas práticas.

Pastas criadas:

controller

service

repository

entity

dto

Atividades realizadas:

Implementação dos DTOs de Cliente

Criação da entidade Cliente

Criação do repositório ClienteRepository

📘 Roteiro 5 – CRUD Completo de Cliente

Objetivo: implementar o CRUD completo do módulo Cliente.

Atividades realizadas:

Endpoint para cadastrar clientes

Listagem de todos clientes ativos

Busca por ID

Atualização de dados

Inativação (soft delete)

Validações importantes no Service

📘 Roteiro 6 – Módulos Produto, Restaurante e Pedido

Objetivo: criar os demais módulos seguindo o mesmo padrão de Cliente.

Atividades realizadas:

Criação das entidades Produto, Restaurante e Pedido

Criação dos DTOs de Request e Response

Criação dos Services e Controllers

Configuração dos relacionamentos

Regras iniciais de negócio (ex.: estoque, valores)

📘 Roteiro 7 – Endpoints Extras e Regras de Negócio

Objetivo: complementar o projeto com funcionalidades avançadas.

Atividades realizadas:

Busca de produtos por nome

Listagem de produtos por restaurante

Busca de clientes por nome

Filtro de pedidos por status

Cálculo automático de valores

Implementação de soft delete em todas as entidades

Ajustes nos DTOs e validações

📘 Roteiro 8 – Documentação com Swagger

Objetivo: gerar documentação automática da API.

Atividades realizadas:

Configuração do SpringDoc no projeto

Documentação dos endpoints

Organização e padronização das respostas

📘 Roteiro 9 – Padronização, Refatoração e README

Objetivo: corrigir detalhes finais do código.

Atividades realizadas:

Ajustes gerais nos Services e Controllers

Padronização das respostas HTTP

Melhorias nos nomes de variáveis e DTOs

Organização das camadas

Criação deste README completo

📘 Roteiro 10 – Dockerfile e Docker Compose

Objetivo: preparar o projeto para rodar em containers.

Atividades realizadas:

Criação do Dockerfile

Criação do docker-compose.yml

Configuração dos serviços necessários

Build e execução dos containers

Teste completo da aplicação rodando via Docker

▶ Como rodar o projeto (sem Docker)

Windows:

mvnw.cmd spring-boot:run


Linux / WSL:

./mvnw spring-boot:run

🗄 Console H2

Configuração usada:

JDBC URL: jdbc:h2:mem:deliverydb

Usuário: sa

Senha: vazio

🛠 Tecnologias utilizadas

Java 21

Spring Boot

Spring Data JPA

H2 Database

Maven

Lombok

Swagger

Docker

Git e GitHub