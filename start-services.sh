#!/usr/bin/env bash
set -euo pipefail

# ponytail: one trap, one loop, no process manager needed

BASEDIR="$(cd "$(dirname "$0")" && pwd)"
PIDS=()

cleanup() {
  echo -e "\nStopping services..."
  for pid in "${PIDS[@]}"; do
    kill "$pid" 2>/dev/null || true
    wait "$pid" 2>/dev/null || true
  done
  echo "All stopped."
  exit 0
}

trap cleanup SIGINT SIGTERM

wait_for_port() {
  local port=$1 name=$2 timeout=30 elapsed=0
  echo "Waiting for $name on :$port..."
  while ! curl -sf "http://localhost:$port" >/dev/null 2>&1; do
    sleep 1
    elapsed=$((elapsed + 1))
    if [ "$elapsed" -ge "$timeout" ]; then
      echo "ERROR: $name did not start within ${timeout}s"
      cleanup
      exit 1
    fi
  done
  echo "$name is up."
}

start_service() {
  local dir=$1 name=$2 port=$3
  echo -e "\n--- Starting $name ---"
  (cd "$BASEDIR/$dir" && ./mvnw spring-boot:run -q) &
  PIDS+=($!)
  wait_for_port "$port" "$name"
  sleep 2
}

start_service "service-registry" "service-registry" 8761
start_service "quiz-service"     "quiz-service"     8081
start_service "auth-service"     "auth-service"     8082
start_service "api-gateway"      "api-gateway"      8080

echo -e "\n=== All services running. Press Ctrl+C to stop. ==="
wait
