package com.realestate.backend.controller;

import com.realestate.backend.model.Listing;
import com.realestate.backend.service.ListingService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/listings")
@RequiredArgsConstructor
public class ListingController {

    private final ListingService listingService;

    @GetMapping
    public ResponseEntity<List<Listing>> getAllListings() {
        return ResponseEntity.ok(listingService.getAllListings());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Listing> getListingById(@PathVariable UUID id) {
        return ResponseEntity.ok(listingService.getListingById(id));
    }

    @PostMapping
    public ResponseEntity<Listing> createListing(@RequestBody Listing listing) {
        return ResponseEntity.ok(listingService.createListing(listing));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteListing(@PathVariable UUID id) {
        listingService.deleteListing(id);
        return ResponseEntity.noContent().build();
    }
}