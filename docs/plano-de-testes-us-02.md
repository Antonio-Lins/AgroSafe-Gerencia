# Plano de testes: US-02 · Fazer login

**Responsável (QA):** Antonio
**Ticket:** SCRUM-21
**Regras cobertas:** RNF03 ("acesso exige login; senhas com hash BCrypt; permissões por perfil")
**Automação:** `backend/src/test/java/br/com/agrosafe/backend/controller/AuthIntegracaoTest.java`

## Escopo

- **Dentro:** login por e-mail e senha, sessão, logout, usuário logado (`/me`), acesso sem login e a ligação do perfil com as permissões (US-03).
- **Fora:** a tela de login em React (front-end), a criação dos usuários iniciais (US-04) e a tela de gestão de usuários (fora do MVP).

## Contrato para o front-end

O login usa **JSON com sessão (cookie)**.

| Endpoint | Corpo | Sucesso | Erros |
|---|---|---|---|
| `POST /api/auth/login` | `{"email": "...", "senha": "..."}` | 200 com `{id, nome, email, perfil}` | 401 `"E-mail ou senha inválidos."`; 400 se algum campo vier vazio |
| `POST /api/auth/logout` | nenhum | 204 | nenhum (sempre 204) |
| `GET /api/auth/me` | nenhum | 200 com `{id, nome, email, perfil}` | 401 sem login |

Pontos para o front-end:
- Enviar as requisições com cookies habilitados (`credentials: 'include'` no `fetch`, `withCredentials: true` no axios).
- Se o React rodar em outra porta (por exemplo 5173), é preciso um proxy do Vite ou configurar CORS com credenciais no backend.
- O campo `perfil` (`GESTOR` ou `OPERADOR`) é o que a interface usa para esconder os links de cadastro do Operador.
- 401 em qualquer rota significa "sessão inexistente ou expirada": voltar para a tela de login.

## Casos de teste

| ID | Cenário | Resultado esperado |
|---|---|---|
| CT-02-01 | Gestor faz login válido | 200 com nome, e-mail e perfil `GESTOR`; sem senha nem hash na resposta |
| CT-02-02 | Operador faz login válido | 200 com perfil `OPERADOR` |
| CT-02-03 | Senha errada | 401 com "E-mail ou senha inválidos." |
| CT-02-04 | E-mail inexistente | 401 com a mesma mensagem |
| CT-02-05 | Senha errada vs. e-mail inexistente | Respostas idênticas (não revela se o e-mail existe) |
| CT-02-06 | E-mail e senha vazios | 400 |
| CT-02-07 | `/api/auth/me`, `/talhoes` e `/defensivos` sem login | 401 |
| CT-02-08 | Acessar rotas depois do login, usando a sessão | 200 e usuário correto em `/me` |
| CT-02-09 | Logout | 204 e, depois, `/me` devolve 401 |
| CT-02-10 | Perfil vira autoridade do Spring Security | `ROLE_GESTOR` e `ROLE_OPERADOR` |
| CT-02-11 | Ponta a ponta: Gestor logado cria talhão | 201 |
| CT-02-12 | Ponta a ponta: Operador logado cria talhão | 403 e nada gravado |

## Como executar

```bash
cd backend
./mvnw test -Dtest=AuthIntegracaoTest   # só esta classe
./mvnw verify                           # tudo, como no CI
```

## Observações de segurança

- **CSRF:** continua desabilitado (API consumida por SPA). Como o login agora usa cookie de sessão, o cookie é emitido com `SameSite=Lax`, que impede o navegador de enviá-lo em requisições POST vindas de outro site. Se o sistema for publicado, vale reavaliar e habilitar CSRF.
- **Mensagem única de erro:** o sistema não diferencia "e-mail não existe" de "senha errada" (CT-02-05).
- **Fixação de sessão:** o id da sessão é trocado quando já existia uma sessão antes do login.
- **Usuários de teste:** os testes criam usuários com e-mail `*.test`, para não colidir com a carga inicial (US-04). Cada teste desfaz seus dados no final.
- **Senhas com hash BCrypt na carga inicial** é critério da US-04, que deve ter o próprio teste.

## Critérios de pronto da US-02

- [ ] Todos os casos CT-02-01 a CT-02-12 passam no CI
- [ ] Pull request revisado por outra pessoa do time
- [ ] Build da `main` continua verde após o merge
- [ ] Demonstrado na review da sprint
