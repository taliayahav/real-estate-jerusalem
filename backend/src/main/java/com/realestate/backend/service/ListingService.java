package com.realestate.backend.service;

import com.realestate.backend.dto.ListingRequest;
import com.realestate.backend.dto.ListingResponse;
import com.realestate.backend.model.Listing;
import com.realestate.backend.repository.ListingRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ListingService {

    private final ListingRepository listingRepository;

    public List<ListingResponse> getAllListings() {
        return listingRepository.findAll()
                .stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    public ListingResponse getListingById(UUID id) {
        Listing listing = listingRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Listing not found"));
        return toResponse(listing);
    }

    public ListingResponse createListing(ListingRequest request) {
        Listing listing = toEntity(request);
        Listing saved = listingRepository.save(listing);
        return toResponse(saved);
    }

    public void deleteListing(UUID id) {
        listingRepository.deleteById(id);
    }

    // --- private helpers ---

    private Listing toEntity(ListingRequest request) {
        Listing listing = new Listing();
        listing.setTitle(request.getTitle());
        listing.setDescription(request.getDescription());
        listing.setAddress(request.getAddress());
        listing.setNeighborhood(request.getNeighborhood());
        listing.setPrice(request.getPrice());
        listing.setBedrooms(request.getBedrooms());
        listing.setBathrooms(request.getBathrooms());
        listing.setSizeInSqm(request.getSizeInSqm());
        listing.setListingType(request.getListingType());
        listing.setHasParking(request.getHasParking());
        listing.setHasBalcony(request.getHasBalcony());
        listing.setHasWasherDryer(request.getHasWasherDryer());
        listing.setHasDishwasher(request.getHasDishwasher());
        listing.setHasAC(request.getHasAC());
        listing.setHasElevator(request.getHasElevator());
        listing.setIsFurnished(request.getIsFurnished());
        listing.setIsPetFriendly(request.getIsPetFriendly());
        listing.setHasShabbatElevator(request.getHasShabbatElevator());
        listing.setHasSukkahBalcony(request.getHasSukkahBalcony());
        listing.setHasMamad(request.getHasMamad());
        return listing;
    }

    private ListingResponse toResponse(Listing listing) {
        ListingResponse response = new ListingResponse();
        response.setId(listing.getId());
        response.setTitle(listing.getTitle());
        response.setDescription(listing.getDescription());
        response.setAddress(listing.getAddress());
        response.setNeighborhood(listing.getNeighborhood());
        response.setPrice(listing.getPrice());
        response.setBedrooms(listing.getBedrooms());
        response.setBathrooms(listing.getBathrooms());
        response.setSizeInSqm(listing.getSizeInSqm());
        response.setListingType(listing.getListingType());
        response.setHasParking(listing.getHasParking());
        response.setHasBalcony(listing.getHasBalcony());
        response.setHasWasherDryer(listing.getHasWasherDryer());
        response.setHasDishwasher(listing.getHasDishwasher());
        response.setHasAC(listing.getHasAC());
        response.setHasElevator(listing.getHasElevator());
        response.setIsFurnished(listing.getIsFurnished());
        response.setIsPetFriendly(listing.getIsPetFriendly());
        response.setHasShabbatElevator(listing.getHasShabbatElevator());
        response.setHasSukkahBalcony(listing.getHasSukkahBalcony());
        response.setHasMamad(listing.getHasMamad());
        response.setCreatedAt(listing.getCreatedAt());
        return response;
    }
}
