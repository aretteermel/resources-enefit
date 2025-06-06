package org.backend.resourcesenefit.mapper;

import org.backend.resourcesenefit.dto.*;
import org.backend.resourcesenefit.entity.*;
import org.backend.resourcesenefit.model.CharacteristicType;
import org.backend.resourcesenefit.model.ResourceType;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.backend.resourcesenefit.model.CharacteristicType.CONSUMPTION_TYPE;
import static org.backend.resourcesenefit.model.ResourceType.CONNECTION_POINT;
import static org.junit.jupiter.api.Assertions.*;

class ResourceMapperTest {

    private final ResourceMapper mapper = new ResourceMapper();

    @Test
    void toEntity_shouldMapDtoToEntityCorrectly() {
        ResourceDto dto = new ResourceDto();
        dto.setType(CONNECTION_POINT);
        dto.setCountryCode("EE");

        LocationDto locationDto = new LocationDto();
        locationDto.setStreetAddress("Tartu mnt 1");
        locationDto.setCity("Tallinn");
        locationDto.setPostalCode("12345");
        locationDto.setCountryCode("EE");
        dto.setLocation(locationDto);

        CharacteristicDto cDto = new CharacteristicDto();
        cDto.setCode("C001");
        cDto.setType(CharacteristicType.CHARGING_POINT);
        cDto.setValue("Characteristic value");
        dto.setCharacteristics(List.of(cDto));

        Resource entity = mapper.toEntity(dto);

        assertEquals(CONNECTION_POINT, entity.getType());
        assertEquals("EE", entity.getCountryCode());
        assertNotNull(entity.getLocation());
        assertEquals("Tartu mnt 1", entity.getLocation().getStreetAddress());
        assertEquals(1, entity.getCharacteristics().size());
        assertEquals("C001", entity.getCharacteristics().getFirst().getCode());
    }

    @Test
    void toDto_shouldMapEntityToDtoCorrectly() {
        Resource entity = new Resource();
        entity.setId(1L);
        entity.setType(ResourceType.METERING_POINT);
        entity.setCountryCode("EE");

        Location loc = new Location();
        loc.setId(10L);
        loc.setStreetAddress("Tartu mnt 1");
        loc.setCity("Tallinn");
        loc.setPostalCode("12345");
        loc.setCountryCode("EE");
        entity.setLocation(loc);

        Characteristic c = new Characteristic();
        c.setId(99L);
        c.setCode("C001");
        c.setType(CONSUMPTION_TYPE);
        c.setValue("Characteristic value");
        entity.setCharacteristics(List.of(c));

        ResourceDto dto = mapper.toDto(entity);

        assertEquals(1L, dto.getId());
        assertEquals(ResourceType.METERING_POINT, dto.getType());
        assertEquals("EE", dto.getCountryCode());
        assertNotNull(dto.getLocation());
        assertEquals("Tartu mnt 1", dto.getLocation().getStreetAddress());
        assertEquals(1, dto.getCharacteristics().size());
        assertEquals("C001", dto.getCharacteristics().getFirst().getCode());
    }
}
