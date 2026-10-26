package io.github.mingssssss.fleet.api;

import java.time.Instant;

import io.github.mingssssss.fleet.robot.RobotCommand;

public record CommandView(
        Long id,
        String robotId,
        String type,
        String payload,
        RobotCommand.Status status,
        String requestedBy,
        Instant createdAt,
        Instant sentAt,
        String errorMessage) {

    public static CommandView from(RobotCommand c) {
        return new CommandView(c.getId(), c.getRobotId(), c.getType(), c.getPayload(), c.getStatus(),
                c.getRequestedBy(), c.getCreatedAt(), c.getSentAt(), c.getErrorMessage());
    }
}
