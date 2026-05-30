#!/bin/bash
PROFILE=${1:-dev}

# Detecta Java 17 se disponível
if [ -d "$HOME/Library/Java/JavaVirtualMachines" ]; then
  JAVA17=$(find "$HOME/Library/Java/JavaVirtualMachines" -maxdepth 1 -name "*17*" -type d | head -1)
  if [ -n "$JAVA17" ]; then
    export JAVA_HOME="$JAVA17/Contents/Home"
  fi
elif command -v /usr/libexec/java_home &>/dev/null; then
  export JAVA_HOME=$(/usr/libexec/java_home -v 17 2>/dev/null || true)
fi

echo "🚀 Iniciando aplicação com profile: $PROFILE"
echo "☕ JAVA_HOME: ${JAVA_HOME:-sistema}"

# Mata processo na porta 8080 se existir
PID=$(lsof -ti:8080 2>/dev/null)
if [ -n "$PID" ]; then
  echo "⚠️  Porta 8080 em uso (PID: $PID). Encerrando..."
  kill -9 $PID 2>/dev/null
  sleep 1
fi

mvn spring-boot:run -Dspring-boot.run.profiles=$PROFILE
