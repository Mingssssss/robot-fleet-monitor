package io.github.mingssssss.fleet.api;

import java.security.Principal;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.github.mingssssss.fleet.command.CommandService;
import io.github.mingssssss.fleet.robot.RobotCommandRepository;
import jakarta.validation.Valid;

/**
 * [TODO - 직접 구현] 명령 API.
 *
 * POST /api/robots/{id}/commands  (OPERATOR 만) body: CommandRequest → 201 + CommandView
 *      로봇이 없으면 404, 발행 실패면 502 와 FAILED 상태의 CommandView
 * GET  /api/robots/{id}/commands?limit=20 → 최근 명령 목록
 *
 * 구현 힌트
 * - 요청자 이름: principal.getName()
 * - commandService.send(id, request.type(), request.payload(), principal.getName())
 */
@RestController
@RequestMapping("/api/robots/{id}/commands")
public class CommandController {

    private final CommandService commandService;
    private final RobotCommandRepository commandRepository;

    public CommandController(CommandService commandService, RobotCommandRepository commandRepository) {
        this.commandService = commandService;
        this.commandRepository = commandRepository;
    }

    @PostMapping
    public ResponseEntity<CommandView> create(@PathVariable String id, @Valid @RequestBody CommandRequest request,
            Principal principal) {
        // TODO 구현
        return ResponseEntity.status(HttpStatus.NOT_IMPLEMENTED).build();
    }

    @GetMapping
    public ResponseEntity<List<CommandView>> list(@PathVariable String id) {
        // TODO 구현
        return ResponseEntity.status(HttpStatus.NOT_IMPLEMENTED).build();
    }
}
