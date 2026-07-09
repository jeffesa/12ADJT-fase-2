#!/bin/bash

# --- Detecção de Java 17 ---
detect_java() {
  if command -v java &>/dev/null; then
    JAVA_VER=$(java -version 2>&1 | head -1 | grep -oP '"\K[^"]+' | cut -d. -f1)
    if [ "$JAVA_VER" = "17" ]; then
      return 0
    fi
  fi

  # macOS: tenta localizar Java 17
  if command -v /usr/libexec/java_home &>/dev/null; then
    JAVA17=$(/usr/libexec/java_home -v 17 2>/dev/null)
    if [ -n "$JAVA17" ]; then
      export JAVA_HOME="$JAVA17"
      return 0
    fi
  fi

  # Linux: verifica alternativas comuns
  if [ -d "/usr/lib/jvm/java-17-openjdk-amd64" ]; then
    export JAVA_HOME="/usr/lib/jvm/java-17-openjdk-amd64"
    return 0
  fi

  echo "⚠️  Java 17 não encontrado. Certifique-se de que está instalado."
  return 1
}

# --- Detecção de Docker ---
ensure_docker() {
  if docker ps &>/dev/null; then
    return 0
  fi

  # macOS com Colima
  if command -v colima &>/dev/null; then
    # Limpa DOCKER_HOST que pode conflitar com context
    unset DOCKER_HOST
    docker context use colima &>/dev/null

    if docker ps &>/dev/null; then
      return 0
    fi

    echo "⚠️  Docker não está respondendo. Iniciando Colima..."
    colima start 2>/dev/null
    sleep 5

    if docker ps &>/dev/null; then
      echo "✅ Docker disponível."
      return 0
    fi

    echo "❌ Não foi possível iniciar o Docker via Colima."
    echo "   Tente: colima stop && colima start"
    return 1
  fi

  # Docker Desktop ou Linux
  echo "❌ Docker não está rodando."
  echo "   - macOS: Abra o Docker Desktop ou instale Colima (brew install colima)"
  echo "   - Linux: sudo systemctl start docker"
  return 1
}

kill_port() {
  local PORT=$1
  PID=$(lsof -ti:$PORT 2>/dev/null)
  if [ -n "$PID" ]; then
    echo "⚠️  Porta $PORT em uso (PID: $PID). Encerrando..."
    kill -9 $PID 2>/dev/null
    sleep 1
    echo "✅ Porta $PORT liberada."
  fi
}

run_local() {
  local PROFILE=${1:-dev}
  detect_java || return 1
  echo ""
  echo "🚀 Iniciando aplicação LOCAL com profile: $PROFILE"
  echo "☕ JAVA_HOME: ${JAVA_HOME:-sistema}"
  echo ""
  kill_port 8080
  mvn spring-boot:run -Dspring-boot.run.profiles=$PROFILE
}

run_docker() {
  echo ""
  echo "🐳 Iniciando aplicação via Docker Compose..."
  echo ""
  ensure_docker || return 1
  kill_port 8080
  docker-compose down 2>/dev/null
  docker-compose up --build -d
  echo ""
  echo "✅ Docker Compose iniciado."
  echo "   Acompanhe: docker-compose logs -f app"
  echo ""
  echo "📍 Health:   http://localhost:8080/actuator/health"
  echo "📍 Swagger:  http://localhost:8080/swagger-ui.html"
}

stop_docker() {
  echo ""
  echo "🛑 Parando Docker Compose..."
  docker-compose down
  echo "✅ Containers parados."
}

run_tests() {
  detect_java || return 1
  echo ""
  echo "🧪 Executando testes..."
  echo ""
  mvn clean verify
}

run_collection() {
  echo ""
  echo "📬 Executando Collection (Newman + HTML report)..."
  echo ""
  if ! command -v newman &>/dev/null; then
    echo "❌ Newman não instalado. Instale com: npm install -g newman newman-reporter-htmlextra"
    echo "   Ou use a opção 9 (script shell puro, sem dependências)."
    return 1
  fi
  newman run "docs/api-collection/FIAP Fase 2 - Gestao de Restaurantes.postman_collection.json" \
    --folder local \
    -r htmlextra \
    --reporter-htmlextra-export docs/api-collection/evidencia-testes-api.html
  echo ""
  echo "📊 Relatório gerado em: docs/api-collection/evidencia-testes-api.html"
  echo "   Abra no navegador: open docs/api-collection/evidencia-testes-api.html"
}

run_collection_shell() {
  local API_URL="${1:-http://localhost:8080}"
  echo ""
  echo -e "\033[0;36m╔══════════════════════════════════════════════════════════╗\033[0m"
  echo -e "\033[0;36m║   📋 Testes da API - Gestão de Restaurantes (Fase 2)    ║\033[0m"
  echo -e "\033[0;36m║   URL: $API_URL\033[0m"
  echo -e "\033[0;36m╚══════════════════════════════════════════════════════════╝\033[0m"
  echo ""

  if ! command -v jq &>/dev/null; then
    echo "❌ jq não encontrado. Instale com: brew install jq (mac) ou apt install jq (linux)"
    return 1
  fi

  local PASSED=0 FAILED=0 TOTAL=0
  local FAIL_LIST=""
  local USER_TYPE_ID="" USER_TYPE_ID_2="" USER_ID="" RESTAURANT_ID="" MENU_ITEM_ID=""

  _assert() {
    local NAME="$1" EXPECTED="$2" ACTUAL="$3" RESP="$4"
    TOTAL=$((TOTAL + 1))
    if [ "$ACTUAL" -eq "$EXPECTED" ]; then
      PASSED=$((PASSED + 1))
      echo -e "  \033[0;32m✅ $NAME\033[0m (esperado: $EXPECTED, recebido: $ACTUAL)"
    else
      FAILED=$((FAILED + 1))
      FAIL_LIST="$FAIL_LIST\n  ❌ $NAME (esperado: $EXPECTED, recebido: $ACTUAL)"
      echo -e "  \033[0;31m❌ $NAME\033[0m (esperado: $EXPECTED, recebido: $ACTUAL)"
      [ -n "$RESP" ] && echo -e "     \033[1;33mBody: $(echo "$RESP" | head -c 200)\033[0m"
    fi
  }

  _get() { RESPONSE=$(curl -s -w "\n%{http_code}" "$1"); HTTP_CODE=$(echo "$RESPONSE" | tail -1); BODY=$(echo "$RESPONSE" | sed '$d'); }
  _post() { RESPONSE=$(curl -s -w "\n%{http_code}" -X POST "$1" -H "Content-Type: application/json" -d "$2"); HTTP_CODE=$(echo "$RESPONSE" | tail -1); BODY=$(echo "$RESPONSE" | sed '$d'); }
  _put() { RESPONSE=$(curl -s -w "\n%{http_code}" -X PUT "$1" -H "Content-Type: application/json" -d "$2"); HTTP_CODE=$(echo "$RESPONSE" | tail -1); BODY=$(echo "$RESPONSE" | sed '$d'); }
  _patch() { RESPONSE=$(curl -s -w "\n%{http_code}" -X PATCH "$1" -H "Content-Type: application/json" -d "$2"); HTTP_CODE=$(echo "$RESPONSE" | tail -1); BODY=$(echo "$RESPONSE" | sed '$d'); }
  _del() { RESPONSE=$(curl -s -w "\n%{http_code}" -X DELETE "$1"); HTTP_CODE=$(echo "$RESPONSE" | tail -1); BODY=$(echo "$RESPONSE" | sed '$d'); }

  # --- Health Check ---
  echo -e "\033[0;36m── Health Check ──\033[0m"
  _get "$API_URL/actuator/health"
  _assert "Health" 200 "$HTTP_CODE" "$BODY"
  echo ""

  # --- Tipos de Usuário ---
  echo -e "\033[0;36m── Tipos de Usuário ──\033[0m"
  _post "$API_URL/api/v1/user-types" '{"name": "CUSTOMER"}'
  _assert "Criar Tipo - CUSTOMER" 201 "$HTTP_CODE" "$BODY"
  USER_TYPE_ID=$(echo "$BODY" | jq -r '.id // empty')

  _post "$API_URL/api/v1/user-types" '{"name": "RESTAURANT_OWNER"}'
  _assert "Criar Tipo - RESTAURANT_OWNER" 201 "$HTTP_CODE" "$BODY"
  USER_TYPE_ID_2=$(echo "$BODY" | jq -r '.id // empty')

  _post "$API_URL/api/v1/user-types" '{"name": "CUSTOMER"}'
  _assert "Criar Tipo - Nome duplicado" 422 "$HTTP_CODE" "$BODY"

  _post "$API_URL/api/v1/user-types" '{"name": ""}'
  _assert "Criar Tipo - Nome vazio" 400 "$HTTP_CODE" "$BODY"

  _get "$API_URL/api/v1/user-types"
  _assert "Listar Tipos" 200 "$HTTP_CODE" "$BODY"

  _get "$API_URL/api/v1/user-types/$USER_TYPE_ID"
  _assert "Buscar Tipo por ID" 200 "$HTTP_CODE" "$BODY"

  _get "$API_URL/api/v1/user-types/aaaaaaaa-bbbb-cccc-dddd-eeeeeeeeeeee"
  _assert "Buscar Tipo - ID inexistente" 404 "$HTTP_CODE" "$BODY"

  _put "$API_URL/api/v1/user-types/$USER_TYPE_ID" '{"name": "ADMIN"}'
  _assert "Atualizar Tipo" 200 "$HTTP_CODE" "$BODY"

  _put "$API_URL/api/v1/user-types/$USER_TYPE_ID" '{"name": "RESTAURANT_OWNER"}'
  _assert "Atualizar Tipo - Nome duplicado" 422 "$HTTP_CODE" "$BODY"

  _put "$API_URL/api/v1/user-types/aaaaaaaa-bbbb-cccc-dddd-eeeeeeeeeeee" '{"name": "NOVO"}'
  _assert "Atualizar Tipo - ID inexistente" 404 "$HTTP_CODE" "$BODY"

  _del "$API_URL/api/v1/user-types/$USER_TYPE_ID"
  _assert "Deletar Tipo" 204 "$HTTP_CODE" "$BODY"

  _del "$API_URL/api/v1/user-types/aaaaaaaa-bbbb-cccc-dddd-eeeeeeeeeeee"
  _assert "Deletar Tipo - ID inexistente" 404 "$HTTP_CODE" "$BODY"
  echo ""

  # --- Usuários ---
  echo -e "\033[0;36m── Usuários ──\033[0m"
  _post "$API_URL/api/v1/user-types" '{"name": "USER_TEST"}'
  USER_TYPE_ID=$(echo "$BODY" | jq -r '.id // empty')

  _post "$API_URL/api/v1/users" "{\"name\": \"João Silva\", \"email\": \"joao@email.com\", \"login\": \"joaosilva\", \"password\": \"Senha123\", \"address\": \"Rua das Flores, 123\", \"userTypeId\": \"$USER_TYPE_ID\"}"
  _assert "Criar Usuário (CUSTOMER)" 201 "$HTTP_CODE" "$BODY"
  USER_ID=$(echo "$BODY" | jq -r '.id // empty')

  _post "$API_URL/api/v1/users" "{\"name\": \"Outro\", \"email\": \"joao@email.com\", \"login\": \"outro\", \"password\": \"Senha123\", \"address\": \"Rua B\", \"userTypeId\": \"$USER_TYPE_ID\"}"
  _assert "Criar Usuário - Email duplicado" 422 "$HTTP_CODE" "$BODY"

  _post "$API_URL/api/v1/users" '{"name": "", "email": "invalido", "login": "", "password": "123", "address": "", "userTypeId": null}'
  _assert "Criar Usuário - Dados inválidos" 400 "$HTTP_CODE" "$BODY"

  _get "$API_URL/api/v1/users"
  _assert "Listar Usuários" 200 "$HTTP_CODE" "$BODY"

  _get "$API_URL/api/v1/users?name=Jo%C3%A3o"
  _assert "Buscar Usuários por Nome" 200 "$HTTP_CODE" "$BODY"

  _get "$API_URL/api/v1/users/$USER_ID"
  _assert "Buscar Usuário por ID" 200 "$HTTP_CODE" "$BODY"

  _get "$API_URL/api/v1/users/aaaaaaaa-bbbb-cccc-dddd-eeeeeeeeeeee"
  _assert "Buscar Usuário - ID inexistente" 404 "$HTTP_CODE" "$BODY"

  _put "$API_URL/api/v1/users/$USER_ID" "{\"name\": \"João Atualizado\", \"email\": \"joao@email.com\", \"login\": \"joaosilva\", \"address\": \"Rua Nova, 456\", \"userTypeId\": \"$USER_TYPE_ID\"}"
  _assert "Atualizar Usuário" 200 "$HTTP_CODE" "$BODY"

  _del "$API_URL/api/v1/users/$USER_ID"
  _assert "Deletar Usuário" 204 "$HTTP_CODE" "$BODY"

  _del "$API_URL/api/v1/users/aaaaaaaa-bbbb-cccc-dddd-eeeeeeeeeeee"
  _assert "Deletar Usuário - ID inexistente" 404 "$HTTP_CODE" "$BODY"
  echo ""

  # --- Autenticação ---
  echo -e "\033[0;36m── Autenticação ──\033[0m"
  _post "$API_URL/api/v1/user-types" '{"name": "AUTH_TYPE"}'
  USER_TYPE_ID=$(echo "$BODY" | jq -r '.id // empty')

  _post "$API_URL/api/v1/users" "{\"name\": \"Auth User\", \"email\": \"auth@email.com\", \"login\": \"authuser\", \"password\": \"Senha123\", \"address\": \"Rua Auth, 1\", \"userTypeId\": \"$USER_TYPE_ID\"}"
  USER_ID=$(echo "$BODY" | jq -r '.id // empty')

  _post "$API_URL/api/v1/users/login" '{"login": "authuser", "password": "Senha123"}'
  _assert "Login" 200 "$HTTP_CODE" "$BODY"

  _post "$API_URL/api/v1/users/login" '{"login": "authuser", "password": "SenhaErrada"}'
  _assert "Login - Credenciais inválidas" 422 "$HTTP_CODE" "$BODY"

  _patch "$API_URL/api/v1/users/$USER_ID/password" '{"currentPassword": "Senha123", "newPassword": "NovaSenha456"}'
  _assert "Trocar Senha" 200 "$HTTP_CODE" "$BODY"

  _patch "$API_URL/api/v1/users/$USER_ID/password" '{"currentPassword": "SenhaErrada", "newPassword": "NovaSenha456"}'
  _assert "Trocar Senha - Atual incorreta" 400 "$HTTP_CODE" "$BODY"

  _patch "$API_URL/api/v1/users/aaaaaaaa-bbbb-cccc-dddd-eeeeeeeeeeee/password" '{"currentPassword": "Senha123", "newPassword": "NovaSenha456"}'
  _assert "Trocar Senha - ID inexistente" 404 "$HTTP_CODE" "$BODY"
  echo ""

  # --- Restaurantes ---
  echo -e "\033[0;36m── Restaurantes ──\033[0m"
  _post "$API_URL/api/v1/user-types" '{"name": "REST_OWNER"}'
  USER_TYPE_ID=$(echo "$BODY" | jq -r '.id // empty')

  _post "$API_URL/api/v1/users" "{\"name\": \"Owner Rest\", \"email\": \"ownerrest@email.com\", \"login\": \"ownerrest\", \"password\": \"Senha123\", \"address\": \"Rua Owner, 1\", \"userTypeId\": \"$USER_TYPE_ID\"}"
  USER_ID=$(echo "$BODY" | jq -r '.id // empty')

  _post "$API_URL/api/v1/restaurants" "{\"name\": \"Pizzaria do João\", \"address\": \"Rua das Flores, 123\", \"cuisineType\": \"ITALIANA\", \"openingHours\": \"2024-01-01T11:00:00\", \"closingTime\": \"2024-01-01T23:00:00\", \"ownerId\": \"$USER_ID\"}"
  _assert "Criar Restaurante" 201 "$HTTP_CODE" "$BODY"
  RESTAURANT_ID=$(echo "$BODY" | jq -r '.id // empty')

  _post "$API_URL/api/v1/restaurants" "{\"name\": \"Inválido\", \"address\": \"Rua X\", \"cuisineType\": \"BRASILEIRA\", \"openingHours\": \"2024-01-01T08:00:00\", \"closingTime\": \"2024-01-01T22:00:00\", \"ownerId\": \"aaaaaaaa-bbbb-cccc-dddd-eeeeeeeeeeee\"}"
  _assert "Criar Restaurante - Owner inexistente" 422 "$HTTP_CODE" "$BODY"

  _post "$API_URL/api/v1/restaurants" "{\"name\": \"\", \"address\": \"Rua Z\", \"cuisineType\": \"MEXICANA\", \"openingHours\": \"2024-01-01T09:00:00\", \"closingTime\": \"2024-01-01T21:00:00\", \"ownerId\": \"$USER_ID\"}"
  _assert "Criar Restaurante - Nome vazio" 400 "$HTTP_CODE" "$BODY"

  _get "$API_URL/api/v1/restaurants"
  _assert "Listar Restaurantes" 200 "$HTTP_CODE" "$BODY"

  _get "$API_URL/api/v1/restaurants/$RESTAURANT_ID"
  _assert "Buscar Restaurante por ID" 200 "$HTTP_CODE" "$BODY"

  _get "$API_URL/api/v1/restaurants/aaaaaaaa-bbbb-cccc-dddd-eeeeeeeeeeee"
  _assert "Buscar Restaurante - ID inexistente" 404 "$HTTP_CODE" "$BODY"

  _get "$API_URL/api/v1/restaurants?ownerId=$USER_ID"
  _assert "Buscar Restaurantes por Owner" 200 "$HTTP_CODE" "$BODY"

  _put "$API_URL/api/v1/restaurants/$RESTAURANT_ID" "{\"name\": \"Pizzaria Atualizada\", \"address\": \"Rua Nova, 456\", \"cuisineType\": \"ITALIANA\", \"openingHours\": \"2024-01-01T10:00:00\", \"closingTime\": \"2024-01-02T00:00:00\", \"ownerId\": \"$USER_ID\"}"
  _assert "Atualizar Restaurante" 200 "$HTTP_CODE" "$BODY"

  _put "$API_URL/api/v1/restaurants/aaaaaaaa-bbbb-cccc-dddd-eeeeeeeeeeee" "{\"name\": \"Novo\", \"address\": \"Rua\", \"cuisineType\": \"X\", \"openingHours\": \"2024-01-01T11:00:00\", \"closingTime\": \"2024-01-01T23:00:00\", \"ownerId\": \"$USER_ID\"}"
  _assert "Atualizar Restaurante - ID inexistente" 404 "$HTTP_CODE" "$BODY"

  _del "$API_URL/api/v1/restaurants/$RESTAURANT_ID"
  _assert "Deletar Restaurante" 204 "$HTTP_CODE" "$BODY"

  _del "$API_URL/api/v1/restaurants/aaaaaaaa-bbbb-cccc-dddd-eeeeeeeeeeee"
  _assert "Deletar Restaurante - ID inexistente" 404 "$HTTP_CODE" "$BODY"
  echo ""

  # --- Cardápio (MenuItem) ---
  echo -e "\033[0;36m── Cardápio (MenuItem) ──\033[0m"
  _post "$API_URL/api/v1/user-types" '{"name": "OWNER_MENU"}'
  USER_TYPE_ID=$(echo "$BODY" | jq -r '.id // empty')

  _post "$API_URL/api/v1/users" "{\"name\": \"Owner Menu\", \"email\": \"ownermenu@email.com\", \"login\": \"ownermenu\", \"password\": \"Senha123\", \"address\": \"Rua Owner, 1\", \"userTypeId\": \"$USER_TYPE_ID\"}"
  USER_ID=$(echo "$BODY" | jq -r '.id // empty')

  _post "$API_URL/api/v1/restaurants" "{\"name\": \"Restaurante Teste\", \"cuisineType\": \"ITALIANA\", \"openingHours\": \"2026-01-01T08:00:00\", \"closingTime\": \"2026-01-01T22:00:00\", \"ownerId\": \"$USER_ID\"}"
  RESTAURANT_ID=$(echo "$BODY" | jq -r '.id // empty')

  if [ -z "$RESTAURANT_ID" ] || [ "$RESTAURANT_ID" = "null" ]; then
    echo -e "  \033[1;33m⚠️  Endpoint POST /api/v1/restaurants não disponível (TASK-020 pendente)\033[0m"
    echo -e "  \033[1;33m   Testes de MenuItem ignorados.\033[0m"
  else
    _post "$API_URL/api/v1/restaurants/$RESTAURANT_ID/menu-items" '{"name": "Pizza Margherita", "description": "Molho, mussarela e manjericão", "price": 39.90, "dineInOnly": false, "photoPath": "/img/pizza.jpg"}'
    _assert "Criar Item" 201 "$HTTP_CODE" "$BODY"
    MENU_ITEM_ID=$(echo "$BODY" | jq -r '.id // empty')

    _post "$API_URL/api/v1/restaurants/aaaaaaaa-bbbb-cccc-dddd-eeeeeeeeeeee/menu-items" '{"name": "Pizza", "description": "desc", "price": 10.00, "dineInOnly": false}'
    _assert "Criar Item - Restaurante inexistente" 404 "$HTTP_CODE" "$BODY"

    _post "$API_URL/api/v1/restaurants/$RESTAURANT_ID/menu-items" '{"name": "Item", "price": 0, "dineInOnly": false}'
    _assert "Criar Item - Preço zero" 400 "$HTTP_CODE" "$BODY"

    _get "$API_URL/api/v1/restaurants/$RESTAURANT_ID/menu-items"
    _assert "Listar Itens do Restaurante" 200 "$HTTP_CODE" "$BODY"

    _get "$API_URL/api/v1/menu-items/$MENU_ITEM_ID"
    _assert "Buscar Item por ID" 200 "$HTTP_CODE" "$BODY"

    _get "$API_URL/api/v1/menu-items/aaaaaaaa-bbbb-cccc-dddd-eeeeeeeeeeee"
    _assert "Buscar Item - ID inexistente" 404 "$HTTP_CODE" "$BODY"

    _put "$API_URL/api/v1/menu-items/$MENU_ITEM_ID" '{"name": "Pizza Calabresa", "description": "Calabresa e cebola", "price": 42.90, "dineInOnly": true, "photoPath": "/img/calabresa.jpg"}'
    _assert "Atualizar Item" 200 "$HTTP_CODE" "$BODY"

    _del "$API_URL/api/v1/menu-items/$MENU_ITEM_ID"
    _assert "Deletar Item" 204 "$HTTP_CODE" "$BODY"

    _del "$API_URL/api/v1/menu-items/aaaaaaaa-bbbb-cccc-dddd-eeeeeeeeeeee"
    _assert "Deletar Item - ID inexistente" 404 "$HTTP_CODE" "$BODY"
  fi
  echo ""

  # --- Resumo ---
  echo -e "\033[0;36m══════════════════════════════════════════════════════════\033[0m"
  echo -e "\033[0;36m                    📊 RESUMO\033[0m"
  echo -e "\033[0;36m══════════════════════════════════════════════════════════\033[0m"
  echo ""
  echo -e "  Total de testes:  $TOTAL"
  echo -e "  \033[0;32mPassou:           $PASSED\033[0m"
  echo -e "  \033[0;31mFalhou:           $FAILED\033[0m"
  echo ""
  if [ "$FAILED" -eq 0 ]; then
    echo -e "  \033[0;32m🎉 Todos os testes passaram!\033[0m"
  else
    echo -e "  \033[0;31m⚠️  Testes que falharam:\033[0m"
    echo -e "$FAIL_LIST"
  fi
  echo ""

  [ "$FAILED" -gt 0 ] && return 1
  return 0
}

show_menu() {
  echo ""
  echo "╔══════════════════════════════════════════╗"
  echo "║   🚀 Tech Challenge Fase 2 - Runner     ║"
  echo "╠══════════════════════════════════════════╣"
  echo "║  1) Local - profile dev (H2)            ║"
  echo "║  2) Local - profile test (H2)           ║"
  echo "║  3) Local - profile prod (PostgreSQL)   ║"
  echo "║  4) Docker Compose (build + up)         ║"
  echo "║  5) Docker Compose (stop)               ║"
  echo "║  6) Rodar testes (mvn clean verify)     ║"
  echo "║  7) Rodar collection (Newman + HTML)    ║"
  echo "║  8) Rodar testes API (curl + jq)        ║"
  echo "║  9) Kill porta 8080                     ║"
  echo "║  0) Sair                                ║"
  echo "╚══════════════════════════════════════════╝"
  echo ""
  read -p "Escolha uma opção: " option

  case $option in
    1) run_local dev ;;
    2) run_local test ;;
    3) run_local prod ;;
    4) run_docker ;;
    5) stop_docker ;;
    6) run_tests ;;
    7) run_collection ;;
    8) run_collection_shell ;;
    9) kill_port 8080 ;;
    0) echo "👋 Até mais!" && exit 0 ;;
    *) echo "❌ Opção inválida." && show_menu ;;
  esac
}

# Se receber argumento, executa direto sem menu
if [ -n "$1" ]; then
  case $1 in
    dev|test|prod) run_local $1 ;;
    docker) run_docker ;;
    stop) stop_docker ;;
    tests) run_tests ;;
    collection) run_collection ;;
    test-api) run_collection_shell ;;
    kill) kill_port 8080 ;;
    *) echo "Uso: ./run.sh [dev|test|prod|docker|stop|tests|collection|test-api|kill]" ;;
  esac
else
  show_menu
fi
