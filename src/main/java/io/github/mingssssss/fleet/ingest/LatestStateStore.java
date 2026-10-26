package io.github.mingssssss.fleet.ingest;

import java.util.Optional;

import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.ObjectMapper;

import io.github.mingssssss.fleet.config.FleetProperties;

/**
 * [TODO - 직접 구현] 로봇의 최신 상태를 Redis 에 TTL 과 함께 보관한다.
 *
 * 설계
 * - 키: "robot:{robotId}:last", 값: RobotStatus 를 JSON 문자열로 직렬화한 것
 * - TTL: fleet.online-ttl-seconds (기본 30초). 상태가 들어올 때마다 다시 set 하므로 TTL 이 연장된다.
 * - 온라인 판정: 키가 살아 있으면 온라인, 만료됐으면 오프라인. 별도 스케줄러 없이 TTL 만으로 끊긴 로봇을 감지한다.
 *
 * 구현 힌트
 * - redis.opsForValue().set(key, json, Duration.ofSeconds(ttl))
 * - redis.opsForValue().get(key) → null 이면 Optional.empty()
 * - Boolean.TRUE.equals(redis.hasKey(key))
 * - JSON 변환은 objectMapper.writeValueAsString / readValue 사용 (Instant 는 Spring Boot 기본 ObjectMapper 가 처리)
 */
@Component
public class LatestStateStore {

    private final StringRedisTemplate redis;
    private final ObjectMapper objectMapper;
    private final int ttlSeconds;

    public LatestStateStore(StringRedisTemplate redis, ObjectMapper objectMapper, FleetProperties props) {
        this.redis = redis;
        this.objectMapper = objectMapper;
        this.ttlSeconds = props.onlineTtlSeconds();
    }

    static String key(String robotId) {
        return "robot:" + robotId + ":last";
    }

    public void put(RobotStatus status) {
        // TODO 구현
        throw new UnsupportedOperationException("LatestStateStore.put 미구현");
    }

    public Optional<RobotStatus> get(String robotId) {
        // TODO 구현
        throw new UnsupportedOperationException("LatestStateStore.get 미구현");
    }

    public boolean isOnline(String robotId) {
        // TODO 구현
        throw new UnsupportedOperationException("LatestStateStore.isOnline 미구현");
    }
}
