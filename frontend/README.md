# AgroSafe - Frontend (React + Vite)

Por enquanto só existe a tela de login (US-14, início do front). As demais telas entram nas próximas tarefas.

## Como rodar

Pré-requisito: Node.js 20 ou superior.

```bash
cd frontend
npm install
npm run dev
```

Abra http://localhost:5173. Para o login funcionar de verdade, suba também o backend (`./mvnw spring-boot:run` na pasta `backend`, porta 8080). Em desenvolvimento, o Vite encaminha as chamadas `/api` para o backend (ver `vite.config.js`).

Contas de demonstração: `gestor@agrosafe.com` / `Gestor@123` e `operador@agrosafe.com` / `Operador@123`.

## Estrutura

- `src/pages/Login.jsx`: tela de login
- `src/components/`: componentes reutilizáveis
- `src/services/api.js`: chamadas ao backend
- `src/styles/global.css`: estilos e cores (mesma identidade visual do protótipo em `docs/prototipos`)
