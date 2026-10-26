package io.github.mingssssss.fleet.command;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.ObjectMapper;

import io.github.mingssssss.fleet.robot.RobotCommand;
import io.github.mingssssss.fleet.robot.RobotCommandRepository;
import io.github.mingssssss.fleet.robot.RobotRepository;

/**
 * [TODO - 직접 구현] 로봇 명령 등록·발행.
 *
 * send(robotId, type, payload, requestedBy) 가 해야 할 일
 * 1. robotRepository 에 robotId 가 없으면 IllegalArgumentException("알 수 없는 로봇") 을 던진다.
 * 2. RobotCommand 를 PENDING 으로 저장한다.
 * 3. MQTT 토픽 "robots/{robotId}/cmd" 로 JSON 을 발행한다.
 *    JSON 예: {"commandId":12,"type":"STOP","payload":"...","requestedBy":"operator","issuedAt":"2026-10-26T10:00:00Z"}
 *    (objectMapper.writeValueAsString(Map.of(...)) 로 만들면 된다)
 * 4. 발행에 성공하면 markSent(), 예외가 나면 markFailed(메시지) 후 저장한다. 예외는 삼키지 말고 다시 던져도 되고,
 *    FAILED 상태의 RobotCommand 를 돌려줘도 된다. 어느 쪽이든 호출자가 결과를 알 수 있어야 한다.
 * 5. 저장된 RobotCommand 를 반환한다.
 */
@Service
public class CommandService {

    private static final Logger log = LoggerFactory.getLogger(CommandService.class);

    private final RobotRepository robotRepository;
    private final RobotCommandRepository commandRepository;
    private final CommandPublisher publisher;
    private final ObjectMapper objectMapper;

    public CommandService(RobotRepository robotRepository, RobotCommandRepository commandRepository,
            CommandPublisher publisher, ObjectMapper objectMapper) {
        this.robotRepository = robotRepository;
        this.commandRepository = commandRepository;
        this.publisher = publisher;
        this.objectMapper = objectMapper;
    }

    public RobotCommand send(String robotId, String type, String payload, String requestedBy) {
        // TODO 구현
        log.warn("CommandService.send 미구현: robotId={}, type={}", robotId, type);
        throw new UnsupportedOperationException("CommandService.send 미구현");
    }
}
