package io.github.mingssssss.fleet.api;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import io.github.mingssssss.fleet.ingest.LatestStateStore;
import io.github.mingssssss.fleet.robot.RobotRepository;
import io.github.mingssssss.fleet.robot.RobotStatusEventRepository;

/**
 * [TODO - 직접 구현] 로봇 조회 API. 모든 엔드포인트는 로그인(JWT) 필요.
 *
 * GET /api/robots                 → List<RobotView>. DB 의 Robot 목록에 Redis 온라인 여부를 합쳐서 반환.
 * GET /api/robots/{id}            → RobotView. 없으면 404.
 * GET /api/robots/{id}/events?limit=50 → List<StatusEventView>. 최근 순. limit 기본 50, 최대 500.
 *
 * 구현 힌트
 * - 온라인 여부: latestStateStore.isOnline(robot.getId())
 * - 이력: eventRepository.findByRobotIdOrderByReportedAtDesc(id, PageRequest.of(0, limit))
 * - 404: ResponseEntity.notFound().build()
 */
@RestController
@RequestMapping("/api/robots")
public class RobotController {

    private final RobotRepository robotRepository;
    private final RobotStatusEventRepository eventRepository;
    private final LatestStateStore latestStateStore;

    public RobotController(RobotRepository robotRepository, RobotStatusEventRepository eventRepository,
            LatestStateStore latestStateStore) {
        this.robotRepository = robotRepository;
        this.eventRepository = eventRepository;
        this.latestStateStore = latestStateStore;
    }

    @GetMapping
    public ResponseEntity<List<RobotView>> list() {
        // TODO 구현
        return ResponseEntity.status(HttpStatus.NOT_IMPLEMENTED).build();
    }

    @GetMapping("/{id}")
    public ResponseEntity<RobotView> get(@PathVariable String id) {
        // TODO 구현
        return ResponseEntity.status(HttpStatus.NOT_IMPLEMENTED).build();
    }

    @GetMapping("/{id}/events")
    public ResponseEntity<List<StatusEventView>> events(@PathVariable String id,
            @RequestParam(defaultValue = "50") int limit) {
        // TODO 구현
        return ResponseEntity.status(HttpStatus.NOT_IMPLEMENTED).build();
    }
}
