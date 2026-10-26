package io.github.mingssssss.fleet.robot;

import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RobotStatusEventRepository extends JpaRepository<RobotStatusEvent, Long> {

    List<RobotStatusEvent> findByRobotIdOrderByReportedAtDesc(String robotId, Pageable pageable);
}
