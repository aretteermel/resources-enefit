package org.backend.resourcesenefit.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.backend.resourcesenefit.dto.ResourceDto;
import org.backend.resourcesenefit.service.ResourceService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/resources")
@RequiredArgsConstructor
public class ResourceController {
    private final ResourceService resourceService;

    @PostMapping
    public ResponseEntity<ResourceDto> createResource(@Valid @RequestBody ResourceDto resourceDto) {
        ResourceDto resource = resourceService.createResource(resourceDto);
        return ResponseEntity.ok(resource);
    }

    @GetMapping
    public ResponseEntity<List<ResourceDto>> getAllResources() {
        List<ResourceDto> resources = resourceService.getAllResources();
        return ResponseEntity.ok(resources);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ResourceDto> getResource(@PathVariable Long id) {
        ResourceDto resource = resourceService.getResourceById(id);
        return ResponseEntity.ok(resource);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ResourceDto> updateResource(@PathVariable Long id, @Valid @RequestBody ResourceDto resourceDto) {
        ResourceDto resource = resourceService.updateResourceById(id, resourceDto);
        return ResponseEntity.ok(resource);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteResource(@PathVariable Long id) {
        resourceService.deleteResourceById(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/notify-all")
    public ResponseEntity<String> notifyAllResources() {
        resourceService.notifyAllResources();
        return ResponseEntity.ok("All resources sent to Kafka.");
    }

}
