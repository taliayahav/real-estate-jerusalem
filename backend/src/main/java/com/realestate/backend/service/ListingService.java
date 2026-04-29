package com.realestate.backend.service;

import com.realestate.backend.dto.ListingFilter;
import com.realestate.backend.dto.ListingRequest;
import com.realestate.backend.dto.ListingResponse;
import com.realestate.backend.model.Listing;
import com.realestate.backend.repository.ListingRepository;
import com.realestate.backend.repository.VectorRepository;
import com.realestate.backend.specification.ListingSpecification;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ListingService {

    private final ListingRepository listingRepository;
    private final VectorRepository vectorRepository;
    private final EmbeddingService embeddingService;

    public List<ListingResponse> getAllListings(ListingFilter filter) {
        return listingRepository.findAll(ListingSpecification.withFilters(filter))
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
        float[] embedding = embeddingService.embedListing(saved);
        vectorRepository.saveEmbedding(saved.getId(), embedding);
        return toResponse(saved);
    }

    public void deleteListing(UUID id) {
        listingRepository.deleteById(id);
    }

    public List<ListingResponse> semanticSearch(String query, int limit) {
        float[] queryEmbedding = embeddingService.embedQuery(query);
        List<UUID> ids = vectorRepository.findSimilar(queryEmbedding, limit);
        return ids.stream()
                .map(id -> listingRepository.findById(id).orElse(null))
                .filter(listing -> listing != null)
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    public void reindexAllListings() {
        listingRepository.findAll().forEach(listing -> {
            float[] embedding = embeddingService.embedListing(listing);
            vectorRepository.saveEmbedding(listing.getId(), embedding);
        });
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
