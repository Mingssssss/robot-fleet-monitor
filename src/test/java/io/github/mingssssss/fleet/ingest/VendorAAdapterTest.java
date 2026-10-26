package io.github.mingssssss.fleet.ingest;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.Instant;

import org.junit.jupiter.api.Test;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import io.github.mingssssss.fleet.robot.RobotState;

class VendorAAdapterTest {

    private final ObjectMapper mapper = new ObjectMapper();
    private final VendorAAdapter adapter = new VendorAAdapter();

    @Test
    void supportsAlphaPayloadOnly() throws Exception {
        JsonNode alpha = mapper.readTree("{\"id\":\"amr-01\",\"st\":\"MOV\",\"bat\":87.5,\"pos\":{\"x\":1.2,\"y\":3.4},\"ts\":1761460000}");
        JsonNode bravo = mapper.readTree("{\"robotId\":\"arm-02\",\"status\":\"idle\",\"batteryLevel\":0.62,\"x\":10.0,\"y\":2.0}");
        assertThat(adapter.supports(alpha)).isTrue();
        assertThat(adapter.supports(bravo)).isFalse();
    }

    @Test
    void mapsFieldsToCommonModel() throws Exception {
        JsonNode alpha = mapper.readTree("{\"id\":\"amr-01\",\"st\":\"MOV\",\"bat\":87.5,\"pos\":{\"x\":1.2,\"y\":3.4},\"ts\":1761460000}");
        RobotStatus status = adapter.toStatus("amr-01", alpha);
        assertThat(status.robotId()).isEqualTo("amr-01");
        assertThat(status.vendor()).isEqualTo("ALPHA");
        assertThat(status.state()).isEqualTo(RobotState.MOVING);
        assertThat(status.batteryPct()).isEqualTo(87.5);
        assertThat(status.posX()).isEqualTo(1.2);
        assertThat(status.posY()).isEqualTo(3.4);
        assertThat(status.reportedAt()).isEqualTo(Instant.ofEpochSecond(1761460000L));
    }

    @Test
    void unknownStateCodeBecomesUnknownAndMissingFieldsBecomeNull() throws Exception {
        JsonNode alpha = mapper.readTree("{\"st\":\"XYZ\",\"bat\":50}");
        RobotStatus status = adapter.toStatus("amr-09", alpha);
        assertThat(status.state()).isEqualTo(RobotState.UNKNOWN);
        assertThat(status.posX()).isNull();
        assertThat(status.posY()).isNull();
        assertThat(status.reportedAt()).isNotNull();
    }

    @Test
    void topicRobotIdWinsOverPayloadId() throws Exception {
        JsonNode alpha = mapper.readTree("{\"id\":\"other\",\"st\":\"IDL\",\"bat\":10}");
        assertThat(adapter.toStatus("amr-01", alpha).robotId()).isEqualTo("amr-01");
    }
}
