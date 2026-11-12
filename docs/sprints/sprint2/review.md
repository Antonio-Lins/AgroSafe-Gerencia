# Ata de Sprint Review - Sprint 2 (AgroSafe)

* **Data:** 11/11/2026
* **Período da Sprint:** 02/11/2026 a 12/11/2026
* **Horário (Timebox):** 18:00 às 18:45 (45 minutos)
* **Participantes:** Equipe AgroSafe
    * João Gabriel (PO + DevOps)
    * Antonio Lins (SM)
    * Joaquim Laureano (QA)

## 1. Pauta da Reunião
1. Demonstração prática do incremento funcional entregue na Sprint 2.
2. Verificação e registo formal do aceite ou rejeição de cada US nas *issues* do Jira.
3. Apresentação das *release notes* redigidas com foco em valor para o utilizador.

## 2. Resultados da Sprint
* **US Entregues e Aprovadas:** 7 das 8 US planejadas (37 de 40 pontos), aceites pelo PO.
    * **US-09** (SCRUM-41): Registrar aplicação com cálculo da data de liberação.
    * **US-10** (SCRUM-46): Manter no talhão a maior data de liberação.
    * **US-11** (SCRUM-47): Rejeitar aplicação com data futura.
    * **US-12** (SCRUM-49): Registrar colheita.
    * **US-13** (SCRUM-50): Recusar colheita de talhão em quarentena.
    * **US-15** (SCRUM-53): Configurar o ambiente e criar o banco de dados.
    * **US-16** (SCRUM-54): Implementar a estrutura e integrar o banco de dados.
* **US Pendentes / Rejeitadas:**
    * **US-14** (SCRUM-51): Exibir o botão Colher desabilitado com a data de liberação. **Não entregue.**
        * **Motivo:** a US depende da interface web, e o desenvolvimento do front-end só começou nesta sprint. Até agora existe apenas a página de login.
        * **Situação:** não foi rejeitada pelo PO, foi adiada. O motivo fica registado na *issue* e a US volta ao *backlog*, para ser replanejada quando as telas de talhão e de colheita existirem.
        * **Impacto:** nenhum na regra de negócio. A recusa de colheita em quarentena já é garantida no backend pela US-13, e a US-14 apenas a reforça na interface.
* **Feedback Geral:**
    * O núcleo da regra de negócio já funciona no backend: a aplicação é registada com a data de liberação calculada, o talhão passa a ter a maior data de liberação entre as suas aplicações, aplicações com data futura são rejeitadas, e a colheita é registada ou recusada conforme o talhão esteja livre ou em quarentena.
    * O ponto de atenção é a interface: o front-end ainda tem só a página de login, e por isso o incremento desta sprint é demonstrável pela API, e não por telas.
    * O ambiente e o banco de dados ficaram configurados e integrados (US-15 e US-16), o que dá base para as próximas USs.

## 3. Detalhe por US

| Issue | US | Épico | Pontos | Responsável | Resultado |
|---|---|---|---|---|---|
| SCRUM-41 | US-09: Registrar aplicação com cálculo da data de liberação | EP03 | 8 | João Gabriel | Entregue e aprovada |
| SCRUM-46 | US-10: Manter no talhão a maior data de liberação | EP03 | 5 | João Gabriel | Entregue e aprovada |
| SCRUM-47 | US-11: Rejeitar aplicação com data futura | EP03 | 3 | Antonio | Entregue e aprovada |
| SCRUM-49 | US-12: Registrar colheita | EP04 | 5 | Joaquim | Entregue e aprovada |
| SCRUM-50 | US-13: Recusar colheita de talhão em quarentena | EP04 | 8 | Antonio | Entregue e aprovada |
| SCRUM-51 | US-14: Exibir o botão Colher desabilitado com a data de liberação | EP04 | 3 | Antonio | **Não entregue** (depende da interface) |
| SCRUM-53 | US-15: Configurar o ambiente e criar o banco de dados | EP05 | 3 | Joaquim | Entregue e aprovada |
| SCRUM-54 | US-16: Implementar a estrutura e integrar o banco de dados | EP05 | 5 | Joaquim | Entregue e aprovada |

## 4. Release Notes da Sprint 2
Redigidas com foco no valor para o utilizador.

**Novidades**
* **Data de liberação automática (US-09):** ao registar uma aplicação de defensivo, o sistema calcula sozinho a data a partir da qual a colheita é segura, sem contas manuais, e guarda quem fez o registo.
* **Talhão liberado pela carência mais longa (US-10):** quando um talhão recebe vários defensivos, ele só fica liberado depois do prazo mais longo. Uma aplicação com carência menor não adianta a liberação.
* **Proteção contra datas impossíveis (US-11):** o sistema recusa aplicações com data futura. Lançamentos de hoje ou de dias anteriores (lançamentos atrasados) continuam aceites.
* **Registo de colheita (US-12):** a colheita é registada com a data e a quantidade colhida, associada a quem a registou.
* **Colheita bloqueada em quarentena (US-13):** o sistema recusa a colheita de um talhão cujo prazo de carência ainda não terminou, impedindo a venda de produto com resíduo acima do permitido. No dia da liberação, a colheita é aceite.

**Base técnica**
* Ambiente e banco de dados configurados e integrados ao projeto (US-15 e US-16).

**Ainda não disponível**
* Botão Colher desabilitado, com a data de liberação, na interface (US-14). Será entregue quando as telas de talhão e de colheita estiverem em desenvolvimento.

## 5. Encaminhamentos
1. **US-14 (SCRUM-51):** registar o motivo na *issue*, retirá-la da Sprint 2 e devolvê-la ao *backlog*, para replanejamento junto com as USs de interface.
2. **Front-end:** continuar o desenvolvimento das telas, hoje apenas com a página de login, e planejar as USs de interface na próxima sprint.
3. **Jira:** mover as *issues* aceites (US-09, US-10, US-11, US-12, US-13, US-15 e US-16) para Concluído.