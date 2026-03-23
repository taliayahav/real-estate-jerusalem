package com.realestate.backend.service;

import com.realestate.backend.model.Listing;
import com.realestate.backend.repository.ListingRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ListingService {

    private final ListingRepository listingRepository;

    public List<Listing> getAllListings() {
        return listingRepository.findAll();
    }

    public Listing getListingById(UUID id) {
        return listingRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Listing not found"));
    }

    public Listing createListing(Listing listing) {
        return listingRepository.save(listing);
    }

    public void deleteListing(UUID id) {
        listingRepository.deleteById(id);
    }
}
