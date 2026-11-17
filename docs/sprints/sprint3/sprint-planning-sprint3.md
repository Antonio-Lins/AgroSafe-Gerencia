# Ata de Sprint Planning - Sprint 3 (AgroSafe)

* **Data:** 16/11/2026
* **Período da Sprint:** 17/11/2026 a 26/11/2026
* **Horário (Timebox):** 19:00 às 20:00 (1 hora)
* **Release:** 2
* **Participantes:** Equipa AgroSafe
  * João Gabriel (Product Owner)
  * Antonio Lins (Scrum Master)
  * Joaquim Laureano (QA)

## 1. Pauta da Reunião
1. Validação e refinamento do backlog priorizado pelo Product Owner.
2. Seleção das User Stories para a Sprint 3 (painel, cancelamento de registos e a US-14, pendente da Sprint 2).
3. Verificação do status "Pronto para a sprint" e atribuição de *story points* e responsáveis nas US.
4. Apresentação da Definition of Done da sprint ao time.
5. Definição do objetivo principal da sprint.

## 2. Decisões e Alinhamentos

* **Objetivo da Sprint:** entregar o painel de estado dos talhões (cor, texto, contadores e atalhos), o cancelamento de aplicações e colheitas com motivo, com a data de liberação do talhão recalculada, além das interfaces necessárias para o registro de aplicações, colheitas e gerenciamento de talhões e defensivos. A sprint também conclui o botão Colher desabilitado (US-14), que ficou pendente da Sprint 2 por depender da interface.

* **User Stories Selecionadas:** 12 US, sendo 29 pontos estimados nas 7 histórias funcionais e 5 histórias de interface com 25 pontos estimados, no total são 54.

| Issue | US                                                                | Épico | Pontos | Responsável | Origem |
|---|-------------------------------------------------------------------|---|--------|-------------|---|
| SCRUM-51 | US-14: Exibir o botão Colher desabilitado com a data de liberação | EP04: Registro de colheita | 3      | Antonio     | Pendente da Sprint 2 |
| SCRUM-57 | US-17: Ver o estado dos talhões por cor e texto                   | EP06: Painel | 5      | Joaquim     | Nova |
| SCRUM-58 | US-18: Ver os contadores do painel                                | EP06: Painel | 5      | Joaquim     | Nova |
| SCRUM-59 | US-19: Usar atalhos para registrar aplicação e colheita           | EP06: Painel | 3      | Antonio     | Nova |
| SCRUM-61 | US-20: Cancelar aplicação com motivo                              | EP07: Cancelamento | 5      | João        | Nova |
| SCRUM-62 | US-21: Recalcular a liberação do talhão após o cancelamento       | EP07: Cancelamento | 5      | Joaquim     | Nova |
| SCRUM-63 | US-22: Cancelar colheita com motivo                               | EP07: Cancelamento | 3      | Antonio     | Nova |
| SCRUM-68 | UI-01: Integrar o login ao backend e proteger as rotas            | EP01: Fundação e autenticação | 5      | Antonio     | Nova (UI) |
| SCRUM-69 | UI-02: Tela de registrar aplicação                                | EP03: Registro de aplicação | 5      | João        | Nova (UI) |
| SCRUM-70 | UI-03: Tela de registrar colheita                                 | EP04: Registro de colheita | 5      | Joaquim     | Nova (UI) |
| SCRUM-71 | UI-04: Telas de talhões (listar, cadastrar, editar e remover)     | EP02: CRUD de talhões | 5      | Antonio     | Nova (UI) |
| SCRUM-72 | UI-05: Telas de defensivos (listar, cadastrar, editar e remover)  | EP02: CRUD de defensivos | 5      | João        | Nova (UI) |
| | **Total**                                                         | | **54** |             | |

* **Capacidade da Equipe:** referência da Sprint 2, com 37 pontos entregues de 40 planejados, em sua maioria de backend. A seleção atual contempla 54 pontos estimados. A carga final deve ser confirmada com os responsáveis nesta reunião, considerando que o desenvolvimento front-end é novo para a equipe, com apenas a página de login pronta até agora.
## 3. Dependências e Riscos Identificados
* **Interface ainda inexistente:** as US-14, US-17, US-18 e US-19 são de interface. Hoje existe apenas a página de login, e o incremento das sprints anteriores é demonstrável só pela API.
* **Atalhos do painel (US-19):** levam ao registo de aplicação e de colheita, cujas telas não fazem parte da seleção.
* **Botão Colher (US-14):** aparece na tela do talhão ou da colheita, que também ainda não existe.
* **Cancelamento (US-20 a US-22):** o fluxo do documento de descrição parte do histórico do talhão, onde o Gestor escolhe o registo a cancelar. O histórico não está nesta sprint. A decisão fica:
  * entregar o cancelamento pela API nesta sprint, com a tela na seguinte.
* **Concorrência no cancelamento e no recálculo:** o controle de concorrência está fora do escopo do MVP (risco aceite).

## 4. Qualidade e Drivers Previstos
* **Painel (US-17 a US-19):** primeiras verificações dos drivers de usabilidade em campo: D7 (uso em celular, 360 px), D8 (estado visível de imediato) e D9 (poucos passos para registar).
* **Cancelamento (US-20 a US-22):** drivers D5 (cancelamento justificado), D4 (imutabilidade), D2 (cálculo correto da data de liberação no recálculo) e D3 (cancelamento e recálculo na mesma transação).
* **DoD da sprint:** apresentada ao time nesta reunião. Como há telas, o critério da equipa de teste manual em 360 px passa a se aplicar.

## 5. Encaminhamentos
1. Mover as 7 *issues* para "Pronto para a sprint", preencher os campos Sprint e Release e atribuir um responsável a cada uma.
2. O PO marca o DoR nas US selecionadas.
3. Definir as datas da Sprint 3 no Jira.
4. Apresentar a DoD da sprint ao time.
5. Atualizar o Plano da Release II com o escopo da Sprint 3.
6. Registar a decisão sobre as US de interface que faltam (telas de registo de aplicação e de colheita) e sobre o histórico, e reestimar a sprint conforme o resultado.
