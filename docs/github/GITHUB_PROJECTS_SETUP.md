# 🚀 Guia: Configurar GitHub Projects (Kanban Board) - Fase 2

## 📋 Passo 1: Criar o Repositório

1. Acesse [GitHub](https://github.com)
2. Clique em **"New repository"**
3. Nome: `12ADJT-fase-2` (ou o nome que preferir)
4. Descrição: `Sistema de Gestão de Restaurantes - Tech Challenge FIAP Fase 2`
5. Escolha **Public**
6. Marque **"Add a README file"**
7. Clique em **"Create repository"**

---

## 📊 Passo 2: Criar o GitHub Project

1. No seu repositório, clique na aba **"Projects"**
2. Clique em **"Link a project"** → **"New project"**
3. Escolha o template **"Board"** (Kanban)
4. Nome do projeto: `Tech Challenge Fase 2 - Sprint Board`
5. Clique em **"Create"**

---

## 🎯 Passo 3: Configurar as Colunas do Kanban

### Colunas Recomendadas:

1. **📦 Backlog** (renomear "Todo")
2. **📝 To Do** (adicionar coluna)
3. **⚙️ In Progress** (manter)
4. **🔍 Review** (adicionar coluna)
5. **✅ Done** (manter)

---

## 🏷️ Passo 4: Criar Labels no Repositório

1. No repositório, vá em **"Issues"** → **"Labels"**
2. Clique em **"New label"** e crie as seguintes:

### Labels de Prioridade:
| Label | Cor | Hex |
|-------|-----|-----|
| `priority: high` | Vermelho | `#d73a4a` |
| `priority: medium` | Amarelo | `#fbca04` |
| `priority: low` | Verde | `#0e8a16` |

### Labels de Épico:
| Label | Cor | Hex |
|-------|-----|-----|
| `épico: setup` | Azul | `#1d76db` |
| `épico: user-type` | Roxo | `#5319e7` |
| `épico: user` | Azul escuro | `#0052cc` |
| `épico: restaurant` | Teal | `#006b75` |
| `épico: menu-item` | Azul claro | `#c5def5` |
| `épico: documentação` | Verde claro | `#c2e0c6` |
| `épico: extras` | Rosa | `#e99695` |

### Labels de Estimativa:
| Label | Cor | Hex |
|-------|-----|-----|
| `pontos: 1` | Cinza | `#ededed` |
| `pontos: 2` | Cinza | `#ededed` |
| `pontos: 3` | Cinza | `#ededed` |
| `pontos: 4` | Cinza | `#ededed` |
| `pontos: 5` | Cinza | `#ededed` |

### Labels de Tipo:
| Label | Cor | Hex |
|-------|-----|-----|
| `type: feature` | Verde | `#0e8a16` |
| `type: test` | Amarelo | `#fbca04` |
| `type: docs` | Azul | `#0075ca` |
| `type: infra` | Laranja | `#d93f0b` |

---

## 📝 Passo 5: Criar Milestones (Sprints)

1. Vá em **"Issues"** → **"Milestones"**
2. Clique em **"New milestone"**
3. Crie 6 milestones:

### Sprint 1 - Fundação
- **Título:** Sprint 1 - Fundação
- **Descrição:** Setup do projeto, Clean Architecture, Docker, Swagger, GlobalExceptionHandler (Tasks 001-005)

### Sprint 2 - Tipo de Usuário
- **Título:** Sprint 2 - Tipo de Usuário
- **Descrição:** CRUD completo de UserType: entidade, gateway, use cases, persistência, controller, testes (Tasks 006-012)

### Sprint 3 - Usuário
- **Título:** Sprint 3 - Usuário
- **Descrição:** Refatoração do User para Clean Architecture com associação a UserType (Tasks 013-016)

### Sprint 4 - Restaurante
- **Título:** Sprint 4 - Restaurante
- **Descrição:** CRUD completo de Restaurant: entidade, gateway, use cases, persistência, controller, testes (Tasks 017-022)

### Sprint 5 - Cardápio
- **Título:** Sprint 5 - Cardápio
- **Descrição:** CRUD completo de MenuItem: entidade, gateway, use cases, persistência, controller, testes (Tasks 023-028)

### Sprint 6 - Finalização
- **Título:** Sprint 6 - Finalização
- **Descrição:** README, API docs, Postman, cobertura, CI/CD, entrega, vídeo + extras opcionais (Tasks 029-040)

---

## 📋 Passo 6: Criar as Issues

Use os templates do arquivo `GITHUB_ISSUES_TEMPLATES.md` para criar as 40 issues.

### Como criar cada issue:

1. Vá em **"Issues"** → **"New issue"**
2. Copie o template correspondente
3. Cole no campo de descrição
4. Adicione as **Labels** apropriadas
5. Selecione o **Milestone** (Sprint)
6. Clique em **"Submit new issue"**
7. No lado direito, em **"Projects"**, adicione ao seu projeto

---

## 🔄 Passo 7: Workflow

### Ao começar uma task:
1. Mova de "To Do" para "In Progress"
2. Crie branch: `feature/task-XXX-descricao`
3. Trabalhe na task

### Ao concluir uma task:
1. Faça commit: `feat: implementa TASK-XXX - descrição #N`
2. Push para a branch
3. Crie PR para develop
4. Mova issue para "Review"
5. Após merge, mova para "Done"

### Integração com Commits:
- `git commit -m "feat: TASK-001 setup projeto spring boot #1"`
- O `#1` cria link automático com a issue

---

## ✅ Checklist de Configuração

- [ ] Repositório criado (público)
- [ ] GitHub Project criado (Board/Kanban)
- [ ] Colunas configuradas (Backlog, To Do, In Progress, Review, Done)
- [ ] Labels criadas (prioridade, épico, pontos, tipo)
- [ ] Milestones criados (6 sprints)
- [ ] 40 Issues criadas com templates
- [ ] Issues adicionadas ao Project
- [ ] Primeiras tasks (Sprint 1) movidas para "To Do"

---

**Pronto! Board configurado para a Fase 2! 🎉**
