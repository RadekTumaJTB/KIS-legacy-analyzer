import { BrowserRouter as Router, Routes, Route } from 'react-router-dom';
import Navigation from './components/Navigation';
import DashboardPage from './pages/DashboardPage';
import DocumentsListPageAdvanced from './pages/DocumentsListPageAdvanced';
import DocumentDetailPage from './pages/DocumentDetailPage';
import BudgetDashboard from './pages/BudgetDashboard';
import BudgetListPage from './pages/BudgetListPage';
import BudgetDetailPage from './pages/BudgetDetailPage';
import ProjectListPage from './pages/ProjectListPage';
import ProjectDetailPage from './pages/ProjectDetailPage';
// Assets Module
import AssetCompaniesPage from './pages/AssetCompaniesPage';
import AssetParticipationPage from './pages/AssetParticipationPage';
// TODO: Move remaining Assets pages to src/pages/
import {
  EmissionListPage,
  AssetOverviewPage,
  AssetControlPage
} from './components/assets';
import './App.css';

function App() {
  return (
    <Router>
      <div className="app">
        <Navigation />

        <main className="app-main">
          <Routes>
            <Route path="/" element={<DashboardPage />} />
            <Route path="/documents" element={<DocumentsListPageAdvanced />} />
            <Route path="/documents/:id" element={<DocumentDetailPage />} />
            <Route path="/budgets/dashboard" element={<BudgetDashboard />} />
            <Route path="/budgets/:id" element={<BudgetDetailPage />} />
            <Route path="/budgets" element={<BudgetListPage />} />
            <Route path="/projects" element={<ProjectListPage />} />
            <Route path="/projects/:id" element={<ProjectDetailPage />} />
            {/* Assets Module Routes */}
            <Route path="/assets/emissions" element={<EmissionListPage />} />
            <Route path="/assets/companies" element={<AssetCompaniesPage />} />
            <Route path="/assets/participations/:companyId" element={<AssetParticipationPage />} />
            <Route path="/assets/overview" element={<AssetOverviewPage />} />
            <Route path="/assets/controls" element={<AssetControlPage />} />
          </Routes>
        </main>

        <footer className="app-footer">
          <p>
            ⚡ Powered by BFF - 1 API call instead of 5 (80% faster)
          </p>
          <small>
            Spring Boot 3.2.1 + React 19 + Vite + TypeScript + React Router
          </small>
        </footer>
      </div>
    </Router>
  );
}

export default App;
