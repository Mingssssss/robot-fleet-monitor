package io.github.mingssssss.fleet.ingest;

import com.fasterxml.jackson.databind.JsonNode;

/**
 * 제조사(벤더)마다 다른 상태 메시지를 공통 모델(RobotStatus)로 바꾸는 어댑터.
 * 새 제조사의 로봇을 붙일 때는 이 인터페이스 구현체 하나만 추가하면 된다.
 */
public interface VendorAdapter {

    /** 어댑터가 담당하는 제조사 코드. 예: ALPHA, BRAVO */
    String vendor();

    /** 이 메시지가 내 제조사 형식인지 판단한다. 필드 이름으로 구분한다. */
    boolean supports(JsonNode payload);

    /**
     * 메시지를 공통 모델로 변환한다.
     *
     * @param robotId 토픽(robots/{robotId}/status)에서 읽은 로봇 ID. 메시지 안의 ID 보다 이 값을 우선한다.
     * @param payload JSON 메시지
     */
    RobotStatus toStatus(String robotId, JsonNode payload);
}
