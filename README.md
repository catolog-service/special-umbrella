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

**Status atual:** ✅ Operações implementadas:
- ✅ **CREATE** — Criar eventos (POST /api/events)
- ✅ **LIST** — Listar todos os eventos (GET /api/events)
- ✅ **DELETE ALL** — Deletar todos os eventos + limpar RabbitMQ (DELETE /api/events)
- 🚧 **GET by ID** — Buscar evento por ID (em desenvolvimento)
- 🚧 **UPDATE** — Atualizar evento (em desenvolvimento)
- 🚧 **DELETE by ID** — Deletar evento específico (em desenvolvimento)

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

### 1️⃣ POST /api/events — Criar evento

**Descrição:** Cria um novo evento e publica automaticamente no RabbitMQ

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

**Fluxo:**
1. Evento é salvo no PostgreSQL
2. Evento é publicado no RabbitMQ automaticamente
3. Logs registram sucesso da operação

---

### 2️⃣ GET /api/events — Listar todos os eventos

**Descrição:** Retorna lista de todos os eventos cadastrados

**Request:**
```http
GET /api/events
Content-Type: application/json
```

**Response (200 OK):**
```json
[
  {
    "id": "550e8400-e29b-41d4-a716-446655440000",
    "name": "Conferência Spring Boot",
    "description": "Conferência anual sobre Spring Boot e microserviços",
    "dateTime": "2026-06-15T10:00:00",
    "location": "São Paulo - SP"
  },
  {
    "id": "660e8400-e29b-41d4-a716-446655440001",
    "name": "Meetup Java",
    "description": null,
    "dateTime": null,
    "location": null
  }
]
```

---

### 3️⃣ DELETE /api/events — Deletar todos os eventos

**Descrição:** ⚠️ **OPERAÇÃO IRREVERSÍVEL!**  
Deleta TODOS os eventos do banco de dados E expurga a fila RabbitMQ

**Request:**
```http
DELETE /api/events
Content-Type: application/json
```

**Response (204 No Content):**
```
(sem corpo de resposta)
```

**O que acontece:**
1. ✓ Todos os eventos são removidos do PostgreSQL
2. ✓ Todas as mensagens na fila RabbitMQ são removidas
3. ✓ Logs detalhados registram cada etapa

**Logs esperados:**
```
🗑️  Iniciando exclusão de TODOS os eventos (banco de dados + RabbitMQ)...
✓ 5 evento(s) deletado(s) do banco de dados com sucesso
✓ Eventos deletados do banco de dados com sucesso
✓ Fila RabbitMQ expurgada com sucesso
✓ ✓ Exclusão completa: BANCO DE DADOS + RABBITMQ limpos com sucesso!
```

---

## 📝 Exemplos de Uso

### Com curl

```bash
# ✅ Criar evento completo
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
# ✅ Criar evento mínimo (apenas nome)
curl -X POST http://localhost:8080/api/events \
  -H "Content-Type: application/json" \
  -d '{"name": "Meetup Java"}'
```

```bash
# ✅ Listar todos os eventos
curl -X GET http://localhost:8080/api/events \
  -H "Content-Type: application/json" | jq .
```

```bash
# ⚠️ DELETAR TODOS os eventos (banco + RabbitMQ)
curl -X DELETE http://localhost:8080/api/events \
  -H "Content-Type: application/json"
```

### Com Postman/Insomnia

#### Criar Evento
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

#### Listar Eventos
1. **URL:** `http://localhost:8080/api/events`
2. **Method:** `GET`
3. **Headers:**
   ```
   Content-Type: application/json
   ```

#### Deletar Todos os Eventos
1. **URL:** `http://localhost:8080/api/events`
2. **Method:** `DELETE`
3. **Headers:**
   ```
   Content-Type: application/json
   ```
4. **⚠️ Aviso:** Esta ação é irreversível!

### Com Swagger UI

Acesse a documentação interativa em:
```
http://localhost:8080/swagger-ui.html
```

Recursos:
- 📖 Visualizar todos os endpoints
- 🧪 Testar operações diretamente
- 📋 Ver modelos de request/response
- 🔍 Explorar documentação OpenAPI

---

## 📂 Estrutura do Projeto

```
event-service/
├── src/main/java/com/eventhub/event_service/
│
├── 📁 domain/                          (DOMÍNIO - Puro, sem frameworks)
│   └── entity/
│       └── Event.java                  Entidade de domínio (UUID, name, description, etc)
│
├── 📁 application/                     (APLICAÇÃO - Orquestra o domínio)
│   ├── port/
│   │   ├── in/
│   │   │   ├── CreateEventUseCase.java     Use case: criar evento
│   │   │   ├── DeleteEventUseCase.java     Use case: deletar todos
│   │   │   └── ListEventUsecase.java       Use case: listar eventos
│   │   └── out/
│   │       ├── EventOutputPort.java        Porto de saída: persistência
│   │       └── EventPublisher.java         Porto de saída: mensageria
│   └── service/
│       └── EventApplicationService.java    Orquestra use cases
│
├── 📁 adapter/                         (ADAPTERS - Implementações concretas)
│   ├── in/
│   │   └── web/
│   │       ├── EventController.java     REST API controller
│   │       └── dto/
│   │           ├── EventCreateRequest.java  DTO entrada
│   │           └── EventResponse.java       DTO saída
│   │
│   └── out/
│       ├── persistence/
│       │   ├── EventRepositoryAdapter.java  Implementa EventOutputPort
│       │   ├── EventEntity.java             Entidade JPA/Hibernate
│       │   └── EventJpaRepository.java      Spring Data JPA
│       │
│       └── messaging/
│           ├── EventPublisherAdapter.java   Implementa EventPublisher
│           └── EventPublisher.java          Interface de saída
│
├── 📁 config/
│   ├── RabbitMqConfig.java              Configuração: queues, exchanges, bindings
│   └── SecurityConfig.java              Configuração: Spring Security
│
└── 📁 resources/
    ├── application.properties           Configurações (BD, RabbitMQ, etc)
    └── db/
        └── migration/
            └── V1__create_events_table.sql  Flyway: criar tabela

├── compose.yaml                        Docker Compose (PostgreSQL + RabbitMQ)
├── pom.xml                             Dependências Maven
├── README.md                           Este arquivo
└── DELETAR_EVENTOS.md                  Guia: função deleteAll()
```

### 📊 Fluxo de Dados

```
Cliente HTTP
    ↓
[EventController] ← REST Adapter (in)
    ↓
[CreateEventUseCase / DeleteEventUseCase / ListEventUsecase] ← Ports (in)
    ↓
[EventApplicationService] ← Application Layer
    ↓                           ↓
[EventOutputPort]           [EventPublisher]
    ↓                           ↓
[EventRepositoryAdapter]    [EventPublisherAdapter]
    ↓                           ↓
[PostgreSQL]                [RabbitMQ]
```

---

## 🛠️ Tecnologias

### Core
- **Java 21** — Linguagem de programação moderna
- **Spring Boot 4.0.6** — Framework web robusto
- **Spring Data JPA** — Acesso a dados
- **Hibernate 7.2.12** — ORM

### Messaging & Events
- **RabbitMQ** — Message broker para eventos
- **Spring AMQP** — Integração com RabbitMQ
- **Jackson** — Serialização JSON

### Database
- **PostgreSQL 16** — Banco de dados relacional
- **Flyway** — Versionamento de schema
- **Docker Compose** — Orquestração de containers

### API & Documentation
- **Spring Web MVC** — REST controllers
- **SpringDoc OpenAPI 3.0.2** — Swagger UI / OpenAPI 3.0

### Security
- **Spring Security** — Autenticação e autorização
- **Spring Validation** — Validação de dados

### Development Tools
- **Lombok** — Reduz boilerplate de código
- **Maven** — Gerenciador de dependências
- **Spring Boot Maven Plugin** — Build e run

### Resiliência
- **Spring Cloud Circuit Breaker** — Padrão Circuit Breaker
- **Resilience4j** — Implementação de Circuit Breaker

---

## ⚙️ Configurações

### Banco de Dados (PostgreSQL)

Arquivo: `src/main/resources/application.properties`

```properties
# PostgreSQL
spring.datasource.url=jdbc:postgresql://localhost:5432/eventdb
spring.datasource.username=eventuser
spring.datasource.password=eventpassword
spring.datasource.driver-class-name=org.postgresql.Driver

# Hibernate/JPA
spring.jpa.database-platform=org.hibernate.dialect.PostgreSQLDialect
spring.jpa.hibernate.ddl-auto=validate
spring.jpa.show-sql=true

# Flyway (Versionamento de Schema)
spring.flyway.enabled=true
spring.flyway.locations=classpath:db/migration
spring.flyway.baseline-on-migrate=true
```

**Aviso:** `ddl-auto=validate` é para **produção**. Para desenvolvimento, use `validate` (migrations via Flyway).

### RabbitMQ (Message Broker)

```properties
# RabbitMQ
spring.rabbitmq.host=localhost
spring.rabbitmq.port=5672
spring.rabbitmq.username=guest
spring.rabbitmq.password=guest
spring.rabbitmq.virtual-host=/

# AMQP Logging
logging.level.org.springframework.amqp=DEBUG
```

**Credenciais padrão (desenvolvimento):**
- Host: `localhost`
- Port: `5672`
- Username: `guest`
- Password: `guest`

**Console RabbitMQ:** `http://localhost:15672` (usuário/senha: guest/guest)

### Security

Por padrão, todos os endpoints estão **sem autenticação** (desenvolvimento).

Para produção, configure:
```properties
spring.security.user.name=admin
spring.security.user.password=sua-senha-segura
```

### Logging

```properties
# Hibernate SQL
logging.level.org.hibernate.SQL=DEBUG
logging.level.org.hibernate.type.descriptor.sql=TRACE

# AMQP
logging.level.org.springframework.amqp=DEBUG
```

---

## 🧪 Testando

### ✅ Teste Completo do Sistema

```bash
# 1. Criar primeiro evento
curl -X POST http://localhost:8080/api/events \
  -H "Content-Type: application/json" \
  -d '{"name":"Evento 1"}'

# 2. Criar segundo evento
curl -X POST http://localhost:8080/api/events \
  -H "Content-Type: application/json" \
  -d '{"name":"Evento 2","location":"São Paulo"}'

# 3. Listar todos os eventos
curl -X GET http://localhost:8080/api/events | jq .

# 4. Deletar TODOS os eventos e limpar RabbitMQ
curl -X DELETE http://localhost:8080/api/events

# 5. Verificar que está vazio
curl -X GET http://localhost:8080/api/events
# Deve retornar: []
```

### 📊 Verificar Base de Dados

```bash
# Acessar PostgreSQL direto
docker exec -it event-service-db psql -U eventuser -d eventdb
```

Dentro do PostgreSQL:
```sql
-- Ver todos os eventos
SELECT * FROM events;

-- Contar eventos
SELECT COUNT(*) FROM events;

-- Ver estrutura da tabela
\d events;
```

### 📨 Verificar RabbitMQ

#### Console Web do RabbitMQ
```
URL: http://localhost:15672
Usuário: guest
Senha: guest
```

Ou via CLI:
```bash
# Listar filas
docker exec -it event-service-rabbitmq rabbitmqctl list_queues

# Listar exchanges
docker exec -it event-service-rabbitmq rabbitmqctl list_exchanges

# Listar bindings
docker exec -it event-service-rabbitmq rabbitmqctl list_bindings
```

### 🔍 Ver Logs da Aplicação

```bash
# Ver logs em tempo real
./mvnw spring-boot:run | grep -E "✓|✗|🗑️|RabbitMQ|Evento"

# Ver logs do PostgreSQL
docker logs event-service-db

# Ver logs do RabbitMQ
docker logs event-service-rabbitmq
```

### 🩺 Health Check da Aplicação

```bash
# Verificar se a aplicação está rodando
curl -s http://localhost:8080/swagger-ui.html | head -20
```

---

## 🐛 Troubleshooting

### Erro: "Endereço já em uso" (Porta 8080)

```bash
# Matar processo na porta 8080
lsof -i :8080 | grep LISTEN | awk '{print $2}' | xargs kill -9
```

### Erro: "Connection refused" (PostgreSQL/RabbitMQ)

```bash
# Verificar se Docker está rodando
docker ps

# Reiniciar todos os containers
docker compose down && docker compose up -d

# Verificar logs
docker compose logs -f
```

### Erro: "Table 'events' does not exist"

A tabela é criada automaticamente via Flyway na primeira execução:

```bash
# Verificar se Flyway executou
./mvnw spring-boot:run | grep -i "flyway\|migration"

# Se não funcionar, resetar manualmente
docker exec -it event-service-db psql -U eventuser -d eventdb -c "DROP TABLE IF EXISTS flyway_schema_history; DROP TABLE IF EXISTS events;"
```

### Erro: RabbitMQ não conecta

```bash
# Verificar se RabbitMQ está saudável
docker logs event-service-rabbitmq | tail -20

# Reiniciar RabbitMQ
docker restart event-service-rabbitmq

# Esperar 10 segundos e tentar novamente
sleep 10 && ./mvnw spring-boot:run
```

### Erro: "Could not autowire RabbitAdmin"

Verifique se RabbitMqConfig.java tem:
```java
@Bean
public RabbitAdmin rabbitAdmin(ConnectionFactory connectionFactory) {
    return new RabbitAdmin(connectionFactory);
}
```

### Deletar Tudo e Recomeçar (Hard Reset)

```bash
# 1. Parar containers
docker compose down -v

# 2. Limpar compilação
./mvnw clean

# 3. Reiniciar
docker compose up -d

# 4. Compilar e rodar
./mvnw spring-boot:run
```

---

## 📚 Próximos Passos

### 🔴 Crítico (Impacto Alto)
- [x] ✅ Implementar GET /api/events (listar todos)
- [x] ✅ Implementar DELETE /api/events (deletar todos + RabbitMQ)
- [ ] Implementar validações (Bean Validation)
- [ ] Adicionar testes unitários e integração
- [ ] Implementar Global Exception Handler

### 🟡 Importante
- [ ] Implementar GET /api/events/{id} (buscar por ID)
- [ ] Implementar PUT /api/events/{id} (atualizar)
- [ ] Implementar DELETE /api/events/{id} (deletar específico)
- [ ] Configurar autenticação JWT
- [ ] Implementar paginação

### 🟢 Bom ter
- [ ] Adicionar cache (Redis)
- [ ] Implementar filtros avançados
- [ ] Adicionar metrics (Micrometer)
- [ ] Documentar padrões de erro em OpenAPI
- [ ] Implementar soft delete
- [ ] Adicionar auditoria de mudanças

---

## 📞 Suporte

Para dúvidas ou problemas:

1. **Verificar logs:** 
   ```bash
   ./mvnw spring-boot:run | grep -E "✓|✗|ERROR|RabbitMQ"
   ```

2. **Consultar Swagger:** 
   ```
   http://localhost:8080/swagger-ui.html
   ```

3. **Verificar banco de dados:** 
   ```bash
   docker exec event-service-db psql -U eventuser -d eventdb -c "SELECT COUNT(*) FROM events;"
   ```

4. **Verificar RabbitMQ:** 
   ```bash
   docker exec event-service-rabbitmq rabbitmqctl list_queues
   ```

5. **Ler documentação adicional:**
   ```bash
   cat DELETAR_EVENTOS.md  # Guia específico para delete all
   ```

---

## 📄 Licença

MIT License - Sinta-se livre para usar este projeto!

---

## ✨ Padrões Utilizados

- **Hexagonal Architecture** — Separação clara de camadas (domain, application, adapter, port)
- **Domain-Driven Design (DDD)** — Foco no domínio (Event é a entidade central)
- **Dependency Injection** — Através do Spring (Constructor Injection)
- **DTO Pattern** — Separação entre DTOs (API) e entidades de domínio
- **Repository Pattern** — Abstração de persistência via EventOutputPort
- **Publisher Pattern** — Abstração de mensageria via EventPublisher
- **Use Case Pattern** — Cada operação é um caso de uso segregado

---

## 🎯 Destaques Recentes

### ✅ Função DeleteAll Aprimorada (v1.1)

O endpoint `DELETE /api/events` agora executa **dois passos coordenados**:

1. **Limpa o Banco de Dados**
   - Remove todos os registros da tabela `events`
   - Registra quantos eventos foram deletados
   - Usa transação para garantir consistência

2. **Expurga a Fila RabbitMQ**
   - Remove todas as mensagens publicadas
   - Impede que consumidores processem eventos antigos
   - Registra sucesso/erro da operação

**Benefícios:**
- ✅ Sincronização entre BD e message broker
- ✅ Logging detalhado de cada etapa
- ✅ Tratamento robusto de exceções
- ✅ Operação atômica e confiável

Para mais detalhes, veja: **[DELETAR_EVENTOS.md](DELETAR_EVENTOS.md)**

---
