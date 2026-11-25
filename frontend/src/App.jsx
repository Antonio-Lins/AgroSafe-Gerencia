import { BrowserRouter, Navigate, Route, Routes } from 'react-router-dom';
import { AuthProvider } from './features/auth/AuthContext.jsx';
import RotaProtegida from './features/auth/components/RotaProtegida.jsx';
import Login from './features/auth/pages/Login.jsx';
import Layout from './shared/components/Layout.jsx';
import Inicio from './features/home/pages/Inicio.jsx';
import TalhoesLista from './features/talhoes/pages/TalhoesLista.jsx';

export default function App() {
  return (
    <BrowserRouter>
      <AuthProvider>
        <Routes>
          {/* pública */}
          <Route path="/login" element={<Login />} />

          {/* tudo aqui dentro exige login e aparece com o menu no topo */}
          <Route element={<RotaProtegida />}>
            <Route element={<Layout />}>
              <Route index element={<Inicio />} />
              <Route path="talhoes" element={<TalhoesLista />} />
              {/* Próximas telas entram aqui. Tela só para Gestor (ex.: cadastro):
                  <Route element={<RotaProtegida perfis={['GESTOR']} />}>
                    <Route path="talhoes/novo" element={<TalhaoForm />} />
                  </Route>
              */}
            </Route>
          </Route>

          {/* qualquer endereço desconhecido volta para o início */}
          <Route path="*" element={<Navigate to="/" replace />} />
        </Routes>
      </AuthProvider>
    </BrowserRouter>
  );
}
