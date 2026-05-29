# 📝 Templates de Issues para GitHub - Fase 2

Use estes templates para criar as 40 issues no seu repositório GitHub.

---

## ÉPICO 1: Configuração Inicial do Projeto e Infraestrutura

### TASK-001: Criar estrutura inicial do projeto Spring Boot

**Labels:** `priority: high`, `épico: setup`, `pontos: 3`, `type: infra`  
**Milestone:** Sprint 1 - Setup e Infraestrutura

**Descrição:**

```markdown
## 📋 Descrição
Configurar o projeto Spring Boot com Spring Initializr (Java 17, Spring Boot 3.2.3, Maven) com todas as dependências necessárias para a Fase 2.

## ✅ Critérios de Aceitação
- [ ] Projeto Maven com Java 17 e Spring Boot 3.2.3
- [ ] Dependências: Spring Web, Spring Data JPA, PostgreSQL Driver, H2 Database, Validation, Actuator, SpringDoc OpenAPI, Spring Security (BCrypt)
- [ ] Aplicação inicia sem erros
- [ ] Profiles configurados (dev com H2, test com H2, prod com PostgreSQL)
- [ ] application.properties com configurações base

## 🔧 Dependências Técnicas
- Java 17+
- Spring Boot 3.2.3
- Maven

## 📊 Estimativa
3 pontos
```

---

### TASK-002: Configurar estrutura Clean Architecture

**Labels:** `priority: high`, `épico: setup`, `pontos: 3`, `type: infra`  
**Milestone:** Sprint 1 - Setup e Infraestrutura

**Descrição:**

```markdown
## 📋 Descrição
Organizar pacotes seguindo a referência do professor com separação em camadas: domain, application, infra. A dependência flui de fora para dentro (infra → application → domain).

## ✅ Critérios de Aceitação
- [ ] Pacote `domain/entity/` com entidades puras (sem anotações JPA)
- [ ] Pacote `domain/gateway/` com interfaces (ports)
- [ ] Pacote `domain/exception/` com exceções de domínio
- [ ] Pacote `application/useCase/` com casos de uso
- [ ] Pacote `infra/adapter/web/` com controllers REST e DTOs
- [ ] Pacote `infra/persistence/` com entidades JPA, repositories e implementações de gateway
- [ ] Pacote `infra/config/` com configurações (OpenAPI, Security, Beans)
- [ ] Dependências fluem de fora para dentro (infra → application → domain)

## 🔧 Dependências Técnicas
- TASK-001 concluída

## 📊 Estimativa
3 pontos
```

---

### TASK-003: Configurar Docker Compose

**Labels:** `priority: high`, `épico: setup`, `pontos: 3`, `type: infra`  
**Milestone:** Sprint 1 - Setup e Infraestrutura

**Descrição:**

```markdown
## 📋 Descrição
Criar Dockerfile multi-stage e docker-compose.yml para aplicação Java + PostgreSQL, seguindo o padrão da Fase 1.

## ✅ Critérios de Aceitação
- [ ] Dockerfile com multi-stage build (Maven build + JRE runtime)
- [ ] docker-compose.yml com serviços: app e postgres
- [ ] Network configurada entre os serviços
- [ ] Health checks implementados
- [ ] Volumes para persistência do banco
- [ ] Variáveis de ambiente configuráveis
- [ ] Aplicação conecta ao banco via Docker Compose
- [ ] Sobe com um único comando (docker-compose up)

## 🔧 Dependências Técnicas
- TASK-001 concluída

## 📊 Estimativa
3 pontos
```

---

### TASK-004: Configurar OpenAPI/Swagger

**Labels:** `priority: high`, `épico: setup`, `pontos: 2`, `type: infra`  
**Milestone:** Sprint 1 - Setup e Infraestrutura

**Descrição:**

```markdown
## 📋 Descrição
Configurar SpringDoc OpenAPI para documentação automática da API, seguindo o padrão da Fase 1 (OpenApiConfig com info, tags, descrições).

## ✅ Critérios de Aceitação
- [ ] Swagger UI acessível em /swagger-ui.html
- [ ] OpenApiConfig com título, descrição, versão e contato
- [ ] Tags organizadas por domínio (Tipos de Usuário, Usuários, Restaurantes, Cardápio)
- [ ] Endpoints documentados com @Operation, @ApiResponse, exemplos
- [ ] Schemas de request/response visíveis

## 🔧 Dependências Técnicas
- TASK-001 concluída

## 📊 Estimativa
2 pontos
```

---

### TASK-005: Configurar tratamento global de erros (ProblemDetail)

**Labels:** `priority: high`, `épico: setup`, `pontos: 3`, `type: feature`  
**Milestone:** Sprint 1 - Setup e Infraestrutura

**Descrição:**

```markdown
## 📋 Descrição
Implementar GlobalExceptionHandler com ProblemDetail (RFC 7807), reutilizando o padrão da Fase 1.

## ✅ Critérios de Aceitação
- [ ] @RestControllerAdvice criado
- [ ] Respostas de erro seguem RFC 7807 (type, title, status, detail, instance)
- [ ] Tratamento de MethodArgumentNotValidException (400) com campo "campos"
- [ ] Tratamento de EntityNotFoundException (404)
- [ ] Tratamento de BusinessException (422)
- [ ] Tratamento de DataIntegrityViolationException (409)
- [ ] Tratamento de IllegalArgumentException (400)
- [ ] Logs de erros apropriados

## 🔧 Dependências Técnicas
- TASK-002 concluída

## 📊 Estimativa
3 pontos
```

---

## ÉPICO 2: CRUD de Tipo de Usuário

### TASK-006: Criar entidade de domínio UserType

**Labels:** `priority: high`, `épico: user-type`, `pontos: 2`, `type: feature`  
**Milestone:** Sprint 2 - Tipo de Usuário

**Descrição:**

```markdown
## 📋 Descrição
Criar a entidade pura UserType no pacote domain/entity. Na Fase 1, UserType era um enum; agora passa a ser uma entidade com CRUD próprio.

## ✅ Critérios de Aceitação
- [ ] Classe UserType com campos: id (UUID), name (String)
- [ ] POJO puro sem anotações de framework (sem JPA, sem Spring)
- [ ] Validações de domínio (nome não pode ser nulo/vazio)
- [ ] Construtores, getters e setters

## 🔧 Dependências Técnicas
- TASK-002 concluída

## 📊 Estimativa
2 pontos
```

---

### TASK-007: Criar gateway (port) UserTypeGateway

**Labels:** `priority: high`, `épico: user-type`, `pontos: 1`, `type: feature`  
**Milestone:** Sprint 2 - Tipo de Usuário

**Descrição:**

```markdown
## 📋 Descrição
Criar interface de repositório no pacote domain/gateway, definindo o contrato de acesso a dados para UserType.

## ✅ Critérios de Aceitação
- [ ] Interface UserTypeGateway com métodos: create, update, delete, findById, findAll
- [ ] Sem dependência de framework (interface pura)
- [ ] Tipos de retorno adequados (Optional para findById)

## 🔧 Dependências Técnicas
- TASK-006 concluída

## 📊 Estimativa
1 ponto
```

---

### TASK-008: Criar casos de uso de UserType

**Labels:** `priority: high`, `épico: user-type`, `pontos: 3`, `type: feature`  
**Milestone:** Sprint 2 - Tipo de Usuário

**Descrição:**

```markdown
## 📋 Descrição
Implementar use cases no pacote application/useCase para operações CRUD de UserType.

## ✅ Critérios de Aceitação
- [ ] CreateUserTypeUseCase — cria tipo com validação de nome
- [ ] UpdateUserTypeUseCase — atualiza com validação de existência
- [ ] DeleteUserTypeUseCase — remove com validação de existência
- [ ] FindUserTypeByIdUseCase — busca por ID com exceção se não encontrar
- [ ] FindAllUserTypesUseCase — lista todos
- [ ] Use cases dependem apenas de interfaces do domain (gateway)

## 🔧 Dependências Técnicas
- TASK-007 concluída

## 📊 Estimativa
3 pontos
```

---

### TASK-009: Criar persistência JPA de UserType

**Labels:** `priority: high`, `épico: user-type`, `pontos: 3`, `type: feature`  
**Milestone:** Sprint 2 - Tipo de Usuário

**Descrição:**

```markdown
## 📋 Descrição
Implementar adapter de saída (entidade JPA + repository Spring Data + implementação do gateway).

## ✅ Critérios de Aceitação
- [ ] UserTypeJpaEntity com anotações JPA (@Entity, @Table, @Id)
- [ ] UserTypeRepository (interface Spring Data JPA)
- [ ] UserTypeJpaGateway implementando UserTypeGateway
- [ ] Mapeamento bidirecional entre entidade de domínio e entidade JPA
- [ ] Tabela "user_types" no banco

## 🔧 Dependências Técnicas
- TASK-007 concluída

## 📊 Estimativa
3 pontos
```

---

### TASK-010: Criar controller REST de UserType

**Labels:** `priority: high`, `épico: user-type`, `pontos: 3`, `type: feature`  
**Milestone:** Sprint 2 - Tipo de Usuário

**Descrição:**

```markdown
## 📋 Descrição
Implementar adapter de entrada (controller + DTOs request/response) para UserType.

## ✅ Critérios de Aceitação
- [ ] POST /api/v1/user-types — criar tipo de usuário (201 Created)
- [ ] GET /api/v1/user-types — listar todos (200 OK)
- [ ] GET /api/v1/user-types/{id} — buscar por ID (200 OK / 404)
- [ ] PUT /api/v1/user-types/{id} — atualizar (200 OK / 404)
- [ ] DELETE /api/v1/user-types/{id} — remover (204 No Content / 404)
- [ ] DTOs como records com Bean Validation e anotações Swagger
- [ ] Documentação Swagger com exemplos

## 🔧 Dependências Técnicas
- TASK-008 e TASK-009 concluídas

## 📊 Estimativa
3 pontos
```

---

### TASK-011: Testes unitários de UserType

**Labels:** `priority: high`, `épico: user-type`, `pontos: 3`, `type: test`  
**Milestone:** Sprint 2 - Tipo de Usuário

**Descrição:**

```markdown
## 📋 Descrição
Testes unitários para use cases e controller de UserType.

## ✅ Critérios de Aceitação
- [ ] Testes dos 5 use cases com mocks do gateway (JUnit 5 + Mockito)
- [ ] Testes do controller com MockMvc
- [ ] Cenários de sucesso e erro cobertos
- [ ] Cobertura ≥ 80% nas classes de UserType

## 🔧 Dependências Técnicas
- TASK-010 concluída

## 📊 Estimativa
3 pontos
```

---

### TASK-012: Testes de integração de UserType

**Labels:** `priority: high`, `épico: user-type`, `pontos: 3`, `type: test`  
**Milestone:** Sprint 2 - Tipo de Usuário

**Descrição:**

```markdown
## 📋 Descrição
Testes de integração end-to-end para o CRUD de UserType.

## ✅ Critérios de Aceitação
- [ ] Testes com @SpringBootTest e banco H2 em memória
- [ ] Teste de criação, leitura, atualização e remoção
- [ ] Validação de respostas HTTP (status code e corpo)
- [ ] Validação de cenários de erro (404, 400)

## 🔧 Dependências Técnicas
- TASK-010 concluída

## 📊 Estimativa
3 pontos
```

---

## ÉPICO 3: Refatoração de Usuário (User)

### TASK-013: Criar entidade de domínio User e gateway

**Labels:** `priority: high`, `épico: user`, `pontos: 3`, `type: feature`  
**Milestone:** Sprint 3 - Usuário

**Descrição:**

```markdown
## 📋 Descrição
Criar entidade User no domain (POJO puro) e interface UserGateway. Na Fase 1, User era uma entidade JPA direta; agora segue Clean Architecture com separação.

## ✅ Critérios de Aceitação
- [ ] Classe User com campos: id (UUID), name, email, login, password, address, lastModifiedDate, userType (referência a UserType)
- [ ] POJO puro sem anotações de framework
- [ ] Interface UserGateway com métodos: create, update, delete, findById, findByEmail, findByLogin, findAll
- [ ] Validações de domínio mantidas

## 🔧 Dependências Técnicas
- TASK-006 concluída

## 📊 Estimativa
3 pontos
```

---

### TASK-014: Criar casos de uso de User

**Labels:** `priority: high`, `épico: user`, `pontos: 5`, `type: feature`  
**Milestone:** Sprint 3 - Usuário

**Descrição:**

```markdown
## 📋 Descrição
Implementar use cases de User, reutilizando a lógica de negócio da Fase 1 (validações de email/login únicos, BCrypt, etc.).

## ✅ Critérios de Aceitação
- [ ] CreateUserUseCase — cria com validação de email/login únicos, BCrypt na senha, userType obrigatório
- [ ] UpdateUserUseCase — atualiza dados (sem senha), valida unicidade
- [ ] DeleteUserUseCase — remove com validação de existência
- [ ] FindUserByIdUseCase — busca por ID
- [ ] FindAllUsersUseCase — lista todos (com filtro opcional por nome)
- [ ] ChangePasswordUseCase — troca senha com validação da atual
- [ ] LoginUseCase — valida credenciais com BCrypt

## 🔧 Dependências Técnicas
- TASK-013 concluída
- TASK-008 concluída (para validar UserType)

## 📊 Estimativa
5 pontos
```

---

### TASK-015: Criar persistência JPA e controller de User

**Labels:** `priority: high`, `épico: user`, `pontos: 5`, `type: feature`  
**Milestone:** Sprint 3 - Usuário

**Descrição:**

```markdown
## 📋 Descrição
Implementar camada de infraestrutura para User (entidade JPA, repository, gateway impl, controller, DTOs).

## ✅ Critérios de Aceitação
- [ ] UserJpaEntity com anotações JPA e relacionamento ManyToOne com UserTypeJpaEntity
- [ ] UserRepository (Spring Data JPA)
- [ ] UserJpaGateway implementando UserGateway
- [ ] Controller com endpoints: POST, GET, GET/{id}, PUT/{id}, DELETE/{id}, PATCH/{id}/password, POST/login
- [ ] DTOs (UserRequestDTO, UserResponseDTO, UserUpdateDTO, ChangePasswordDTO, LoginRequestDTO, LoginResponseDTO) como records
- [ ] Validadores customizados (@ValidPassword, @SafeInput) reutilizados da Fase 1
- [ ] Documentação Swagger com exemplos

## 🔧 Dependências Técnicas
- TASK-014 concluída
- TASK-009 concluída

## 📊 Estimativa
5 pontos
```

---

### TASK-016: Testes de User (unitários + integração)

**Labels:** `priority: high`, `épico: user`, `pontos: 5`, `type: test`  
**Milestone:** Sprint 3 - Usuário

**Descrição:**

```markdown
## 📋 Descrição
Testes unitários e de integração para User, cobrindo todos os use cases e endpoints.

## ✅ Critérios de Aceitação
- [ ] Testes unitários dos use cases com mocks
- [ ] Testes do controller com MockMvc
- [ ] Testes de integração com H2 (CRUD completo + login + troca de senha)
- [ ] Cenários de erro (email duplicado, login duplicado, credenciais inválidas, not found)
- [ ] Cobertura ≥ 80%

## 🔧 Dependências Técnicas
- TASK-015 concluída

## 📊 Estimativa
5 pontos
```

---

## ÉPICO 4: CRUD de Restaurante

### TASK-017: Criar entidade de domínio Restaurant e gateway

**Labels:** `priority: high`, `épico: restaurant`, `pontos: 2`, `type: feature`  
**Milestone:** Sprint 4 - Restaurante

**Descrição:**

```markdown
## 📋 Descrição
Criar entidade Restaurant no domain (POJO puro) e interface RestaurantGateway.

## ✅ Critérios de Aceitação
- [ ] Classe Restaurant com campos: id (UUID), name, address, cuisineType, openingHours, ownerId (UUID)
- [ ] POJO puro sem anotações de framework
- [ ] Interface RestaurantGateway com métodos: create, update, delete, findById, findAll, findByOwnerId

## 🔧 Dependências Técnicas
- TASK-002 concluída

## 📊 Estimativa
2 pontos
```

---

### TASK-018: Criar casos de uso de Restaurant

**Labels:** `priority: high`, `épico: restaurant`, `pontos: 4`, `type: feature`  
**Milestone:** Sprint 4 - Restaurante

**Descrição:**

```markdown
## 📋 Descrição
Implementar use cases de Restaurant com validações de negócio.

## ✅ Critérios de Aceitação
- [ ] CreateRestaurantUseCase — valida que owner existe e é do tipo RESTAURANT_OWNER
- [ ] UpdateRestaurantUseCase — atualiza com validação de existência
- [ ] DeleteRestaurantUseCase — remove com validação de existência
- [ ] FindRestaurantByIdUseCase — busca por ID
- [ ] FindAllRestaurantsUseCase — lista todos
- [ ] FindRestaurantsByOwnerUseCase — busca por dono

## 🔧 Dependências Técnicas
- TASK-017 concluída
- TASK-013 concluída (UserGateway para validar owner)

## 📊 Estimativa
4 pontos
```

---

### TASK-019: Criar persistência JPA de Restaurant

**Labels:** `priority: high`, `épico: restaurant`, `pontos: 3`, `type: feature`  
**Milestone:** Sprint 4 - Restaurante

**Descrição:**

```markdown
## 📋 Descrição
Implementar adapter de saída para Restaurant.

## ✅ Critérios de Aceitação
- [ ] RestaurantJpaEntity com anotações JPA
- [ ] Relacionamento ManyToOne com UserJpaEntity (owner)
- [ ] RestaurantRepository (Spring Data JPA)
- [ ] RestaurantJpaGateway implementando RestaurantGateway
- [ ] Tabela "restaurants" no banco

## 🔧 Dependências Técnicas
- TASK-017 concluída
- TASK-015 concluída (UserJpaEntity)

## 📊 Estimativa
3 pontos
```

---

### TASK-020: Criar controller REST de Restaurant

**Labels:** `priority: high`, `épico: restaurant`, `pontos: 3`, `type: feature`  
**Milestone:** Sprint 4 - Restaurante

**Descrição:**

```markdown
## 📋 Descrição
Implementar endpoints REST para Restaurant com DTOs e documentação Swagger.

## ✅ Critérios de Aceitação
- [ ] POST /api/v1/restaurants — criar restaurante (201 Created)
- [ ] GET /api/v1/restaurants — listar todos (200 OK)
- [ ] GET /api/v1/restaurants/{id} — buscar por ID (200 OK / 404)
- [ ] PUT /api/v1/restaurants/{id} — atualizar (200 OK / 404)
- [ ] DELETE /api/v1/restaurants/{id} — remover (204 No Content / 404)
- [ ] GET /api/v1/restaurants?ownerId={ownerId} — buscar por dono
- [ ] DTOs como records com Bean Validation e Swagger annotations

## 🔧 Dependências Técnicas
- TASK-018 e TASK-019 concluídas

## 📊 Estimativa
3 pontos
```

---

### TASK-021: Testes unitários de Restaurant

**Labels:** `priority: high`, `épico: restaurant`, `pontos: 3`, `type: test`  
**Milestone:** Sprint 4 - Restaurante

**Descrição:**

```markdown
## 📋 Descrição
Testes unitários para use cases e controller de Restaurant.

## ✅ Critérios de Aceitação
- [ ] Testes dos use cases com mocks (JUnit 5 + Mockito)
- [ ] Testes do controller com MockMvc
- [ ] Cenários de sucesso e erro (owner não existe, owner não é RESTAURANT_OWNER)
- [ ] Cobertura ≥ 80%

## 🔧 Dependências Técnicas
- TASK-020 concluída

## 📊 Estimativa
3 pontos
```

---

### TASK-022: Testes de integração de Restaurant

**Labels:** `priority: high`, `épico: restaurant`, `pontos: 3`, `type: test`  
**Milestone:** Sprint 4 - Restaurante

**Descrição:**

```markdown
## 📋 Descrição
Testes de integração end-to-end para o CRUD de Restaurant.

## ✅ Critérios de Aceitação
- [ ] Testes com @SpringBootTest e H2
- [ ] CRUD completo testado
- [ ] Validação de associação com owner (UserType = RESTAURANT_OWNER)
- [ ] Cenários de erro testados

## 🔧 Dependências Técnicas
- TASK-020 concluída

## 📊 Estimativa
3 pontos
```

---

## ÉPICO 5: CRUD de Item do Cardápio

### TASK-023: Criar entidade de domínio MenuItem e gateway

**Labels:** `priority: high`, `épico: menu-item`, `pontos: 2`, `type: feature`  
**Milestone:** Sprint 5 - Cardápio

**Descrição:**

```markdown
## 📋 Descrição
Criar entidade MenuItem no domain (POJO puro) e interface MenuItemGateway.

## ✅ Critérios de Aceitação
- [ ] Classe MenuItem com campos: id (UUID), name, description, price (BigDecimal), dineInOnly (boolean), photoPath (String), restaurantId (UUID)
- [ ] POJO puro sem anotações de framework
- [ ] Interface MenuItemGateway com métodos: create, update, delete, findById, findByRestaurantId

## 🔧 Dependências Técnicas
- TASK-002 concluída

## 📊 Estimativa
2 pontos
```

---

### TASK-024: Criar casos de uso de MenuItem

**Labels:** `priority: high`, `épico: menu-item`, `pontos: 3`, `type: feature`  
**Milestone:** Sprint 5 - Cardápio

**Descrição:**

```markdown
## 📋 Descrição
Implementar use cases de MenuItem com validações de negócio.

## ✅ Critérios de Aceitação
- [ ] CreateMenuItemUseCase — valida que restaurante existe, preço > 0
- [ ] UpdateMenuItemUseCase — atualiza com validação de existência
- [ ] DeleteMenuItemUseCase — remove com validação de existência
- [ ] FindMenuItemByIdUseCase — busca por ID
- [ ] FindMenuItemsByRestaurantUseCase — lista itens de um restaurante

## 🔧 Dependências Técnicas
- TASK-023 concluída
- TASK-017 concluída (RestaurantGateway para validar restaurante)

## 📊 Estimativa
3 pontos
```

---

### TASK-025: Criar persistência JPA de MenuItem

**Labels:** `priority: high`, `épico: menu-item`, `pontos: 3`, `type: feature`  
**Milestone:** Sprint 5 - Cardápio

**Descrição:**

```markdown
## 📋 Descrição
Implementar adapter de saída para MenuItem.

## ✅ Critérios de Aceitação
- [ ] MenuItemJpaEntity com anotações JPA
- [ ] Relacionamento ManyToOne com RestaurantJpaEntity
- [ ] MenuItemRepository (Spring Data JPA)
- [ ] MenuItemJpaGateway implementando MenuItemGateway
- [ ] Tabela "menu_items" no banco

## 🔧 Dependências Técnicas
- TASK-023 concluída
- TASK-019 concluída (RestaurantJpaEntity)

## 📊 Estimativa
3 pontos
```

---

### TASK-026: Criar controller REST de MenuItem

**Labels:** `priority: high`, `épico: menu-item`, `pontos: 3`, `type: feature`  
**Milestone:** Sprint 5 - Cardápio

**Descrição:**

```markdown
## 📋 Descrição
Implementar endpoints REST para MenuItem com DTOs e documentação Swagger.

## ✅ Critérios de Aceitação
- [ ] POST /api/v1/restaurants/{restaurantId}/menu-items — criar item (201 Created)
- [ ] GET /api/v1/restaurants/{restaurantId}/menu-items — listar itens do restaurante (200 OK)
- [ ] GET /api/v1/menu-items/{id} — buscar por ID (200 OK / 404)
- [ ] PUT /api/v1/menu-items/{id} — atualizar (200 OK / 404)
- [ ] DELETE /api/v1/menu-items/{id} — remover (204 No Content / 404)
- [ ] DTOs como records com Bean Validation e Swagger annotations

## 🔧 Dependências Técnicas
- TASK-024 e TASK-025 concluídas

## 📊 Estimativa
3 pontos
```

---

### TASK-027: Testes unitários de MenuItem

**Labels:** `priority: high`, `épico: menu-item`, `pontos: 3`, `type: test`  
**Milestone:** Sprint 5 - Cardápio

**Descrição:**

```markdown
## 📋 Descrição
Testes unitários para use cases e controller de MenuItem.

## ✅ Critérios de Aceitação
- [ ] Testes dos use cases com mocks (JUnit 5 + Mockito)
- [ ] Testes do controller com MockMvc
- [ ] Cenários de sucesso e erro (restaurante não existe, preço inválido)
- [ ] Cobertura ≥ 80%

## 🔧 Dependências Técnicas
- TASK-026 concluída

## 📊 Estimativa
3 pontos
```

---

### TASK-028: Testes de integração de MenuItem

**Labels:** `priority: high`, `épico: menu-item`, `pontos: 3`, `type: test`  
**Milestone:** Sprint 5 - Cardápio

**Descrição:**

```markdown
## 📋 Descrição
Testes de integração end-to-end para o CRUD de MenuItem.

## ✅ Critérios de Aceitação
- [ ] Testes com @SpringBootTest e H2
- [ ] CRUD completo testado
- [ ] Validação de associação com Restaurant
- [ ] Cenários de erro testados

## 🔧 Dependências Técnicas
- TASK-026 concluída

## 📊 Estimativa
3 pontos
```

---

## ÉPICO 6: Documentação, Qualidade e Entregáveis

### TASK-029: Configurar repositório Git

**Labels:** `priority: high`, `épico: documentação`, `pontos: 2`, `type: infra`  
**Milestone:** Sprint 6 - Finalização

**Descrição:**

```markdown
## 📋 Descrição
Configurar repositório público no GitHub com boas práticas de branching e proteção.

## ✅ Critérios de Aceitação
- [ ] Repositório público criado no GitHub
- [ ] Branch main protegida (merge via PR)
- [ ] Branch develop como branch de desenvolvimento
- [ ] Fluxo: feature/* → develop → main
- [ ] Commits com mensagens descritivas (conventional commits)
- [ ] README.md inicial na raiz

## 🔧 Dependências Técnicas
- TASK-001 concluída

## 📊 Estimativa
2 pontos
```

---

### TASK-030: Verificar cobertura de testes ≥ 80%

**Labels:** `priority: high`, `épico: documentação`, `pontos: 2`, `type: test`  
**Milestone:** Sprint 6 - Finalização

**Descrição:**

```markdown
## 📋 Descrição
Configurar JaCoCo e garantir cobertura mínima de 80% conforme exigido no PDF.

## ✅ Critérios de Aceitação
- [ ] Plugin JaCoCo configurado no pom.xml
- [ ] Relatório de cobertura gerado em target/site/jacoco/
- [ ] Cobertura total de linhas ≥ 80%
- [ ] Build falha se cobertura < 80% (jacoco:check)
- [ ] Exclusões configuradas (classes de configuração, Application.java, DTOs)

## 🔧 Dependências Técnicas
- Todas as tasks de testes concluídas (011, 012, 016, 021, 022, 027, 028)

## 📊 Estimativa
2 pontos
```

---

### TASK-031: Criar README.md completo

**Labels:** `priority: high`, `épico: documentação`, `pontos: 3`, `type: docs`  
**Milestone:** Sprint 6 - Finalização

**Descrição:**

```markdown
## 📋 Descrição
Documentação principal do projeto com arquitetura, tecnologias, endpoints e instruções de execução. Seguir padrão da Fase 1.

## ✅ Critérios de Aceitação
- [ ] Descrição do projeto e objetivo
- [ ] Diagrama de camadas (Clean Architecture)
- [ ] Tecnologias utilizadas com justificativas
- [ ] Tabela com lista completa de endpoints da API
- [ ] Instruções de configuração e execução (local e Docker)
- [ ] Instruções para rodar testes e ver cobertura
- [ ] Link do repositório GitHub
- [ ] Badges (CI/CD, cobertura)

## 🔧 Dependências Técnicas
- Todas as tasks de implementação concluídas

## 📊 Estimativa
3 pontos
```

---

### TASK-032: Criar documentação detalhada da API (docs/API.md)

**Labels:** `priority: high`, `épico: documentação`, `pontos: 3`, `type: docs`  
**Milestone:** Sprint 6 - Finalização

**Descrição:**

```markdown
## 📋 Descrição
Documentação detalhada de todos os endpoints da API com exemplos de request/response, DTOs, códigos de status. Seguir padrão do docs/API.md da Fase 1.

## ✅ Critérios de Aceitação
- [ ] Documentação de cada DTO (campos, tipos, regras)
- [ ] Documentação de cada endpoint (método, path, descrição)
- [ ] Exemplos de request body para cada operação
- [ ] Exemplos de response (sucesso e erro) com ProblemDetail
- [ ] Tabela de códigos de status HTTP utilizados
- [ ] Base URL local e produção (se aplicável)

## 🔧 Dependências Técnicas
- Todos os endpoints implementados

## 📊 Estimativa
3 pontos
```

---

### TASK-033: Criar Collection Postman

**Labels:** `priority: high`, `épico: documentação`, `pontos: 3`, `type: docs`  
**Milestone:** Sprint 6 - Finalização

**Descrição:**

```markdown
## 📋 Descrição
Collection com todos os endpoints para teste, seguindo o padrão da Fase 1 (cenários de sucesso + erro, variáveis, organização por pastas). Inclui documentação de uso.

## ✅ Critérios de Aceitação
- [ ] Requests para todos os endpoints (UserType, User, Restaurant, MenuItem)
- [ ] Variáveis de ambiente (localUrl, prodUrl)
- [ ] Exemplos de request body para cada operação
- [ ] Organização por pastas/módulos
- [ ] Cenários de sucesso e erro cobertos
- [ ] Scripts de teste automatizados em cada request
- [ ] Arquivo JSON exportado em docs/api-collection/
- [ ] docs/api-collection/README.md com instruções de importação, estrutura da collection, endpoints cobertos e ordem de execução recomendada

## 🔧 Dependências Técnicas
- Todos os endpoints implementados

## 📊 Estimativa
3 pontos
```

---

### TASK-034: Configurar CI/CD (GitHub Actions)

**Labels:** `priority: high`, `épico: documentação`, `pontos: 3`, `type: infra`  
**Milestone:** Sprint 6 - Finalização

**Descrição:**

```markdown
## 📋 Descrição
Pipeline de build, teste e verificação de cobertura no GitHub Actions. Inclui documentação das opções de CI/CD e guia de configuração.

## ✅ Critérios de Aceitação
- [ ] Workflow para build e testes em push/PR para develop e main
- [ ] Verificação de cobertura no pipeline
- [ ] Badge de status no README
- [ ] Build falha se testes falharem
- [ ] docs/ci-cd/CI_CD_OPTIONS.md — comparação de opções de CI/CD (minimalista, simplificada, completa)
- [ ] docs/ci-cd/CI_CD_SETUP.md — guia completo de configuração do pipeline escolhido

## 🔧 Dependências Técnicas
- TASK-030 concluída

## 📊 Estimativa
3 pontos
```

---

### TASK-035: Criar índice de documentação (docs/README.md)

**Labels:** `priority: high`, `épico: documentação`, `pontos: 1`, `type: docs`  
**Milestone:** Sprint 6 - Finalização

**Descrição:**

```markdown
## 📋 Descrição
Índice geral da pasta docs/ linkando todos os documentos do projeto, seguindo padrão da Fase 1.

## ✅ Critérios de Aceitação
- [ ] Links para todos os documentos (BACKLOG, API, Postman, CI/CD, etc.)
- [ ] Estrutura do projeto documentada
- [ ] Quick Start com ordem de leitura sugerida

## 🔧 Dependências Técnicas
- TASK-031 e TASK-032 concluídas

## 📊 Estimativa
1 ponto
```

---

### TASK-036: Preparar entrega final

**Labels:** `priority: high`, `épico: documentação`, `pontos: 2`, `type: docs`  
**Milestone:** Sprint 6 - Finalização

**Descrição:**

```markdown
## 📋 Descrição
Preparar todos os artefatos necessários para entrega do projeto.

## ✅ Critérios de Aceitação
- [ ] Repositório público no GitHub
- [ ] README.md completo e atualizado
- [ ] Collection Postman incluída no repositório
- [ ] Docker Compose funcional (sobe com um comando)
- [ ] Documentação completa (README, API.md, docs/)
- [ ] Código-fonte organizado (Clean Architecture)
- [ ] Testes passando com cobertura ≥ 80%
- [ ] Swagger UI funcional

## 🔧 Dependências Técnicas
- Todas as tasks anteriores concluídas

## 📊 Estimativa
2 pontos
```

---

### TASK-037: Gravar vídeo de apresentação

**Labels:** `priority: high`, `épico: documentação`, `pontos: 2`, `type: docs`  
**Milestone:** Sprint 6 - Finalização

**Descrição:**

```markdown
## 📋 Descrição
Vídeo de ~5 minutos demonstrando as funcionalidades e o projeto rodando (obrigatório conforme PDF).

## ✅ Critérios de Aceitação
- [ ] Demonstração das funcionalidades (CRUD UserType, User, Restaurant, MenuItem)
- [ ] Projeto rodando via Docker Compose
- [ ] Explicação da arquitetura Clean Architecture
- [ ] Swagger UI demonstrado
- [ ] Duração ~5 minutos

## 🔧 Dependências Técnicas
- TASK-036 concluída

## 📊 Estimativa
2 pontos
```

---

## ÉPICO 7: Extras (Não Obrigatórios)

> ⚠️ As tasks abaixo **NÃO são obrigatórias** conforme o PDF da Fase 2.
> São boas práticas que agregam qualidade ao projeto mas não são exigidas para a entrega.

### TASK-038: Configurar SonarCloud (NÃO OBRIGATÓRIO)

**Labels:** `priority: medium`, `épico: extras`, `pontos: 3`, `type: infra`  
**Milestone:** Sprint 6 - Finalização

**Descrição:**

```markdown
## 📋 Descrição
Configurar análise estática de código com SonarCloud para validação de qualidade em PRs. Não exigido no PDF da Fase 2, mas agrega valor ao projeto. Inclui documentação de setup.

## ✅ Critérios de Aceitação
- [ ] Conta criada no SonarCloud
- [ ] sonar-project.properties configurado
- [ ] SONAR_TOKEN adicionado nos secrets do GitHub
- [ ] Workflow de análise em PRs (opcional por label ou obrigatório)
- [ ] Quality Gate configurado (cobertura ≥ 80%, 0 bugs, 0 vulnerabilidades)
- [ ] Badge no README
- [ ] docs/qualidade/SONARCLOUD_SETUP.md — guia completo de configuração (conta, token, workflow, troubleshooting)

## 🔧 Dependências Técnicas
- TASK-034 concluída

## 📊 Estimativa
3 pontos
```

---

### TASK-039: Deploy no Render.com (NÃO OBRIGATÓRIO)

**Labels:** `priority: medium`, `épico: extras`, `pontos: 3`, `type: infra`  
**Milestone:** Sprint 6 - Finalização

**Descrição:**

```markdown
## 📋 Descrição
Realizar deploy da aplicação no Render.com para disponibilizar em produção com URL pública. Não exigido no PDF da Fase 2, mas facilita a avaliação pelos professores. Inclui documentação de deploy.

## ✅ Critérios de Aceitação
- [ ] Conta criada no Render.com
- [ ] PostgreSQL gratuito provisionado no Render
- [ ] Web Service criado com Dockerfile
- [ ] Variáveis de ambiente configuradas
- [ ] Build e deploy executados com sucesso
- [ ] Aplicação acessível via URL pública
- [ ] Health check configurado (/actuator/health)
- [ ] Deploy automático a cada push na main
- [ ] URL pública documentada no README
- [ ] docs/deploy/RENDER_DEPLOY_GUIDE.md — guia completo de deploy (passo a passo, variáveis, troubleshooting, limites do plano gratuito)

## 🔧 Dependências Técnicas
- TASK-003 concluída (Dockerfile)
- TASK-036 concluída (entrega final)

## 📊 Estimativa
3 pontos
```

---

### TASK-040: Criar documentação de contexto do projeto (NÃO OBRIGATÓRIO)

**Labels:** `priority: medium`, `épico: extras`, `pontos: 2`, `type: docs`  
**Milestone:** Sprint 6 - Finalização

**Descrição:**

```markdown
## 📋 Descrição
Documento com contexto do projeto, decisões técnicas tomadas e resumo do planejamento. Não exigido no PDF da Fase 2, mas serve como registro histórico e facilita onboarding.

## ✅ Critérios de Aceitação
- [ ] docs/planejamento/RESUMO_CONVERSA.md criado
- [ ] Contexto do projeto (continuação da Fase 1)
- [ ] Decisões técnicas documentadas (por que Clean Architecture, por que UUID, etc.)
- [ ] Diferenças em relação à Fase 1 explicadas
- [ ] Referências utilizadas (PDF do professor, estrutura de referência)

## 🔧 Dependências Técnicas
- Nenhuma (pode ser criado a qualquer momento)

## 📊 Estimativa
2 pontos
```

---

## 🎯 Resumo Rápido

**Total:** 40 Issues

- **Alta Prioridade:** 37 issues (obrigatórias)
- **Média Prioridade:** 3 issues (não obrigatórias)

### Labels necessárias (criar antes das issues):

**Prioridade:**
- `priority: high` (#d73a4a)
- `priority: medium` (#fbca04)

**Épicos:**
- `épico: setup` (#1d76db)
- `épico: user-type` (#5319e7)
- `épico: user` (#0052cc)
- `épico: restaurant` (#006b75)
- `épico: menu-item` (#c5def5)
- `épico: documentação` (#c2e0c6)
- `épico: extras` (#e99695)

**Estimativa:**
- `pontos: 1` (#ededed)
- `pontos: 2` (#ededed)
- `pontos: 3` (#ededed)
- `pontos: 4` (#ededed)
- `pontos: 5` (#ededed)

**Tipo:**
- `type: feature` (#0e8a16)
- `type: test` (#fbca04)
- `type: docs` (#0075ca)
- `type: infra` (#d93f0b)

### Milestones necessários:
1. Sprint 1 - Setup e Infraestrutura (Tasks 001-005)
2. Sprint 2 - Tipo de Usuário (Tasks 006-012)
3. Sprint 3 - Usuário (Tasks 013-016)
4. Sprint 4 - Restaurante (Tasks 017-022)
5. Sprint 5 - Cardápio (Tasks 023-028)
6. Sprint 6 - Finalização (Tasks 029-040)
