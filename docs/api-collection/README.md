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
