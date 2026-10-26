package io.github.mingssssss.fleet.ingest;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.JsonNode;

/**
 * 등록된 어댑터 중 메시지를 처리할 수 있는 것을 찾는다.
 * 스프링이 VendorAdapter 구현체(@Component)를 모두 모아 생성자로 넣어 준다.
 */
@Component
public class AdapterRegistry {

    private final List<VendorAdapter> adapters;

    public AdapterRegistry(List<VendorAdapter> adapters) {
        this.adapters = List.copyOf(adapters);
    }

    public Optional<VendorAdapter> find(JsonNode payload) {
        for (VendorAdapter adapter : adapters) {
            if (adapter.supports(payload)) {
                return Optional.of(adapter);
            }
        }
        return Optional.empty();
    }

    public List<String> vendors() {
        return adapters.stream().map(VendorAdapter::vendor).toList();
    }
}
