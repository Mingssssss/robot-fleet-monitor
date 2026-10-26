package io.github.mingssssss.fleet.command;

import org.springframework.integration.annotation.MessagingGateway;
import org.springframework.integration.mqtt.support.MqttHeaders;
import org.springframework.messaging.handler.annotation.Header;

/**
 * MQTT 로 명령을 발행하는 게이트웨이. 호출하면 mqttOutboundChannel 로 메시지가 들어간다.
 * 사용 예: publisher.publish("robots/amr-01/cmd", "{\"type\":\"STOP\"}");
 */
@MessagingGateway(defaultRequestChannel = "mqttOutboundChannel")
public interface CommandPublisher {

    void publish(@Header(MqttHeaders.TOPIC) String topic, String payload);
}
