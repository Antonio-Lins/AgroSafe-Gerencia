# Ata de Sprint Review - Sprint 3 (AgroSafe)

* **Data:** 26/11/2026
* **Período da Sprint:** 17/11/2026 a 26/11/2026
* **Horário (Timebox):** 18:00 às 18:45 (45 minutos)
* **Release:** 2
* **Participantes:** Equipe AgroSafe
    * João Gabriel (PO + DevOps)
    * Antonio Lins (SM)
    * Joaquim Laureano (QA)

## 1. Pauta da Reunião
1. Demonstração do incremento da Sprint 3: painel de talhões, atalhos, telas de cadastro e registro, e cancelamento com motivo.
2. Verificação e registro do aceite ou rejeição de cada US nas *issues* do Jira.
3. Apresentação das *release notes* da sprint, com foco em valor para o usuário.

## 2. Resultados da Sprint
* **Meta da sprint:** entregar o painel de estado dos talhões, o cancelamento de aplicações e colheitas com motivo (com recálculo da liberação) e as telas necessárias para operar o sistema. Isso inclui o botão Colher desabilitado (US-14), adiado da Sprint 2.
* **US entregues:** 12 de 12 planejadas (54 de 54 pontos), conforme o código integrado na `main`. *[Confirmar o aceite do PO issue a issue no Jira antes de fechar esta ata.]*
* **Primeira sprint com interface demonstrável por telas.** Nas sprints anteriores o incremento só podia ser mostrado pela API.
* **US-14 (SCRUM-51)**, pendente da Sprint 2, foi concluída: o card do talhão em quarentena mostra o botão de colheita desabilitado e o aviso "Colheita bloqueada até dd/mm/aaaa".
* **Cancelamento (US-20 a US-22):** entregue pela API, como decidido no planning (`PATCH /aplicacoes/{id}/cancelamento` e `PATCH /colheitas/{id}/cancelamento`). A tela de cancelamento e o histórico do talhão ficam para a próxima sprint.

## 3. Detalhe por US

| Issue | US | Épico | Pontos | Responsável | Resultado |
|---|---|---|---|---|---|
| SCRUM-51 | US-14: Exibir o botão Colher desabilitado com a data de liberação | EP04 | 3 | Antonio | Entregue |
| SCRUM-57 | US-17: Ver o estado dos talhões por cor e texto | EP06 | 5 | Joaquim | Entregue |
| SCRUM-58 | US-18: Ver os contadores do painel | EP06 | 5 | Joaquim | Entregue |
| SCRUM-59 | US-19: Usar atalhos para registrar aplicação e colheita | EP06 | 3 | Antonio | Entregue |
| SCRUM-61 | US-20: Cancelar aplicação com motivo | EP07 | 5 | João | Entregue (API) |
| SCRUM-62 | US-21: Recalcular a liberação do talhão após o cancelamento | EP07 | 5 | Joaquim | Entregue (API) |
| SCRUM-63 | US-22: Cancelar colheita com motivo | EP07 | 3 | Antonio | Entregue (API) |
| SCRUM-68 | UI-01: Integrar o login ao backend e proteger as rotas | EP01 | 5 | Antonio | Entregue |
| SCRUM-69 | UI-02: Tela de registrar aplicação | EP03 | 5 | João | Entregue |
| SCRUM-70 | UI-03: Tela de registrar colheita | EP04 | 5 | Joaquim | Entregue |
| SCRUM-71 | UI-04: Telas de talhões (listar, cadastrar, editar e remover) | EP02 | 5 | Antonio | Entregue |
| SCRUM-72 | UI-05: Telas de defensivos (listar, cadastrar, editar e remover) | EP02 | 5 | João | Entregue |
| | **Total** | | **54** | | **54 entregues** |

**Pontos por responsável:** Antonio 19 · Joaquim 20 · João 15.

## 4. Demonstração
* **Roteiro:** login → painel (cores, contadores) → atalho "Registrar aplicação" → painel com talhão em quarentena e data de liberação → botão de colheita desabilitado → talhões e defensivos → cancelamento com motivo pelo Swagger → talhão com liberação recalculada.
* **Ambiente:** local (frontend React + backend Spring Boot com PostgreSQL).
* **Conferência dos drivers de qualidade:**

| Driver | Verificação | Situação |
|---|---|---|
| D7 | Uso em celular (360 px) | *[QA confirmar o teste manual em 360 px]* |
| D8 | Estado do talhão visível de imediato (cor + texto) | Atendido no painel: cor e texto ("Livre" / "Em quarentena") |
| D9 | Poucos passos para registrar | Atalhos do painel levam à tela já com o talhão selecionado (`?talhaoId=`) |
| D5 | Cancelamento justificado | Atendido: motivo obrigatório (máx. 500 caracteres) |
| D3 | Cancelamento e recálculo na mesma transação | Atendido: método transacional no `AplicacaoService` |
| D4 | Imutabilidade | Cancelamento por `PATCH`, sem `PUT`/`DELETE` nos registros |

## 5. Qualidade e Evidências
* **Testes:** cerca de 111 métodos de teste no backend (eram 94 ao fim da Sprint 2) e 43 casos no frontend (Vitest).
* **CI:** o pipeline agora roda backend (`verify` + JaCoCo), frontend (lint + build) e teste das migrações Flyway em PostgreSQL 16.
* **Cobertura do backend (JaCoCo):** 86 % das instruções e 69 % dos *branches*. O pacote `colheita` está em 53 %, o mais baixo do projeto.
* **Lacuna:** o CI não executa os testes do frontend (`npm run test:ci`), e as telas de talhões, defensivos, aplicação e login não têm teste automatizado.

## 6. Release Notes da Sprint 3
**Novidades**
* **Painel de talhões (US-17 e US-18):** cada talhão aparece como "Livre" ou "Em quarentena", com cor e texto, e três contadores mostram o total, os livres e os em quarentena, com o percentual.
* **Atalhos (US-19):** cada card leva direto para registrar aplicação ou colheita daquele talhão.
* **Botão de colheita desabilitado (US-14):** em quarentena, o sistema mostra até quando a colheita está bloqueada.
* **Cancelamento com motivo (US-20 e US-22):** o Gestor cancela uma aplicação ou colheita informando o motivo. O registro não é apagado.
* **Liberação recalculada (US-21):** ao cancelar uma aplicação, a data de liberação do talhão é recalculada.
* **Telas do sistema (UI-01 a UI-05):** login integrado com rotas protegidas, registro de aplicação, registro de colheita, e gestão de talhões e defensivos.

**Ainda não disponível**
* Tela de cancelamento e histórico do talhão (hoje o cancelamento só pela API).
* Testes de ponta a ponta (Cucumber/Cypress).

## 7. Encaminhamentos
1. **Jira:** mover para Concluído as 12 *issues* aceitas e registrar o aceite em cada uma.
2. **Próxima sprint:** planejar a tela de cancelamento e o histórico do talhão.
3. **Qualidade:** executar o teste manual em 360 px (D7) e registrar a evidência.
4. **CI:** incluir `npm run test:ci` no pipeline do frontend.
5. **Release 2:** consolidar as evidências (cobertura, PRs fechados, GitHub Actions) na pasta `docs/releases/release2/evidencias`.
