# 🚀 Guia: Deploy no Render.com - Fase 2

> ⚠️ **NÃO OBRIGATÓRIO** — O PDF da Fase 2 não exige deploy em produção.
> Este guia é opcional e facilita a avaliação pelos professores.

---

## 🎯 Por que Render.com?

✅ **100% gratuito** para projetos pequenos  
✅ **PostgreSQL incluído**  
✅ **Deploy automático** via GitHub  
✅ **HTTPS automático**  
✅ **Suporta Docker** nativamente  

---

## 📋 Pré-requisitos

- [ ] Repositório no GitHub com código completo
- [ ] Dockerfile criado e testado (TASK-003)
- [ ] Aplicação funcionando localmente via Docker Compose
- [ ] Testes passando

---

## 🗄️ Passo 1: Criar Banco de Dados PostgreSQL

1. Acesse [render.com](https://render.com) e faça login com GitHub
2. Clique em **"New +"** → **"PostgreSQL"**
3. Configure:
   - **Name:** `fase2-db`
   - **Database:** `fase2db`
   - **Region:** `Oregon (US West)`
   - **Plan:** **Free** ✅
4. Clique em **"Create Database"**
5. Copie a **Internal Database URL**

---

## 🐳 Passo 2: Criar Web Service

1. Clique em **"New +"** → **"Web Service"**
2. Conecte o repositório da Fase 2
3. Configure:
   - **Name:** `12adjt-fase-2`
   - **Region:** `Oregon (US West)`
   - **Branch:** `main`
   - **Runtime:** `Docker`
   - **Plan:** **Free** ✅

### Variáveis de Ambiente:

| Key | Value |
|-----|-------|
| `SPRING_PROFILES_ACTIVE` | `prod` |
| `SPRING_DATASOURCE_URL` | `jdbc:postgresql://HOST:5432/fase2db` |
| `SPRING_DATASOURCE_USERNAME` | `(do PostgreSQL criado)` |
| `SPRING_DATASOURCE_PASSWORD` | `(do PostgreSQL criado)` |
| `SERVER_PORT` | `8080` |

> ⚠️ Use a **Internal Database URL** e converta para formato JDBC:
> `postgresql://user:pass@host/db` → `jdbc:postgresql://host/db`

4. Clique em **"Create Web Service"**
5. Aguarde o build (5-10 minutos)

---

## ✅ Passo 3: Verificar Deploy

Após o build completar, teste:

```bash
# Health Check
curl https://one2adjt-fase-2.onrender.com/actuator/health

# Swagger UI
# Abra no navegador: https://one2adjt-fase-2.onrender.com/swagger-ui.html

# Listar tipos de usuário
curl https://one2adjt-fase-2.onrender.com/api/v1/user-types
```

---

## 🔄 Passo 4: Deploy Automático

O Render faz deploy automático a cada push na branch `main`.

Para integrar com GitHub Actions, adicione o secret `RENDER_DEPLOY_HOOK_URL`:
1. Render → Web Service → Settings → Deploy Hook
2. Copie a URL
3. GitHub → Settings → Secrets → `RENDER_DEPLOY_HOOK_URL`

---

## 🔧 Troubleshooting

### Aplicação demora para responder
- Normal no plano gratuito — instância "dorme" após 15 min de inatividade
- Primeiro request demora ~50s para "acordar"

### Erro de conexão com banco
- Verifique se usou a **Internal Database URL** (não a External)
- Confirme o formato JDBC: `jdbc:postgresql://host:5432/db`

### Build falha
- Verifique os logs no Render dashboard
- Teste o build local: `docker build -t test .`

### Erro 502 Bad Gateway
- Aguarde o deploy completar
- Verifique se `SERVER_PORT=8080` está configurado

---

## 💰 Limites do Plano Gratuito

| Recurso | Limite |
|---------|--------|
| Web Service | 750 horas/mês, 512 MB RAM |
| PostgreSQL | 1 GB storage, expira em 90 dias |
| Build time | 500 min/mês |
| Inatividade | Dorme após 15 min sem requests |

---

## 🎨 Badge para README

```markdown
[![Deploy on Render](https://img.shields.io/badge/Deploy-Render-46E3B7?style=flat&logo=render&logoColor=white)](https://one2adjt-fase-2.onrender.com)
```

---

## ✅ Checklist de Deploy

- [ ] Conta criada no Render.com
- [ ] PostgreSQL provisionado
- [ ] Web Service criado com Docker
- [ ] Variáveis de ambiente configuradas
- [ ] Build executado com sucesso
- [ ] Aplicação acessível via URL pública
- [ ] Health check respondendo
- [ ] Endpoints testados em produção
- [ ] URL documentada no README
- [ ] Deploy Hook configurado no GitHub Actions (opcional)

---

*Este deploy é **NÃO OBRIGATÓRIO** para a entrega da Fase 2.*
