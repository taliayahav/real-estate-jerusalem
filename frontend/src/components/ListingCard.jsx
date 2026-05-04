import { Link } from 'react-router-dom';

function ListingCard({ listing }) {
  const priceLabel = listing.listingType === 'RENT'
    ? `₪${listing.price.toLocaleString()} / mo`
    : `₪${listing.price.toLocaleString()}`;

  return (
    <Link to={`/listings/${listing.id}`}>
      <div className="listing-card">
        <span className="listing-type">{listing.listingType}</span>
        <h3>{listing.title}</h3>
        <p className="neighborhood">{listing.neighborhood}</p>
        <p className="details">
          {listing.bedrooms} bed · {listing.bathrooms} bath · {listing.sizeInSqm} sqm
        </p>
        <p className="price">{priceLabel}</p>
      </div>
    </Link>
  );
}

export default ListingCard;
