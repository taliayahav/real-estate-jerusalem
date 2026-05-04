import { useEffect, useState } from 'react';
import { getAllListings, semanticSearch } from '../api/listings';
import ListingCard from '../components/ListingCard';
import ListingFilters from '../components/ListingFilters';

function ListingsPage() {
  const [listings, setListings] = useState([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState(null);
  const [filters, setFilters] = useState({});
  const [semanticQuery, setSemanticQuery] = useState('');
  const [isSemanticMode, setIsSemanticMode] = useState(false);
  const [page, setPage] = useState(0);
  const [totalPages, setTotalPages] = useState(0);
  const [totalElements, setTotalElements] = useState(0);

  useEffect(() => {
    if (isSemanticMode) return;
    setLoading(true);
    setError(null);
    getAllListings(filters, page)
      .then(response => {
        const data = response.data;
        setListings(data.content);
        setTotalPages(data.totalPages);
        setTotalElements(data.totalElements);
        setLoading(false);
      })
      .catch(_err => {
        setError('Failed to load listings');
        setLoading(false);
      });
  }, [filters, page, isSemanticMode]);

  const handleSemanticSearch = (e) => {
    e.preventDefault();
    if (!semanticQuery.trim()) return;
    setLoading(true);
    setError(null);
    setIsSemanticMode(true);
    semanticSearch(semanticQuery)
      .then(response => {
        setListings(response.data);
        setTotalPages(0);
        setLoading(false);
      })
      .catch(() => {
        setError('Semantic search failed');
        setLoading(false);
      });
  };

  const handleClearSemantic = () => {
    setSemanticQuery('');
    setIsSemanticMode(false);
    setPage(0);
  };

  const handleFiltersChange = (newFilters) => {
    setFilters(newFilters);
    setPage(0);
  };

  return (
    <div className="listings-page">
      <h2>Available Listings</h2>

      <form className="semantic-search-bar" onSubmit={handleSemanticSearch}>
        <input
          className="semantic-search-input"
          type="text"
          value={semanticQuery}
          onChange={e => setSemanticQuery(e.target.value)}
          placeholder={`Describe what you're looking for - e.g. "quiet apartment near the shuk with a balcony"`}
        />
        <button type="submit" className="btn-search">Search</button>
        {isSemanticMode && (
          <button type="button" className="btn-clear" onClick={handleClearSemantic}>
            Clear
          </button>
        )}
      </form>

      {!isSemanticMode && <ListingFilters onSearch={handleFiltersChange} />}

      {isSemanticMode && (
        <p className="semantic-mode-label">Showing AI search results for: <em>"{semanticQuery}"</em></p>
      )}

      {loading && <p className="status-message">Loading listings...</p>}
      {error && <p className="status-message">{error}</p>}

      {!loading && !error && listings.length === 0 && (
        <p className="status-message">No listings match your search.</p>
      )}

      {!loading && !error && (
        <>
          {!isSemanticMode && totalElements > 0 && (
            <p className="results-count">{totalElements} listing{totalElements !== 1 ? 's' : ''} found</p>
          )}
          <div className="listings-grid">
            {listings.map(listing => (
              <ListingCard key={listing.id} listing={listing} />
            ))}
          </div>

          {!isSemanticMode && totalPages > 1 && (
            <div className="pagination">
              <button
                className="pagination-btn"
                onClick={() => setPage(p => p - 1)}
                disabled={page === 0}
              >
                Previous
              </button>
              <span className="pagination-info">Page {page + 1} of {totalPages}</span>
              <button
                className="pagination-btn"
                onClick={() => setPage(p => p + 1)}
                disabled={page >= totalPages - 1}
              >
                Next
              </button>
            </div>
          )}
        </>
      )}
    </div>
  );
}

export default ListingsPage;
