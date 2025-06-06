package org.backend.resourcesenefit.mapper;

import org.backend.resourcesenefit.dto.CharacteristicDto;
import org.backend.resourcesenefit.dto.LocationDto;
import org.backend.resourcesenefit.dto.ResourceDto;
import org.backend.resourcesenefit.entity.Characteristic;
import org.backend.resourcesenefit.entity.Location;
import org.backend.resourcesenefit.entity.Resource;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class ResourceMapper {

    public Resource toEntity(ResourceDto dto) {
        Resource resource = new Resource();
        resource.setType(dto.getType());
        resource.setCountryCode(dto.getCountryCode());

        if (dto.getLocation() != null) {
            Location loc = new Location();
            loc.setStreetAddress(dto.getLocation().getStreetAddress());
            loc.setCity(dto.getLocation().getCity());
            loc.setPostalCode(dto.getLocation().getPostalCode());
            loc.setCountryCode(dto.getLocation().getCountryCode());
            loc.setResource(resource);
            resource.setLocation(loc);
        }

        if (dto.getCharacteristics() != null) {
            List<Characteristic> characteristics = dto.getCharacteristics().stream()
                    .map(c -> {
                        Characteristic characteristic = new Characteristic();
                        characteristic.setCode(c.getCode());
                        characteristic.setType(c.getType());
                        characteristic.setValue(c.getValue());
                        characteristic.setResource(resource);
                        return characteristic;
                    }).toList();
            resource.setCharacteristics(characteristics);
        }

        return resource;
    }

    public ResourceDto toDto(Resource resource) {
        ResourceDto dto = new ResourceDto();
        dto.setId(resource.getId());
        dto.setType(resource.getType());
        dto.setCountryCode(resource.getCountryCode());

        if (resource.getLocation() != null) {
            Location location = resource.getLocation();
            var locationDto = new LocationDto();
            locationDto.setId(location.getId());
            locationDto.setStreetAddress(location.getStreetAddress());
            locationDto.setCity(location.getCity());
            locationDto.setPostalCode(location.getPostalCode());
            locationDto.setCountryCode(location.getCountryCode());
            dto.setLocation(locationDto);
        }

        if (resource.getCharacteristics() != null) {
            List<CharacteristicDto> characteristicDtos = resource.getCharacteristics()
                    .stream()
                    .map(c -> {
                        var cDto = new CharacteristicDto();
                        cDto.setId(c.getId());
                        cDto.setCode(c.getCode());
                        cDto.setType(c.getType());
                        cDto.setValue(c.getValue());
                        return cDto;
                    })
                    .toList();
            dto.setCharacteristics(characteristicDtos);
        }

        return dto;
    }

}
