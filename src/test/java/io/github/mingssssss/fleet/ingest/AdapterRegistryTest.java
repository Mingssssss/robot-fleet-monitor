package io.github.mingssssss.fleet.ingest;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;

import org.junit.jupiter.api.Test;

import com.fasterxml.jackson.databind.ObjectMapper;

class AdapterRegistryTest {

    private final ObjectMapper mapper = new ObjectMapper();
    private final AdapterRegistry registry = new AdapterRegistry(List.of(new VendorAAdapter(), new VendorBAdapter()));

    @Test
    void picksAdapterByPayloadShape() throws Exception {
        assertThat(registry.find(mapper.readTree("{\"st\":\"IDL\",\"bat\":5}")))
                .map(VendorAdapter::vendor).contains("ALPHA");
        assertThat(registry.find(mapper.readTree("{\"status\":\"idle\",\"batteryLevel\":0.5}")))
                .map(VendorAdapter::vendor).contains("BRAVO");
    }

    @Test
    void unknownShapeIsEmpty() throws Exception {
        assertThat(registry.find(mapper.readTree("{\"hello\":\"world\"}"))).isEmpty();
    }
}
