
# Projeto Exemplo: Monólito Modular com Arquitetura Hexagonal

Este projeto é uma aplicação de e-commerce desenvolvida em Java, demonstrando a implementação de uma arquitetura de software moderna, escalável e de fácil manutenção. O objetivo principal é servir como um exemplo prático de como estruturar uma aplicação complexa utilizando os princípios do **Domain-Driven Design (DDD)**, **Arquitetura Hexagonal (Portas e Adaptadores)** e o padrão **Monólito Modular**.

## Visão Geral da Arquitetura

A arquitetura foi projetada para ser robusta, testável e de fácil manutenção. A seguir, os principais conceitos aplicados:

- **Arquitetura Hexagonal (Portas e Adaptadores)**: O núcleo da aplicação (o domínio) é completamente isolado de preocupações externas, como frameworks web, bancos de dados ou serviços de terceiros. A comunicação com o mundo exterior é feita através de "portas" (interfaces no domínio) e "adaptadores" (implementações concretas fora do domínio).
  - **Domínio**: Contém a lógica de negócio pura, livre de dependências de infraestrutura.
  - **Adaptadores de Entrada (Driving)**: Ex.: Controladores REST que recebem requisições e invocam os casos de uso do domínio.
  - **Adaptadores de Saída (Driven)**: Ex.: Repositórios que persistem dados, clientes de API que consomem outros serviços.

- **Monólito Modular**: Em vez de uma arquitetura de microserviços, o projeto adota uma abordagem de monólito com alta coesão e baixo acoplamento entre os módulos. Isso simplifica o desenvolvimento e o deploy, ao mesmo tempo que mantém uma separação clara de responsabilidades.

- **Domain-Driven Design (DDD)**: Os módulos são organizados em torno de contextos de negócio (Bounded Contexts). A comunicação entre eles é feita de forma assíncrona através de eventos, garantindo o desacoplamento.

## Tecnologias Utilizadas

- **Java 21**: Plataforma de desenvolvimento.
- **Spring Boot**: Framework para criação de aplicações Java.
- **Spring Modulith**: Para suporte a monólitos modulares.
- **Spring Data JPA**: Para persistência de dados em bancos relacionais.
- **Spring Data MongoDB**: Para interação com o banco de dados MongoDB.
- **Spring Data Redis**: Para caching e outras operações com Redis.
- **Spring Security**: Para autenticação e autorização.
- **Spring Cloud OpenFeign**: Para clientes de API REST declarativos.
- **PostgreSQL**: Banco de dados relacional.
- **MongoDB**: Banco de dados NoSQL.
- **Redis**: Banco de dados em memória.
- **Flyway**: Para migrações de banco de dados.
- **Stripe**: Para processamento de pagamentos.
- **Springdoc OpenAPI**: Para documentação de API.
- **Maven**: Ferramenta de automação de compilação.
- **Docker**: Para containerização da aplicação e serviços.
- **JUnit 5 & Mockito**: Para testes unitários.
- **H2 Database**: Banco de dados em memória para testes.

### Módulos Principais

- **`ecommerce`**: Contém todas as funcionalidades relacionadas ao e-commerce, como gerenciamento de produtos, carrinho de compras e processamento de pedidos.
- **`notifications`**: Responsável por lidar com a criação e o envio de notificações dentro do sistema.
- **`shared`**: Um módulo que contém código compartilhado entre os diferentes módulos, como classes de utilitários, exceções e configurações comuns.

## Primeiros Passos

Siga as instruções abaixo para configurar e executar o projeto em seu ambiente local.

### Pré-requisitos

- **Java 17+**
- **Maven 3.8+**
- **Docker** e **Docker Compose** (para os serviços de infraestrutura)

### Instalação

1. Clone o repositório:
   ```sh
   git clone https://github.com/your-username/your-repository.git
   cd your-repository
   ```

2. **Configuração do Ambiente**

   Copie o arquivo de exemplo de configuração e ajuste as variáveis de ambiente conforme necessário. Por padrão, ele está configurado para usar os serviços do Docker Compose.

   ```sh
   cp src/main/resources/application.properties.example src/main/resources/application.properties
   ```

   Revise o arquivo `application.properties` para garantir que as portas e credenciais estão corretas para seu ambiente.

### Executando a Aplicação

Existem duas maneiras de executar a aplicação:

#### Opção 1: Usando Docker Compose (Recomendado)

Este método irá iniciar a aplicação Java junto com todos os serviços de dependência (MongoDB, etc.) de forma containerizada.

```sh
docker-compose up --build
```

A aplicação estará disponível em `http://localhost:8080`.

#### Opção 2: Executando Localmente com Maven

Se preferir, você pode executar os serviços de dependência via Docker e a aplicação Java diretamente na sua máquina local.

1. Inicie os serviços de banco de dados:
   ```sh
   docker-compose up -d mongo
   ```

2. Em um novo terminal, execute a aplicação Spring Boot:
   ```sh
   ./mvnw spring-boot:run
   ```

## Executando os Testes

Para garantir a qualidade e a integridade do código, execute a suíte de testes completa.

```sh
./mvnw clean test
```

Para verificar a cobertura dos testes, execute:

```sh
./mvnw clean verify
```
