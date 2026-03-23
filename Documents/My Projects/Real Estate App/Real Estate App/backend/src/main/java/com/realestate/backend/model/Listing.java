package com.realestate.backend.model;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "listings")
@Data
public class Listing {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false)
    private String title;

    @Column(columnDefinition = "TEXT")
    private String description;

    @Column(nullable = false)
    private String address;

    @Column(nullable = false)
    private String neighborhood;

    @Column(nullable = false)
    private Long price;

    @Column(nullable = false)
    private Integer bedrooms;

    @Column(nullable = false)
    private Integer bathrooms;

    @Column(nullable = false)
    private Integer sizeInSqm;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ListingType listingType;

    @Column(nullable = false)
    private Boolean hasParking;

    @Column(nullable = false)
    private Boolean hasBalcony;

    @Column(nullable = false)
    private Boolean hasWasherDryer;

    @Column(nullable = false)
    private Boolean hasDishwasher;

    @Column(name = "has_ac", nullable = false)
    private Boolean hasAC;

    @Column(nullable = false)
    private Boolean hasElevator;

    @Column(nullable = false)
    private Boolean isFurnished;

    @Column(nullable = false)
    private Boolean isPetFriendly;

    @Column(nullable = false)
    private Boolean hasShabbatElevator;

    @Column(nullable = false)
    private Boolean hasSukkahBalcony;

    @Column(nullable = false)
    private Boolean hasMamad;

    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
    }

}