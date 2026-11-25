import { useAuth } from '../../auth/AuthContext.jsx';

// Página inicial provisória. O painel de talhões (US-17 a US-19) vai ocupar este lugar.
export default function Inicio() {
  const { usuario, ehGestor } = useAuth();

  return (
    <section>
      <h1>Olá, {usuario.nome}</h1>
      <p className="sub">
        {ehGestor
          ? 'Você é Gestor: pode cadastrar, editar e remover.'
          : 'Você é Operador: pode apenas consultar.'}
      </p>
      <p>As telas de talhões e defensivos ainda estão em desenvolvimento.</p>
    </section>
  );
}
