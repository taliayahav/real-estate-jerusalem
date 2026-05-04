import { useState } from 'react';
import { useNavigate, Link } from 'react-router-dom';
import { register as registerApi } from '../api/auth';
import { useAuth } from '../context/AuthContext';

function RegisterPage() {
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
      const response = await registerApi(username, password);
      login(response.data.token);
      navigate('/');
    } catch (err) {
      setError(err.response?.data || 'Registration failed');
    } finally {
      setLoading(false);
    }
  };

  return (
    <div className="auth-page">
      <form className="auth-form" onSubmit={handleSubmit}>
        <h2>Create your Homebase account</h2>

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
          {loading ? 'Creating account...' : 'Create account'}
        </button>

        <p className="auth-switch">
          Already have an account? <Link to="/login">Sign in</Link>
        </p>
      </form>
    </div>
  );
}

export default RegisterPage;
