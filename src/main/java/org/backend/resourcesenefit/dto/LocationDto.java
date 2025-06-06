package org.backend.resourcesenefit.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LocationDto {
    private Long id;

    @Size(max = 100)
    @NotBlank
    private String streetAddress;

    @Size(max = 100)
    @NotBlank
    private String city;

    @Size(max = 12)
    @NotBlank
    private String postalCode;

    @Size(min = 2, max = 2, message = "Country code must be exactly 2 characters")
    @NotBlank
    private String countryCode;
}
