# Registro de Métricas - AgroSafe

Este documento armazena o histórico do *velocity* e o desempenho das entregas da equipa ao longo das sprints, garantindo a transparência das métricas apontadas no projeto.

## 1. Histórico de Sprints e Velocity
| Sprint | Planeado (Story Points) | Entregue (Story Points) | Taxa de Previsibilidade | Observações / Desvios |
| :--- | :--- | :--- | :--- | :--- |
| **Sprint 1** | 49 | 49 | 100 % | Projeto executado com H2 em memória; migração para PostgreSQL ficou para a Sprint 2. |
| **Sprint 2** | 40 | 37 | 92,5 % | 7 de 8 US entregues. US-14 adiada por depender da interface; entregas concentradas no fim da sprint. |
| **Sprint 3** | 54 | 54 *(confirmar no Jira)* | 100 % | 12 de 12 US. Primeira sprint com telas. Cancelamento entregue só pela API. Planejado 35 % acima da Sprint 2. |
| **Sprint 4** | — | — | — | — |

**Velocity média (Sprints 1 a 3):** 46,7 pontos por sprint.

## 2. Distribuição de carga na Sprint 3
| Responsável | US | Pontos |
| :--- | :--- | :--- |
| Antonio | US-14, US-19, US-22, UI-01, UI-04 | 19 |
| Joaquim | US-17, US-18, US-21, UI-03 | 20 |
| João | US-20, UI-02, UI-05 | 15 |
| **Total** | 12 US | **54** |

## 3. Burndown da Sprint 3 (17/11 a 26/11)
Linha ideal: 54 pontos em 8 dias úteis (6,75 pontos por dia).

| Dia | 17/11 | 18/11 | 19/11 | 20/11 | 23/11 | 24/11 | 25/11 | 26/11 |
| :--- | :--- | :--- | :--- | :--- | :--- | :--- | :--- | :--- |
| Ideal (restante) | 47,3 | 40,5 | 33,8 | 27,0 | 20,3 | 13,5 | 6,8 | 0 |
| Real (restante) | *Jira* | *Jira* | *Jira* | *Jira* | *Jira* | *Jira* | *Jira* | *Jira* |

*Preencher a linha "Real" com o gráfico de burndown do Jira e anexar a imagem à Retrospectiva.*

## 4. Análise de Desempenho
* **Ritmo da Equipa:** 49 → 37 → 54 pontos. A queda da Sprint 2 veio de uma US dependente de interface; a alta da Sprint 3 veio da entrega das telas, que concentraram 25 dos 54 pontos.
* **Previsibilidade:** 100 % → 92,5 % → 100 %. A Sprint 3 só deve ser tratada como referência de capacidade se o burndown mostrar queima gradual.
* **Burndown:** utilizado para monitorizar a queima diária de escopo e identificar gargalos ou acúmulo de tarefas na reta final. Na Sprint 2 nenhum ticket estava concluído na véspera do fim.
* **Qualidade (apoio às métricas):** cobertura do backend em 86 % das instruções e 69 % dos *branches* (JaCoCo); CI com 3 jobs (backend, frontend e PostgreSQL).

## 5. Consolidado por Release
| Release | Sprints | Planeado (pts) | Entregue (pts) | Previsibilidade | Observações |
| :--- | :--- | :--- | :--- | :--- | :--- |
| **Release 1** | Sprint 1 | 49 | 49 | 100 % | Cadastros e acesso; banco de dados ficou para a Release 2. |
| **Release 2** | Sprints 2 e 3 | 94 | 91 | 96,8 % | A diferença de 3 pts é a US-14, planejada na Sprint 2 e entregue na Sprint 3. Em escopo único, 91 de 91 pts. |
| **Release 3** | — | — | — | — | — |
