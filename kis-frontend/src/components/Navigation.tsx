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
          <Link
            to="/budgets"
            className={`nav-link ${isActive('/budgets') || location.pathname.startsWith('/budgets') ? 'active' : ''}`}
          >
            💰 Rozpočty
          </Link>
          <Link
            to="/projects"
            className={`nav-link ${isActive('/projects') || location.pathname.startsWith('/projects') ? 'active' : ''}`}
          >
            📋 Projekty
          </Link>
          <Link
            to="/assets/companies"
            className={`nav-link ${location.pathname.startsWith('/assets') ? 'active' : ''}`}
          >
            🏢 Majetek
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
