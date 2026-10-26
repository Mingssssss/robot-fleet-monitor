package io.github.mingssssss.fleet.ingest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.within;

import java.time.Instant;

import org.junit.jupiter.api.Test;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import io.github.mingssssss.fleet.robot.RobotState;

class VendorBAdapterTest {

    private final ObjectMapper mapper = new ObjectMapper();
    private final VendorBAdapter adapter = new VendorBAdapter();

    @Test
    void supportsBravoPayloadOnly() throws Exception {
        JsonNode bravo = mapper.readTree("{\"robotId\":\"arm-02\",\"status\":\"idle\",\"batteryLevel\":0.62,\"x\":10.0,\"y\":2.0,\"timestamp\":\"2026-10-26T10:00:00Z\"}");
        JsonNode alpha = mapper.readTree("{\"id\":\"amr-01\",\"st\":\"MOV\",\"bat\":87.5}");
        assertThat(adapter.supports(bravo)).isTrue();
        assertThat(adapter.supports(alpha)).isFalse();
    }

    @Test
    void mapsFieldsToCommonModel() throws Exception {
        JsonNode bravo = mapper.readTree("{\"robotId\":\"arm-02\",\"status\":\"Charging\",\"batteryLevel\":0.62,\"x\":10.0,\"y\":2.0,\"timestamp\":\"2026-10-26T10:00:00Z\"}");
        RobotStatus status = adapter.toStatus("arm-02", bravo);
        assertThat(status.vendor()).isEqualTo("BRAVO");
        assertThat(status.state()).isEqualTo(RobotState.CHARGING);
        assertThat(status.batteryPct()).isCloseTo(62.0, within(0.0001));
        assertThat(status.posX()).isEqualTo(10.0);
        assertThat(status.posY()).isEqualTo(2.0);
        assertThat(status.reportedAt()).isEqualTo(Instant.parse("2026-10-26T10:00:00Z"));
    }

    @Test
    void missingOptionalFields() throws Exception {
        JsonNode bravo = mapper.readTree("{\"status\":\"weird\",\"batteryLevel\":1.0}");
        RobotStatus status = adapter.toStatus("arm-03", bravo);
        assertThat(status.state()).isEqualTo(RobotState.UNKNOWN);
        assertThat(status.batteryPct()).isCloseTo(100.0, within(0.0001));
        assertThat(status.posX()).isNull();
        assertThat(status.reportedAt()).isNotNull();
    }
}
