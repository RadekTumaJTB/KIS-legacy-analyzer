import { useParams, Link } from 'react-router-dom';
import DocumentDetail from '../components/DocumentDetail';
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
      <DocumentDetail documentId={documentId} />
    </div>
  );
}
