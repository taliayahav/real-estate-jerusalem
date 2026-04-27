import { useState } from 'react';

const emptyFilters = {
  neighborhood: '',
  listingType: '',
  bedrooms: '',
  minPrice: '',
  maxPrice: '',
  hasParking: false,
  hasBalcony: false,
  hasMamad: false,
};

function ListingFilters({ onSearch }) {
  const [filters, setFilters] = useState(emptyFilters);

  const handleChange = (e) => {
    const { name, value, type, checked } = e.target;
    setFilters(prev => ({
      ...prev,
      [name]: type === 'checkbox' ? checked : value,
    }));
  };

  const handleSubmit = (e) => {
    e.preventDefault();
    const clean = Object.fromEntries(
      Object.entries(filters).filter(([_, v]) => v !== '' && v !== false)
    );
    onSearch(clean);
  };

  const handleReset = () => {
    setFilters(emptyFilters);
    onSearch({});
  };

  return (
    <form className="filter-bar" onSubmit={handleSubmit}>
      <div className="filter-row">
        <input
          className="filter-input"
          name="neighborhood"
          value={filters.neighborhood}
          onChange={handleChange}
          placeholder="Neighborhood"
        />

        <select
          className="filter-select"
          name="listingType"
          value={filters.listingType}
          onChange={handleChange}
        >
          <option value="">All types</option>
          <option value="RENT">Rent</option>
          <option value="SALE">Sale</option>
        </select>

        <select
          className="filter-select"
          name="bedrooms"
          value={filters.bedrooms}
          onChange={handleChange}
        >
          <option value="">Any bedrooms</option>
          <option value="1">1 bed</option>
          <option value="2">2 beds</option>
          <option value="3">3 beds</option>
          <option value="4">4 beds</option>
        </select>

        <input
          className="filter-input filter-input--short"
          name="minPrice"
          type="number"
          value={filters.minPrice}
          onChange={handleChange}
          placeholder="Min ₪"
          min="0"
        />

        <input
          className="filter-input filter-input--short"
          name="maxPrice"
          type="number"
          value={filters.maxPrice}
          onChange={handleChange}
          placeholder="Max ₪"
          min="0"
        />
      </div>

      <div className="filter-row filter-row--bottom">
        <div className="filter-checkboxes">
          <label className="filter-checkbox">
            <input type="checkbox" name="hasParking" checked={filters.hasParking} onChange={handleChange} />
            Parking
          </label>
          <label className="filter-checkbox">
            <input type="checkbox" name="hasBalcony" checked={filters.hasBalcony} onChange={handleChange} />
            Balcony
          </label>
          <label className="filter-checkbox">
            <input type="checkbox" name="hasMamad" checked={filters.hasMamad} onChange={handleChange} />
            Mamad
          </label>
        </div>

        <div className="filter-actions">
          <button type="button" className="btn-clear" onClick={handleReset}>Clear</button>
          <button type="submit" className="btn-search">Search</button>
        </div>
      </div>
    </form>
  );
}

export default ListingFilters;
