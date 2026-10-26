package io.github.mingssssss.fleet.auth;

public record TokenResponse(String accessToken, String tokenType, long expiresInSeconds) {
}
