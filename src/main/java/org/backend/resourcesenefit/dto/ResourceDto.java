package org.backend.resourcesenefit.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import org.backend.resourcesenefit.model.ResourceType;

import java.util.List;

@Getter
@Setter
public class ResourceDto {
    private Long id;

    @NotNull(message = "Resource type must not be null")
    private ResourceType type;

    @Size(min = 2, max = 2, message = "Country code must be exactly 2 characters")
    @NotBlank
    private String countryCode;

    @Valid
    @NotNull(message = "Location is required")
    private LocationDto location;

    @Valid
    @NotNull(message = "Characteristics list must not be null")
    @Size(min = 1, message = "At least one characteristic is required")
    private List<CharacteristicDto> characteristics;
}
