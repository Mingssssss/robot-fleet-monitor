# 3일 구현 계획 (10/26 ~ 10/28)

이 문서는 사용자님(본인) 작업용 체크리스트입니다. 저장소에 올려도 되고 지워도 됩니다.

## 역할 구분 (정직하게 말할 수 있는 범위)

- Claude 가 준 것: 프로젝트 뼈대(빌드 설정, Docker Compose, 엔티티, MQTT·보안 설정, JWT 발급·검증, DTO, 시뮬레이터, 테스트 코드, 문서 틀)
- 본인이 직접 구현할 것 (TODO 표시된 6개 파일): 제조사 어댑터 2개, 상태 수신 서비스, Redis 최신 상태 저장소, 로봇 조회 API, 명령 서비스·API
- 면접 기준: 어느 파일이든 왜 이렇게 설계했는지 설명할 수 있어야 한다. 뼈대 코드도 한 번씩 읽고 이해한다.

## Day 1 (10/26 저녁, 약 3시간)

1. GitHub 에 공개 저장소 `robot-fleet-monitor` 생성 (README 없이 빈 저장소)
2. 압축을 푼 폴더에서 (Git Bash)
   ```bash
   cd robot-fleet-monitor
   git init -b main
   git add .
   git commit -m "chore: project skeleton (spring boot, mqtt, jpa, redis, jwt, docker compose)"
   git remote add origin https://github.com/Mingssssss/robot-fleet-monitor.git
   git push -u origin main
   ```
3. 빌드 확인
   ```bash
   export JAVA_HOME="/c/Program Files/Java/jdk-17"
   ./gradlew build -x test
   ```
   첫 빌드는 의존성 내려받기로 몇 분 걸린다. 실패하면 오류 전문을 그대로 Claude 에게 준다.
4. `docker compose up -d` 로 브로커·DB·Redis 기동 확인 (`docker compose ps`)
5. 어댑터 2개 구현: `ingest/VendorAAdapter.java`, `ingest/VendorBAdapter.java`
   - `./gradlew test --tests "*VendorAAdapterTest" --tests "*VendorBAdapterTest" --tests "*AdapterRegistryTest"` 통과
   - 커밋: `feat: vendor adapters for ALPHA/BRAVO payloads`

## Day 2 (10/27 저녁, 약 3~4시간)

6. `ingest/LatestStateStore.java` 구현 (Redis set with TTL / get / hasKey)
7. `ingest/StatusIngestService.java` 구현 (토픽 파싱 → 어댑터 → DB 저장 → Redis)
   - `./gradlew bootRun` 후 시뮬레이터 실행 → 로그에 info 한 줄씩 찍히는지, DB `robot_status_events` 에 쌓이는지, `redis-cli --scan` 으로 키가 보이는지 확인
   - 시뮬레이터를 끄고 30초 뒤 키가 사라지는지 확인(온라인→오프라인)
   - 커밋: `feat: mqtt status ingest with postgres history and redis latest state`
8. `api/RobotController.java` 구현 (목록·상세·이력)
   - `bash scripts/smoke.sh` 의 1~5번이 기대대로 나오는지 확인
   - 커밋: `feat: robot query api with online flag`

## Day 3 (10/28 저녁, 약 3시간)

9. `command/CommandService.java`, `api/CommandController.java` 구현
   - smoke.sh 6번(201)·7번(403) 확인, 시뮬레이터 창에 `[sim] command received` 가 찍히는지 확인
   - 커밋: `feat: command api publishing to mqtt`
10. README 의 "진행 중" 문구를 현재 상태에 맞게 고치고, 실행 화면 캡처 1~2장을 `docs/` 에 추가
11. 시간이 남으면 (선택): gRPC 로 명령 채널 하나 추가, 또는 Actuator `/actuator/metrics` 에 수신 건수 카운터 추가
12. 최종 push 후 Claude 에게 알리기 → 저장소를 읽어 코드 리뷰와 README 교정

## 검토 요청 방법

- push 한 뒤 채팅에 "리뷰해줘" 라고만 적으면 된다. Claude 가 저장소를 읽어 파일별로 지적한다.
- 막히면 오류 전문과 어느 단계인지(위 번호)만 적어서 보낸다.

## 자주 틀리는 곳 미리 안내

- JSON 숫자 읽기: `payload.path("bat").isMissingNode()` 로 없음을 확인하고, 있으면 `.asDouble()`. `get()` 은 없을 때 null 을 돌려주므로 NPE 주의.
- `Instant.ofEpochSecond(long)` 과 `Instant.parse(String)` 을 제조사별로 구분.
- Redis TTL: `Duration.ofSeconds(ttlSeconds)` 를 `set(key, value, timeout)` 세 번째 인자로.
- 트랜잭션: `@Transactional` 은 `public` 메서드에, 같은 클래스 안에서 자기 메서드를 호출하면 적용되지 않는다. DB 저장 부분을 별도 public 메서드로 빼고 바깥에서 호출하거나, 서비스 전체 메서드에 걸고 예외를 안에서 잡는다.
- 컨트롤러 404: `robotRepository.findById(id).map(...).orElseGet(() -> ResponseEntity.notFound().build())`
