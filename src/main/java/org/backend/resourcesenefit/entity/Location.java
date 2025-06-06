package org.backend.resourcesenefit.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Location {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String streetAddress;

    @Column(nullable = false)
    private String city;

    @Column(nullable = false)
    private String postalCode;

    @Column(nullable = false)
    private String countryCode;

    @OneToOne
    @JoinColumn(name = "resource_id", nullable = false, unique = true)
    private Resource resource;

    public Location(String streetAddress, String city, String postalCode, String countryCode) {
        this.streetAddress = streetAddress;
        this.city = city;
        this.postalCode = postalCode;
        this.countryCode = countryCode;
    }
}
