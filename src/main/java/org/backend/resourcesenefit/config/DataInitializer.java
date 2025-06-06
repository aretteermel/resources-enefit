package org.backend.resourcesenefit.config;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.backend.resourcesenefit.entity.*;
import org.backend.resourcesenefit.model.*;
import org.backend.resourcesenefit.repository.ResourceRepository;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

import static org.backend.resourcesenefit.model.CharacteristicType.CHARGING_POINT;
import static org.backend.resourcesenefit.model.CharacteristicType.CONNECTION_POINT_STATUS;
import static org.backend.resourcesenefit.model.CharacteristicType.CONSUMPTION_TYPE;
import static org.backend.resourcesenefit.model.ResourceType.CONNECTION_POINT;
import static org.backend.resourcesenefit.model.ResourceType.METERING_POINT;

@Configuration
@RequiredArgsConstructor
@Slf4j
public class DataInitializer {

    private final ResourceRepository resourceRepository;

    @Bean
    public ApplicationRunner loadSampleData() {
        return args -> {
            if (resourceRepository.count() > 0) {
                log.info("Sample data already loaded.");
                return;
            }

            log.info("Loading sample data...");

            Resource resource1 = createResource(
                    METERING_POINT, "EE",
                    new Location("Laki 11", "Tallinn", "10621", "EE"),
                    List.of(
                            new Characteristic("C001", CONSUMPTION_TYPE, "Industrial"),
                            new Characteristic("C002", CHARGING_POINT, "FastDC")
                    )
            );

            Resource resource2 = createResource(
                    CONNECTION_POINT, "FI",
                    new Location("Mannerheimintie 5", "Helsinki", "00100", "FI"),
                    List.of(
                            new Characteristic("C003", CONSUMPTION_TYPE, "Residential"),
                            new Characteristic("C004", CONNECTION_POINT_STATUS, "Active")
                    )
            );

            Resource resource3 = createResource(
                    METERING_POINT, "FI",
                    new Location("Itäkatu 12", "Espoo", "02100", "FI"),
                    List.of(
                            new Characteristic("C005", CONSUMPTION_TYPE, "Commercial"),
                            new Characteristic("C006", CHARGING_POINT, "Type2")
                    )
            );

            resourceRepository.saveAll(List.of(resource1, resource2, resource3));

            log.info("Sample data loaded.");
        };
    }

    private Resource createResource(ResourceType type, String countryCode, Location location, List<Characteristic> characteristics) {
        Resource resource = new Resource();
        resource.setType(type);
        resource.setCountryCode(countryCode);

        location.setResource(resource);
        resource.setLocation(location);

        for (Characteristic characteristic : characteristics) {
            characteristic.setResource(resource);
        }
        resource.setCharacteristics(characteristics);

        return resource;
    }
}
