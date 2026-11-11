import { useState } from 'react';
import Logo from '../components/Logo.jsx';
import { login } from '../services/api.js';

// Mosaico decorativo da lateral (mesmo desenho do protótipo). "q" é o talhão em quarentena.
const MOSAICO = ['', 'a', '', 'b', 'c', 'a', 'b', '', 'a', 'q', '', 'c', 'a', 'c', '', 'a', 'b', '', '', 'b', 'a', '', 'c', 'a'];

const CONTAS_DEMO = [
  { perfil: 'Gestor', descricao: 'cadastra e edita', email: 'gestor@agrosafe.com', senha: 'Gestor@123' },
  { perfil: 'Operador', descricao: 'só consulta', email: 'operador@agrosafe.com', senha: 'Operador@123' },
];

const MENSAGENS_ERRO = {
  credenciais: 'E-mail ou senha inválidos.',
  rede: 'Não foi possível conectar ao servidor. Tente novamente.',
  erro: 'Não foi possível entrar agora. Tente novamente.',
};

export default function Login() {
  const [email, setEmail] = useState('');
  const [senha, setSenha] = useState('');
  const [erros, setErros] = useState({ email: '', senha: '' });
  const [erroGeral, setErroGeral] = useState('');
  const [usuario, setUsuario] = useState(null);
  const [enviando, setEnviando] = useState(false);

  function usarConta(conta) {
    setEmail(conta.email);
    setSenha(conta.senha);
  }

  async function enviar(evento) {
    evento.preventDefault();
    const emailLimpo = email.trim();
    const novosErros = {
      email: emailLimpo ? '' : 'O e-mail é obrigatório',
      senha: senha ? '' : 'A senha é obrigatória',
    };
    setErros(novosErros);
    setErroGeral('');
    setUsuario(null);
    if (novosErros.email || novosErros.senha) return;

    setEnviando(true);
    const resultado = await login(emailLimpo, senha);
    setEnviando(false);

    if (resultado.ok) {
      // Ainda não há outras telas: só confirmamos que o login funcionou.
      setUsuario(resultado.usuario);
    } else {
      setErroGeral(MENSAGENS_ERRO[resultado.motivo]);
    }
  }

  return (
    <div className="login">
      <section className="login-art">
        <Logo />
        <div>
          <h1>Cada talhão com a data certa de colheita.</h1>
          <p>Cadastre talhões e defensivos e saiba quais áreas estão livres ou em quarentena.</p>
        </div>
        <div className="field-art" aria-hidden="true">
          {MOSAICO.map((classe, i) => (
            <span key={i} className={classe}></span>
          ))}
        </div>
      </section>

      <section className="login-form">
        <form onSubmit={enviar} noValidate>
          <h2>Entrar</h2>
          <p className="sub">Use o e-mail e a senha da sua conta.</p>

          <label htmlFor="email">E-mail</label>
          <input
            id="email"
            type="email"
            autoComplete="username"
            className={erros.email ? 'bad' : ''}
            value={email}
            onChange={(e) => setEmail(e.target.value)}
          />
          <div className="err">{erros.email}</div>

          <label htmlFor="senha">Senha</label>
          <input
            id="senha"
            type="password"
            autoComplete="current-password"
            className={erros.senha ? 'bad' : ''}
            value={senha}
            onChange={(e) => setSenha(e.target.value)}
          />
          <div className="err">{erros.senha}</div>

          {erroGeral && (
            <div className="form-err" role="alert">
              {erroGeral}
            </div>
          )}
          {usuario && (
            <div className="form-ok" role="status">
              Login realizado como {usuario.nome} ({usuario.perfil === 'GESTOR' ? 'Gestor' : 'Operador'}).
              As próximas telas ainda estão em desenvolvimento.
            </div>
          )}

          <button className="btn" type="submit" disabled={enviando}>
            {enviando ? 'Entrando...' : 'Entrar'}
          </button>

          <div className="demo">
            Contas de demonstração
            {CONTAS_DEMO.map((conta) => (
              <button key={conta.perfil} type="button" onClick={() => usarConta(conta)}>
                <b>{conta.perfil}</b> · {conta.descricao}
              </button>
            ))}
          </div>
        </form>
      </section>
    </div>
  );
}
