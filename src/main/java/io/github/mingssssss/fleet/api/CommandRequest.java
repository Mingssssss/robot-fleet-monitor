package io.github.mingssssss.fleet.api;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

/** 명령 등록 요청. 예: {"type":"STOP"} 또는 {"type":"MOVE_TO","payload":"{\"x\":1,\"y\":2}"} */
public record CommandRequest(
        @NotBlank @Size(max = 32) String type,
        @Size(max = 2000) String payload) {
}
