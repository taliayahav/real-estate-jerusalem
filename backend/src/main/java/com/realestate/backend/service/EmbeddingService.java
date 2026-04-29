package com.realestate.backend.service;

import com.realestate.backend.model.Listing;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Map;

@Service
public class EmbeddingService {

    private static final String OPENAI_URL = "https://api.openai.com/v1/embeddings";
    private static final String MODEL = "text-embedding-3-small";

    private final RestTemplate restTemplate = new RestTemplate();

    @Value("${openai.api-key}")
    private String apiKey;

    public float[] embedListing(Listing listing) {
        return callOpenAI(buildListingText(listing));
    }

    public float[] embedQuery(String query) {
        return callOpenAI(query);
    }

    @SuppressWarnings("unchecked")
    private float[] callOpenAI(String text) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(apiKey);

        Map<String, String> body = Map.of("model", MODEL, "input", text);
        HttpEntity<Map<String, String>> request = new HttpEntity<>(body, headers);

        Map<String, Object> response = restTemplate.postForObject(OPENAI_URL, request, Map.class);

        List<Map<String, Object>> data = (List<Map<String, Object>>) response.get("data");
        List<Double> vector = (List<Double>) data.get(0).get("embedding");

        float[] embedding = new float[vector.size()];
        for (int i = 0; i < vector.size(); i++) {
            embedding[i] = vector.get(i).floatValue();
        }
        return embedding;
    }

    private String buildListingText(Listing listing) {
        StringBuilder sb = new StringBuilder();
        sb.append(listing.getTitle()).append(". ");
        if (listing.getDescription() != null && !listing.getDescription().isBlank()) {
            sb.append(listing.getDescription()).append(". ");
        }
        sb.append("Located in ").append(listing.getNeighborhood()).append(". ");
        sb.append(listing.getBedrooms()).append(" bedrooms, ");
        sb.append(listing.getBathrooms()).append(" bathrooms, ");
        sb.append(listing.getSizeInSqm()).append(" sqm. ");
        sb.append(listing.getListingType().name().toLowerCase()).append(". ");
        if (listing.getIsFurnished()) sb.append("Furnished. ");
        if (listing.getHasParking()) sb.append("Has parking. ");
        if (listing.getHasBalcony()) sb.append("Has balcony. ");
        if (listing.getHasWasherDryer()) sb.append("Has washer and dryer. ");
        if (listing.getHasDishwasher()) sb.append("Has dishwasher. ");
        if (listing.getHasAC()) sb.append("Has air conditioning. ");
        if (listing.getHasElevator()) sb.append("Has elevator. ");
        if (listing.getIsPetFriendly()) sb.append("Pet friendly. ");
        if (listing.getHasShabbatElevator()) sb.append("Has Shabbat elevator. ");
        if (listing.getHasSukkahBalcony()) sb.append("Has Sukkah balcony. ");
        if (listing.getHasMamad()) sb.append("Has mamad safe room. ");
        return sb.toString();
    }
}
