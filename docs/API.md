# Documentação da API - Gestão de Restaurantes

Base URL Local: `http://localhost:8080`  
Base URL Produção: `https://12adjt-fase-2.onrender.com`  
Documentação interativa (Swagger): `http://localhost:8080/swagger-ui.html`

> ⚠️ O primeiro acesso ao Render.com pode demorar ~50 segundos (instância gratuita em modo de espera).

---

## Códigos de Status HTTP

| Código | Descrição | Quando |
|--------|-----------|--------|
| 200 | OK | Sucesso em GET/PUT/PATCH |
| 201 | Created | Recurso criado com sucesso |
| 204 | No Content | Recurso deletado com sucesso |
| 400 | Bad Request | Dados inválidos (validação) |
| 404 | Not Found | Recurso não encontrado |
| 422 | Unprocessable Entity | Violação de regra de negócio |

Todas as respostas de erro seguem o padrão **ProblemDetail (RFC 7807)**:

```json
{
  "type": "https://api.fiap.com/errors/business",
  "title": "Erro de negócio",
  "status": 422,
  "detail": "Tipo de usuário já existe com nome: CUSTOMER",
  "instance": "/api/v1/user-types"
}
```

---

## DTOs

### UserType

#### UserTypeRequest — Criação/Atualização

| Campo | Tipo | Obrigatório | Regras |
|-------|------|:-----------:|--------|
| `name` | string | sim | Não pode ser vazio, único |

#### UserTypeResponse — Retorno

| Campo | Tipo |
|-------|------|
| `id` | UUID |
| `name` | string |

---

### User

#### UserRequest — Criação

| Campo | Tipo | Obrigatório | Regras |
|-------|------|:-----------:|--------|
| `name` | string | sim | 2–100 caracteres |
| `email` | string | sim | Formato email válido, único |
| `login` | string | sim | 3–50 caracteres, único |
| `password` | string | sim | Mín. 8 chars, 1 maiúscula, 1 minúscula, 1 número |
| `address` | string | sim | Máx. 255 caracteres |
| `userTypeId` | UUID | sim | ID de um tipo existente |

#### UserUpdateRequest — Atualização

Mesmo que UserRequest, porém **sem o campo `password`**.

#### ChangePasswordRequest — Troca de senha

| Campo | Tipo | Obrigatório |
|-------|------|:-----------:|
| `currentPassword` | string | sim |
| `newPassword` | string | sim |

#### LoginRequest — Login

| Campo | Tipo | Obrigatório |
|-------|------|:-----------:|
| `login` | string | sim |
| `password` | string | sim |

#### UserResponse — Retorno

| Campo | Tipo |
|-------|------|
| `id` | UUID |
| `name` | string |
| `email` | string |
| `login` | string |
| `address` | string |
| `userType` | UserTypeResponse |

---

### Restaurant

#### RestaurantRequest — Criação

| Campo | Tipo | Obrigatório | Regras |
|-------|------|:-----------:|--------|
| `name` | string | sim | 2–100 caracteres |
| `address` | string | não | Endereço do restaurante |
| `cuisineType` | string | não | Tipo de cozinha |
| `openingHours` | datetime | não | ISO 8601 (ex: 2024-01-01T11:00:00) |
| `closingTime` | datetime | não | ISO 8601 |
| `ownerId` | UUID | sim | Deve ser RESTAURANT_OWNER |

#### RestaurantUpdateRequest — Atualização

Todos os campos opcionais (atualização parcial).

#### RestaurantResponse — Retorno

| Campo | Tipo |
|-------|------|
| `id` | UUID |
| `name` | string |
| `address` | string |
| `cuisineType` | string |
| `openingHours` | datetime |
| `closingTime` | datetime |
| `ownerId` | UUID |

---

### MenuItem

#### MenuItemRequest — Criação

| Campo | Tipo | Obrigatório | Regras |
|-------|------|:-----------:|--------|
| `name` | string | sim | Não pode ser vazio |
| `description` | string | não | Descrição do item |
| `price` | decimal | sim | Maior que zero |
| `dineInOnly` | boolean | sim | Apenas para consumo local |
| `photoPath` | string | não | Caminho da foto |

#### MenuItemResponse — Retorno

| Campo | Tipo |
|-------|------|
| `id` | UUID |
| `name` | string |
| `description` | string |
| `price` | decimal |
| `dineInOnly` | boolean |
| `photoPath` | string |
| `restaurantId` | UUID |

---

## Endpoints

### POST /api/v1/user-types — Criar tipo de usuário

```json
// Request
{ "name": "CUSTOMER" }

// Response 201
{ "id": "uuid", "name": "CUSTOMER" }

// Response 422 (nome duplicado)
{ "type": "https://api.fiap.com/errors/business", "title": "Erro de negócio", "status": 422, "detail": "Tipo de usuário já existe com nome: CUSTOMER" }
```

### GET /api/v1/user-types — Listar tipos

```json
// Response 200
[
  { "id": "uuid-1", "name": "CUSTOMER" },
  { "id": "uuid-2", "name": "RESTAURANT_OWNER" }
]
```

### GET /api/v1/user-types/{id} — Buscar por ID

```json
// Response 200
{ "id": "uuid", "name": "CUSTOMER" }

// Response 404
{ "type": "https://api.fiap.com/errors/not-found", "status": 404, "detail": "Tipo de usuário não encontrado" }
```

### PUT /api/v1/user-types/{id} — Atualizar

```json
// Request
{ "name": "ADMIN" }

// Response 200
{ "id": "uuid", "name": "ADMIN" }
```

### DELETE /api/v1/user-types/{id} — Remover

```
Response 204 (sem body)
Response 404 (não encontrado)
```

---

### POST /api/v1/users — Criar usuário

```json
// Request
{
  "name": "João Silva",
  "email": "joao@email.com",
  "login": "joaosilva",
  "password": "Senha123",
  "address": "Rua das Flores, 123",
  "userTypeId": "uuid-do-tipo"
}

// Response 201
{
  "id": "uuid",
  "name": "João Silva",
  "email": "joao@email.com",
  "login": "joaosilva",
  "address": "Rua das Flores, 123",
  "userType": { "id": "uuid-do-tipo", "name": "CUSTOMER" }
}

// Response 422 (email duplicado)
{ "detail": "Email já cadastrado: joao@email.com" }
```

### GET /api/v1/users — Listar usuários

```
GET /api/v1/users           → lista todos
GET /api/v1/users?name=João → filtra por nome
```

### POST /api/v1/users/login — Login

```json
// Request
{ "login": "joaosilva", "password": "Senha123" }

// Response 200
{ "id": "uuid", "name": "João Silva", "login": "joaosilva", ... }

// Response 422
{ "detail": "Credenciais inválidas" }
```

### PATCH /api/v1/users/{id}/password — Trocar senha

```json
// Request
{ "currentPassword": "Senha123", "newPassword": "NovaSenha456" }

// Response 200 (sucesso)
// Response 400 (senha atual incorreta)
// Response 404 (usuário não encontrado)
```

---

### POST /api/v1/restaurants — Criar restaurante

```json
// Request
{
  "name": "Pizzaria do João",
  "address": "Rua das Flores, 123",
  "cuisineType": "ITALIANA",
  "openingHours": "2024-01-01T11:00:00",
  "closingTime": "2024-01-01T23:00:00",
  "ownerId": "uuid-do-owner"
}

// Response 201
{
  "id": "uuid",
  "name": "Pizzaria do João",
  "address": "Rua das Flores, 123",
  "cuisineType": "ITALIANA",
  "openingHours": "2024-01-01T11:00:00",
  "closingTime": "2024-01-01T23:00:00",
  "ownerId": "uuid-do-owner"
}

// Response 422 (owner não é RESTAURANT_OWNER)
{ "detail": "Usuário não tem permissão para ser proprietário (deve ser RESTAURANT_OWNER)" }
```

### GET /api/v1/restaurants — Listar restaurantes

```
GET /api/v1/restaurants              → lista todos
GET /api/v1/restaurants?ownerId=uuid → filtra por dono
```

### PUT /api/v1/restaurants/{id} — Atualizar

```json
// Request (todos os campos opcionais)
{
  "name": "Novo Nome",
  "address": "Novo Endereço",
  "cuisineType": "BRASILEIRA",
  "openingHours": "2024-01-01T10:00:00",
  "closingTime": "2024-01-02T00:00:00",
  "ownerId": "uuid-novo-owner"
}
```

---

### POST /api/v1/restaurants/{restaurantId}/menu-items — Criar item

```json
// Request
{
  "name": "Pizza Margherita",
  "description": "Molho, mussarela e manjericão",
  "price": 39.90,
  "dineInOnly": false,
  "photoPath": "/img/pizza.jpg"
}

// Response 201
{
  "id": "uuid",
  "name": "Pizza Margherita",
  "description": "Molho, mussarela e manjericão",
  "price": 39.90,
  "dineInOnly": false,
  "photoPath": "/img/pizza.jpg",
  "restaurantId": "uuid-do-restaurante"
}

// Response 404 (restaurante não existe)
// Response 400 (preço <= 0)
```

### GET /api/v1/restaurants/{restaurantId}/menu-items — Listar itens

```json
// Response 200
[
  { "id": "uuid", "name": "Pizza Margherita", "price": 39.90, ... }
]
```

### PUT /api/v1/menu-items/{id} — Atualizar item

```json
// Request
{
  "name": "Pizza Calabresa",
  "description": "Calabresa e cebola",
  "price": 42.90,
  "dineInOnly": true,
  "photoPath": "/img/calabresa.jpg"
}
```

### DELETE /api/v1/menu-items/{id} — Remover item

```
Response 204 (sem body)
Response 404 (não encontrado)
```
