package io.github.mingssssss.fleet.ingest;

import java.time.Instant;

import io.github.mingssssss.fleet.robot.RobotState;

/**
 * 제조사별 메시지를 공통으로 맞춘 로봇 상태.
 * batteryPct 는 0~100 퍼센트 기준으로 통일한다.
 */
public record RobotStatus(
        String robotId,
        String vendor,
        RobotState state,
        Double batteryPct,
        Double posX,
        Double posY,
        Instant reportedAt) {
}
