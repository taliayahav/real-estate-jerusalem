package com.realestate.backend.dto;

import com.realestate.backend.model.ListingType;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
public class ListingResponse {
    private UUID id;
    private String title;
    private String description;
    private String address;
    private String neighborhood;
    private Long price;
    private Integer bedrooms;
    private Integer bathrooms;
    private Integer sizeInSqm;
    private ListingType listingType;
    private Boolean hasParking;
    private Boolean hasBalcony;
    private Boolean hasWasherDryer;
    private Boolean hasDishwasher;
    private Boolean hasAC;
    private Boolean hasElevator;
    private Boolean isFurnished;
    private Boolean isPetFriendly;
    private Boolean hasShabbatElevator;
    private Boolean hasSukkahBalcony;
    private Boolean hasMamad;
    private LocalDateTime createdAt;
}