package com.realestate.backend.controller;

import com.realestate.backend.dto.ListingFilter;
import com.realestate.backend.dto.ListingRequest;
import com.realestate.backend.dto.ListingResponse;
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
    public ResponseEntity<List<ListingResponse>> getAllListings(@ModelAttribute ListingFilter filter) {
        return ResponseEntity.ok(listingService.getAllListings(filter));
    }

    @GetMapping("/semantic-search")
    public ResponseEntity<List<ListingResponse>> semanticSearch(
            @RequestParam String q,
            @RequestParam(defaultValue = "10") int limit) {
        return ResponseEntity.ok(listingService.semanticSearch(q, limit));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ListingResponse> getListingById(@PathVariable UUID id) {
        return ResponseEntity.ok(listingService.getListingById(id));
    }

    @PostMapping
    public ResponseEntity<ListingResponse> createListing(@RequestBody ListingRequest request) {
        return ResponseEntity.ok(listingService.createListing(request));
    }

    @PostMapping("/reindex")
    public ResponseEntity<String> reindex() {
        listingService.reindexAllListings();
        return ResponseEntity.ok("All listings reindexed");
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteListing(@PathVariable UUID id) {
        listingService.deleteListing(id);
        return ResponseEntity.noContent().build();
    }
}
