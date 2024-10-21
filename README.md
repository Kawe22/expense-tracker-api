# Expense Tracker API

**Expense Tracker API** é uma aplicação RESTful desenvolvida em **Spring Boot** para gerenciamento de despesas pessoais. A API permite que usuários registrem, consultem e gerenciem suas despesas e categorias, além de fornecer autenticação e autorização por meio de **JWT (JSON Web Tokens)**.

## Funcionalidades

- **Autenticação e Autorização JWT**: Usuários podem se registrar e realizar login. As rotas da API são protegidas e requerem tokens JWT para acesso.
- **Gerenciamento de Categorias**: Criação, visualização, atualização e exclusão de categorias de despesas.
- **Gerenciamento de Transações**: Criação, consulta, atualização e exclusão de transações dentro das categorias.
- **Validação de Dados**: Utiliza anotações de validação em DTOs para garantir que os dados fornecidos sejam válidos.
- **Tratamento de Erros**: Sistema de tratamento de exceções para lidar com erros comuns, como autenticação falha, requisições inválidas e recursos não encontrados.

## Tecnologias Utilizadas

- **Java 21**
- **Spring Boot**
- **JWT (JSON Web Tokens)**
- **Hibernate/JPA**
- **H2 Database** (ambiente de desenvolvimento)
- **PostgreSQL** (ambiente de produção)
- **Swagger** (documentação da API)

## Endpoints Principais

### Autenticação

- `POST /api/auth/register`: Registra um novo usuário.
- `POST /api/auth/login`: Autentica um usuário e gera um token JWT.

### Usuários

- `GET /api/users/{id}`: Retorna os dados do usuário autenticado.
- `PUT /api/users/{id}`: Atualiza os dados do usuário.
- `DELETE /api/users/{id}`: Exclui um usuário.

### Categorias

- `GET /api/categories`: Lista todas as categorias do usuário autenticado.
- `POST /api/categories`: Cria uma nova categoria.
- `PUT /api/categories/{categoryId}`: Atualiza uma categoria existente.
- `DELETE /api/categories/{categoryId}`: Exclui uma categoria.

### Transações

- `GET /api/categories/{categoryId}/transactions`: Lista todas as transações de uma categoria.
- `POST /api/categories/{categoryId}/transactions`: Cria uma nova transação.
- `PUT /api/categories/{categoryId}/transactions/{transactionId}`: Atualiza uma transação existente.
- `DELETE /api/categories/{categoryId}/transactions/{transactionId}`: Exclui uma transação.

## Configuração

### Banco de Dados

A API utiliza **H2** para desenvolvimento e **PostgreSQL** em produção. A configuração do banco de dados pode ser ajustada no arquivo `application.properties` ou `application-prod.properties`.

### Dependências

Certifique-se de ter as seguintes dependências configuradas no `pom.xml`:

- Spring Boot Starter Web
- Spring Boot Starter Data JPA
- Spring Boot Starter Security
- JWT
- H2 Database (desenvolvimento)
- PostgreSQL Driver (produção)
- Swagger

Acesse o console do H2 em: http://localhost:8080/h2-console

Acesse a documentação do Swagger em: http://localhost:8080/swagger-ui.html

### Testes
O projeto inclui testes unitários para os serviços.

## Executando o Projeto

### Ambiente de Desenvolvimento

Navegue até o diretório do projeto e execute o seguinte comando para rodar a aplicação:

```bash
./mvnw spring-boot:run

