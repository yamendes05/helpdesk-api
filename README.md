# HelpDesk API

API REST para registrar e consultar solicitações de suporte. O projeto centraliza dados como título, descrição, categoria, prioridade e situação dos tickets.

## Tecnologias
- Java 27
- Spring Boot 4.1.2
- Maven
- Spring Web MVC
- Spring Data JPA
- H2
- Jakarta Bean Validation
- JUnit 5 e Spring Boot Test

## Funcionalidades implementadas
- Criar tickets com título, descrição, categoria e prioridade.
- Listar todos os tickets.
- Consultar um ticket pelo ID.
- Validar campos obrigatórios na entrada da API.
- Tratar erros de validação, JSON inválido, parâmetros incompatíveis e tickets não encontrados.
- Aplicar regras de transição de situação no domínio: `OPEN`, `IN_PROGRESS`, `WAITING_FOR_USER`, `RESOLVED` e `CLOSED`.

As transições de situação estão implementadas na entidade, mas ainda não são expostas por endpoints próprios.

## Organização do código
- `controller`: endpoints HTTP.
- `service`: casos de uso e coordenação das operações.
- `domain`: entidade, enums e regras de negócio.
- `dtos`: contratos de entrada e saída.
- `repository`: acesso a dados com Spring Data JPA.
- `exception`: exceções e tratamento centralizado de erros.

## Como executar
Requer Java 27. Na raiz do projeto:

Linux/macOS:
```bash
./mvnw spring-boot:run
```

Windows PowerShell:
```powershell
.\mvnw.cmd spring-boot:run
```

A API fica disponível em `http://localhost:8080`. O banco H2 está configurado para execução local.

## Endpoints
| Método | Rota | Descrição | Sucesso |
|---|---|---|---|
| `POST` | `/tickets` | Cria um ticket | `201 Created` |
| `GET` | `/tickets` | Lista tickets | `200 OK` |
| `GET` | `/tickets/{id}` | Consulta por ID | `200 OK` |

### Exemplo de criação
Requisição `POST /tickets`:

```json
{
  "title": "Erro de login",
  "description": "Não consigo acessar minha conta",
  "category": "Acesso",
  "priority": "HIGH"
}
```

Prioridades aceitas: `LOW`, `MEDIUM`, `HIGH` e `CRITICAL`. A situação e a data de criação são definidas pela aplicação.

## Testes
Execute:

Linux/macOS:
```bash
./mvnw test
```

Windows PowerShell:
```powershell
.\mvnw.cmd test
```

A suíte de testes inspecionada cobre as regras de transição de estado do domínio. Próximo passo: ampliar a cobertura com testes de integração HTTP para criação, listagem, consulta e respostas de erro.

## Próximas evoluções
- Endpoints para transição de status.
- Testes de integração dos endpoints.
- Banco PostgreSQL.
- Documentação OpenAPI/Swagger.
- Autenticação e autorização.