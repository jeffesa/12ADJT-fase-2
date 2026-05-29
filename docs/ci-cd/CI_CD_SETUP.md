# 🔄 Guia: Configurar CI/CD - Fase 2

Este guia configura o pipeline CI/CD recomendado (Opção 2 - Simplificada).

---

## 🎯 Fluxo

```
1. Developer cria branch feature/*
   ↓
2. Push → GitHub Actions roda build + testes
   ↓
3. Cria PR para develop
   ↓
4. GitHub Actions valida (build + testes + cobertura)
   ↓
5. Merge para develop
   ↓
6. PR de develop para main
   ↓
7. Merge na main → Deploy automático (se Render configurado)
```

---

## 📋 Pré-requisitos

- [ ] Repositório no GitHub
- [ ] Projeto com Maven e JaCoCo configurado
- [ ] Testes passando localmente (`mvn clean verify`)

---

## 🔧 Passo 1: Criar Workflow

Crie o arquivo `.github/workflows/ci-cd.yml`:

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
      - name: Checkout code
        uses: actions/checkout@v4

      - name: Set up JDK 17
        uses: actions/setup-java@v4
        with:
          java-version: '17'
          distribution: 'temurin'
          cache: maven

      - name: Build and Test with Coverage
        run: mvn clean verify

      - name: Upload Coverage Report
        if: always()
        uses: actions/upload-artifact@v4
        with:
          name: jacoco-report
          path: target/site/jacoco/

  # Job opcional: SonarCloud (ativa com label "sonar" no PR)
  sonarcloud:
    name: SonarCloud Analysis (Optional)
    runs-on: ubuntu-latest
    if: contains(github.event.pull_request.labels.*.name, 'sonar')

    steps:
      - name: Checkout code
        uses: actions/checkout@v4
        with:
          fetch-depth: 0

      - name: Set up JDK 17
        uses: actions/setup-java@v4
        with:
          java-version: '17'
          distribution: 'temurin'
          cache: maven

      - name: Cache SonarCloud packages
        uses: actions/cache@v3
        with:
          path: ~/.sonar/cache
          key: ${{ runner.os }}-sonar
          restore-keys: ${{ runner.os }}-sonar

      - name: Build and Analyze
        env:
          GITHUB_TOKEN: ${{ secrets.GITHUB_TOKEN }}
          SONAR_TOKEN: ${{ secrets.SONAR_TOKEN }}
        run: mvn -B verify org.sonarsource.scanner.maven:sonar-maven-plugin:sonar

  # Job opcional: Deploy no Render (só em push na main)
  deploy:
    name: Deploy to Render
    needs: build
    runs-on: ubuntu-latest
    if: github.ref == 'refs/heads/main' && github.event_name == 'push'

    steps:
      - name: Trigger Render Deploy
        if: ${{ secrets.RENDER_DEPLOY_HOOK_URL != '' }}
        run: curl -X POST ${{ secrets.RENDER_DEPLOY_HOOK_URL }}

      - name: Deployment Status
        run: echo "✅ Deploy triggered on Render.com"
```

---

## 🔑 Passo 2: Configurar Secrets (Opcional)

No GitHub: **Settings → Secrets and variables → Actions**

| Secret | Quando configurar |
|--------|-------------------|
| `SONAR_TOKEN` | Apenas se for usar SonarCloud (TASK-038) |
| `RENDER_DEPLOY_HOOK_URL` | Apenas se for fazer deploy no Render (TASK-039) |

---

## 🛡️ Passo 3: Branch Protection (Recomendado)

1. GitHub → **Settings → Branches**
2. **Add branch protection rule** para `main`:
   - ✅ Require a pull request before merging
   - ✅ Require status checks to pass before merging
   - Adicionar status check: `Build & Test`
3. **Add branch protection rule** para `develop`:
   - ✅ Require a pull request before merging

---

## 🧪 Passo 4: Testar

```bash
# Criar branch de teste
git checkout -b feature/test-ci

# Fazer alteração mínima
echo "// test" >> README.md

# Commit e push
git add .
git commit -m "ci: testa pipeline"
git push origin feature/test-ci

# Criar PR no GitHub → workflow roda automaticamente
```

---

## 🎨 Passo 5: Badge no README

Adicione no `README.md`:

```markdown
[![CI/CD](https://github.com/SEU_USUARIO/REPO/actions/workflows/ci-cd.yml/badge.svg)](https://github.com/SEU_USUARIO/REPO/actions/workflows/ci-cd.yml)
```

---

## 🔧 Troubleshooting

### Build falha por cobertura < 80%
- Verifique o relatório JaCoCo: `target/site/jacoco/index.html`
- Adicione mais testes ou configure exclusões no `pom.xml`

### Workflow não executa
- Verifique se o arquivo está em `.github/workflows/`
- Verifique a sintaxe YAML (indentação)

### SonarCloud não roda
- Verifique se a label `sonar` foi adicionada ao PR
- Verifique se `SONAR_TOKEN` está nos secrets

---

## 📚 Recursos

- [GitHub Actions Docs](https://docs.github.com/en/actions)
- [JaCoCo Maven Plugin](https://www.eclemma.org/jacoco/trunk/doc/maven.html)
- [SonarCloud GitHub Action](https://docs.sonarcloud.io/advanced-setup/ci-based-analysis/github-actions-for-sonarcloud/)

---

*Opções de CI/CD: [CI_CD_OPTIONS.md](CI_CD_OPTIONS.md)*
