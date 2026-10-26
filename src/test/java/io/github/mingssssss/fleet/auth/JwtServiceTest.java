package io.github.mingssssss.fleet.auth;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.util.List;

import org.junit.jupiter.api.Test;

import io.github.mingssssss.fleet.config.FleetProperties;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;

class JwtServiceTest {

    private final FleetProperties props = new FleetProperties(
            new FleetProperties.Mqtt("tcp://localhost:1883", "test", "robots/+/status"),
            30,
            new FleetProperties.Jwt("unit-test-secret-that-is-definitely-32-bytes-long", 10),
            List.of());

    @Test
    void issueAndParseRoundTrip() {
        JwtService service = new JwtService(props);
        String token = service.issue("operator", List.of("OPERATOR"));
        Claims claims = service.parse(token);
        assertThat(claims.getSubject()).isEqualTo("operator");
        assertThat(claims.get(JwtService.ROLES_CLAIM, List.class)).containsExactly("OPERATOR");
    }

    @Test
    void tamperedTokenIsRejected() {
        JwtService service = new JwtService(props);
        String token = service.issue("viewer", List.of("VIEWER"));
        String tampered = token.substring(0, token.length() - 2) + "xx";
        assertThatThrownBy(() -> service.parse(tampered)).isInstanceOf(JwtException.class);
    }
}
