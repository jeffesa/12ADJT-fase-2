# 📝 Resumo do Projeto - Tech Challenge Fase 2

## 🎯 Contexto

Este projeto é a continuação do Tech Challenge Fase 1 da Pós-Graduação em Arquitetura e Desenvolvimento Java (FIAP). A Fase 1 entregou um sistema de gerenciamento de usuários com API RESTful em Spring Boot (MVC clássico). A Fase 2 expande o sistema para incluir gestão de restaurantes e cardápios, migrando para Clean Architecture.

**Repositório Fase 1:** https://github.com/jeffesa/12ADJT-fase-1

---

## 🏗️ Decisões Técnicas

### Por que Clean Architecture?

O PDF da Fase 2 exige explicitamente: "Organizar o código em camadas (Domain, Application, Infrastructure etc.) para garantir separação de responsabilidades e escalabilidade."

O professor disponibilizou uma estrutura de referência:
```
src/main/java/com/fiap/fase2/
├── application/useCase/       # Casos de uso (lógica de aplicação)
├── domain/
│   ├── entity/                # Entidades puras (POJO, sem JPA)
│   └── gateway/               # Interfaces (ports) para acesso a dados
├── infra/
│   ├── adapter/web/           # Controllers REST (adapters de entrada)
│   ├── config/                # Configurações (OpenAPI, Security, etc.)
│   └── persistence/           # Implementações JPA (adapters de saída)
```

**Princípio:** A dependência flui de fora para dentro (infra → application → domain). O domain não conhece frameworks.

### Por que UUID em vez de Long (auto-increment)?

Na Fase 1, usamos `Long` com `@GeneratedValue(strategy = GenerationType.IDENTITY)`. Na Fase 2, migramos para `UUID` porque:
- Entidades de domínio não devem depender do banco para gerar IDs
- UUID pode ser gerado na camada de aplicação (independente de infraestrutura)
- Melhor para sistemas distribuídos e Clean Architecture

### Por que UserType deixou de ser enum?

Na Fase 1, `UserType` era um enum (`CUSTOMER`, `RESTAURANT_OWNER`). Na Fase 2, o PDF exige: "Implementar uma estrutura para distinguir entre usuários, incluindo um CRUD para gerenciar tipos de usuário e associá-los a usuários existentes."

Isso significa que UserType precisa ser uma **entidade com CRUD próprio**, não um enum fixo no código.

### Por que manter Spring Security apenas para BCrypt?

Mesma abordagem da Fase 1: Spring Security configurado com `permitAll()` em todos os endpoints, usando apenas o `PasswordEncoder` (BCrypt) para hash de senhas. Não há autenticação/autorização real — o foco é na gestão de dados.

### Por que manter ProblemDetail (RFC 7807)?

Padrão já implementado na Fase 1 e exigido como boa prática. Respostas de erro padronizadas com `type`, `title`, `status`, `detail`, `instance`.

---

## 🔄 Diferenças em relação à Fase 1

| Aspecto | Fase 1 | Fase 2 |
|---------|--------|--------|
| Arquitetura | MVC (controller → service → repository) | Clean Architecture (domain → application → infra) |
| UserType | Enum (CUSTOMER, RESTAURANT_OWNER) | Entidade com CRUD próprio |
| ID das entidades | Long (auto-increment) | UUID (gerado na aplicação) |
| Entidades | User apenas | User, UserType, Restaurant, MenuItem |
| Testes | Opcional (~70%) | Obrigatório (≥ 80% com JaCoCo check) |
| Entidade JPA | Direta no model/ | Separada (domain/entity puro + infra/persistence JPA) |
| Vídeo | Não exigido | Obrigatório (~5 min) |

---

## 📦 O que foi reutilizado da Fase 1

- **Lógica de negócio de User:** validações de email/login únicos, BCrypt, troca de senha
- **GlobalExceptionHandler:** ProblemDetail com URIs tipadas
- **Validadores customizados:** `@ValidPassword`, `@SafeInput`
- **Padrão de DTOs:** records com Bean Validation + Swagger annotations
- **Dockerfile:** multi-stage build (Maven → JRE)
- **docker-compose.yml:** PostgreSQL + app com health checks
- **Profiles:** dev (H2/PostgreSQL), test (H2), prod (env vars)
- **Fluxo Git:** main → develop → feature branches
- **GitHub Actions:** CI/CD com build + testes
- **Documentação:** estrutura de docs/, Collection Postman, README detalhado

---

## 📋 Entregáveis Obrigatórios (PDF Fase 2)

1. ✅ Funcionalidade: CRUD UserType, Restaurant, MenuItem
2. ✅ Qualidade do código: boas práticas Spring Boot
3. ✅ Documentação: arquitetura, endpoints, instruções
4. ✅ Collections Postman: testar todos os endpoints
5. ✅ Docker Compose: app Java + banco de dados
6. ✅ Repositório aberto: GitHub público
7. ✅ Clean Architecture: Domain, Application, Infrastructure
8. ✅ Cobertura de teste: unitários ≥ 80% + integração
9. ✅ Vídeo: ~5 minutos demonstrando funcionalidades

---

## 🛠️ Stack Tecnológica

- **Java 17** (LTS)
- **Spring Boot 3.2.3**
- **Spring Data JPA** (PostgreSQL + H2)
- **Spring Validation** (Bean Validation)
- **Spring Security** (apenas BCrypt)
- **SpringDoc OpenAPI 2.3.0** (Swagger UI)
- **Spring Actuator** (health check)
- **PostgreSQL 15** (produção/dev Docker)
- **H2** (testes e dev local)
- **Docker + Docker Compose**
- **JUnit 5 + Mockito** (testes)
- **JaCoCo** (cobertura ≥ 80%)
- **Maven** (build)
- **GitHub Actions** (CI/CD)

---

## 📅 Planejamento

O projeto está organizado em **40 tasks** distribuídas em **7 épicos** e **6 sprints**.

Detalhes completos em: [BACKLOG.md](BACKLOG.md)

---

*Última atualização: Maio/2026*
