package org.backend.resourcesenefit.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.backend.resourcesenefit.model.CharacteristicType;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Characteristic {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String code;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private CharacteristicType type;

    @Column(nullable = false)
    private String value;

    @ManyToOne
    @JoinColumn(name = "resource_id", nullable = false)
    private Resource resource;

    public Characteristic(String code, CharacteristicType type, String value) {
        this.code = code;
        this.type = type;
        this.value = value;
    }
}
