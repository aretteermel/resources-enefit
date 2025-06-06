package org.backend.resourcesenefit.service;

import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.backend.resourcesenefit.dto.CharacteristicDto;
import org.backend.resourcesenefit.dto.ResourceDto;
import org.backend.resourcesenefit.entity.Characteristic;
import org.backend.resourcesenefit.entity.Location;
import org.backend.resourcesenefit.entity.Resource;
import org.backend.resourcesenefit.kafka.NotificationProducer;
import org.backend.resourcesenefit.mapper.ResourceMapper;
import org.backend.resourcesenefit.repository.ResourceRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ResourceService {
    private final ResourceRepository resourceRepository;
    private final ResourceMapper resourceMapper;
    private final NotificationProducer kafkaProducer;

    @Transactional
    public ResourceDto createResource(ResourceDto dto) {
        Resource resource = resourceMapper.toEntity(dto);

        if (resource.getLocation() != null) {
            resource.getLocation().setResource(resource);
        }

        if (resource.getCharacteristics() != null) {
            for (Characteristic c : resource.getCharacteristics()) {
                c.setResource(resource);
            }
        }

        Resource saved = resourceRepository.save(resource);
        return resourceMapper.toDto(saved);
    }

    public List<ResourceDto> getAllResources() {
        return resourceRepository.findAll()
                .stream()
                .map(resourceMapper::toDto)
                .toList();
    }

    public ResourceDto getResourceById(Long id) {
        Resource resource = resourceRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Resource with id " + id + " not found"));
        return resourceMapper.toDto(resource);
    }

    @Transactional
    public ResourceDto updateResourceById(Long id, ResourceDto dto) {
        Resource existing = resourceRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Resource with id " + id + " not found"));

        existing.setType(dto.getType());
        existing.setCountryCode(dto.getCountryCode());

        if (dto.getLocation() != null) {
            Location loc = existing.getLocation();
            loc.setStreetAddress(dto.getLocation().getStreetAddress());
            loc.setCity(dto.getLocation().getCity());
            loc.setPostalCode(dto.getLocation().getPostalCode());
            loc.setCountryCode(dto.getLocation().getCountryCode());
        }

        existing.getCharacteristics().clear();

        for (CharacteristicDto cDto : dto.getCharacteristics()) {
            Characteristic characteristic = new Characteristic();
            characteristic.setCode(cDto.getCode());
            characteristic.setType(cDto.getType());
            characteristic.setValue(cDto.getValue());
            characteristic.setResource(existing);
            existing.getCharacteristics().add(characteristic);
        }

        Resource saved = resourceRepository.saveAndFlush(existing);

        ResourceDto updatedDto = resourceMapper.toDto(saved);
        kafkaProducer.sendNotification(updatedDto);

        return updatedDto;
    }

    public void deleteResourceById(Long id) {
        Resource resource = resourceRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Resource with id " + id + " not found"));
        resourceRepository.delete(resource);
    }

    public void notifyAllResources() {
        List<ResourceDto> dtos = resourceRepository.findAll()
                .stream()
                .map(resourceMapper::toDto)
                .toList();
        kafkaProducer.sendAllResources(dtos);
    }

}
