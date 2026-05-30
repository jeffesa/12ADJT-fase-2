# 🚀 Sistema de Gestão de Restaurantes

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

Clean Architecture com separação em camadas:

```
src/main/java/com/fiap/fase2/
├── domain/          # Entidades puras e interfaces (gateway)
├── application/     # Casos de uso (lógica de aplicação)
└── infra/           # Controllers, JPA, configurações
```

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
