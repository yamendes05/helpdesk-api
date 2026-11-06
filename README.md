# HelpDesk API

## Sobre o projeto

O HelpDesk é uma API REST para registrar e consultar solicitações de suporte.
Ela centraliza informações como título, descrição, categoria, prioridade e
situação dos tickets, facilitando o acompanhamento de demandas de atendimento.

## Tecnologias

- Java 27
- Spring Boot 4.1.2
- Maven, para dependências e build
- Spring Web MVC, para a API REST
- Spring Data JPA, para persistência
- H2, banco de dados em tempo de execução
- JUnit 5 e Spring Boot Test, para testes

## Funcionalidades

- Criar tickets com título, descrição, categoria e prioridade.
- Listar todos os tickets e consultar um ticket pelo ID.
- Validar os campos obrigatórios e tratar erros HTTP, incluindo `400 Bad Request`
  e `404 Not Found`.
- Representar e validar transições de situação do ticket no domínio: aberto,
  em atendimento, aguardando usuário, resolvido e fechado.

Atualmente, a API HTTP expõe criação, listagem e consulta. As transições de
situação existem nas regras de domínio, mas ainda não possuem endpoints próprios.

## Arquitetura

O código é organizado por responsabilidade nos seguintes pacotes:

- `controller`: recebe as requisições HTTP e define as rotas da API.
- `service`: executa os casos de uso e coordena as operações sobre tickets.
- `domain`: contém a entidade `Ticket`, as prioridades, situações e regras de
  transição de estado.
- `dtos`: define os formatos de entrada e saída da API, separando o contrato HTTP
  da entidade de persistência.
- `repository`: fornece acesso aos tickets persistidos por meio do Spring Data
  JPA.
- `exception`: contém exceções da aplicação e o tratamento centralizado das
  respostas de erro.

## Como executar

Requer Java 27. Na pasta do projeto, inicie a aplicação com Maven Wrapper:

```bash
./mvnw spring-boot:run
```

No Windows PowerShell:

```powershell
.\mvnw.cmd spring-boot:run
```

A API fica disponível em `http://localhost:8080`. O H2 é configurado como banco
de dados em tempo de execução.

## Endpoints

| Método | Rota | Descrição | Resposta de sucesso |
|---|---|---|---|
| `POST` | `/tickets` | Cria um ticket | `201 Created` |
| `GET` | `/tickets` | Lista os tickets | `200 OK` |
| `GET` | `/tickets/{id}` | Consulta um ticket pelo ID | `200 OK` |

### Criar ticket

Exemplo de requisição para `POST /tickets`:

```json
{
  "title": "Erro de login",
  "description": "Não consigo acessar minha conta",
  "category": "Acesso",
  "priority": "HIGH"
}
```

Os campos são obrigatórios. As prioridades aceitas são `LOW`, `MEDIUM`, `HIGH` e
`CRITICAL`. A resposta `201 Created` contém o ticket criado e o cabeçalho
`Location` com o endereço do recurso. `status` e `createdAt` são definidos pelo
servidor.

Exemplo de resposta:

```json
{
  "id": 1,
  "title": "Erro de login",
  "description": "Não consigo acessar minha conta",
  "category": "Acesso",
  "priority": "HIGH",
  "status": "OPEN",
  "createdAt": "2026-11-06T19:00:00"
}
```

`GET /tickets` retorna uma lista com os tickets. `GET /tickets/1` retorna o
ticket correspondente ao ID informado.

### Erros HTTP

- `400 Bad Request`: JSON inválido, prioridade desconhecida ou falha de
  validação. Erros de validação incluem os campos inválidos.
- `404 Not Found`: ticket inexistente para o ID solicitado.
- `500 Internal Server Error`: erro inesperado no servidor.

## Testes

Execute toda a suíte de testes a partir da pasta do projeto:

```bash
./mvnw test
```

No Windows PowerShell:

```powershell
.\mvnw.cmd test
```

Os testes incluem as regras de transição de estado da entidade e testes de
integração da API HTTP com MockMvc e o banco H2.
