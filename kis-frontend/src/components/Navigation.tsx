import { Link, useLocation } from 'react-router-dom';
import './Navigation.css';

export default function Navigation() {
  const location = useLocation();

  const isActive = (path: string) => {
    return location.pathname === path;
  };

  return (
    <nav className="main-nav">
      <div className="nav-container">
        <Link to="/" className="nav-logo">
          <span className="logo-icon">🏦</span>
          <span className="logo-text">KIS Banking</span>
        </Link>

        <div className="nav-links">
          <Link
            to="/"
            className={`nav-link ${isActive('/') ? 'active' : ''}`}
          >
            📊 Dashboard
          </Link>
          <Link
            to="/documents"
            className={`nav-link ${isActive('/documents') || location.pathname.startsWith('/documents/') ? 'active' : ''}`}
          >
            📄 Dokumenty
          </Link>
        </div>

        <div className="nav-user">
          <span className="user-name">Eva Černá</span>
          <span className="user-role">CFO</span>
        </div>
      </div>
    </nav>
  );
}
