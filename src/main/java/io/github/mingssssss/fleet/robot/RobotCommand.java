package io.github.mingssssss.fleet.robot;

import java.time.Instant;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

/**
 * 로봇에 보낸 명령 기록.
 */
@Entity
@Table(name = "robot_commands")
public class RobotCommand {

    public enum Status {
        PENDING, SENT, FAILED
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 64, nullable = false)
    private String robotId;

    @Column(length = 32, nullable = false)
    private String type;

    @Column(columnDefinition = "text")
    private String payload;

    @Enumerated(EnumType.STRING)
    @Column(length = 16, nullable = false)
    private Status status = Status.PENDING;

    @Column(length = 64, nullable = false)
    private String requestedBy;

    @Column(nullable = false)
    private Instant createdAt = Instant.now();

    private Instant sentAt;

    @Column(length = 500)
    private String errorMessage;

    protected RobotCommand() {
    }

    public RobotCommand(String robotId, String type, String payload, String requestedBy) {
        this.robotId = robotId;
        this.type = type;
        this.payload = payload;
        this.requestedBy = requestedBy;
    }

    public void markSent() {
        this.status = Status.SENT;
        this.sentAt = Instant.now();
    }

    public void markFailed(String message) {
        this.status = Status.FAILED;
        this.errorMessage = message;
    }

    public Long getId() {
        return id;
    }

    public String getRobotId() {
        return robotId;
    }

    public String getType() {
        return type;
    }

    public String getPayload() {
        return payload;
    }

    public Status getStatus() {
        return status;
    }

    public String getRequestedBy() {
        return requestedBy;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public Instant getSentAt() {
        return sentAt;
    }

    public String getErrorMessage() {
        return errorMessage;
    }
}
