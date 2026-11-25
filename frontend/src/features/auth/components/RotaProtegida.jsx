import { Navigate, Outlet, useLocation } from 'react-router-dom';
import { useAuth } from '../AuthContext.jsx';

/**
 * Protege um grupo de rotas.
 * - Sem login: manda para /login (e lembra a página que a pessoa queria abrir).
 * - Com a prop "perfis" (ex.: perfis={['GESTOR']}): quem não tem o perfil volta para o início.
 */
export default function RotaProtegida({ perfis }) {
  const { usuario, carregando } = useAuth();
  const local = useLocation();

  if (carregando) {
    return (
      <div className="carregando" role="status">
        Carregando...
      </div>
    );
  }
  if (!usuario) {
    return <Navigate to="/login" replace state={{ de: local }} />;
  }
  if (perfis && !perfis.includes(usuario.perfil)) {
    return <Navigate to="/" replace />;
  }
  return <Outlet />;
}
