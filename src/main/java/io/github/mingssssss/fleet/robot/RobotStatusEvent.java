package io.github.mingssssss.fleet.robot;

import java.time.Instant;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.Table;

/**
 * 로봇 상태 이력(수신한 모든 상태를 한 건씩 저장).
 */
@Entity
@Table(name = "robot_status_events", indexes = {
        @Index(name = "idx_status_events_robot_reported", columnList = "robot_id, reported_at")
})
public class RobotStatusEvent {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 64, nullable = false)
    private String robotId;

    @Column(length = 32, nullable = false)
    private String vendor;

    @Enumerated(EnumType.STRING)
    @Column(length = 16, nullable = false)
    private RobotState state;

    private Double batteryPct;

    private Double posX;

    private Double posY;

    @Column(nullable = false)
    private Instant reportedAt;

    @Column(nullable = false)
    private Instant receivedAt;

    @Column(columnDefinition = "text")
    private String rawPayload;

    protected RobotStatusEvent() {
    }

    public RobotStatusEvent(String robotId, String vendor, RobotState state, Double batteryPct,
            Double posX, Double posY, Instant reportedAt, Instant receivedAt, String rawPayload) {
        this.robotId = robotId;
        this.vendor = vendor;
        this.state = state;
        this.batteryPct = batteryPct;
        this.posX = posX;
        this.posY = posY;
        this.reportedAt = reportedAt;
        this.receivedAt = receivedAt;
        this.rawPayload = rawPayload;
    }

    public Long getId() {
        return id;
    }

    public String getRobotId() {
        return robotId;
    }

    public String getVendor() {
        return vendor;
    }

    public RobotState getState() {
        return state;
    }

    public Double getBatteryPct() {
        return batteryPct;
    }

    public Double getPosX() {
        return posX;
    }

    public Double getPosY() {
        return posY;
    }

    public Instant getReportedAt() {
        return reportedAt;
    }

    public Instant getReceivedAt() {
        return receivedAt;
    }

    public String getRawPayload() {
        return rawPayload;
    }
}
