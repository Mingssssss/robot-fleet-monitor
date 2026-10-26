package io.github.mingssssss.fleet.robot;

import java.time.Instant;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

/**
 * 로봇의 최신 요약 정보(DB). 실시간 온라인 여부는 Redis(LatestStateStore)에서 본다.
 */
@Entity
@Table(name = "robots")
public class Robot {

    @Id
    @Column(length = 64)
    private String id;

    @Column(length = 32, nullable = false)
    private String vendor;

    @Enumerated(EnumType.STRING)
    @Column(length = 16, nullable = false)
    private RobotState state = RobotState.UNKNOWN;

    private Double batteryPct;

    private Double posX;

    private Double posY;

    private Instant lastSeenAt;

    protected Robot() {
    }

    public Robot(String id, String vendor) {
        this.id = id;
        this.vendor = vendor;
    }

    public void applyStatus(RobotState newState, Double battery, Double x, Double y, Instant reportedAt) {
        this.state = newState;
        this.batteryPct = battery;
        this.posX = x;
        this.posY = y;
        this.lastSeenAt = reportedAt;
    }

    public String getId() {
        return id;
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

    public Instant getLastSeenAt() {
        return lastSeenAt;
    }
}
