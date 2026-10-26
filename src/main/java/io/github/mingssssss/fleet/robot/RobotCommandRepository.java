package io.github.mingssssss.fleet.robot;

import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RobotCommandRepository extends JpaRepository<RobotCommand, Long> {

    List<RobotCommand> findByRobotIdOrderByCreatedAtDesc(String robotId, Pageable pageable);
}
