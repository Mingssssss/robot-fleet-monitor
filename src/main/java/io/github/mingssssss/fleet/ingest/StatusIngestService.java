package io.github.mingssssss.fleet.ingest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.ObjectMapper;

import io.github.mingssssss.fleet.robot.RobotRepository;
import io.github.mingssssss.fleet.robot.RobotStatusEventRepository;

/**
 * [TODO - 직접 구현] MQTT 로 들어온 상태 메시지를 처리하는 핵심 서비스.
 *
 * handle(topic, payload) 가 해야 할 일 (순서대로)
 * 1. topic "robots/{robotId}/status" 에서 robotId 를 꺼낸다. 형식이 다르면 warn 로그 후 종료.
 * 2. payload 를 objectMapper.readTree 로 JsonNode 로 바꾼다. JSON 이 아니면 warn 로그 후 종료.
 * 3. registry.find(json) 으로 어댑터를 찾는다. 없으면 warn 로그("지원하지 않는 메시지 형식") 후 종료.
 * 4. adapter.toStatus(robotId, json) 으로 공통 모델 RobotStatus 를 만든다.
 * 5. robotRepository 에서 로봇을 찾고 없으면 새로 만든 뒤 applyStatus(...) 로 최신값을 갱신하고 저장한다.
 * 6. eventRepository 에 RobotStatusEvent 를 한 건 저장한다(rawPayload 에 원문 그대로, receivedAt 은 지금 시각).
 * 7. latestStateStore.put(status) 로 Redis 최신 상태를 갱신한다.
 * 8. info 로그 한 줄: robotId, vendor, state, battery.
 *
 * 주의
 * - 5~6 은 하나의 트랜잭션으로 묶는다(@Transactional). Redis 갱신(7)은 DB 커밋 뒤에 해도 된다.
 * - 메시지 하나가 실패해도 예외를 밖으로 던지지 않는다. 던지면 MQTT 어댑터가 재전달을 반복할 수 있다.
 *   예외는 잡아서 error 로그로 남기고 종료한다.
 */
@Service
public class StatusIngestService {

    private static final Logger log = LoggerFactory.getLogger(StatusIngestService.class);

    private final ObjectMapper objectMapper;
    private final AdapterRegistry registry;
    private final RobotRepository robotRepository;
    private final RobotStatusEventRepository eventRepository;
    private final LatestStateStore latestStateStore;

    public StatusIngestService(ObjectMapper objectMapper, AdapterRegistry registry,
            RobotRepository robotRepository, RobotStatusEventRepository eventRepository,
            LatestStateStore latestStateStore) {
        this.objectMapper = objectMapper;
        this.registry = registry;
        this.robotRepository = robotRepository;
        this.eventRepository = eventRepository;
        this.latestStateStore = latestStateStore;
    }

    public void handle(String topic, String payload) {
        // TODO 구현. 지금은 수신 사실만 로그로 남긴다.
        log.warn("StatusIngestService.handle 미구현: topic={}, payloadLength={}", topic,
                payload == null ? 0 : payload.length());
    }
}
