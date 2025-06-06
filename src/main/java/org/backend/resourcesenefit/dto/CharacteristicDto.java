package org.backend.resourcesenefit.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import org.backend.resourcesenefit.model.CharacteristicType;

@Getter
@Setter
public class CharacteristicDto {
    private Long id;

    @Size(max = 5)
    @NotBlank
    private String code;

    @NotNull
    private CharacteristicType type;

    @Size(max = 500)
    @NotBlank
    private String value;
}
