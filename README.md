# 🎉 Event Service

Microserviço para gerenciamento de eventos construído com **Spring Boot** seguindo o padrão **Hexagonal Architecture** (Clean Architecture).

---

## 📋 Índice

- [Visão Geral](#visão-geral)
- [Arquitetura](#arquitetura)
- [Pré-requisitos](#pré-requisitos)
- [Instalação](#instalação)
- [Rodando a Aplicação](#rodando-a-aplicação)
- [Endpoints](#endpoints)
- [Exemplos de Uso](#exemplos-de-uso)
- [Estrutura do Projeto](#estrutura-do-projeto)
- [Tecnologias](#tecnologias)
- [Configurações](#configurações)

---

## 🎯 Visão Geral

Event Service é um microserviço que implementa operações CRUD para gerenciar eventos. Cada evento possui:

- **ID**: UUID gerado automaticamente
- **Nome**: Obrigatório
- **Descrição**: Opcional
- **Data/Hora**: Opcional (ISO 8601)
- **Localização**: Opcional

**Status atual:** Apenas operação CREATE implementada (POST /api/events)

---

## 🏗️ Arquitetura

O projeto segue o **Padrão Hexagonal** (Clean Architecture), que organiza o código em camadas concêntricas:

```
┌─────────────────────────────────────────────────────┐
│  🌐 ADAPTER IN (Entrada)                            │
│  - EventController (HTTP REST)                      │
│  - DTOs (EventCreateRequest, EventResponse)         │
└────────────────┬────────────────────────────────────┘
                 │
        ┌────────▼────────┐
        │  🚪 PORT IN     │
        │ EventInputPort  │
        └────────┬────────┘
                 │
        ┌────────▼──────────────────┐
        │  📦 APPLICATION LAYER     │
        │  - EventApplicationService│
        │  - Casos de uso           │
        └────────┬──────────────────┘
                 │
        ┌────────▼────────┐
        │  🚪 PORT OUT    │
        │ EventOutputPort │
        └────────┬────────┘
                 │
┌────────────────▼────────────────┐
│  💾 ADAPTER OUT (Saída)         │
│  - EventRepositoryAdapter       │
│  - EventEntity (JPA/Hibernate)  │
│  - EventJpaRepository           │
└────────────────┬────────────────┘
                 │
        ┌────────▼────────┐
        │  🗄️ DATABASE    │
        │  PostgreSQL     │
        └─────────────────┘

🎯 Domínio (Centro - Puro)
   └─ Event.java (sem dependências de framework)
```

### Benefícios dessa arquitetura:

✅ **Domínio isolado** — Lógica de negócio sem dependências de framework  
✅ **Testável** — Fácil fazer testes unitários sem mocks complexos  
✅ **Reutilizável** — Mesmo domínio funciona com gRPC, CLI, eventos  
✅ **Manutenível** — Mudanças em HTTP/BD não afetam lógica  
✅ **Escalável** — Fácil adicionar novos adapters  

---

## 📦 Pré-requisitos

- **Java 21+**
- **Maven 3.8+**
- **Docker** e **Docker Compose** (para PostgreSQL)
- **Git**

---

## 🚀 Instalação

### 1. Clonar o repositório

```bash
git clone https://github.com/seu-usuario/event-service.git
cd event-service
```

### 2. Iniciar PostgreSQL com Docker Compose

```bash
docker compose up -d
```

**Credenciais do banco:**
- Host: `localhost`
- Port: `5432`
- Database: `eventdb`
- User: `eventuser`
- Password: `eventpassword`

Verificar se está rodando:
```bash
docker ps | grep event-service-db
```

### 3. Compilar o projeto

```bash
./mvnw clean compile
```

---

## ▶️ Rodando a Aplicação

### Opção 1: Maven

```bash
./mvnw spring-boot:run
```

### Opção 2: Build e executar JAR

```bash
./mvnw clean package
java -jar target/event-service-0.0.1-SNAPSHOT.jar
```

A aplicação estará disponível em: **http://localhost:8080**

---

## 🔌 Endpoints

### POST /api/events — Criar evento

**Request:**
```http
POST /api/events
Content-Type: application/json

{
  "name": "Conferência Spring Boot",
  "description": "Conferência anual sobre Spring Boot e microserviços",
  "dateTime": "2026-06-15T10:00:00",
  "location": "São Paulo - SP"
}
```

**Response (201 Created):**
```json
{
  "id": "550e8400-e29b-41d4-a716-446655440000",
  "name": "Conferência Spring Boot",
  "description": "Conferência anual sobre Spring Boot e microserviços",
  "dateTime": "2026-06-15T10:00:00",
  "location": "São Paulo - SP"
}
```

---

## 📝 Exemplos de Uso

### Com curl

```bash
# Criar evento completo
curl -X POST http://localhost:8080/api/events \
  -H "Content-Type: application/json" \
  -d '{
    "name": "Workshop Hexagonal",
    "description": "Aprendendo padrão hexagonal com Spring",
    "dateTime": "2026-07-20T14:00:00",
    "location": "Online"
  }'
```

```bash
# Criar evento mínimo
curl -X POST http://localhost:8080/api/events \
  -H "Content-Type: application/json" \
  -d '{
    "name": "Meetup Java"
  }'
```

### Com Postman/Insomnia

1. **URL:** `http://localhost:8080/api/events`
2. **Method:** `POST`
3. **Headers:**
   ```
   Content-Type: application/json
   ```
4. **Body (JSON):**
   ```json
   {
     "name": "Tech Summit 2026",
     "description": "Summit de tecnologia",
     "dateTime": "2026-08-15T09:00:00",
     "location": "Brasília"
   }
   ```

### Com Swagger UI

Acesse a documentação interativa em:
```
http://localhost:8080/swagger-ui.html
```

Teste os endpoints diretamente pela interface web.

---

## 📂 Estrutura do Projeto

```
event-service/
├── src/main/java/com/eventhub/event_service/
│
├── 📁 domain/                          (DOMÍNIO - Puro, sem frameworks)
│   ├── entity/
│   │   └── Event.java                  Entidade de domínio
│   ├── repository/
│   │   └── EventRepository.java        Interface (depreciada)
│   └── service/
│
├── 📁 application/                     (APLICAÇÃO - Orquestra o domínio)
│   ├── port/
│   │   ├── in/
│   │   │   └── EventInputPort.java     Porto de entrada (use cases expostos)
│   │   └── out/
│   │       └── EventOutputPort.java    Porto de saída (persistência)
│   ├── service/
│   │   └── EventApplicationService.java Implementa a lógica de aplicação
│   └── usecase/
│       └── CreateEventUseCase.java      Define contrato de criação
│
├── 📁 adapter/                         (ADAPTERS - Implementações concretas)
│   ├── in/
│   │   └── web/
│   │       ├── EventController.java     REST controller
│   │       └── dto/
│   │           ├── EventCreateRequest.java  DTO de entrada
│   │           └── EventResponse.java       DTO de saída
│   │
│   └── out/
│       └── persistence/
│           ├── EventRepositoryAdapter.java  Implementa EventOutputPort
│           ├── EventEntity.java             Entidade JPA
│           └── EventJpaRepository.java      Spring Data JPA
│
├── 📁 config/
│   └── SecurityConfig.java              Configuração de segurança Spring
│
└── 📁 resources/
    ├── application.properties           Configurações da aplicação
    ├── static/
    └── templates/

├── compose.yaml                        Docker Compose (PostgreSQL)
├── pom.xml                             Dependências Maven
└── README.md                           Este arquivo
```

---

## 🛠️ Tecnologias

### Core
- **Java 21** — Linguagem de programação
- **Spring Boot 4.0.6** — Framework web
- **Spring Data JPA** — Acesso a dados
- **Hibernate 7.2.12** — ORM

### Database
- **PostgreSQL 16** — Banco de dados relacional
- **Docker Compose** — Orquestração de containers

### API & Documentation
- **Spring Web MVC** — REST controllers
- **SpringDoc OpenAPI 3.0.2** — Swagger UI / OpenAPI 3.0

### Security
- **Spring Security** — Autenticação e autorização

### Build & Packaging
- **Maven** — Gerenciador de dependências
- **Spring Boot Maven Plugin** — Build e run

---

## ⚙️ Configurações

### Banco de Dados

Arquivo: `src/main/resources/application.properties`

```properties
# PostgreSQL
spring.datasource.url=jdbc:postgresql://localhost:5432/eventdb
spring.datasource.username=eventuser
spring.datasource.password=eventpassword

# Hibernate
spring.jpa.hibernate.ddl-auto=create-drop  # Auto-criar/dropar tabelas
spring.jpa.show-sql=true                  # Log SQL
```

**Aviso:** `ddl-auto=create-drop` é apenas para **desenvolvimento**. Para produção, use `validate` ou `update`.

### Security

Por padrão, todos os endpoints estão **sem autenticação** (desenvolvimento).

Para produção, adicione:
```properties
spring.security.user.name=admin
spring.security.user.password=sua-senha-segura
```

---

## 🧪 Testando

### Verificar saúde da aplicação

```bash
curl -s http://localhost:8080/api/events \
  -X POST \
  -H "Content-Type: application/json" \
  -d '{"name":"Test"}' | jq .
```

### Ver logs do banco

```bash
docker logs event-service-db
```

### Acessar PostgreSQL direto

```bash
docker exec -it event-service-db psql -U eventuser -d eventdb
```

Dentro do PostgreSQL:
```sql
SELECT * FROM events;
SELECT COUNT(*) FROM events;
```

---

## 🐛 Troubleshooting

### Erro: "Endereço já em uso" (Porta 8080)

```bash
# Matar processo na porta 8080
lsof -i :8080 | grep LISTEN | awk '{print $2}' | xargs kill -9
```

### Erro: "Connection refused" (PostgreSQL)

```bash
# Verificar se Docker está rodando
docker ps

# Reiniciar container
docker compose down && docker compose up -d
```

### Erro: "Table 'events' does not exist"

A tabela é criada automaticamente na primeira execução. Se não aparecer:

```bash
# Verificar logs do Hibernate
./mvnw spring-boot:run | grep -i "create table"
```

---

## 📚 Próximos Passos

- [ ] Implementar GET /api/events/{id}
- [ ] Implementar GET /api/events (listar todos)
- [ ] Implementar PUT /api/events/{id} (atualizar)
- [ ] Implementar DELETE /api/events/{id} (deletar)
- [ ] Adicionar validações (Bean Validation)
- [ ] Adicionar testes unitários e integração
- [ ] Configurar autenticação JWT
- [ ] Adicionar cache (Redis)
- [ ] Implementar paginação
- [ ] Adicionar logs estruturados (SLF4J)

---

## 📞 Suporte

Para dúvidas ou problemas:

1. Verificar logs: `./mvnw spring-boot:run`
2. Consultar Swagger: `http://localhost:8080/swagger-ui.html`
3. Verificar banco: `docker exec event-service-db psql -U eventuser -d eventdb`

---

## 📄 Licença

MIT License - Sinta-se livre para usar este projeto!

---

## ✨ Padrões Utilizados

- **Hexagonal Architecture** — Separação clara de camadas
- **Domain-Driven Design (DDD)** — Foco no domínio
- **Dependency Injection** — Através do Spring
- **DTO Pattern** — Separação entre DTOs e entidades de domínio
- **Repository Pattern** — Abstração de persistência

---

**Desenvolvido com ❤️ usando Hexagonal Architecture**

