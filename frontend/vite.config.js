import { defineConfig } from 'vite';
import react from '@vitejs/plugin-react';

export default defineConfig({
  plugins: [react()],
  server: {
    port: 5173,
    proxy: {
      '/api': {
        target: 'http://localhost:8080',
        changeOrigin: true,
      },
      '/talhoes': {
        target: 'http://localhost:8080',
        changeOrigin: true,
      },
      '/defensivos': {
        target: 'http://localhost:8080',
        changeOrigin: true,
      },
      '/aplicacoes': {
        target: 'http://localhost:8080',
        changeOrigin: true,
      },
      '/colheitas': {
        target: 'http://localhost:8080',
        changeOrigin: true,
      },
    },
  },
});
