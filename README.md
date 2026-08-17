# 🚀 Sistema de Gestão de Restaurantes

[![CI/CD Pipeline](https://github.com/jeffesa/12ADJT-fase-2/actions/workflows/ci-cd.yml/badge.svg)](https://github.com/jeffesa/12ADJT-fase-2/actions/workflows/ci-cd.yml)
[![Quality Gate Status](https://sonarcloud.io/api/project_badges/measure?project=jeffesa_12ADJT-fase-2&metric=alert_status)](https://sonarcloud.io/summary/new_code?id=jeffesa_12ADJT-fase-2)
[![Coverage](https://sonarcloud.io/api/project_badges/measure?project=jeffesa_12ADJT-fase-2&metric=coverage)](https://sonarcloud.io/summary/new_code?id=jeffesa_12ADJT-fase-2)

Sistema de gestão de restaurantes desenvolvido com Spring Boot para o Tech Challenge - Fase 2 da FIAP.

> 🌐 **Aplicação em produção:** [https://one2adjt-fase-2.onrender.com](https://one2adjt-fase-2.onrender.com)
>
> 📖 **Swagger UI:** [https://one2adjt-fase-2.onrender.com/swagger-ui.html](https://one2adjt-fase-2.onrender.com/swagger-ui.html)
>
> 📬 **Collection Postman:** [`docs/api-collection/`](docs/api-collection/) — [Como importar e executar](docs/api-collection/README.md)

---

## 📋 Sobre o Projeto

API RESTful para gestão de restaurantes com funcionalidades de:

- ✅ Cadastro de tipos de usuário (CUSTOMER, RESTAURANT_OWNER)
- ✅ Cadastro de usuários (donos de restaurante e clientes)
- ✅ Cadastro de restaurantes (vinculados a um owner)
- ✅ Cadastro de itens do cardápio (vinculados a um restaurante)
- ✅ Autenticação (login + troca de senha com BCrypt)

---

## 🛠️ Tecnologias

| Tecnologia | Versão | Uso |
|-----------|--------|-----|
| Java | 17 | Linguagem principal |
| Spring Boot | 3.2.3 | Framework web |
| Spring Data JPA | - | Persistência |
| PostgreSQL | 15 | Banco de dados (produção) |
| H2 Database | - | Banco de dados (dev/test) |
| Docker & Compose | - | Containerização |
| Maven | 3.8+ | Build |
| SpringDoc OpenAPI | 2.3.0 | Documentação (Swagger) |
| JUnit 5 + Mockito | - | Testes |
| JaCoCo | 0.8.11 | Cobertura (≥ 80%) |
| SonarCloud | - | Qualidade de código |

---

## 🏗️ Arquitetura

Clean Architecture organizada por feature/domínio:

### Diagrama de Camadas

```
┌─────────────────────────────────────────────────────┐
│                    CLIENT (HTTP)                     │
└─────────────────────┬───────────────────────────────┘
                      │
┌─────────────────────▼───────────────────────────────┐
│           Controller (infra/web)                     │
│   Recebe HTTP, converte DTOs, delega ao use case    │
└─────────────────────┬───────────────────────────────┘
                      │
┌─────────────────────▼───────────────────────────────┐
│            Use Case (application)                    │
│   Orquestra o fluxo, valida regras de processo      │
└─────────────────────┬───────────────────────────────┘
                      │
┌─────────────────────▼───────────────────────────────┐
│             Domain (domain)                          │
│   Entidades puras, validações, interfaces gateway   │
└─────────────────────┬───────────────────────────────┘
                      │
┌─────────────────────▼───────────────────────────────┐
│          Gateway Impl (infra/persistence)            │
│   JPA entities, repositories, acesso ao banco       │
└─────────────────────────────────────────────────────┘
```

**Regra de dependência:** `infra → application → domain` (de fora para dentro)

### Estrutura de Pacotes

```
src/main/java/com/fiap/fase2/
├── domain/                        # Regras de negócio (sem framework)
│   ├── usertype/                  #   UserType + UserTypeGateway
│   ├── user/                      #   User + UserGateway
│   ├── restaurant/                #   Restaurant + RestaurantGateway
│   ├── menuitem/                  #   MenuItem + MenuItemGateway
│   └── shared/                    #   Exceções de domínio
├── application/                   # Casos de uso (orquestração)
│   ├── usertype/                  #   5 use cases
│   ├── user/                      #   7 use cases
│   ├── restaurant/                #   6 use cases
│   └── menuitem/                  #   5 use cases
└── infra/                         # Frameworks e drivers
    ├── web/                       #   Controllers REST + DTOs
    │   ├── usertype/
    │   ├── user/
    │   ├── restaurant/
    │   └── menuitem/
    ├── persistence/               #   JPA entities + repositories
    │   ├── usertype/
    │   ├── user/
    │   ├── restaurant/
    │   └── menuitem/
    ├── config/                    #   OpenAPI, Security, BeanConfigs
    └── shared/                    #   GlobalExceptionHandler
```

---

## 📡 Endpoints da API

### Tipos de Usuário

| Método | Endpoint | Descrição | Status |
|--------|----------|-----------|--------|
| POST | `/api/v1/user-types` | Criar tipo | 201, 400, 422 |
| GET | `/api/v1/user-types` | Listar todos | 200 |
| GET | `/api/v1/user-types/{id}` | Buscar por ID | 200, 404 |
| PUT | `/api/v1/user-types/{id}` | Atualizar | 200, 404, 422 |
| DELETE | `/api/v1/user-types/{id}` | Remover | 204, 404 |

### Usuários

| Método | Endpoint | Descrição | Status |
|--------|----------|-----------|--------|
| POST | `/api/v1/users` | Criar usuário | 201, 400, 422 |
| GET | `/api/v1/users` | Listar todos | 200 |
| GET | `/api/v1/users?name={nome}` | Buscar por nome | 200 |
| GET | `/api/v1/users/{id}` | Buscar por ID | 200, 404 |
| PUT | `/api/v1/users/{id}` | Atualizar | 200, 404 |
| DELETE | `/api/v1/users/{id}` | Remover | 204, 404 |
| POST | `/api/v1/users/login` | Login | 200, 422 |
| PATCH | `/api/v1/users/{id}/password` | Trocar senha | 200, 400, 404 |

### Restaurantes

| Método | Endpoint | Descrição | Status |
|--------|----------|-----------|--------|
| POST | `/api/v1/restaurants` | Criar restaurante | 201, 400, 422 |
| GET | `/api/v1/restaurants` | Listar todos | 200 |
| GET | `/api/v1/restaurants?ownerId={id}` | Buscar por dono | 200 |
| GET | `/api/v1/restaurants/{id}` | Buscar por ID | 200, 404 |
| PUT | `/api/v1/restaurants/{id}` | Atualizar | 200, 404, 422 |
| DELETE | `/api/v1/restaurants/{id}` | Remover | 204, 404 |

### Cardápio (MenuItem)

| Método | Endpoint | Descrição | Status |
|--------|----------|-----------|--------|
| POST | `/api/v1/restaurants/{id}/menu-items` | Criar item | 201, 400, 404 |
| GET | `/api/v1/restaurants/{id}/menu-items` | Listar itens | 200 |
| GET | `/api/v1/menu-items/{id}` | Buscar por ID | 200, 404 |
| PUT | `/api/v1/menu-items/{id}` | Atualizar | 200, 404 |
| DELETE | `/api/v1/menu-items/{id}` | Remover | 204, 404 |

---

## 🚀 Como Executar

### Script Runner (recomendado)

```bash
./run.sh
```

Menu interativo:
```
╔══════════════════════════════════════════╗
║   🚀 Tech Challenge Fase 2 - Runner     ║
╠══════════════════════════════════════════╣
║  1) Local - profile dev (H2)            ║
║  2) Local - profile test (H2)           ║
║  3) Local - profile prod (PostgreSQL)   ║
║  4) Docker Compose (build + up)         ║
║  5) Docker Compose (stop)               ║
║  6) Rodar testes (mvn clean verify)     ║
║  7) Rodar collection (Newman + HTML)    ║
║  8) Rodar testes API (curl + jq)        ║
║  9) Limpar banco (docker-compose -v)    ║
║ 10) Kill porta 8080                     ║
║  0) Sair                                ║
╚══════════════════════════════════════════╝
```

Também aceita argumentos diretos:
```bash
./run.sh dev        # Inicia com profile dev (H2)
./run.sh docker     # Docker Compose build + up
./run.sh stop       # Para Docker Compose
./run.sh tests      # Roda testes
./run.sh collection # Newman + HTML report
./run.sh test-api   # Testes via curl + jq
./run.sh reset-db   # Limpa banco (remove volume)
./run.sh kill       # Mata processo na porta 8080
```

### Pré-requisitos

- Java 17+
- Maven 3.8+
- Docker e Docker Compose (ou Colima no macOS)

### Com Docker Compose

```bash
docker-compose up --build -d
```

Sobe PostgreSQL + aplicação. Acesse:
- Health: http://localhost:8080/actuator/health
- Swagger: http://localhost:8080/swagger-ui.html

### Local (dev com H2)

```bash
mvn spring-boot:run -Dspring-boot.run.profiles=dev
```

### Testes

```bash
mvn clean verify
```

Gera relatório de cobertura em `target/site/jacoco/index.html`. Build falha se cobertura < 80%.

---

## 🧪 Evidência de Testes

### Via Newman (gera relatório HTML)

```bash
npm install -g newman newman-reporter-htmlextra
./run.sh collection
open docs/api-collection/evidencia-testes-api.html
```

### Via Shell (sem dependências externas)

```bash
./run.sh test-api
```

Usa apenas `curl` + `jq`. Ideal para ambientes com restrição de instalação.

Detalhes em [`docs/api-collection/README.md`](docs/api-collection/README.md).

---

## 🗃️ Banco de Dados

### DBeaver / Conexão local (Docker)

| Campo | Valor |
|-------|-------|
| Host | `localhost` |
| Port | `5433` |
| Database | `fiap_fase2` |
| Username | `postgres` |
| Password | `postgres` |

### Profiles

| Profile | Banco | Uso |
|---------|-------|-----|
| `dev` | H2 in-memory | Desenvolvimento local |
| `test` | H2 in-memory | Testes automatizados |
| `prod` | PostgreSQL | Docker Compose / Produção |

---

## 🌿 Convenção de Branches

Validação automática via GitHub Actions. PRs com branches fora do padrão são bloqueados.

| Prefixo | Uso |
|---------|-----|
| `feature/*` | Novas funcionalidades |
| `bugfix/*` | Correção de bugs |
| `hotfix/*` | Correções urgentes |
| `release/*` | Preparação de release |
| `chore/*` | Manutenção |
| `docs/*` | Documentação |
| `test/*` | Testes |
| `refactor/*` | Refatoração |
| `perf/*` | Performance |
| `ci/*` | CI/CD |
| `style/*` | Formatação |

Exemplo: `feature/task-001-setup-spring-boot`

---

## 📚 Documentação

- [Backlog Completo](docs/planejamento/BACKLOG.md)
- [Collection Postman](docs/api-collection/README.md)
- [Setup GitHub Projects](docs/github/GITHUB_PROJECTS_SETUP.md)
- [Opções de CI/CD](docs/ci-cd/CI_CD_OPTIONS.md)
- [Deploy no Render](docs/deploy/)
- [Qualidade (SonarCloud)](docs/qualidade/)

---

## 👥 Autores

- **Jefferson** - [GitHub](https://github.com/jeffesa)
- **John** - [GitHub](https://github.com/John-Duque)

---

## 📝 Licença

Projeto desenvolvido para fins educacionais - FIAP Tech Challenge Fase 2.
