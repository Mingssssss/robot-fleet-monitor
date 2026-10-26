#!/usr/bin/env bash
# Git Bash 에서 실행: bash scripts/smoke.sh
# 서버(8080)가 떠 있고 시뮬레이터가 돌고 있을 때 API 흐름을 한 번에 확인한다.
set -u
BASE=${BASE:-http://localhost:8080}

echo "== 1) health"
curl -s "$BASE/actuator/health"; echo

echo "== 2) login (operator)"
TOKEN=$(curl -s -X POST "$BASE/api/auth/login" -H 'Content-Type: application/json' \
  -d '{"username":"operator","password":"operator123"}' | sed -E 's/.*"accessToken":"([^"]+)".*/\1/')
echo "token length: ${#TOKEN}"

echo "== 3) robots (without token -> 401 expected)"
curl -s -o /dev/null -w "%{http_code}\n" "$BASE/api/robots"

echo "== 4) robots (with token)"
curl -s -H "Authorization: Bearer $TOKEN" "$BASE/api/robots"; echo

echo "== 5) events of amr-01 (limit 3)"
curl -s -H "Authorization: Bearer $TOKEN" "$BASE/api/robots/amr-01/events?limit=3"; echo

echo "== 6) command STOP to amr-01 (operator)"
curl -s -X POST -H "Authorization: Bearer $TOKEN" -H 'Content-Type: application/json' \
  -d '{"type":"STOP"}' "$BASE/api/robots/amr-01/commands"; echo

echo "== 7) command as viewer -> 403 expected"
VTOKEN=$(curl -s -X POST "$BASE/api/auth/login" -H 'Content-Type: application/json' \
  -d '{"username":"viewer","password":"viewer123"}' | sed -E 's/.*"accessToken":"([^"]+)".*/\1/')
curl -s -o /dev/null -w "%{http_code}\n" -X POST -H "Authorization: Bearer $VTOKEN" -H 'Content-Type: application/json' \
  -d '{"type":"STOP"}' "$BASE/api/robots/amr-01/commands"
