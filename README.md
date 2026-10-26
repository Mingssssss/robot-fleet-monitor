# robot-fleet-monitor

서로 다른 제조사 형식으로 상태를 보내는 로봇들을 MQTT 로 수집해 공통 모델로 정규화하고,
PostgreSQL 에 이력을 남기며, Redis TTL 키로 최신 상태와 온라인 여부를 관리하는 관제 API 입니다.
개인 학습 프로젝트이며, 현재 진행 중입니다.

## 구성

```
가상 로봇(simulator/robot_sim.py)
   │ MQTT publish  robots/{robotId}/status   (ALPHA 형식 / BRAVO 형식)
   ▼
Mosquitto 브로커
   │ subscribe robots/+/status
   ▼
Spring Boot (robot-fleet-monitor)
   ├─ VendorAdapter(ALPHA, BRAVO) → RobotStatus 공통 모델
   ├─ PostgreSQL: robots(최신 요약), robot_status_events(이력), robot_commands(명령)
   ├─ Redis: robot:{id}:last (TTL 30초) → 온라인/오프라인 판정
   └─ REST API (JWT) : 로봇 목록·상세·이력 조회, 명령 등록 → MQTT robots/{id}/cmd 발행
```

## 요구 사항

- JDK 17 이상, Docker Desktop, Python 3.10 이상(시뮬레이터)

## 실행 순서

1. 브로커·DB·Redis 기동

   ```bash
   docker compose up -d
   ```

2. 서버 기동 (Git Bash 기준. JAVA_HOME 이 1.8 이면 아래처럼 17 을 지정한다)

   ```bash
   export JAVA_HOME="/c/Program Files/Java/jdk-17"
   ./gradlew bootRun
   ```

3. 시뮬레이터 기동 (다른 터미널)

   ```bash
   cd simulator
   python -m venv .venv && source .venv/Scripts/activate
   pip install -r requirements.txt
   python robot_sim.py
   ```

4. API 확인 (다른 터미널)

   ```bash
   bash scripts/smoke.sh
   ```

## API

| 메서드 | 경로 | 권한 | 설명 |
|---|---|---|---|
| POST | /api/auth/login | 없음 | JWT 발급. 데모 계정 viewer/viewer123 (VIEWER), operator/operator123 (OPERATOR) |
| GET | /api/robots | 로그인 | 로봇 목록 + 온라인 여부 |
| GET | /api/robots/{id} | 로그인 | 로봇 상세 |
| GET | /api/robots/{id}/events?limit=50 | 로그인 | 상태 이력(최근 순) |
| POST | /api/robots/{id}/commands | OPERATOR | 명령 등록 후 MQTT 발행 |
| GET | /api/robots/{id}/commands | 로그인 | 명령 이력 |
| GET | /actuator/health | 없음 | 상태 점검 |

## 테스트

```bash
./gradlew test
```

어댑터 변환 규칙과 JWT 발급·검증을 단위 테스트로 확인합니다.

## 설계 메모

- 제조사별 차이는 `VendorAdapter` 구현체 하나로 격리한다. 새 제조사를 붙일 때 수집·저장·API 코드는 바뀌지 않는다.
- 온라인 판정은 별도 스케줄러 없이 Redis TTL 로 처리한다. 상태가 들어올 때마다 키를 다시 쓰므로 끊긴 로봇은 TTL 이 지나면 자동으로 오프라인이 된다.
- 수신 처리에서 예외를 밖으로 던지지 않는다. 메시지 한 건의 실패가 구독 자체를 흔들지 않게 하기 위해서다.
- 비밀값(JWT secret, DB 비밀번호)은 데모용 기본값이며 실제 환경에서는 환경변수나 비밀 저장소에서 주입한다.
