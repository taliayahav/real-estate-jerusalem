import { useEffect, useState } from 'react';
import { getAllListings } from '../api/listings';
import ListingCard from '../components/ListingCard';
import ListingFilters from '../components/ListingFilters';

function ListingsPage() {
  const [listings, setListings] = useState([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState(null);
  const [filters, setFilters] = useState({});

  useEffect(() => {
    setLoading(true);
    setError(null);
    getAllListings(filters)
      .then(response => {
        setListings(response.data);
        setLoading(false);
      })
      .catch(err => {
        setError('Failed to load listings');
        setLoading(false);
      });
  }, [filters]);

  return (
    <div className="listings-page">
      <h2>Available Listings</h2>
      <ListingFilters onSearch={setFilters} />

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
