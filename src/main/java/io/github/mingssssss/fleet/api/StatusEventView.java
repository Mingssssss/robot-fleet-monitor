package io.github.mingssssss.fleet.api;

import java.time.Instant;

import io.github.mingssssss.fleet.robot.RobotState;

public record StatusEventView(
        Instant reportedAt,
        Instant receivedAt,
        RobotState state,
        Double batteryPct,
        Double posX,
        Double posY) {
}
