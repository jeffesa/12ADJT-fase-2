# 🔍 Guia: SonarCloud - Fase 2

## 🎯 O que o SonarCloud faz?

- ✅ Detecta bugs e vulnerabilidades
- ✅ Identifica code smells
- ✅ Mede cobertura de testes
- ✅ Analisa duplicação de código
- ✅ Comenta automaticamente nos PRs

---

## ⚙️ Configuração Atual

| Item | Valor |
|------|-------|
| Organização | `jeffesa` |
| Project Key | `jeffesa_12ADJT-fase-2` |
| Dashboard | https://sonarcloud.io/project/overview?id=jeffesa_12ADJT-fase-2 |
| Execução | Automática em todo PR e push (develop/main) |

---

## 📋 Como Foi Configurado

### 1. Importar projeto no SonarCloud
1. Acessar [sonarcloud.io](https://sonarcloud.io) com login GitHub
2. "+" → "Analyze new project" → selecionar `12ADJT-fase-2`
3. New Code Definition: "Previous version"

### 2. Gerar token
1. My Account → Security → Generate Token
2. Nome: `fase-2-github-actions`

### 3. Adicionar secret no GitHub
1. Settings → Secrets → Actions → New: `SONAR_TOKEN`

### 4. Arquivos no repositório
- `sonar-project.properties` — configuração do projeto
- `.github/workflows/ci-cd.yml` — job `sonarcloud` executa a análise

---

## 📁 sonar-project.properties

```properties
sonar.projectKey=jeffesa_12ADJT-fase-2
sonar.organization=jeffesa
sonar.sources=src/main/java
sonar.tests=src/test/java
sonar.java.binaries=target/classes
sonar.coverage.jacoco.xmlReportPaths=target/site/jacoco/jacoco.xml
sonar.exclusions=**/config/**,**/dto/**,**/*Application.java
sonar.java.source=17
```

---

## 🔄 Fluxo

```
1. Developer cria PR → develop
2. CI/CD Pipeline roda (build + testes)
3. SonarCloud analisa código
4. Quality Gate valida (cobertura, bugs, vulnerabilidades)
5. PR mergeado → develop
6. Auto PR: develop → main
```

---

## 🔧 Troubleshooting

### "SONAR_TOKEN not found"
- Verifique se o secret está em Settings → Secrets → Actions

### "Project key not found"
- Confirme que o project key é `jeffesa_12ADJT-fase-2` (igual no sonar-project.properties)

### Análise não aparece no PR
- SonarCloud pode demorar 2-3 minutos na primeira execução
- Verifique se o workflow rodou com sucesso em Actions

### Cobertura mostra 0%
- JaCoCo precisa gerar o XML: `target/site/jacoco/jacoco.xml`
- Verifique se `mvn verify` roda os testes antes do Sonar

---

## 🎨 Badges disponíveis

```markdown
[![Quality Gate](https://sonarcloud.io/api/project_badges/measure?project=jeffesa_12ADJT-fase-2&metric=alert_status)](https://sonarcloud.io/summary/new_code?id=jeffesa_12ADJT-fase-2)
[![Coverage](https://sonarcloud.io/api/project_badges/measure?project=jeffesa_12ADJT-fase-2&metric=coverage)](https://sonarcloud.io/summary/new_code?id=jeffesa_12ADJT-fase-2)
[![Bugs](https://sonarcloud.io/api/project_badges/measure?project=jeffesa_12ADJT-fase-2&metric=bugs)](https://sonarcloud.io/summary/new_code?id=jeffesa_12ADJT-fase-2)
```
