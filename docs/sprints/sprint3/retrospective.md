# Ata de Sprint Retrospective - Sprint 3 (AgroSafe)

* **Projeto:** AgroSafe · **Release:** 2 · **Sprint:** 3
* **Data:** 26/11/2026
* **Horário (Timebox):** 18:45 às 19:30 (45 minutos)
* **Participantes:** João Gabriel, Joaquim Laureano, Antonio Lins (SM)

## 1. Pauta da Reunião
1. Análise de métricas: *burndown* e *velocity*.
2. Acompanhamento das ações da Retrospectiva da Sprint 2.
3. Revisão dos 9 riscos do projeto.
4. Dinâmica de melhoria contínua (o que foi bem, o que pode melhorar).
5. Plano de ação para a Sprint 4.

## 2. Resumo da sprint (contexto)
* **Meta:** painel de estado dos talhões, cancelamento com motivo e recálculo da liberação, e as telas do sistema (EP06, EP07, US-14 e UI-01 a UI-05).
* **Entregue:** 54 de 54 pts · 12/12 US concluídas. *[Confirmar no Jira.]*
* **Contexto:** a sprint planejou 54 pontos, 35 % acima dos 40 planejados na Sprint 2, com o front-end ainda novo para a equipe.

## 3. Acompanhamento das ações da Sprint 2

| Ação | Responsável | Situação | Evidência |
|---|---|---|---|
| Listar as dependências no planning e ordenar as USs | Antonio | Concluída | Seção 3 da ata de planning lista as dependências (interface, atalhos, cancelamento) |
| Abrir PRs menores ao longo da sprint | Todos | *[Confirmar com o histórico de PRs]* | Comparar as datas de merge com o *burndown* |
| Incluir build do frontend e teste Flyway no CI | Joaquim | Concluída | Jobs `frontend` e `postgresql` no `ci.yml` |
| Refinar o critério de aceite da US-14 com o PO | João Gabriel | Concluída | US-14 detalhada no planning e entregue |
| Padronizar a versão do Java | Antonio | **Pendente** | `pom.xml` com Java 21, README e CI com Java 25, release notes da Release 1 com 21 |
| Criar o cenário `@smoke` (Cucumber/Cypress) | Joaquim | **Pendente** | Sem arquivos `.feature` nem testes Cypress no repositório |
| Avisar antes de criar estrutura compartilhada | Todos | Em andamento | Manter |

## 4. Dinâmica de melhoria contínua

### O que foi bem (manter)
| # | Ponto |
|---|-------|
| 1 | Primeira entrega com telas: o incremento deixou de depender só da API |
| 2 | Cancelamento entregue com motivo obrigatório, transação única e sem apagar o registro (D3, D4 e D5) |
| 3 | CI mais completo: frontend (lint e build) e migrações Flyway em PostgreSQL |
| 4 | Decisão de escopo registrada no planning (cancelamento só pela API), evitando retrabalho no meio da sprint |
| 5 | Meta cumprida mesmo com 35 % mais pontos que a sprint anterior |

### O que pode melhorar (problemas)
| # | Ponto |
|---|-------|
| 1 | Testes do frontend existem, mas não rodam no CI, e só cobrem painel e colheita |
| 2 | Pacote `colheita` com 53 % de cobertura no backend |
| 3 | Java divergente entre `pom.xml`, README, CI e release notes (pendente desde a Sprint 1) |
| 4 | Testes de ponta a ponta (Cucumber/Cypress) ainda não existem |
| 5 | Planning sem confirmação de carga: os 54 pontos foram aceitos com a ressalva de "front-end novo" |
| 6 | Documentos de cerimônia incompletos (ex.: `planning.md` da Sprint 2 ficou como modelo em branco) |

## 5. Métricas

* **Velocity:** 54 pontos entregues (Sprint 1: 49 · Sprint 2: 37). Média das três sprints: 46,7 pontos.
* **Previsibilidade:** 100 % (54 de 54), contra 92,5 % na Sprint 2.
* **Carga por pessoa:** Antonio 19 pts · Joaquim 20 pts · João 15 pts. A distribuição ficou menos equilibrada que na Sprint 2 (13 a 14 pts por pessoa).
* **Burndown (ideal):** 54 pontos em 8 dias úteis, ou 6,75 pontos por dia (tabela em `docs/metricas.md`). *[Anexar o gráfico do Jira e comentar se a queima foi gradual ou concentrada no fim, como na Sprint 2.]*
* **Conclusões para o próximo ciclo:** a velocity subiu junto com o escopo, mas a Sprint 4 deve partir de uma capacidade realista (média das três sprints: ~47 pts). A carga por pessoa deve ser revista no planning.

## 6. Plano de Ação (Sprint 4)

| Melhoria Identificada | Ação Prática | Responsável | Prazo | Status |
|---|---|---|---|---|
| Testes do frontend fora do CI | Adicionar `npm run test:ci` ao job `frontend` do `ci.yml` | Joaquim | Sprint 4 | Pendente |
| Versão do Java divergente | Padronizar Java no `pom.xml`, README, CI e release notes | Antonio | Início da Sprint 4 | Pendente |
| Sem teste de ponta a ponta | Criar o cenário `@smoke` (login → painel) | Joaquim | Sprint 4 | Pendente |
| Cobertura baixa em `colheita` | Cobrir os fluxos de cancelamento e recusa de colheita | Joaquim | Sprint 4 | Pendente |
| Carga desigual e entregas no fim da sprint | Confirmar a capacidade por pessoa no planning e acompanhar o quadro diariamente | Antonio | Planning da Sprint 4 | Pendente |
| Atas incompletas | Preencher a ata de planning no mesmo dia da cerimônia | Antonio | Contínuo | Pendente |

*Nota: na próxima Planning ou Retrospective, o Scrum Master deve abrir esta tabela para verificar o que foi concluído.*

## 7. Revisão dos 9 Riscos do Projeto

| ID | Descrição do Risco | Status Atual (Nesta Sprint) | Ação de Mitigação / Observação |
|---|---|---|---|
| R01 | Instabilidade ou atraso na configuração do ambiente de testes | Controlado | CI com 3 jobs rodando a cada PR |
| R02 | Dificuldade na migração do banco para PostgreSQL | Mitigado | `FlywayMigrationIT` roda no CI com PostgreSQL 16 (V1 a V3) |
| R03 | Falta de clareza nos critérios de aceitação | Controlado | US-14 refinada e entregue; manter o refinamento antes do planning |
| R04 | Atraso por sobrecarga de um membro | Em monitorização | 54 pts planejados (+35 %); carga de 15 a 20 pts por pessoa |
| R05 | Divergência de versões de ferramentas | Em aberto | Java 21 no `pom.xml` e na Release 1, Java 25 no README e no CI |
| R06 | Ausência de registro de evidências de testes | Em monitorização | Evidências da Release 2 reunidas; ainda não há evidência por PR |
| R07 | Falha na comunicação síncrona de impedimentos | Em monitorização | Registrar bloqueios no Jira conforme os acordos de trabalho |
| R08 | Problemas com testes automatizados (Cucumber/Cypress) | Em aberto | Nada implementado; priorizar o `@smoke` na Sprint 4 |
| R09 | Escopo desalinhado com as expectativas da disciplina | Controlado | US-17 a US-19 e US-21 entregues conforme a priorização do PO |
