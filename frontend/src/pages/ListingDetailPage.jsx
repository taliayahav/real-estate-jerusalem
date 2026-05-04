import { useEffect, useState } from 'react';
import { useParams, Link } from 'react-router-dom';
import { getListingById } from '../api/listings';

function ListingDetailPage() {
  const { id } = useParams();
  const [listing, setListing] = useState(null);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState(null);

  useEffect(() => {
    getListingById(id)
      .then(response => {
        setListing(response.data);
        setLoading(false);
      })
      .catch(err => {
        setError('Listing not found');
        setLoading(false);
      });
  }, [id]);

  if (loading) return <p className="status-message">Loading...</p>;
  if (error) return <p className="status-message">{error}</p>;

  const priceLabel = listing.listingType === 'RENT'
    ? `₪${listing.price.toLocaleString()} / mo`
    : `₪${listing.price.toLocaleString()}`;

  const amenities = [
    { key: 'hasParking', label: 'Parking' },
    { key: 'hasBalcony', label: 'Balcony' },
    { key: 'hasWasherDryer', label: 'Washer / Dryer' },
    { key: 'hasDishwasher', label: 'Dishwasher' },
    { key: 'hasAC', label: 'Air Conditioning' },
    { key: 'hasElevator', label: 'Elevator' },
    { key: 'isFurnished', label: 'Furnished' },
    { key: 'isPetFriendly', label: 'Pet Friendly' },
    { key: 'hasShabbatElevator', label: 'Shabbat Elevator' },
    { key: 'hasSukkahBalcony', label: 'Sukkah Balcony' },
    { key: 'hasMamad', label: 'Mamad (Safe Room)' },
  ];

  const activeAmenities = amenities.filter(a => listing[a.key]);

  return (
    <div className="listing-detail">
      <Link to="/" className="back-link">← Back to listings</Link>

      <div className="detail-header">
        <div>
          <span className="listing-type">{listing.listingType}</span>
          <h2>{listing.title}</h2>
          <p className="neighborhood">{listing.neighborhood} · {listing.address}</p>
        </div>
        <p className="price">{priceLabel}</p>
      </div>

      <div className="detail-body">
        <div className="detail-section">
          <h3>Details</h3>
          <p>{listing.bedrooms} bedrooms · {listing.bathrooms} bathrooms · {listing.sizeInSqm} sqm</p>
        </div>

        {listing.description && (
          <div className="detail-section">
            <h3>Description</h3>
            <p>{listing.description}</p>
          </div>
        )}

        {activeAmenities.length > 0 && (
          <div className="detail-section">
            <h3>Amenities</h3>
            <ul className="amenities-list">
              {activeAmenities.map(a => (
                <li key={a.key}>{a.label}</li>
              ))}
            </ul>
          </div>
        )}
      </div>
    </div>
  );
}

export default ListingDetailPage;
