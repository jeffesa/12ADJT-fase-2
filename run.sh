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
  echo "📬 Executando Collection Postman (Newman)..."
  echo ""
  if ! command -v newman &>/dev/null; then
    echo "❌ Newman não instalado. Instale com: npm install -g newman newman-reporter-htmlextra"
    return 1
  fi
  newman run "docs/api-collection/FIAP Fase 2 - Gestao de Restaurantes.postman_collection.json" \
    --folder local \
    -r htmlextra \
    --reporter-htmlextra-export docs/api-collection/report.html
  echo ""
  echo "📊 Relatório gerado em: docs/api-collection/report.html"
  echo "   Abra no navegador: open docs/api-collection/report.html"
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
  echo "║  7) Rodar collection (Newman)           ║"
  echo "║  8) Kill porta 8080                     ║"
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
    8) kill_port 8080 ;;
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
    kill) kill_port 8080 ;;
    *) echo "Uso: ./run.sh [dev|test|prod|docker|stop|tests|kill]" ;;
  esac
else
  show_menu
fi
