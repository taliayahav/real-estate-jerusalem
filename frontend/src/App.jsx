import './App.css';
import { BrowserRouter, Routes, Route, Link } from 'react-router-dom';
import { AuthProvider, useAuth } from './context/AuthContext';
import ListingsPage from './pages/ListingsPage';
import ListingDetailPage from './pages/ListingDetailPage';
import LoginPage from './pages/LoginPage';
import RegisterPage from './pages/RegisterPage';

function Navbar() {
  const { isLoggedIn, logout } = useAuth();

  return (
    <nav className="navbar">
      <Link to="/" className="navbar-brand">Homebase</Link>
      <div className="navbar-links">
        {isLoggedIn ? (
          <button className="navbar-logout" onClick={logout}>Sign out</button>
        ) : (
          <>
            <Link to="/login" className="navbar-link">Sign in</Link>
            <Link to="/register" className="navbar-link navbar-link--primary">Register</Link>
          </>
        )}
      </div>
    </nav>
  );
}

function App() {
  return (
    <AuthProvider>
      <BrowserRouter>
        <div className="App">
          <Navbar />
          <Routes>
            <Route path="/" element={<ListingsPage />} />
            <Route path="/listings/:id" element={<ListingDetailPage />} />
            <Route path="/login" element={<LoginPage />} />
            <Route path="/register" element={<RegisterPage />} />
          </Routes>
        </div>
      </BrowserRouter>
    </AuthProvider>
  );
}

export default App;
