# 📚 Documentação - Tech Challenge Fase 2

Índice geral de toda a documentação do projeto.

---

## 🗺️ Quick Start — Ordem de leitura sugerida

1. [README principal](../README.md) — Visão geral, como executar, endpoints
2. [API.md](API.md) — Documentação detalhada dos endpoints (DTOs, exemplos)
3. [BACKLOG.md](planejamento/BACKLOG.md) — Todas as tasks e critérios de aceitação
4. [Collection Postman](api-collection/README.md) — Como importar e rodar os testes

---

## 📁 Estrutura

```
docs/
├── README.md                          ← (este arquivo)
├── API.md                             ← Documentação detalhada da API
├── api-collection/
│   ├── README.md                      ← Como importar/executar (Newman, Bruno, curl)
│   └── *.postman_collection.json      ← Collection com 56 requests + assertions
├── ci-cd/
│   ├── CI_CD_OPTIONS.md               ← Comparação de opções de CI/CD
│   └── CI_CD_SETUP.md                 ← Guia de configuração do pipeline
├── deploy/
│   └── RENDER_DEPLOY_GUIDE.md         ← Guia de deploy no Render.com
├── github/
│   ├── GITHUB_ISSUES_TEMPLATES.md     ← Templates de issues
│   └── GITHUB_PROJECTS_SETUP.md       ← Setup do Kanban no GitHub Projects
├── planejamento/
│   ├── BACKLOG.md                     ← Backlog completo (44 tasks, 8 épicos)
│   └── RESUMO_CONVERSA.md            ← Contexto e decisões técnicas
└── qualidade/
    └── SONARCLOUD_SETUP.md            ← Guia de configuração do SonarCloud
```

---

## 📄 Documentos

### Projeto e API

| Documento | Descrição |
|-----------|-----------|
| [README principal](../README.md) | Visão geral, tecnologias, arquitetura, como executar |
| [API.md](API.md) | DTOs, endpoints, exemplos request/response, códigos HTTP |
| [Collection Postman](api-collection/README.md) | 56 requests com assertions, Newman, curl+jq |

### Planejamento

| Documento | Descrição |
|-----------|-----------|
| [BACKLOG.md](planejamento/BACKLOG.md) | 44 tasks, 8 épicos, critérios de aceitação |
| [RESUMO_CONVERSA.md](planejamento/RESUMO_CONVERSA.md) | Contexto do projeto e decisões técnicas |

### CI/CD e Deploy

| Documento | Descrição |
|-----------|-----------|
| [CI_CD_OPTIONS.md](ci-cd/CI_CD_OPTIONS.md) | Comparação de opções de pipeline |
| [CI_CD_SETUP.md](ci-cd/CI_CD_SETUP.md) | Guia de configuração do GitHub Actions |
| [RENDER_DEPLOY_GUIDE.md](deploy/RENDER_DEPLOY_GUIDE.md) | Deploy no Render.com (passo a passo) |

### Qualidade

| Documento | Descrição |
|-----------|-----------|
| [SONARCLOUD_SETUP.md](qualidade/SONARCLOUD_SETUP.md) | Configuração do SonarCloud (token, workflow, Quality Gate) |

### GitHub

| Documento | Descrição |
|-----------|-----------|
| [GITHUB_PROJECTS_SETUP.md](github/GITHUB_PROJECTS_SETUP.md) | Setup do Kanban board |
| [GITHUB_ISSUES_TEMPLATES.md](github/GITHUB_ISSUES_TEMPLATES.md) | Templates de issues |
