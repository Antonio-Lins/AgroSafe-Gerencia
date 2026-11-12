# Ata de Sprint Retrospective - Sprint 2 (AgroSafe)

* **Projeto:** AgroSafe · **Release:** 2 · **Sprint:** 2
* **Data:** 12/11/2026
* **Horário (Timebox):** 18:45 às 19:30 (45 minutos)
* **Participantes:** João Gabriel, Joaquim Laureano, Antonio Lins

## 1. Pauta da Reunião
1. Análise métrica: Avaliação do *burndown* e do *velocity* da equipa.
2. Revisão obrigatória dos 9 riscos do projeto.
3. Dinâmica de melhoria contínua (O que correu bem, o que correu mal e oportunidades de melhoria).
4. Consolidação do plano de ação com responsáveis e prazos claros.

## 2. Análise de Processos, Riscos e Análise de Métricas (Burndown e Velocity)

### Resumo da sprint (contexto)
* Meta: aplicação e colheita com regra de quarentena, banco PostgreSQL e início do frontend (EP03, EP04 e EP05, US-09 a US-16).
* Entregue: 32 de 40 pts · USs concluídas: 6/8 (US-09, US-11, US-12, US-13, US-15 e US-16).
* Não concluídas: US-10 (em revisão, fora da `main`) e US-14 (parcial, só a tela de login).

### O que foi bem (manter)
| # | Ponto |
|---|-------|
| 1 | Divisão das USs por integrante, com responsável claro de cada ticket no Jira |
| 2 | Fluxo de branch com Pull Request, template de descrição e revisão por um colega |
| 3 | Testes automatizados junto de cada regra e CI rodando a cada PR (94 testes) |
| 4 | Banco PostgreSQL com Flyway entregue, resolvendo a pendência da Sprint 1 |

### O que pode melhorar (problemas)
| # | Ponto |
|---|-------|
| 1 | Dependências entre USs: US-12 e US-13 criaram a mesma base de colheita em paralelo, e US-13 e US-14 dependem da US-10 |
| 2 | Escopo da US-14 mudou no meio da sprint (de botão no protótipo para início do frontend em React) |
| 3 | Entregas concentradas no fim: na véspera do fim da sprint nenhum ticket estava concluído |
| 4 | Versões de ferramentas divergentes entre documentos (problema que já tinha aparecido na Sprint 1) |

### Métricas
* **Métricas:** a previsibilidade caiu em relação à Sprint 1 (49 de 49 pts, 100 %). Nesta sprint foram 32 de 40 pts, 80 %, e a queima de escopo ficou concentrada no fim.
* **Riscos Revistos:** os 9 riscos foram revisados e o status de cada um está na seção 5.
* **Velocity da Sprint:** 32 pontos entregues (vs. 40 planejados).
* **Análise do Burndown:** na véspera do fim da sprint (11/11), 3 tickets (16 pts) estavam em revisão e 5 (24 pts) em andamento, sem nenhum concluído. O gráfico de burndown do Jira deve mostrar queda tardia, o que reduz a previsibilidade e deixa pouco tempo para a revisão dos PRs.
* **Conclusões para o próximo ciclo:** mapear as dependências entre USs antes de distribuir o trabalho, abrir PRs menores ao longo da sprint e quebrar USs grandes (as de 8 pontos, US-09 e US-13, concentraram boa parte do risco).

## 3. Plano de Ação (Acompanhamento para a próxima sprint)
| Melhoria Identificada | Ação Prática | Responsável | Prazo |
| :--- | :--- | :--- | :--- |
| Dependências entre USs não mapeadas | Listar as dependências no planning e ordenar as USs de acordo (ex.: US-10 antes de US-13 e US-14) | Antonio | Planning da Sprint 3 |
| Entregas concentradas no fim da sprint | Abrir PRs menores ao longo da sprint e acompanhar o quadro diariamente | Todos | Contínuo |
| Cobertura do CI | Incluir o build do frontend e o teste das migrações Flyway no pipeline | Joaquim | Sprint 3 |

## 4. Plano de Ação (Acompanhamento da Melhoria Contínua)
*Registe abaixo as ações práticas definidas pela equipa para resolver os problemas levantados na retro.*

| O que melhorar? (Problema) | Ação Prática a Ser Feita | Responsável | Prazo | Status Atual |
| :--- | :--- | :--- | :--- | :--- |
| Trabalho duplicado entre US-12 e US-13 | Avisar no Jira e no grupo antes de criar uma estrutura compartilhada (entidade, endpoint) | Todos | Contínuo | Em andamento |
| Escopo da US-14 pouco claro | Refinar o critério de aceite com o PO antes da planning | João Gabriel | Planning da Sprint 3 | Pendente |
| Versões divergentes (Java 21 no release notes da Release 1, Java 25 no README e no CI) | Padronizar a versão do Java no README, no release notes e no `pom.xml` | Antonio | Sprint 3 | Pendente |
| Ausência de testes de ponta a ponta (Cucumber/Cypress) | Alinhar com o QA o que entra primeiro e criar o cenário `@smoke` | Joaquim | Sprint 3 | Pendente |

---
*Nota para a Sprint Seguinte: Na próxima Sprint Planning ou Retrospective, o Scrum Master deve abrir esta tabela para verificar o que foi concluído e o que ficou pendente.*


## 5. Revisão dos 9 Riscos do Projeto
*Confirmação da revisão de todos os riscos mapeados para o projeto ao fim desta sprint.*

| ID do Risco | Descrição do Risco | Status Atual (Nesta Sprint) | Ação de Mitigação / Observação |
| :--- | :--- | :--- | :--- |
| R01 | Instabilidade ou atraso na configuração do ambiente de testes | Controlado / Não ocorreu | CI rodando a cada PR; mantida a monitorização |
| R02 | Dificuldade na migração do banco de dados para PostgreSQL | Em monitorização | Migrações Flyway e perfis `postgres` e `migrationtest` criados; falta testar as migrações no CI |
| R03 | Falta de clareza nos critérios de aceitação das User Stories | Ocorreu | Escopo da US-14 mudou durante a sprint; refinar com o PO antes da planning |
| R04 | Atraso na entrega das tarefas por sobrecarga de um membro | Controlado | Carga equilibrada (cerca de 13 a 14 pts por pessoa); acompanhar a velocity |
| R05 | Divergência de versões de ferramentas (ex: Java no CI vs README) | Em aberto | Java 21 no release notes da Release 1 e Java 25 no README e no CI; padronizar |
| R06 | Ausência de registo de evidências de testes | Em aberto | Anexar a evidência de execução (saída do `verify`) em cada PR |
| R07 | Falha na comunicação síncrona de impedimentos | Ocorreu | Duplicidade entre US-12 e US-13 e dependência da US-10 não comunicadas a tempo; registrar no Jira ao identificar |
| R08 | Incompatibilidade ou problemas com os testes automatizados (Cucumber/Cypress) | Em aberto | Não implementado nesta sprint; alinhar com o QA |
| R09 | Escopo desalinhado com as expectativas da disciplina | Controlado | US-17 a US-19 e US-21 ficam para a Sprint 3, conforme a priorização do PO |