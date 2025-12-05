import { useParams, Link } from 'react-router-dom';
import DocumentDetailEnhanced from '../components/DocumentDetailEnhanced';
import './DocumentDetailPage.css';

export default function DocumentDetailPage() {
  const { id } = useParams<{ id: string }>();
  const documentId = parseInt(id || '1', 10);

  return (
    <div className="document-detail-page">
      <div className="page-header">
        <Link to="/documents" className="back-link">
          ← Zpět na seznam
        </Link>
      </div>
      <DocumentDetailEnhanced documentId={documentId} />
    </div>
  );
}
