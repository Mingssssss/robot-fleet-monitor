package io.github.mingssssss.fleet.ingest;

import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.JsonNode;

/**
 * [TODO - 직접 구현] 가상 제조사 ALPHA 의 상태 메시지 어댑터.
 *
 * ALPHA 메시지 예:
 * {"id":"amr-01","st":"MOV","bat":87.5,"pos":{"x":1.2,"y":3.4},"ts":1761460000}
 *
 * 규칙
 * - supports: "st" 와 "bat" 필드가 모두 있으면 ALPHA 형식으로 본다.
 * - st 코드 → RobotState: IDL→IDLE, MOV→MOVING, WRK→WORKING, CHG→CHARGING, ERR→ERROR, 그 외→UNKNOWN
 * - bat 은 이미 퍼센트(0~100). 없으면 null.
 * - pos.x, pos.y 가 없으면 null.
 * - ts 는 epoch 초(long). 없으면 Instant.now().
 * - robotId 는 인자로 받은 값(토픽 기준)을 쓴다.
 *
 * 테스트: src/test/java/.../ingest/VendorAAdapterTest.java 가 통과해야 한다.
 */
@Component
public class VendorAAdapter implements VendorAdapter {

    public static final String VENDOR = "ALPHA";

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
        throw new UnsupportedOperationException("VendorAAdapter.toStatus 미구현");
    }
}
