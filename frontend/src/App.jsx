import { BrowserRouter, Navigate, Route, Routes } from 'react-router-dom';
import { AuthProvider } from './features/auth/AuthContext.jsx';
import RotaProtegida from './features/auth/components/RotaProtegida.jsx';
import Login from './features/auth/pages/Login.jsx';
import Layout from './shared/components/Layout.jsx';
import Inicio from './features/home/pages/Inicio.jsx';
import TalhoesLista from './features/talhoes/pages/TalhoesLista.jsx';
import DefensivosLista from './features/defensivos/pages/DefensivosLista.jsx';
import AplicacaoNova from './features/aplicacoes/pages/AplicacaoNova.jsx';

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

              {/* SCRUM-72: CRUD de defensivos */}
              <Route
                  path="defensivos"
                  element={<DefensivosLista />}
              />

              {/* SCRUM-69: Registro de aplicação */}
              <Route
                  path="aplicacoes/nova"
                  element={<AplicacaoNova />}
              />

            </Route>
          </Route>

          {/* qualquer endereço desconhecido volta para o início */}
          <Route path="*" element={<Navigate to="/" replace />} />
        </Routes>
      </AuthProvider>
    </BrowserRouter>
  );
}
