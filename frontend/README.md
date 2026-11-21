# AgroSafe - Frontend (React + Vite)

Frontend do AgroSafe, sistema web para gerenciamento de talhões, defensivos agrícolas, aplicações e colheitas.

Atualmente, está implementada a tela de login (US-14). As demais funcionalidades serão desenvolvidas e integradas à API REST do backend nas próximas tarefas.

## Tecnologias

- React
- Vite
- JavaScript (JSX)
- CSS

## Como executar

**Pré-requisito:** Node.js 20 ou superior.

Na pasta `frontend`, execute:

```bash
npm install
npm run dev
```

Acesse: http://localhost:5173

### Integração com o backend

Para que o login funcione, o backend Spring Boot também precisa estar em execução.

Na pasta `backend`, execute:

```bash
./mvnw spring-boot:run
```

Por padrão, o backend utiliza a porta `8080`.

Durante o desenvolvimento, o Vite encaminha as requisições iniciadas por `/api` para o backend, conforme configurado em `vite.config.js`.

O backend também precisa ter acesso ao banco PostgreSQL configurado.

### Contas de demonstração

Quando a inicialização dos usuários de demonstração estiver habilitada no backend:

| Perfil | E-mail | Senha |
|---|---|---|
| Gestor | `gestor@agrosafe.com` | `Gestor@123` |
| Operador | `operador@agrosafe.com` | `Operador@123` |

Essas credenciais destinam-se exclusivamente ao ambiente de desenvolvimento/demonstração.

## Estrutura do projeto

O frontend está organizado por funcionalidades (*feature-based structure*), separando as configurações da aplicação, as funcionalidades e os recursos compartilhados.

```text
src/
├── app/                     # Configuração de rotas da aplicação
├── features/                # Funcionalidades do sistema
│   ├── auth/
│   │   ├── components/
│   │   ├── context/
│   │   ├── pages/
│   │   │   └── Login.jsx
│   │   └── services/
│   │       └── authService.js
│   ├── aplicacoes/
│   ├── colheitas/
│   ├── dashboard/
│   ├── defensivos/
│   ├── historico/
│   └── talhoes/
├── shared/                  # Recursos reutilizáveis
│   ├── api/
│   └── components/
│       └── Logo.jsx
├── styles/
│   └── global.css
├── App.jsx
└── main.jsx
```

As pastas das funcionalidades serão preenchidas conforme o desenvolvimento das respectivas telas e integrações.

### Organização

- **`app/`**: configuração das rotas e navegação da aplicação.
- **`features/`**: funcionalidades organizadas em módulos independentes, cada um com seus próprios componentes, páginas, serviços e, quando necessário, contextos.
- **`shared/`**: componentes reutilizáveis e infraestrutura compartilhada, como o cliente HTTP.
- **`styles/`**: estilos globais e identidade visual.
- **`App.jsx`**: componente principal da aplicação.
- **`main.jsx`**: ponto de entrada do React.

## Protótipo

A interface segue a identidade visual do protótipo disponível em `docs/prototipos`, na raiz do repositório.

## Build

Para gerar o build de produção, execute:

```bash
npm run build
```

O Vite gera os arquivos de distribuição na pasta `dist/`.