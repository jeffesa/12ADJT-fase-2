# 🚀 Sistema de Gestão de Restaurantes

[![CI/CD Pipeline](https://github.com/jeffesa/12ADJT-fase-2/actions/workflows/ci-cd.yml/badge.svg)](https://github.com/jeffesa/12ADJT-fase-2/actions/workflows/ci-cd.yml)
[![Quality Gate Status](https://sonarcloud.io/api/project_badges/measure?project=jeffesa_12ADJT-fase-2&metric=alert_status)](https://sonarcloud.io/summary/new_code?id=jeffesa_12ADJT-fase-2)
[![Coverage](https://sonarcloud.io/api/project_badges/measure?project=jeffesa_12ADJT-fase-2&metric=coverage)](https://sonarcloud.io/summary/new_code?id=jeffesa_12ADJT-fase-2)

Sistema de gestão de restaurantes desenvolvido com Spring Boot para o Tech Challenge - Fase 2 da FIAP.

---

## 📋 Sobre o Projeto

API RESTful para gestão de restaurantes com funcionalidades de:

- ✅ Cadastro de tipos de usuário
- ✅ Cadastro de usuários (donos de restaurante e clientes)
- ✅ Cadastro de restaurantes
- ✅ Cadastro de itens do cardápio

---

## 🛠️ Tecnologias

- **Java 17**
- **Spring Boot 3.2.3**
- **Spring Data JPA**
- **PostgreSQL**
- **Docker & Docker Compose**
- **Maven**
- **Swagger/OpenAPI**
- **JUnit 5 + Mockito**
- **JaCoCo** (cobertura ≥ 80%)

---

## 🏗️ Arquitetura

Clean Architecture organizada por feature/domínio:

```
src/main/java/com/fiap/fase2/
├── domain/                        # Regras de negócio (sem dependência de framework)
│   ├── usertype/                  #   Entidade + Gateway (port)
│   ├── user/
│   ├── restaurant/
│   ├── menuitem/
│   └── shared/                    #   Exceções de domínio compartilhadas
├── application/                   # Casos de uso (orquestração)
│   ├── usertype/
│   ├── user/
│   ├── restaurant/
│   └── menuitem/
└── infra/                         # Frameworks e drivers (Spring, JPA, HTTP)
    ├── web/                       #   Controllers REST + DTOs (entrada)
    │   ├── usertype/
    │   ├── user/
    │   ├── restaurant/
    │   └── menuitem/
    ├── persistence/               #   JPA entities + repositories (saída)
    │   ├── usertype/
    │   ├── user/
    │   ├── restaurant/
    │   └── menuitem/
    ├── config/                    #   OpenAPI, Security, Beans
    └── shared/                    #   GlobalExceptionHandler
```

**Fluxo de dependência:** `infra → application → domain` (de fora para dentro)

---

## 🌿 Convenção de Branches

O repositório possui validação automática de nomes de branch. PRs com branches fora do padrão serão **bloqueados**.

### Prefixos permitidos:

| Prefixo | Uso |
|---------|-----|
| `feature/*` | Novas funcionalidades |
| `bugfix/*` | Correção de bugs |
| `hotfix/*` | Correções urgentes em produção |
| `release/*` | Preparação de release |
| `chore/*` | Tarefas de manutenção |
| `docs/*` | Documentação |
| `test/*` | Testes |
| `refactor/*` | Refatoração de código |
| `perf/*` | Melhorias de performance |
| `ci/*` | Alterações de CI/CD |
| `style/*` | Formatação e estilo de código |

### Exemplos válidos:
```
feature/task-001-setup-spring-boot
bugfix/fix-user-validation
docs/update-readme
refactor/clean-architecture-user
```

### Exemplos inválidos (PR será bloqueado):
```
minha-branch
task-001
fix-bug
update
```

### Como renomear uma branch:

```bash
# Renomear branch local
git branch -m nome-errado feature/nome-correto

# Se já fez push com nome errado, deletar remote e push com novo nome
git push origin --delete nome-errado
git push -u origin feature/nome-correto
```

### Como criar branch corretamente desde o início:

```bash
# A partir de develop
git checkout develop
git pull origin develop
git checkout -b feature/task-001-setup-spring-boot
```

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
║  7) Kill porta 8080                     ║
║  0) Sair                                ║
╚══════════════════════════════════════════╝
```

Também aceita argumentos diretos:
```bash
./run.sh dev      # Inicia com profile dev (H2)
./run.sh docker   # Docker Compose build + up
./run.sh stop     # Para Docker Compose
./run.sh tests    # Roda testes
./run.sh kill     # Mata processo na porta 8080
```

> **Nota:** Requer macOS/Linux ou WSL no Windows. Detecta automaticamente Java 17 e Docker (Colima ou Docker Desktop).

### Pré-requisitos
- Java 17+
- Maven 3.8+
- Docker e Docker Compose

### Com Docker Compose
```bash
docker-compose up
```

### Local (dev)
```bash
mvn spring-boot:run
```

### Testes
```bash
mvn clean verify
```

---

## 📚 Documentação

- [Backlog Completo](docs/planejamento/BACKLOG.md)
- [Setup GitHub Projects](docs/github/GITHUB_PROJECTS_SETUP.md)
- [Opções de CI/CD](docs/ci-cd/CI_CD_OPTIONS.md)

---

## 👥 Autor

**Jefferson** - [GitHub](https://github.com/jeffesa)

---

## 📝 Licença

Projeto desenvolvido para fins educacionais - FIAP Tech Challenge Fase 2.


<!-- ... -->
