import { createContext, useCallback, useContext, useEffect, useMemo, useState } from 'react';
import { buscarUsuarioLogado, login, logout } from './services/authService.js';
import { EVENTO_NAO_AUTENTICADO } from '../../shared/services/api.js';

const AuthContext = createContext(null);

export function AuthProvider({ children }) {
  const [usuario, setUsuario] = useState(null);
  // true enquanto verificamos se já existe sessão aberta (ex.: ao dar F5 na página)
  const [carregando, setCarregando] = useState(true);

  useEffect(() => {
    let ativo = true;
    buscarUsuarioLogado().then((encontrado) => {
      if (!ativo) return;
      setUsuario(encontrado);
      setCarregando(false);
    });
    return () => {
      ativo = false;
    };
  }, []);

  // Se qualquer chamada da API devolver 401, a sessão acabou: limpa o usuário.
  useEffect(() => {
    const aoPerderSessao = () => setUsuario(null);
    window.addEventListener(EVENTO_NAO_AUTENTICADO, aoPerderSessao);
    return () => window.removeEventListener(EVENTO_NAO_AUTENTICADO, aoPerderSessao);
  }, []);

  const entrar = useCallback(async (email, senha) => {
    const resultado = await login(email, senha);
    if (resultado.ok) setUsuario(resultado.usuario);
    return resultado;
  }, []);

  const sair = useCallback(async () => {
    await logout();
    setUsuario(null);
  }, []);

  const valor = useMemo(
    () => ({
      usuario,
      carregando,
      entrar,
      sair,
      ehGestor: usuario?.perfil === 'GESTOR',
    }),
    [usuario, carregando, entrar, sair],
  );

  return <AuthContext.Provider value={valor}>{children}</AuthContext.Provider>;
}

export function useAuth() {
  const contexto = useContext(AuthContext);
  if (!contexto) {
    throw new Error('useAuth precisa estar dentro de <AuthProvider>');
  }
  return contexto;
}
