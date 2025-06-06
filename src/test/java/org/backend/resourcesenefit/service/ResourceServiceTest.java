package org.backend.resourcesenefit.service;

import jakarta.persistence.EntityNotFoundException;
import org.backend.resourcesenefit.dto.*;
import org.backend.resourcesenefit.entity.*;
import org.backend.resourcesenefit.kafka.NotificationProducer;
import org.backend.resourcesenefit.mapper.ResourceMapper;
import org.backend.resourcesenefit.repository.ResourceRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import java.util.*;

import static org.backend.resourcesenefit.model.CharacteristicType.CONSUMPTION_TYPE;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ResourceServiceTest {

    @Mock
    private ResourceRepository resourceRepository;

    @Mock
    private ResourceMapper resourceMapper;

    @Mock
    private NotificationProducer kafkaProducer;

    @InjectMocks
    private ResourceService resourceService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void createResource_shouldSetResourceOnLocation_whenLocationIsNotNull() {
        ResourceDto dto = new ResourceDto();
        Resource resource = new Resource();
        Location location = new Location();
        resource.setLocation(location);

        Resource savedResource = new Resource();
        ResourceDto savedDto = new ResourceDto();

        when(resourceMapper.toEntity(dto)).thenReturn(resource);
        when(resourceRepository.save(resource)).thenReturn(savedResource);
        when(resourceMapper.toDto(savedResource)).thenReturn(savedDto);

        ResourceDto result = resourceService.createResource(dto);

        assertEquals(savedDto, result);
        assertEquals(resource, location.getResource());
        verify(resourceRepository).save(resource);
    }

    @Test
    void createResource_shouldSetResourceOnCharacteristics_whenCharacteristicsNotNull() {
        ResourceDto dto = new ResourceDto();
        Resource resource = new Resource();
        Characteristic c1 = new Characteristic();
        Characteristic c2 = new Characteristic();

        resource.setCharacteristics(List.of(c1, c2));

        Resource savedResource = new Resource();
        ResourceDto savedDto = new ResourceDto();

        when(resourceMapper.toEntity(dto)).thenReturn(resource);
        when(resourceRepository.save(resource)).thenReturn(savedResource);
        when(resourceMapper.toDto(savedResource)).thenReturn(savedDto);

        ResourceDto result = resourceService.createResource(dto);

        assertEquals(savedDto, result);
        assertEquals(resource, c1.getResource());
        assertEquals(resource, c2.getResource());
        verify(resourceRepository).save(resource);
    }

    @Test
    void getAllResources_shouldReturnListOfDtos() {
        List<Resource> entities = List.of(new Resource(), new Resource());

        when(resourceRepository.findAll()).thenReturn(entities);
        when(resourceMapper.toDto(any())).thenReturn(new ResourceDto());

        List<ResourceDto> result = resourceService.getAllResources();

        assertEquals(2, result.size());
        verify(resourceRepository).findAll();
    }

    @Test
    void getResourceById_shouldReturnDto_ifFound() {
        Resource resource = new Resource();
        ResourceDto dto = new ResourceDto();

        when(resourceRepository.findById(1L)).thenReturn(Optional.of(resource));
        when(resourceMapper.toDto(resource)).thenReturn(dto);

        ResourceDto result = resourceService.getResourceById(1L);

        assertEquals(dto, result);
    }

    @Test
    void getResourceById_shouldThrow_ifNotFound() {
        when(resourceRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> resourceService.getResourceById(1L));
    }

    @Test
    void updateResourceById_shouldUpdateLocationAndCharacteristics() {
        Resource existing = new Resource();
        existing.setLocation(new Location());
        existing.setCharacteristics(new ArrayList<>());

        LocationDto locationDto = new LocationDto();
        locationDto.setStreetAddress("Tartu mnt 1");
        locationDto.setCity("Tallinn");
        locationDto.setPostalCode("12345");
        locationDto.setCountryCode("EE");

        CharacteristicDto c1 = new CharacteristicDto();
        c1.setCode("C001");
        c1.setType(CONSUMPTION_TYPE);
        c1.setValue("value");

        ResourceDto updateDto = new ResourceDto();
        updateDto.setLocation(locationDto);
        updateDto.setCharacteristics(List.of(c1));

        Resource updated = new Resource();
        ResourceDto updatedDto = new ResourceDto();

        when(resourceRepository.findById(1L)).thenReturn(Optional.of(existing));
        when(resourceRepository.saveAndFlush(existing)).thenReturn(updated);
        when(resourceMapper.toDto(updated)).thenReturn(updatedDto);

        ResourceDto result = resourceService.updateResourceById(1L, updateDto);

        assertEquals(updatedDto, result);
        verify(kafkaProducer).sendNotification(updatedDto);
        verify(resourceRepository).saveAndFlush(existing);

        assertEquals("Tartu mnt 1", existing.getLocation().getStreetAddress());
        assertEquals(1, existing.getCharacteristics().size());
        assertEquals("C001", existing.getCharacteristics().getFirst().getCode());
    }

    @Test
    void updateResourceById_shouldThrow_ifNotFound() {
        when(resourceRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> resourceService.updateResourceById(1L, new ResourceDto()));
    }


    //@Test
    //void deleteResourceById_shouldDelete_ifExists() {
    //    when(resourceRepository.existsById(1L)).thenReturn(true);
//
    //    boolean result = resourceService.deleteResourceById(1L);
//
    //    assertTrue(result);
    //    verify(resourceRepository).deleteById(1L);
    //}

    //@Test
    //void deleteResourceById_shouldReturnFalse_ifNotExists() {
    //    when(resourceRepository.existsById(1L)).thenReturn(false);
//
    //    boolean result = resourceService.deleteResourceById(1L);
//
    //    assertFalse(result);
    //}

    @Test
    void notifyAllResources_shouldSendAllToKafka() {
        List<Resource> entities = List.of(new Resource());
        List<ResourceDto> dtos = List.of(new ResourceDto());

        when(resourceRepository.findAll()).thenReturn(entities);
        when(resourceMapper.toDto(any())).thenReturn(dtos.getFirst());

        resourceService.notifyAllResources();

        verify(kafkaProducer).sendAllResources(dtos);
    }
}
