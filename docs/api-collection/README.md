# 📬 API Collection - FIAP Fase 2

Collection completa de testes para a API de Gestão de Restaurantes.

## Arquivo

- `FIAP Fase 2 - Gestao de Restaurantes.postman_collection.json` — Collection Postman v2.1 com 36+ requests

## Como importar

### Postman
1. Abra o Postman
2. Clique em **Import**
3. Arraste o arquivo JSON ou clique em **Upload Files**

### Bruno
1. Abra o Bruno
2. Clique em **Import Collection**
3. Selecione **Postman Collection**
4. Escolha o arquivo JSON

## Variáveis da Collection

| Variável | Valor padrão | Descrição |
|----------|--------------|-----------|
| `localUrl` | `http://localhost:8080` | URL do ambiente local |
| `prodUrl` | `https://12adjt-fase-2.onrender.com` | URL do ambiente de produção |
| `userTypeId` | (auto) | Preenchido ao criar UserType |
| `userId` | (auto) | Preenchido ao criar User |
| `restaurantId` | (manual) | ID do restaurante (aguarda TASK-020) |
| `menuItemId` | (auto) | Preenchido ao criar MenuItem |

## Estrutura da Collection

```
📁 local
├── 📁 Health Check (1 request)
│   └── Health ✅ 200
├── 📁 Tipos de Usuário (12 requests)
│   ├── Criar CUSTOMER ✅ 201
│   ├── Criar RESTAURANT_OWNER ✅ 201
│   ├── Criar - Nome duplicado ❌ 422
│   ├── Criar - Nome vazio ❌ 400
│   ├── Listar ✅ 200
│   ├── Buscar por ID ✅ 200
│   ├── Buscar - ID inexistente ❌ 404
│   ├── Atualizar ✅ 200
│   ├── Atualizar - Nome duplicado ❌ 422
│   ├── Atualizar - ID inexistente ❌ 404
│   ├── Deletar ✅ 204
│   └── Deletar - ID inexistente ❌ 404
├── 📁 Usuários (10 requests)
│   ├── Criar (CUSTOMER) ✅ 201
│   ├── Criar - Email duplicado ❌ 422
│   ├── Criar - Dados inválidos ❌ 400
│   ├── Listar ✅ 200
│   ├── Buscar por Nome ✅ 200
│   ├── Buscar por ID ✅ 200
│   ├── Buscar - ID inexistente ❌ 404
│   ├── Atualizar ✅ 200
│   ├── Deletar ✅ 204
│   └── Deletar - ID inexistente ❌ 404
├── 📁 Autenticação (5 requests)
│   ├── Login ✅ 200
│   ├── Login - Credenciais inválidas ❌ 422
│   ├── Trocar Senha ✅ 200
│   ├── Trocar Senha - Atual incorreta ❌ 400
│   └── Trocar Senha - ID inexistente ❌ 404
└── 📁 Cardápio - MenuItem (9 requests)
    ├── Criar Item ✅ 201
    ├── Criar - Restaurante inexistente ❌ 404
    ├── Criar - Preço zero ❌ 400
    ├── Listar Itens do Restaurante ✅ 200
    ├── Buscar por ID ✅ 200
    ├── Buscar - ID inexistente ❌ 404
    ├── Atualizar ✅ 200
    ├── Deletar ✅ 204
    └── Deletar - ID inexistente ❌ 404

📁 prod (mesma estrutura simplificada)
```

## Endpoints cobertos

| Método | Endpoint | Cenários |
|--------|----------|----------|
| GET | /actuator/health | 200 |
| POST | /api/v1/user-types | 201, 400, 422 |
| GET | /api/v1/user-types | 200 |
| GET | /api/v1/user-types/{id} | 200, 404 |
| PUT | /api/v1/user-types/{id} | 200, 404, 422 |
| DELETE | /api/v1/user-types/{id} | 204, 404 |
| POST | /api/v1/users | 201, 400, 422 |
| GET | /api/v1/users | 200 |
| GET | /api/v1/users?name={nome} | 200 |
| GET | /api/v1/users/{id} | 200, 404 |
| PUT | /api/v1/users/{id} | 200 |
| DELETE | /api/v1/users/{id} | 204, 404 |
| POST | /api/v1/users/login | 200, 422 |
| PATCH | /api/v1/users/{id}/password | 200, 400, 404 |
| POST | /api/v1/restaurants/{id}/menu-items | 201, 400, 404 |
| GET | /api/v1/restaurants/{id}/menu-items | 200 |
| GET | /api/v1/menu-items/{id} | 200, 404 |
| PUT | /api/v1/menu-items/{id} | 200 |
| DELETE | /api/v1/menu-items/{id} | 204, 404 |

> **Nota:** Endpoints de Restaurant (CRUD) serão adicionados após TASK-020.

## Ordem de execução recomendada

1. **Health Check** — verificar se API está rodando
2. **Criar CUSTOMER** — preenche `userTypeId` automaticamente
3. **Criar Usuário** — preenche `userId` automaticamente
4. **Testes de sucesso** — listar, buscar, atualizar
5. **Testes de autenticação** — login, trocar senha
6. **Testes de erro** — validações, conflitos, not found
7. **MenuItem** — requer `restaurantId` (criar via Restaurant controller quando disponível)
8. **Deletar** — limpeza

## Scripts automatizados

Os requests de criação possuem scripts post-response que salvam IDs automaticamente nas variáveis da collection. Não é necessário copiar/colar IDs manualmente.

## Testes automatizados (Assertions)

Cada request possui assertions `pm.test()` que validam:
- **Status code esperado** — baseado no título do request (✅ 201, ❌ 400, ❌ 404, ❌ 422)
- **Validações de body** — verifica campos como `id`, `name`, arrays, campo `detail` em erros

As pastas de Usuários, Autenticação e MenuItem incluem requests de "setup" que criam os dados necessários (UserType, User, Restaurant) antes dos testes. Assim a collection pode ser executada do zero em sequência.

## Executar via Newman (CLI)

Newman permite executar a collection inteira via terminal e gerar um relatório HTML profissional com pass/fail de cada assertion.

### Pré-requisitos

```bash
# Node.js (>= 16)
node --version

# Instalar Newman e o reporter HTML
npm install -g newman newman-reporter-htmlextra
```

### Executar

```bash
# Via run.sh (opção 7 do menu)
./run.sh

# Ou diretamente
newman run "docs/api-collection/FIAP Fase 2 - Gestao de Restaurantes.postman_collection.json" \
  --folder local \
  -r htmlextra \
  --reporter-htmlextra-export docs/api-collection/evidencia-testes-api.html
```

### Abrir relatório

```bash
open docs/api-collection/evidencia-testes-api.html
```

### Requisitos para execução

| Requisito | Descrição |
|-----------|-----------|
| Aplicação rodando | `localhost:8080` (via `./run.sh` opção 1, 2 ou 4) |
| Node.js | >= 16 |
| newman | `npm install -g newman` |
| newman-reporter-htmlextra | `npm install -g newman-reporter-htmlextra` |

### O que o relatório mostra

- **Summary** — total de requests, assertions, tempo de execução
- **Passed/Failed** — cada assertion individual com status verde/vermelho
- **Request details** — headers, body, response para debug
- **Tempo por request** — identificar endpoints lentos

> **Nota:** Os requests de MenuItem dependem do endpoint `POST /api/v1/restaurants` (TASK-020 do outro dev). Até que esteja implementado, os testes de MenuItem falharão em cascata.

## Executar via Shell Script (sem dependências externas)

Alternativa ao Newman para ambientes corporativos com restrições de instalação. Usa apenas `curl` e `jq`. Está integrado no `run.sh` (opção 8).

### Pré-requisitos

| Requisito | Descrição |
|-----------|-----------|
| Aplicação rodando | `localhost:8080` |
| curl | Já vem instalado no macOS/Linux |
| jq | `brew install jq` (mac) ou `apt install jq` (linux) |

### Executar

```bash
# Via menu interativo (opção 8)
./run.sh

# Via argumento direto
./run.sh test-api
```

### O que faz

- Executa todos os endpoints em sequência (UserType, User, Auth, MenuItem)
- Valida status code esperado vs recebido para cada request
- Salva IDs automaticamente entre requests (simula variáveis do Postman)
- Detecta se endpoint de Restaurant está disponível — se não, pula testes de MenuItem
- Mostra resumo final com total/passou/falhou
- Exit code 1 se algum teste falhar (útil para CI/CD)

### Diferenças entre Newman e Shell Script

| Característica | Newman | Shell Script |
|----------------|--------|--------------|
| Dependências | Node.js, newman, htmlextra | curl, jq |
| Relatório HTML | ✅ Sim | ❌ Não (apenas terminal) |
| Total de testes | 48 requests (81 assertions) | ~28-37 testes |
| Instalação global | Necessária | Não necessária |
| Ambiente corporativo | Pode ser bloqueado | Funciona em qualquer máquina |
