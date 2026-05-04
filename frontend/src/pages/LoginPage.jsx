import { useState } from 'react';
import { useNavigate, Link } from 'react-router-dom';
import { login as loginApi } from '../api/auth';
import { useAuth } from '../context/AuthContext';

function LoginPage() {
  const [username, setUsername] = useState('');
  const [password, setPassword] = useState('');
  const [error, setError] = useState(null);
  const [loading, setLoading] = useState(false);

  const { login } = useAuth();
  const navigate = useNavigate();

  const handleSubmit = async (e) => {
    e.preventDefault();
    setError(null);
    setLoading(true);

    try {
      const response = await loginApi(username, password);
      login(response.data.token);
      navigate('/');
    } catch (err) {
      setError('Invalid email or password');
    } finally {
      setLoading(false);
    }
  };

  return (
    <div className="auth-page">
      <form className="auth-form" onSubmit={handleSubmit}>
        <h2>Sign in to Homebase</h2>

        {error && <p className="auth-error">{error}</p>}

        <label className="auth-label">Email</label>
        <input
          className="auth-input"
          type="email"
          value={username}
          onChange={e => setUsername(e.target.value)}
          required
        />

        <label className="auth-label">Password</label>
        <input
          className="auth-input"
          type="password"
          value={password}
          onChange={e => setPassword(e.target.value)}
          required
        />

        <button className="btn-search" type="submit" disabled={loading}>
          {loading ? 'Signing in...' : 'Sign in'}
        </button>

        <p className="auth-switch">
          Don't have an account? <Link to="/register">Register</Link>
        </p>
      </form>
    </div>
  );
}

export default LoginPage;
