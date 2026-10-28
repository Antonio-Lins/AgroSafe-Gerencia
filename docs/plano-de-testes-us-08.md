# Plano de testes: US-08 · Listar talhões e defensivos

**Responsável (QA):** Antonio
**Ticket:** SCRUM-28
**Endpoints cobertos:** `GET /talhoes` e `GET /defensivos`
**Automação:** `backend/src/test/java/br/com/agrosafe/backend/controller/ListagemIntegracaoTest.java`

## Escopo

- **Dentro:** conteúdo e ordenação das listas, estado calculado do talhão (RN01), acesso por perfil e acesso sem login.
- **Fora:** criação, edição e remoção (US-05 a US-07), regras de perfil para escrita (US-03) e o login em si (US-02). Aqui os usuários são simulados nos testes.

## Casos de teste

| ID | Cenário | Pré-condição | Perfil | Resultado esperado |
|---|---|---|---|---|
| CT-08-01 | Lista de talhões vazia | Nenhum talhão cadastrado | Gestor | 200 e lista `[]` |
| CT-08-02 | Ordenação e campos dos talhões | Talhões C3, A1 e B2 cadastrados | Gestor | 200, ordem A1, B2, C3, com `id`, `codigo`, `hectares`, `cultura` |
| CT-08-03 | Talhão sem data de liberação | Talhão com `dataLiberacao` nula | Gestor | `estado = LIVRE`, sem data de liberação |
| CT-08-04 | Talhão em quarentena | `dataLiberacao` = hoje + 5 dias | Gestor | `estado = EM_QUARENTENA` e data de liberação correta |
| CT-08-05 | Libera hoje (borda, RN04) | `dataLiberacao` = hoje | Gestor | `estado = LIVRE` |
| CT-08-06 | Liberação no passado | `dataLiberacao` = ontem | Gestor | `estado = LIVRE` |
| CT-08-07 | Operador lista talhões | Um talhão cadastrado | Operador | 200 com 1 item |
| CT-08-08 | Talhões sem login | Nenhuma | Sem login | 401 |
| CT-08-09 | Lista de defensivos vazia | Nenhum defensivo cadastrado | Gestor | 200 e lista `[]` |
| CT-08-10 | Ordenação e campos dos defensivos | Defensivos "Zeta" e "Alfa" cadastrados | Gestor | 200, ordem Alfa, Zeta, com `nomeComercial`, `principioAtivo`, `diasCarencia` |
| CT-08-11 | Operador lista defensivos | Um defensivo cadastrado | Operador | 200 com 1 item |
| CT-08-12 | Defensivos sem login | Nenhuma | Sem login | 401 |

## Como executar

```bash
cd backend
./mvnw test -Dtest=ListagemIntegracaoTest   # só esta classe
./mvnw verify                               # tudo, como no CI
```

## Observações

- Os testes usam `@WithMockUser` para simular Gestor e Operador, porque o login (US-02) ainda não existe. Quando o login estiver pronto, vale acrescentar um teste ponta a ponta autenticando de verdade.
- O estado do talhão é calculado no momento da consulta (RN01). Por isso os testes usam datas relativas a "hoje" (`LocalDate.now()`).
- Pendência de definição com o PO: confirmar que o Operador pode listar talhões e defensivos. A tabela de endpoints do documento do projeto marca `GET /talhoes` e `GET /defensivos` como "Todos".

## Critérios de pronto da US-08

- [ ] Todos os casos CT-08-01 a CT-08-12 passam no CI
- [ ] Pull request revisado por outra pessoa do time
- [ ] Build da `main` continua verde após o merge
- [ ] Demonstrado na review da sprint
