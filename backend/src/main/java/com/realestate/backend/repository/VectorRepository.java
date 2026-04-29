package com.realestate.backend.repository;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
@RequiredArgsConstructor
public class VectorRepository {

    private final JdbcTemplate jdbcTemplate;

    public void saveEmbedding(UUID listingId, float[] embedding) {
        jdbcTemplate.update(
            "UPDATE listings SET embedding = CAST(? AS vector) WHERE id = ?",
            toVectorString(embedding), listingId
        );
    }

    public List<UUID> findSimilar(float[] queryEmbedding, int limit) {
        return jdbcTemplate.queryForList(
            "SELECT id FROM listings WHERE embedding IS NOT NULL AND embedding <=> CAST(? AS vector) < 0.5 ORDER BY embedding <=> CAST(? AS vector) LIMIT ?",
            UUID.class,
            toVectorString(queryEmbedding), toVectorString(queryEmbedding), limit
        );
    }

    private String toVectorString(float[] embedding) {
        StringBuilder sb = new StringBuilder("[");
        for (int i = 0; i < embedding.length; i++) {
            if (i > 0) sb.append(",");
            sb.append(embedding[i]);
        }
        sb.append("]");
        return sb.toString();
    }
}
