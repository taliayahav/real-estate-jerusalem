import axios from 'axios';

const API_BASE_URL = 'http://localhost:8080/api';

const api = axios.create({
  baseURL: API_BASE_URL,
});

export const getAllListings = (filters = {}) => {
  return api.get('/listings', { params: filters });
};

export const getListingById = (id) => {
  return api.get(`/listings/${id}`);
};

export const createListing = (listing) => {
  return api.post('/listings', listing);
};

export const deleteListing = (id) => {
  return api.delete(`/listings/${id}`);
};

export const semanticSearch = (query, limit = 10) => {
  return api.get('/listings/semantic-search', { params: { q: query, limit } });
};

export const reindexListings = () => {
  return api.post('/listings/reindex');
};
