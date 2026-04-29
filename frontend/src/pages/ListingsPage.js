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

  useEffect(() => {
    if (isSemanticMode) return;
    setLoading(true);
    setError(null);
    getAllListings(filters)
      .then(response => {
        setListings(response.data);
        setLoading(false);
      })
      .catch(_err => {
        setError('Failed to load listings');
        setLoading(false);
      });
  }, [filters, isSemanticMode]);

  const handleSemanticSearch = (e) => {
    e.preventDefault();
    if (!semanticQuery.trim()) return;
    setLoading(true);
    setError(null);
    setIsSemanticMode(true);
    semanticSearch(semanticQuery)
      .then(response => {
        setListings(response.data);
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

      {!isSemanticMode && <ListingFilters onSearch={setFilters} />}

      {isSemanticMode && (
        <p className="semantic-mode-label">Showing AI search results for: <em>"{semanticQuery}"</em></p>
      )}

      {loading && <p className="status-message">Loading listings...</p>}
      {error && <p className="status-message">{error}</p>}

      {!loading && !error && listings.length === 0 && (
        <p className="status-message">No listings match your search.</p>
      )}

      {!loading && !error && (
        <div className="listings-grid">
          {listings.map(listing => (
            <ListingCard key={listing.id} listing={listing} />
          ))}
        </div>
      )}
    </div>
  );
}

export default ListingsPage;
