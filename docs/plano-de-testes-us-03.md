# Plano de testes: US-03 · Restringir acesso por perfil

**Responsável (QA):** Antonio
**Ticket:** SCRUM-22
**Regras cobertas:** RF01 ("Operadores não têm acesso às telas de cadastro") e RNF03 ("permissões por perfil")
**Automação:** `backend/src/test/java/br/com/agrosafe/backend/controller/AcessoPorPerfilIntegracaoTest.java`

## Escopo

- **Dentro:** criação, edição e remoção de talhões e defensivos (`POST`, `PUT` e `DELETE`), com os perfis Gestor e Operador e sem login.
- **Fora:** o login em si (US-02, perfis simulados com `@WithMockUser`), a leitura/listagem (US-08, casos CT-08-07 e CT-08-11) e esconder os links de cadastro no menu (responsabilidade do front-end React).

## Regra de acesso

| Operação | Gestor | Operador | Sem login |
|---|---|---|---|
| `GET /talhoes`, `GET /defensivos` | permitido | permitido | 401 |
| `POST`, `PUT`, `DELETE` em `/talhoes` e `/defensivos` | permitido | **403** | 401 |

## Casos de teste

| ID | Cenário | Perfil | Resultado esperado |
|---|---|---|---|
| CT-03-01 | Criar talhão | Operador | 403 e nenhum talhão gravado |
| CT-03-02 | Editar talhão | Operador | 403 e dados do talhão inalterados |
| CT-03-03 | Remover talhão | Operador | 403 e o talhão continua existindo |
| CT-03-04 | Criar talhão | Gestor | 201 com o talhão criado |
| CT-03-05 | Editar talhão | Gestor | 200 com os dados novos |
| CT-03-06 | Remover talhão sem aplicações | Gestor | 204 e o talhão deixa de existir |
| CT-03-07 | Criar defensivo | Operador | 403 e nenhum defensivo gravado |
| CT-03-08 | Editar defensivo | Operador | 403 e dados inalterados |
| CT-03-09 | Remover defensivo | Operador | 403 e o defensivo continua existindo |
| CT-03-10 | Criar defensivo | Gestor | 201 com o defensivo criado |
| CT-03-11 | Editar defensivo | Gestor | 200 com os dados novos |
| CT-03-12 | Remover defensivo sem aplicações | Gestor | 204 e o defensivo deixa de existir |
| CT-03-13 | Criar talhão | Sem login | 401 e nada gravado |
| CT-03-14 | Remover defensivo | Sem login | 401 e o registro continua existindo |

Nos casos de 403 e 401, o teste confere também o banco: a operação barrada não pode ter alterado nada.

## Defeito encontrado

**Antes da correção, o `@PreAuthorize` dos controllers era ignorado.** O `SecurityConfig` não tinha `@EnableMethodSecurity`, então qualquer usuário autenticado, inclusive o Operador, podia criar, editar e remover talhões e defensivos. Os casos CT-03-01, 02, 03, 07, 08 e 09 falham nesse estado e passam depois da correção.

**Correção:** adicionar `@EnableMethodSecurity` na classe `SecurityConfig`.

## Observação (baixa gravidade)

O Operador que envia um corpo **inválido** (por exemplo, `codigo` vazio) recebe 400 e não 403, porque o Spring valida o corpo antes de avaliar o `@PreAuthorize`. Nada é gravado, mas a resposta revela as regras de validação a quem não deveria ter acesso. Se a equipe quiser tratar, a solução é acrescentar regras por rota no `SecurityConfig` (por exemplo, `requestMatchers(HttpMethod.POST, "/talhoes/**").hasRole("GESTOR")`).

## Como executar

```bash
cd backend
./mvnw test -Dtest=AcessoPorPerfilIntegracaoTest   # só esta classe
./mvnw verify                                      # tudo, como no CI
```

## Observações

- A autoridade esperada pelos testes é `ROLE_GESTOR` / `ROLE_OPERADOR`. Na US-02, o `UserDetailsService` precisa converter o `Perfil` do usuário nessas autoridades, senão o `hasRole('GESTOR')` não reconhece o login real.
- Quando o login (US-02) estiver pronto, vale acrescentar um teste ponta a ponta autenticando de verdade.

## Critérios de pronto da US-03

- [ ] Todos os casos CT-03-01 a CT-03-14 passam no CI
- [ ] Pull request revisado por outra pessoa do time
- [ ] Build da `main` continua verde após o merge
- [ ] Demonstrado na review da sprint
