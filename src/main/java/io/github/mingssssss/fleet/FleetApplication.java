package io.github.mingssssss.fleet;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;
import org.springframework.integration.annotation.IntegrationComponentScan;

@SpringBootApplication
@ConfigurationPropertiesScan
@IntegrationComponentScan
public class FleetApplication {

    public static void main(String[] args) {
        SpringApplication.run(FleetApplication.class, args);
    }
}
