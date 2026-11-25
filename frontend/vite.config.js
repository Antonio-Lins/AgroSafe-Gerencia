import { defineConfig } from 'vite';
import react from '@vitejs/plugin-react';

// Alguns controllers do backend ainda não usam o prefixo /api (/talhoes, /defensivos...).
// O nome é igual ao das telas do front, então só repassamos ao backend as chamadas feitas
// por fetch. Quando o navegador abre o endereço (digitou a URL ou deu F5), o bypass
// devolve o index.html para o React Router mostrar a tela.
const semPrefixoApi = {
  target: 'http://localhost:8080',
  bypass(req) {
    if (req.headers.accept?.includes('text/html')) return '/index.html';
  },
};

// Em desenvolvimento, as chamadas para o backend (Spring Boot, porta 8080) passam pelo proxy.
// Assim o navegador enxerga uma única origem e o cookie de sessão funciona sem CORS.
export default defineConfig({
  plugins: [react()],
  server: {
    port: 5173,
    proxy: {
      '/api': 'http://localhost:8080',
      '/talhoes': semPrefixoApi,
      '/defensivos': semPrefixoApi,
      '/aplicacoes': semPrefixoApi,
      '/colheitas': semPrefixoApi,
    },
  },
});
