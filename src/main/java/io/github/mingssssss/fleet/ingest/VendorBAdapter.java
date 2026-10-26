package io.github.mingssssss.fleet.ingest;

import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.JsonNode;

/**
 * [TODO - 직접 구현] 가상 제조사 BRAVO 의 상태 메시지 어댑터.
 *
 * BRAVO 메시지 예:
 * {"robotId":"arm-02","status":"idle","batteryLevel":0.62,"x":10.0,"y":2.0,"timestamp":"2026-10-26T10:00:00Z"}
 *
 * 규칙
 * - supports: "status" 와 "batteryLevel" 필드가 모두 있으면 BRAVO 형식으로 본다.
 * - status 소문자 단어 → RobotState: idle, moving, working, charging, error (대소문자 무시), 그 외→UNKNOWN
 * - batteryLevel 은 0.0~1.0 비율 → 퍼센트(×100)로 변환. 없으면 null.
 * - x, y 가 없으면 null.
 * - timestamp 는 ISO-8601 문자열 → Instant.parse. 없으면 Instant.now().
 * - robotId 는 인자로 받은 값(토픽 기준)을 쓴다.
 *
 * 테스트: src/test/java/.../ingest/VendorBAdapterTest.java 가 통과해야 한다.
 */
@Component
public class VendorBAdapter implements VendorAdapter {

    public static final String VENDOR = "BRAVO";

    @Override
    public String vendor() {
        return VENDOR;
    }

    @Override
    public boolean supports(JsonNode payload) {
        // TODO 구현
        return false;
    }

    @Override
    public RobotStatus toStatus(String robotId, JsonNode payload) {
        // TODO 구현
        throw new UnsupportedOperationException("VendorBAdapter.toStatus 미구현");
    }
}
