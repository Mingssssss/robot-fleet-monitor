package io.github.mingssssss.fleet.api;

import java.time.Instant;

import io.github.mingssssss.fleet.robot.RobotState;

/** 로봇 목록·상세 응답. online 은 Redis 최신 상태 키의 생존 여부. */
public record RobotView(
        String id,
        String vendor,
        RobotState state,
        Double batteryPct,
        Double posX,
        Double posY,
        Instant lastSeenAt,
        boolean online) {
}
