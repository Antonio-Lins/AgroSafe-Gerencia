import { NavLink, Outlet } from 'react-router-dom';
import Logo from './Logo.jsx';
import { useAuth } from '../../features/auth/AuthContext.jsx';
import '../../styles/layout.css';

// Itens do menu. Quando criar uma tela nova (talhões, defensivos...),
// adicione a rota no App.jsx e uma linha aqui.
const ITENS_MENU = [
  { to: '/', rotulo: 'Início', fim: true },
  { to: '/talhoes', rotulo: 'Talhões' },
  { to: '/defensivos', rotulo: 'Defensivos' },
  { to: '/aplicacoes/nova', rotulo: 'Registrar aplicação' },
];

export default function Layout() {
  const { usuario, sair, ehGestor } = useAuth();

  return (
    <div className="app">
      <header className="app-topo">
        <Logo />

        <nav className="app-menu" aria-label="Principal">
          {ITENS_MENU.map((item) => (
            <NavLink key={item.to} to={item.to} end={item.fim}>
              {item.rotulo}
            </NavLink>
          ))}
        </nav>

        <div className="app-usuario">
          <div className="app-usuario-info">
            <span className="app-usuario-nome">{usuario.nome}</span>
            <span className="app-usuario-perfil">{ehGestor ? 'Gestor' : 'Operador'}</span>
          </div>
          <button type="button" className="btn-sair" onClick={sair}>
            Sair
          </button>
        </div>
      </header>

      <main className="app-conteudo">
        <Outlet />
      </main>
    </div>
  );
}
