# Engineering Decisions

## Database Choice

Decision: PostgreSQL as primary database + Vector database for semantic search

Alternatives Considered:
- MongoDB
-PostgreSQL only (without vector search)

Context

The application is a real estate platform for discovering properties in Jerusalem.
Users need to search listings using structured filters (price, bedrooms, neighborhood) and potentially natural language queries (e.g., “quiet apartment near parks with balcony”).

Structured filtering is best handled by a relational database such as PostgreSQL, while semantic similarity search requires vector embeddings.

Reasoning:
- Real estate listings have structured relational data
- Strong filtering (price, bedrooms, neighborhood)
- Joins between listings, agents, and users
- PostgreSQL provides strong indexing and query optimization
-Vector is necessary because some search queries are better handled using semantic similarity rather than strict keyword matching.


Conclusion:
-PostgreSQL is used as the primary database to manage structured relational data and support complex filtering queries.

-A vector database complements PostgreSQL by enabling semantic search and recommendation features that improve property discovery beyond traditional keyword search.

-This hybrid approach allows the system to support both structured filtering and AI-powered semantic search.