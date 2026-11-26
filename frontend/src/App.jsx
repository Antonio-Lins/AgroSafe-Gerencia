import { BrowserRouter, Navigate, Route, Routes } from 'react-router-dom';

import { AuthProvider } from './features/auth/AuthContext.jsx';
import RotaProtegida from './features/auth/components/RotaProtegida.jsx';
import Login from './features/auth/pages/Login.jsx';

import Layout from './shared/components/Layout.jsx';
import Inicio from './features/home/pages/Inicio.jsx';

import TalhoesLista from './features/talhoes/pages/TalhoesLista.jsx';
import DefensivosLista from './features/defensivos/pages/DefensivosLista.jsx';
import AplicacaoNova from './features/aplicacoes/pages/AplicacaoNova.jsx';
import RegistrarColheita from './features/colheitas/pages/RegistrarColheita.jsx';

export default function App() {
  return (
      <BrowserRouter>
        <AuthProvider>
          <Routes>
            {/* Rota pública */}
            <Route path="/login" element={<Login />} />

            {/* Rotas protegidas por autenticação */}
            <Route element={<RotaProtegida />}>
              <Route element={<Layout />}>
                <Route index element={<Inicio />} />

                <Route
                    path="talhoes"
                    element={<TalhoesLista />}
                />

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

                {/* SCRUM-70: Registro de colheita */}
                <Route
                    path="colheitas/nova"
                    element={<RegistrarColheita />}
                />
              </Route>
            </Route>

            {/* Endereços desconhecidos voltam para o início */}
            <Route path="*" element={<Navigate to="/" replace />} />
          </Routes>
        </AuthProvider>
      </BrowserRouter>
  );
}