package io.github.mingssssss.fleet.config;

import java.util.List;

import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * application.yml 의 fleet.* 설정을 담는 객체.
 */
@ConfigurationProperties(prefix = "fleet")
public record FleetProperties(
        Mqtt mqtt,
        int onlineTtlSeconds,
        Jwt jwt,
        List<User> users) {

    public record Mqtt(String url, String clientId, String statusTopic) {
    }

    public record Jwt(String secret, long ttlMinutes) {
    }

    public record User(String username, String password, String role) {
    }
}
