# BACKLOG - Tech Challenge Fase 2
## Sistema de Gestão de Restaurantes

> **Continuação da Fase 1** — Este projeto expande o sistema ao incluir gestão de tipos de usuário,
> cadastro de restaurantes e cardápios, utilizando Clean Architecture conforme referência do professor.

---

## 📋 ÉPICO 1: Configuração Inicial do Projeto e Infraestrutura

### [TASK-001: Criar estrutura inicial do projeto Spring Boot](#task-001)

**Labels:** `priority: high`, `épico: setup`, `pontos: 3`, `type: infra`  
**Milestone:** Sprint 1 - Fundação

## 📋 Descrição
Configurar o projeto Spring Boot com Spring Initializr (Java 17, Spring Boot 3.2.3, Maven) com todas as dependências necessárias para a Fase 2. Inclui .gitignore configurado.

## ✅ Critérios de Aceitação
- [ ] Projeto Maven com Java 17 e Spring Boot 3.2.3
- [ ] Dependências: Spring Web, Spring Data JPA, PostgreSQL Driver, H2 Database, Validation, Actuator, SpringDoc OpenAPI, Spring Security (BCrypt)
- [ ] Aplicação inicia sem erros
- [ ] Profiles configurados (dev com H2, test com H2, prod com PostgreSQL)
- [ ] application.properties com configurações base
- [ ] .gitignore configurado (target/, .idea/, .DS_Store, .env, etc.)

## 🔧 Dependências Técnicas
- Java 17+
- Spring Boot 3.2.3
- Maven

---

### [TASK-002: Configurar estrutura Clean Architecture](#task-002)

**Labels:** `priority: high`, `épico: setup`, `pontos: 3`, `type: infra`  
**Milestone:** Sprint 1 - Fundação

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
- [TASK-001](#task-001) concluída

---

### [TASK-003: Configurar Docker Compose](#task-003)

**Labels:** `priority: high`, `épico: setup`, `pontos: 3`, `type: infra`  
**Milestone:** Sprint 1 - Fundação

## 📋 Descrição
Criar Dockerfile multi-stage, docker-compose.yml e .dockerignore para aplicação Java + PostgreSQL, seguindo o padrão da Fase 1.

## ✅ Critérios de Aceitação
- [ ] Dockerfile com multi-stage build (Maven build + JRE runtime)
- [ ] docker-compose.yml com serviços: app e postgres
- [ ] .dockerignore configurado (target/, .git/, .idea/, etc.)
- [ ] Network configurada entre os serviços
- [ ] Health checks implementados
- [ ] Volumes para persistência do banco
- [ ] Variáveis de ambiente configuráveis
- [ ] Aplicação conecta ao banco via Docker Compose
- [ ] Sobe com um único comando (docker-compose up)

## 🔧 Dependências Técnicas
- [TASK-001](#task-001) concluída

---

### [TASK-004: Configurar OpenAPI/Swagger](#task-004)

**Labels:** `priority: high`, `épico: setup`, `pontos: 2`, `type: infra`  
**Milestone:** Sprint 1 - Fundação

## 📋 Descrição
Configurar SpringDoc OpenAPI para documentação automática da API, seguindo o padrão da Fase 1 (OpenApiConfig com info, tags, descrições).

## ✅ Critérios de Aceitação
- [ ] Swagger UI acessível em /swagger-ui.html
- [ ] OpenApiConfig com título, descrição, versão e contato
- [ ] Tags organizadas por domínio (Tipos de Usuário, Usuários, Restaurantes, Cardápio)
- [ ] Endpoints documentados com @Operation, @ApiResponse, exemplos
- [ ] Schemas de request/response visíveis

## 🔧 Dependências Técnicas
- [TASK-001](#task-001) concluída

---

### [TASK-005: Configurar tratamento global de erros (ProblemDetail)](#task-005)

**Labels:** `priority: high`, `épico: setup`, `pontos: 3`, `type: feature`  
**Milestone:** Sprint 1 - Fundação

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
- [TASK-002](#task-002) concluída

---

## 📋 ÉPICO 2: CRUD de Tipo de Usuário

### [TASK-006: Criar entidade de domínio UserType](#task-006)

**Labels:** `priority: high`, `épico: user-type`, `pontos: 2`, `type: feature`  
**Milestone:** Sprint 2 - Tipo de Usuário

## 📋 Descrição
Criar a entidade pura UserType no pacote domain/entity. Na Fase 1, UserType era um enum; agora passa a ser uma entidade com CRUD próprio.

## ✅ Critérios de Aceitação
- [ ] Classe UserType com campos: id (UUID), name (String)
- [ ] POJO puro sem anotações de framework (sem JPA, sem Spring)
- [ ] Validações de domínio (nome não pode ser nulo/vazio)
- [ ] Construtores, getters e setters

## 🔧 Dependências Técnicas
- [TASK-002](#task-002) concluída

---

### [TASK-007: Criar gateway (port) UserTypeGateway](#task-007)

**Labels:** `priority: high`, `épico: user-type`, `pontos: 1`, `type: feature`  
**Milestone:** Sprint 2 - Tipo de Usuário

## 📋 Descrição
Criar interface de repositório no pacote domain/gateway, definindo o contrato de acesso a dados para UserType.

## ✅ Critérios de Aceitação
- [ ] Interface UserTypeGateway com métodos: create, update, delete, findById, findAll
- [ ] Sem dependência de framework (interface pura)
- [ ] Tipos de retorno adequados (Optional para findById)

## 🔧 Dependências Técnicas
- [TASK-006](#task-006) concluída

---

### [TASK-008: Criar casos de uso de UserType](#task-008)

**Labels:** `priority: high`, `épico: user-type`, `pontos: 3`, `type: feature`  
**Milestone:** Sprint 2 - Tipo de Usuário

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
- [TASK-007](#task-007) concluída

---

### [TASK-009: Criar persistência JPA de UserType](#task-009)

**Labels:** `priority: high`, `épico: user-type`, `pontos: 3`, `type: feature`  
**Milestone:** Sprint 2 - Tipo de Usuário

## 📋 Descrição
Implementar adapter de saída (entidade JPA + repository Spring Data + implementação do gateway).

## ✅ Critérios de Aceitação
- [ ] UserTypeJpaEntity com anotações JPA (@Entity, @Table, @Id)
- [ ] UserTypeRepository (interface Spring Data JPA)
- [ ] UserTypeJpaGateway implementando UserTypeGateway
- [ ] Mapeamento bidirecional entre entidade de domínio e entidade JPA
- [ ] Tabela "user_types" no banco

## 🔧 Dependências Técnicas
- [TASK-007](#task-007) concluída

---

### [TASK-010: Criar controller REST de UserType](#task-010)

**Labels:** `priority: high`, `épico: user-type`, `pontos: 3`, `type: feature`  
**Milestone:** Sprint 2 - Tipo de Usuário

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
- [TASK-008](#task-008) e [TASK-009](#task-009) concluídas

---

### [TASK-011: Testes unitários de UserType](#task-011)

**Labels:** `priority: high`, `épico: user-type`, `pontos: 3`, `type: test`  
**Milestone:** Sprint 2 - Tipo de Usuário

## 📋 Descrição
Testes unitários para use cases e controller de UserType.

## ✅ Critérios de Aceitação
- [ ] Testes dos 5 use cases com mocks do gateway (JUnit 5 + Mockito)
- [ ] Testes do controller com MockMvc
- [ ] Cenários de sucesso e erro cobertos
- [ ] Cobertura ≥ 80% nas classes de UserType

## 🔧 Dependências Técnicas
- [TASK-010](#task-010) concluída

---

### [TASK-012: Testes de integração de UserType](#task-012)

**Labels:** `priority: high`, `épico: user-type`, `pontos: 3`, `type: test`  
**Milestone:** Sprint 2 - Tipo de Usuário

## 📋 Descrição
Testes de integração end-to-end para o CRUD de UserType.

## ✅ Critérios de Aceitação
- [ ] Testes com @SpringBootTest e banco H2 em memória
- [ ] Teste de criação, leitura, atualização e remoção
- [ ] Validação de respostas HTTP (status code e corpo)
- [ ] Validação de cenários de erro (404, 400)

## 🔧 Dependências Técnicas
- [TASK-010](#task-010) concluída

---

## 📋 ÉPICO 3: Refatoração de Usuário (User)

### [TASK-013: Criar entidade de domínio User e gateway](#task-013)

**Labels:** `priority: high`, `épico: user`, `pontos: 3`, `type: feature`  
**Milestone:** Sprint 3 - Usuário

## 📋 Descrição
Criar entidade User no domain (POJO puro) e interface UserGateway. Na Fase 1, User era uma entidade JPA direta; agora segue Clean Architecture com separação.

## ✅ Critérios de Aceitação
- [ ] Classe User com campos: id (UUID), name, email, login, password, address, lastModifiedDate, userType (referência a UserType)
- [ ] POJO puro sem anotações de framework
- [ ] Interface UserGateway com métodos: create, update, delete, findById, findByEmail, findByLogin, findAll
- [ ] Validações de domínio mantidas

## 🔧 Dependências Técnicas
- [TASK-006](#task-006) concluída

---

### [TASK-014: Criar casos de uso de User](#task-014)

**Labels:** `priority: high`, `épico: user`, `pontos: 5`, `type: feature`  
**Milestone:** Sprint 3 - Usuário

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
- [TASK-013](#task-013) concluída
- [TASK-008](#task-008) concluída (para validar UserType)

---

### [TASK-015: Criar persistência JPA e controller de User](#task-015)

**Labels:** `priority: high`, `épico: user`, `pontos: 5`, `type: feature`  
**Milestone:** Sprint 3 - Usuário

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
- [TASK-014](#task-014) concluída
- [TASK-009](#task-009) concluída

---

### [TASK-016: Testes de User (unitários + integração)](#task-016)

**Labels:** `priority: high`, `épico: user`, `pontos: 5`, `type: test`  
**Milestone:** Sprint 3 - Usuário

## 📋 Descrição
Testes unitários e de integração para User, cobrindo todos os use cases e endpoints.

## ✅ Critérios de Aceitação
- [ ] Testes unitários dos use cases com mocks
- [ ] Testes do controller com MockMvc
- [ ] Testes de integração com H2 (CRUD completo + login + troca de senha)
- [ ] Cenários de erro (email duplicado, login duplicado, credenciais inválidas, not found)
- [ ] Cobertura ≥ 80%

## 🔧 Dependências Técnicas
- [TASK-015](#task-015) concluída

---

## 📋 ÉPICO 4: CRUD de Restaurante

### [TASK-017: Criar entidade de domínio Restaurant e gateway](#task-017)

**Labels:** `priority: high`, `épico: restaurant`, `pontos: 2`, `type: feature`  
**Milestone:** Sprint 4 - Restaurante

## 📋 Descrição
Criar entidade Restaurant no domain (POJO puro) e interface RestaurantGateway.

## ✅ Critérios de Aceitação
- [ ] Classe Restaurant com campos: id (UUID), name, address, cuisineType, openingHours, ownerId (UUID)
- [ ] POJO puro sem anotações de framework
- [ ] Interface RestaurantGateway com métodos: create, update, delete, findById, findAll, findByOwnerId

## 🔧 Dependências Técnicas
- [TASK-002](#task-002) concluída

---

### [TASK-018: Criar casos de uso de Restaurant](#task-018)

**Labels:** `priority: high`, `épico: restaurant`, `pontos: 4`, `type: feature`  
**Milestone:** Sprint 4 - Restaurante

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
- [TASK-017](#task-017) concluída
- [TASK-013](#task-013) concluída (UserGateway para validar owner)

---

### [TASK-019: Criar persistência JPA de Restaurant](#task-019)

**Labels:** `priority: high`, `épico: restaurant`, `pontos: 3`, `type: feature`  
**Milestone:** Sprint 4 - Restaurante

## 📋 Descrição
Implementar adapter de saída para Restaurant.

## ✅ Critérios de Aceitação
- [ ] RestaurantJpaEntity com anotações JPA
- [ ] Relacionamento ManyToOne com UserJpaEntity (owner)
- [ ] RestaurantRepository (Spring Data JPA)
- [ ] RestaurantJpaGateway implementando RestaurantGateway
- [ ] Tabela "restaurants" no banco

## 🔧 Dependências Técnicas
- [TASK-017](#task-017) concluída
- [TASK-015](#task-015) concluída (UserJpaEntity)

---

### [TASK-020: Criar controller REST de Restaurant](#task-020)

**Labels:** `priority: high`, `épico: restaurant`, `pontos: 3`, `type: feature`  
**Milestone:** Sprint 4 - Restaurante

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
- [TASK-018](#task-018) e [TASK-019](#task-019) concluídas

---

### [TASK-021: Testes unitários de Restaurant](#task-021)

**Labels:** `priority: high`, `épico: restaurant`, `pontos: 3`, `type: test`  
**Milestone:** Sprint 4 - Restaurante

## 📋 Descrição
Testes unitários para use cases e controller de Restaurant.

## ✅ Critérios de Aceitação
- [ ] Testes dos use cases com mocks (JUnit 5 + Mockito)
- [ ] Testes do controller com MockMvc
- [ ] Cenários de sucesso e erro (owner não existe, owner não é RESTAURANT_OWNER)
- [ ] Cobertura ≥ 80%

## 🔧 Dependências Técnicas
- [TASK-020](#task-020) concluída

---

### [TASK-022: Testes de integração de Restaurant](#task-022)

**Labels:** `priority: high`, `épico: restaurant`, `pontos: 3`, `type: test`  
**Milestone:** Sprint 4 - Restaurante

## 📋 Descrição
Testes de integração end-to-end para o CRUD de Restaurant.

## ✅ Critérios de Aceitação
- [ ] Testes com @SpringBootTest e H2
- [ ] CRUD completo testado
- [ ] Validação de associação com owner (UserType = RESTAURANT_OWNER)
- [ ] Cenários de erro testados

## 🔧 Dependências Técnicas
- [TASK-020](#task-020) concluída

---

## 📋 ÉPICO 5: CRUD de Item do Cardápio

### [TASK-023: Criar entidade de domínio MenuItem e gateway](#task-023)

**Labels:** `priority: high`, `épico: menu-item`, `pontos: 2`, `type: feature`  
**Milestone:** Sprint 5 - Cardápio

## 📋 Descrição
Criar entidade MenuItem no domain (POJO puro) e interface MenuItemGateway.

## ✅ Critérios de Aceitação
- [ ] Classe MenuItem com campos: id (UUID), name, description, price (BigDecimal), dineInOnly (boolean), photoPath (String), restaurantId (UUID)
- [ ] POJO puro sem anotações de framework
- [ ] Interface MenuItemGateway com métodos: create, update, delete, findById, findByRestaurantId

## 🔧 Dependências Técnicas
- [TASK-002](#task-002) concluída

---

### [TASK-024: Criar casos de uso de MenuItem](#task-024)

**Labels:** `priority: high`, `épico: menu-item`, `pontos: 3`, `type: feature`  
**Milestone:** Sprint 5 - Cardápio

## 📋 Descrição
Implementar use cases de MenuItem com validações de negócio.

## ✅ Critérios de Aceitação
- [ ] CreateMenuItemUseCase — valida que restaurante existe, preço > 0
- [ ] UpdateMenuItemUseCase — atualiza com validação de existência
- [ ] DeleteMenuItemUseCase — remove com validação de existência
- [ ] FindMenuItemByIdUseCase — busca por ID
- [ ] FindMenuItemsByRestaurantUseCase — lista itens de um restaurante

## 🔧 Dependências Técnicas
- [TASK-023](#task-023) concluída
- [TASK-017](#task-017) concluída (RestaurantGateway para validar restaurante)

---

### [TASK-025: Criar persistência JPA de MenuItem](#task-025)

**Labels:** `priority: high`, `épico: menu-item`, `pontos: 3`, `type: feature`  
**Milestone:** Sprint 5 - Cardápio

## 📋 Descrição
Implementar adapter de saída para MenuItem.

## ✅ Critérios de Aceitação
- [ ] MenuItemJpaEntity com anotações JPA
- [ ] Relacionamento ManyToOne com RestaurantJpaEntity
- [ ] MenuItemRepository (Spring Data JPA)
- [ ] MenuItemJpaGateway implementando MenuItemGateway
- [ ] Tabela "menu_items" no banco

## 🔧 Dependências Técnicas
- [TASK-023](#task-023) concluída
- [TASK-019](#task-019) concluída (RestaurantJpaEntity)

---

### [TASK-026: Criar controller REST de MenuItem](#task-026)

**Labels:** `priority: high`, `épico: menu-item`, `pontos: 3`, `type: feature`  
**Milestone:** Sprint 5 - Cardápio

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
- [TASK-024](#task-024) e [TASK-025](#task-025) concluídas

---

### [TASK-027: Testes unitários de MenuItem](#task-027)

**Labels:** `priority: high`, `épico: menu-item`, `pontos: 3`, `type: test`  
**Milestone:** Sprint 5 - Cardápio

## 📋 Descrição
Testes unitários para use cases e controller de MenuItem.

## ✅ Critérios de Aceitação
- [ ] Testes dos use cases com mocks (JUnit 5 + Mockito)
- [ ] Testes do controller com MockMvc
- [ ] Cenários de sucesso e erro (restaurante não existe, preço inválido)
- [ ] Cobertura ≥ 80%

## 🔧 Dependências Técnicas
- [TASK-026](#task-026) concluída

---

### [TASK-028: Testes de integração de MenuItem](#task-028)

**Labels:** `priority: high`, `épico: menu-item`, `pontos: 3`, `type: test`  
**Milestone:** Sprint 5 - Cardápio

## 📋 Descrição
Testes de integração end-to-end para o CRUD de MenuItem.

## ✅ Critérios de Aceitação
- [ ] Testes com @SpringBootTest e H2
- [ ] CRUD completo testado
- [ ] Validação de associação com Restaurant
- [ ] Cenários de erro testados

## 🔧 Dependências Técnicas
- [TASK-026](#task-026) concluída

---

## 📋 ÉPICO 6: Documentação, Qualidade e Entregáveis

### [TASK-029: Configurar repositório Git](#task-029)

**Labels:** `priority: high`, `épico: documentação`, `pontos: 2`, `type: infra`  
**Milestone:** Sprint 6 - Finalização

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
- [TASK-001](#task-001) concluída

---

### [TASK-030: Verificar cobertura de testes ≥ 80%](#task-030)

**Labels:** `priority: high`, `épico: documentação`, `pontos: 2`, `type: test`  
**Milestone:** Sprint 6 - Finalização

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

---

### [TASK-031: Criar README.md completo](#task-031)

**Labels:** `priority: high`, `épico: documentação`, `pontos: 3`, `type: docs`  
**Milestone:** Sprint 6 - Finalização

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

---

### [TASK-032: Criar documentação detalhada da API (docs/API.md)](#task-032)

**Labels:** `priority: high`, `épico: documentação`, `pontos: 3`, `type: docs`  
**Milestone:** Sprint 6 - Finalização

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

---

### [TASK-033: Criar Collection Postman](#task-033)

**Labels:** `priority: high`, `épico: documentação`, `pontos: 3`, `type: docs`  
**Milestone:** Sprint 6 - Finalização

## 📋 Descrição
Collection com todos os endpoints para teste, seguindo o padrão da Fase 1 (cenários de sucesso + erro, variáveis, organização por pastas). Inclui documentação de uso.

## ✅ Critérios de Aceitação
- [ ] Requests para todos os endpoints (UserType, User, Restaurant, MenuItem)
- [ ] Variáveis de ambiente (localUrl, prodUrl)
- [ ] Exemplos de request body para cada operação
- [ ] Organização por pastas/módulos
- [ ] Cenários de sucesso e erro cobertos
- [ ] Scripts de teste automatizados em cada request
- [ ] Arquivo JSON exportado em `docs/api-collection/`
- [ ] `docs/api-collection/README.md` com instruções de importação, estrutura da collection, endpoints cobertos e ordem de execução recomendada

## 🔧 Dependências Técnicas
- Todos os endpoints implementados

---

### [TASK-034: Configurar CI/CD (GitHub Actions)](#task-034)

**Labels:** `priority: high`, `épico: documentação`, `pontos: 3`, `type: infra`  
**Milestone:** Sprint 6 - Finalização

## 📋 Descrição
Pipeline de build, teste e verificação de cobertura no GitHub Actions. Inclui documentação das opções de CI/CD e guia de configuração.

## ✅ Critérios de Aceitação
- [ ] Workflow para build e testes em push/PR para develop e main
- [ ] Verificação de cobertura no pipeline
- [ ] Badge de status no README
- [ ] Build falha se testes falharem
- [ ] `docs/ci-cd/CI_CD_OPTIONS.md` — comparação de opções de CI/CD (minimalista, simplificada, completa)
- [ ] `docs/ci-cd/CI_CD_SETUP.md` — guia completo de configuração do pipeline escolhido

## 🔧 Dependências Técnicas
- [TASK-030](#task-030) concluída

---

### [TASK-035: Criar índice de documentação (docs/README.md)](#task-035)

**Labels:** `priority: high`, `épico: documentação`, `pontos: 1`, `type: docs`  
**Milestone:** Sprint 6 - Finalização

## 📋 Descrição
Índice geral da pasta docs/ linkando todos os documentos do projeto, seguindo padrão da Fase 1.

## ✅ Critérios de Aceitação
- [ ] Links para todos os documentos (BACKLOG, API, Postman, CI/CD, etc.)
- [ ] Estrutura do projeto documentada
- [ ] Quick Start com ordem de leitura sugerida

## 🔧 Dependências Técnicas
- [TASK-031](#task-031) e [TASK-032](#task-032) concluídas

---

### [TASK-036: Preparar entrega final](#task-036)

**Labels:** `priority: high`, `épico: documentação`, `pontos: 2`, `type: docs`  
**Milestone:** Sprint 6 - Finalização

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

---

### [TASK-037: Gravar vídeo de apresentação](#task-037)

**Labels:** `priority: high`, `épico: documentação`, `pontos: 2`, `type: docs`  
**Milestone:** Sprint 6 - Finalização

## 📋 Descrição
Vídeo de ~5 minutos demonstrando as funcionalidades e o projeto rodando (obrigatório conforme PDF).

## ✅ Critérios de Aceitação
- [ ] Demonstração das funcionalidades (CRUD UserType, User, Restaurant, MenuItem)
- [ ] Projeto rodando via Docker Compose
- [ ] Explicação da arquitetura Clean Architecture
- [ ] Swagger UI demonstrado
- [ ] Duração ~5 minutos

## 🔧 Dependências Técnicas
- [TASK-036](#task-036) concluída

---

## 📋 ÉPICO 7: Extras (Não Obrigatórios)

> ⚠️ As tasks abaixo **NÃO são obrigatórias** conforme o PDF da Fase 2.
> São boas práticas que agregam qualidade ao projeto mas não são exigidas para a entrega.

### [TASK-038: Configurar SonarCloud (NÃO OBRIGATÓRIO)](#task-038)

**Labels:** `priority: medium`, `épico: extras`, `pontos: 3`, `type: infra`  
**Milestone:** Sprint 6 - Finalização

## 📋 Descrição
Configurar análise estática de código com SonarCloud para validação de qualidade em PRs. Não exigido no PDF da Fase 2, mas agrega valor ao projeto. Inclui documentação de setup.

## ✅ Critérios de Aceitação
- [ ] Conta criada no SonarCloud
- [ ] `sonar-project.properties` configurado
- [ ] SONAR_TOKEN adicionado nos secrets do GitHub
- [ ] Workflow de análise em PRs (opcional por label ou obrigatório)
- [ ] Quality Gate configurado (cobertura ≥ 80%, 0 bugs, 0 vulnerabilidades)
- [ ] Badge no README
- [ ] `docs/qualidade/SONARCLOUD_SETUP.md` — guia completo de configuração (conta, token, workflow, troubleshooting)

## 🔧 Dependências Técnicas
- [TASK-034](#task-034) concluída

---

### [TASK-039: Deploy no Render.com (NÃO OBRIGATÓRIO)](#task-039)

**Labels:** `priority: medium`, `épico: extras`, `pontos: 3`, `type: infra`  
**Milestone:** Sprint 6 - Finalização

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
- [ ] `docs/deploy/RENDER_DEPLOY_GUIDE.md` — guia completo de deploy (passo a passo, variáveis, troubleshooting, limites do plano gratuito)

## 🔧 Dependências Técnicas
- [TASK-003](#task-003) concluída (Dockerfile)
- [TASK-036](#task-036) concluída (entrega final)

---

### [TASK-040: Criar documentação de contexto do projeto (NÃO OBRIGATÓRIO)](#task-040)

**Labels:** `priority: medium`, `épico: extras`, `pontos: 2`, `type: docs`  
**Milestone:** Sprint 6 - Finalização

## 📋 Descrição
Documento com contexto do projeto, decisões técnicas tomadas e resumo do planejamento. Não exigido no PDF da Fase 2, mas serve como registro histórico e facilita onboarding.

## ✅ Critérios de Aceitação
- [ ] `docs/planejamento/RESUMO_CONVERSA.md` criado
- [ ] Contexto do projeto (continuação da Fase 1)
- [ ] Decisões técnicas documentadas (por que Clean Architecture, por que UUID, etc.)
- [ ] Diferenças em relação à Fase 1 explicadas
- [ ] Referências utilizadas (PDF do professor, estrutura de referência)

## 🔧 Dependências Técnicas
- Nenhuma (pode ser criado a qualquer momento)

---

## 📋 ÉPICO 8: Melhorias Identificadas

> Tasks criadas durante o desenvolvimento para corrigir gaps e melhorar a qualidade.

### [TASK-041: UserType — nome deve ser único](#task-041)

**Labels:** `priority: high`, `épico: user-type`, `pontos: 2`, `type: feature`  
**Milestone:** Sprint 2 - Tipo de Usuário

## 📋 Descrição
O UserType está aceitando nomes duplicados (ex: dois "CUSTOMER"). O nome deve ser único no banco.

## ✅ Critérios de Aceitação
- [ ] Adicionar método `findByName(String name)` no UserTypeGateway
- [ ] Validar unicidade no CreateUserTypeUseCase (lançar BusinessException se já existir)
- [ ] Validar unicidade no UpdateUserTypeUseCase (excluindo o próprio registro)
- [ ] Adicionar `@Column(unique = true)` na UserTypeJpaEntity
- [ ] Adicionar query `findByName` no UserTypeRepository
- [ ] Implementar no UserTypeJpaGateway
- [ ] Testes unitários e integração atualizados
- [ ] Endpoint retorna 422 ao tentar criar/atualizar com nome duplicado

## 🔧 Dependências Técnicas
- [TASK-010](#task-010) concluída

---

## 📊 RESUMO DO BACKLOG

**Total de Tasks:** 41  
**Obrigatórias:** 37 | **Não obrigatórias:** 3 | **Melhorias:** 1  
**Estimativa Total:** ~110 pontos

### Por Prioridade:
- **Alta:** 38 tasks (obrigatórias + melhorias)
- **Média:** 3 tasks (não obrigatórias)

### Por Épico:
| Épico | Tasks | Pontos |
|-------|-------|--------|
| 1. Setup e Infraestrutura | 5 | 14 |
| 2. CRUD Tipo de Usuário | 7 | 18 |
| 3. Refatoração de Usuário | 4 | 18 |
| 4. CRUD Restaurante | 6 | 18 |
| 5. CRUD Item do Cardápio | 6 | 17 |
| 6. Documentação, Qualidade e Entregáveis | 9 | 21 |
| 7. Extras (Não Obrigatórios) | 3 | 8 |
| 8. Melhorias Identificadas | 1 | 2 |

---

## 🎯 ORDEM SUGERIDA DE EXECUÇÃO

| Sprint | Descrição | Tasks |
|--------|-----------|-------|
| Sprint 1 | Fundação | [001](#task-001) → [005](#task-005) |
| Sprint 2 | Tipo de Usuário | [006](#task-006) → [012](#task-012), [041](#task-041) |
| Sprint 3 | Usuário | [013](#task-013) → [016](#task-016) |
| Sprint 4 | Restaurante | [017](#task-017) → [022](#task-022) |
| Sprint 5 | Cardápio | [023](#task-023) → [028](#task-028) |
| Sprint 6 | Finalização | [029](#task-029) → [040](#task-040) |

---

## 📝 NOTAS

- As estimativas são em pontos de história (Story Points)
- 1 ponto ≈ 1-2 horas de trabalho
- Tasks podem ser quebradas em subtasks menores se necessário
- Recomenda-se seguir a ordem sugerida para evitar dependências bloqueantes
- A estrutura Clean Architecture segue a referência do professor (domain → application → infra)
- Tasks 038, 039 e 040 são **NÃO OBRIGATÓRIAS** — não exigidas no PDF da Fase 2
- Novas melhorias identificadas durante o desenvolvimento são adicionadas no Épico 8
