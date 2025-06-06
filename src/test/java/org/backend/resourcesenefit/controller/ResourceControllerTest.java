package org.backend.resourcesenefit.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.EntityNotFoundException;
import org.backend.resourcesenefit.dto.CharacteristicDto;
import org.backend.resourcesenefit.dto.LocationDto;
import org.backend.resourcesenefit.dto.ResourceDto;
import org.backend.resourcesenefit.service.ResourceService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.backend.resourcesenefit.model.CharacteristicType.CONSUMPTION_TYPE;
import static org.backend.resourcesenefit.model.ResourceType.METERING_POINT;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(ResourceController.class)
class ResourceControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private ResourceService resourceService;

    @TestConfiguration
    static class MockConfig {
        @Bean
        public ResourceService resourceService() {
            return Mockito.mock(ResourceService.class);
        }
    }

    private ResourceDto mockResourceDto() {
        LocationDto locationDto = new LocationDto();
        locationDto.setStreetAddress("Tartu mnt 1");
        locationDto.setCity("Tallinn");
        locationDto.setPostalCode("12345");
        locationDto.setCountryCode("EE");

        CharacteristicDto characteristicDto = new CharacteristicDto();
        characteristicDto.setId(1L);
        characteristicDto.setCode("C001");
        characteristicDto.setType(CONSUMPTION_TYPE);
        characteristicDto.setValue("Characteristic value");

        ResourceDto dto = new ResourceDto();
        dto.setId(1L);
        dto.setType(METERING_POINT);
        dto.setCountryCode("EE");
        dto.setLocation(locationDto);
        dto.setCharacteristics(List.of(characteristicDto));
        return dto;
    }

    @Test
    void createResource_shouldReturnCreatedResource() throws Exception {
        ResourceDto resourceDto = mockResourceDto();

        Mockito.when(resourceService.createResource(any(ResourceDto.class))).thenReturn(resourceDto);

        mockMvc.perform(post("/api/resources")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(resourceDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(resourceDto.getId()));
    }

    @Test
    void createResource_shouldReturnBadRequest_whenInputIsInvalid() throws Exception {
        ResourceDto invalidDto = new ResourceDto();

        mockMvc.perform(post("/api/resources")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidDto)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.type").exists());
    }

    @Test
    void createResource_shouldReturn500_whenServiceFails() throws Exception {
        ResourceDto dto = mockResourceDto();

        Mockito.when(resourceService.createResource(any())).thenThrow(new RuntimeException());

        mockMvc.perform(post("/api/resources")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isInternalServerError());
    }

    @Test
    void getAllResources_shouldReturnList() throws Exception {
        List<ResourceDto> list = List.of(mockResourceDto());
        Mockito.when(resourceService.getAllResources()).thenReturn(list);

        mockMvc.perform(get("/api/resources"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1));
    }

    @Test
    void getResourceById_shouldReturnResource() throws Exception {
        ResourceDto dto = mockResourceDto();
        Mockito.when(resourceService.getResourceById(1L)).thenReturn(dto);

        mockMvc.perform(get("/api/resources/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(dto.getId()));
    }

    @Test
    void getResourceById_shouldReturn500_whenServiceFails() throws Exception {
        Mockito.when(resourceService.getResourceById(1L))
                .thenThrow(new RuntimeException());

        mockMvc.perform(get("/api/resources/1"))
                .andExpect(status().isInternalServerError());
    }

    @Test
    void updateResource_shouldReturnUpdatedResource() throws Exception {
        ResourceDto dto = mockResourceDto();
        Mockito.when(resourceService.updateResourceById(eq(1L), any(ResourceDto.class))).thenReturn(dto);

        mockMvc.perform(put("/api/resources/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(dto.getId()));
    }

    @Test
    void updateResource_shouldReturn404_whenResourceNotFound() throws Exception {
        ResourceDto dto = mockResourceDto();

        Mockito.when(resourceService.updateResourceById(eq(1L), any(ResourceDto.class)))
                .thenThrow(new EntityNotFoundException("Resource with id 1 not found"));

        mockMvc.perform(put("/api/resources/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value("Resource with id 1 not found"));
    }

    //@Test
    //void deleteResource_shouldReturnNoContentIfDeleted() throws Exception {
    //    Mockito.when(resourceService.deleteResourceById(1L)).thenReturn(true);
//
    //    mockMvc.perform(delete("/api/resources/1"))
    //            .andExpect(status().isNoContent());
    //}

    //@Test
    //void deleteResource_shouldReturnNotFoundIfNotDeleted() throws Exception {
    //    Mockito.when(resourceService.deleteResourceById(1L)).thenReturn(false);
//
    //    mockMvc.perform(delete("/api/resources/1"))
    //            .andExpect(status().isNotFound());
    //}

    @Test
    void notifyAllResources_shouldReturnSuccessMessage() throws Exception {
        doNothing().when(resourceService).notifyAllResources();

        mockMvc.perform(post("/api/resources/notify-all"))
                .andExpect(status().isOk())
                .andExpect(content().string("All resources sent to Kafka."));
    }

    @Test
    void notifyAllResources_shouldReturn500_whenServiceFails() throws Exception {
        Mockito.doThrow(new RuntimeException("Kafka failure"))
                .when(resourceService).notifyAllResources();

        mockMvc.perform(post("/api/resources/notify-all"))
                .andExpect(status().isInternalServerError())
                .andExpect(jsonPath("$.message").value("Kafka failure"));
    }

}
