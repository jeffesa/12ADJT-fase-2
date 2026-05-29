# 🎯 Escolha Sua Opção de CI/CD - Fase 2

Criamos **3 opções** de complexidade crescente. Escolha a que melhor se adapta ao projeto!

---

## 📊 Comparação Rápida

| Opção | Tempo Config | Complexidade | SonarCloud | Deploy Auto | Recomendado Para |
|-------|--------------|--------------|------------|-------------|------------------|
| **1. Minimalista** | 5 min | 🟢 Baixa | ❌ Não | ❌ Não | Entrega rápida |
| **2. Simplificada** | 10 min | 🟡 Média | ⚠️ Opcional | ⚠️ Opcional | **Recomendado** ⭐ |
| **3. Completa** | 20 min | 🔴 Alta | ✅ Obrigatório | ✅ Sim | Máxima qualidade |

---

## 🎯 Opção 1: MINIMALISTA (5 minutos)

### O que faz:
- ✅ Build e testes em push/PR
- ✅ Verificação de cobertura (JaCoCo)
- ❌ Sem SonarCloud
- ❌ Sem deploy automático

### Workflow:
`.github/workflows/ci.yml`

```yaml
name: CI

on:
  push:
    branches: [main, develop]
  pull_request:
    branches: [main, develop]

jobs:
  build:
    runs-on: ubuntu-latest
    steps:
      - uses: actions/checkout@v4
      - uses: actions/setup-java@v4
        with:
          java-version: '17'
          distribution: 'temurin'
          cache: maven
      - run: mvn clean verify
```

### Prós:
- ✅ Super rápido de configurar
- ✅ Garante que build e testes passam
- ✅ JaCoCo verifica cobertura ≥ 80%

### Contras:
- ❌ Não analisa qualidade do código
- ❌ Sem deploy automático

---

## ⭐ Opção 2: SIMPLIFICADA (10 minutos) - RECOMENDADA

### O que faz:
- ✅ Build e testes em push/PR
- ✅ Verificação de cobertura (JaCoCo)
- ⚠️ SonarCloud opcional (ativa com label `sonar`)
- ⚠️ Deploy no Render opcional (ativa em push na main)

### Workflow:
`.github/workflows/ci-cd.yml`

```yaml
name: CI/CD Pipeline

on:
  push:
    branches: [main, develop]
  pull_request:
    branches: [main, develop]

jobs:
  build:
    name: Build & Test
    runs-on: ubuntu-latest
    steps:
      - uses: actions/checkout@v4
      - uses: actions/setup-java@v4
        with:
          java-version: '17'
          distribution: 'temurin'
          cache: maven
      - name: Build and Test
        run: mvn clean verify

  sonarcloud:
    name: SonarCloud (Optional)
    runs-on: ubuntu-latest
    if: contains(github.event.pull_request.labels.*.name, 'sonar')
    steps:
      - uses: actions/checkout@v4
        with:
          fetch-depth: 0
      - uses: actions/setup-java@v4
        with:
          java-version: '17'
          distribution: 'temurin'
          cache: maven
      - name: Build and Analyze
        env:
          GITHUB_TOKEN: ${{ secrets.GITHUB_TOKEN }}
          SONAR_TOKEN: ${{ secrets.SONAR_TOKEN }}
        run: mvn -B verify org.sonarsource.scanner.maven:sonar-maven-plugin:sonar

  deploy:
    name: Deploy to Render
    needs: build
    runs-on: ubuntu-latest
    if: github.ref == 'refs/heads/main' && github.event_name == 'push'
    steps:
      - name: Trigger Render Deploy
        run: curl -X POST ${{ secrets.RENDER_DEPLOY_HOOK_URL }}
```

### Prós:
- ✅ Flexível — SonarCloud quando quiser
- ✅ Build e testes sempre rodam
- ✅ Deploy automático na main (se configurado)
- ✅ Não bloqueia desenvolvimento

### Contras:
- ⚠️ SonarCloud não é obrigatório (pode esquecer)
- ⚠️ Deploy depende de configurar Render

### Quando usar:
- **Tech Challenge FIAP Fase 2** ⭐

---

## 🏢 Opção 3: COMPLETA (20 minutos)

### O que faz:
- ✅ Build e testes em push/PR
- ✅ SonarCloud **obrigatório** em todos os PRs
- ✅ Quality Gate bloqueia merge se falhar
- ✅ Deploy automático no Render após merge na main

### Workflow:
`.github/workflows/ci-cd-complete.yml`

```yaml
name: CI/CD Complete

on:
  push:
    branches: [main]
  pull_request:
    branches: [main, develop]

jobs:
  build-and-analyze:
    name: Build, Test & SonarCloud
    runs-on: ubuntu-latest
    steps:
      - uses: actions/checkout@v4
        with:
          fetch-depth: 0
      - uses: actions/setup-java@v4
        with:
          java-version: '17'
          distribution: 'temurin'
          cache: maven
      - name: Cache SonarCloud
        uses: actions/cache@v3
        with:
          path: ~/.sonar/cache
          key: ${{ runner.os }}-sonar
      - name: Build, Test and Analyze
        env:
          GITHUB_TOKEN: ${{ secrets.GITHUB_TOKEN }}
          SONAR_TOKEN: ${{ secrets.SONAR_TOKEN }}
        run: mvn -B verify org.sonarsource.scanner.maven:sonar-maven-plugin:sonar

  deploy:
    name: Deploy to Render
    needs: build-and-analyze
    runs-on: ubuntu-latest
    if: github.ref == 'refs/heads/main' && github.event_name == 'push'
    steps:
      - name: Trigger Render Deploy
        run: curl -X POST ${{ secrets.RENDER_DEPLOY_HOOK_URL }}
      - name: Status
        run: echo "✅ Deploy triggered on Render.com"
```

### Configuração adicional:
1. Branch protection na main com status check obrigatório
2. SonarCloud Quality Gate como required check

### Prós:
- ✅ Qualidade de código garantida
- ✅ Pipeline profissional completo
- ✅ Não sobe código ruim para produção

### Contras:
- ❌ Mais complexo de configurar
- ❌ Pode bloquear desenvolvimento se muito rigoroso

---

## 🎯 Recomendação para Fase 2

**Opção 2: SIMPLIFICADA** ⭐

**Justificativa:**
- ✅ Atende ao requisito de CI/CD do projeto
- ✅ Rápida de configurar
- ✅ Flexível (SonarCloud quando quiser)
- ✅ Deploy opcional (não exigido no PDF)
- ✅ Mostra maturidade técnica na apresentação

---

## 📝 Secrets Necessários (GitHub)

| Secret | Obrigatório | Descrição |
|--------|-------------|-----------|
| `SONAR_TOKEN` | Não (só se usar SonarCloud) | Token do SonarCloud |
| `RENDER_DEPLOY_HOOK_URL` | Não (só se usar Render) | Webhook de deploy do Render |

> O `GITHUB_TOKEN` é fornecido automaticamente pelo GitHub Actions.

---

## ✅ Checklist

- [ ] Opção de CI/CD escolhida
- [ ] Workflow criado em `.github/workflows/`
- [ ] Secrets configurados (se necessário)
- [ ] Testado com push/PR
- [ ] Badge adicionado no README

---

*Guia completo de configuração: [CI_CD_SETUP.md](CI_CD_SETUP.md)*
