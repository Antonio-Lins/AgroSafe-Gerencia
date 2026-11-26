# Release 2: Review

**Projeto:** AgroSafe · **Release:** 2 · **Resultado demonstrável:** Aplicação, colheita com bloqueio por quarentena, painel e cancelamento
**Período:** 02/11/2026 a 26/11/2026 (Sprints 2 e 3) · **Data da review:** 26/11/2026
**Participantes:** João Gabriel (PO + DevOps), Antonio Lins (SM), Joaquim Laureano (QA)

## 1. Objetivo da release
Entregar o núcleo do negócio sobre a fundação da Release 1: registrar aplicações com a data de liberação calculada, impedir a colheita de talhões em quarentena, mostrar o estado dos talhões em um painel e permitir o cancelamento justificado de registros. A release também entrega as primeiras telas do sistema e o banco PostgreSQL com Flyway, que ficaram pendentes da Release 1.

## 2. Escopo comprometido × entregue
| Épico | USs | Pts planejados | Pts entregues | Situação |
|-------|-----|----------------|---------------|----------|
| EP03 Registro de aplicação | US-09 a US-11, UI-02 | 21 | 21 | Entregue |
| EP04 Registro de colheita | US-12 a US-14, UI-03 | 21 | 21 | Entregue (US-14 adiada da Sprint 2 para a Sprint 3) |
| EP05 Banco de dados | US-15 e US-16 | 8 | 8 | Entregue |
| EP06 Painel | US-17 a US-19 | 13 | 13 | Entregue |
| EP07 Cancelamento | US-20 a US-22 | 13 | 13 | Entregue pela API (tela fica para a Release 3) |
| EP01 / EP02 Telas de acesso e cadastros | UI-01, UI-04, UI-05 | 15 | 15 | Entregue |
| **Total** | 23 US | **91** | **91** | |

*Os 91 pontos são o escopo único da release. As sprints somam 94 pontos planejados (40 + 54) porque a US-14 (3 pts) foi planejada nas duas e entregue uma vez, na Sprint 3. Pontos entregues por sprint: 37 (Sprint 2) e 54 (Sprint 3).*

**Critério de pronto do épico:** todas as US do épico aceitas pelo PO e com a Definition of Done atendida. *[Confirmar o aceite no Jira antes de fechar o documento.]*

## 3. Demonstração da release
Roteiro usado: login Gestor → painel (cores e contadores) → registrar aplicação pelo atalho → talhão em quarentena com data de liberação → botão de colheita desabilitado → registrar colheita em talhão livre → cancelar aplicação com motivo (Swagger) → liberação recalculada → CRUD de talhões e defensivos.
Ambiente: local (frontend React, backend Spring Boot, PostgreSQL).

## 4. Requisitos atendidos
| Requisito | Descrição | Atendido? |
|-----------|-----------|-----------|
| RF01 | CRUD de talhão e defensivo, agora com telas (UI-04 e UI-05) | Sim |
| Aplicação | Registro com data de liberação calculada, maior data mantida no talhão, rejeição de data futura (US-09 a US-11) | Sim |
| Colheita | Registro e recusa em quarentena, com botão desabilitado na interface (US-12 a US-14) | Sim |
| Painel | Estado por cor e texto, contadores e atalhos (US-17 a US-19) | Sim |
| Cancelamento | Cancelamento com motivo e recálculo da liberação na mesma transação (US-20 a US-22) | Sim, pela API |
| RNF03 | Autenticação, BCrypt e perfis, agora com login integrado e rotas protegidas (UI-01) | Sim |
| RNF02 | Layout responsivo (base, 360 px) | *[QA confirmar o teste manual em 360 px]* |
| Banco de dados | PostgreSQL com migrações Flyway testadas no CI (US-15 e US-16) | Sim |

## 5. Qualidade da release
* **Testes:** cerca de 111 métodos de teste no backend e 43 casos no frontend.
* **Cobertura do backend (JaCoCo):** 86 % das instruções e 69 % dos *branches*. O pacote `colheita` está em 53 %.
* **CI:** 3 jobs a cada PR (backend com cobertura, frontend com lint e build, e migrações Flyway em PostgreSQL 16).
* **Evidências:** prints de cobertura (backend e frontend), de PRs fechados e do GitHub Actions em `docs/releases/release2/evidencias`.

## 6. O que ficou de fora e por quê
| Item | Motivo | Destino |
|------|--------|---------|
| Tela de cancelamento e histórico do talhão | Decisão do planning da Sprint 3: entregar o cancelamento pela API primeiro | Release 3 |
| Testes de ponta a ponta (Cucumber/Cypress) e cenário `@smoke` | Não priorizados nas duas sprints | Release 3 |
| Testes automatizados do frontend no CI | Testes existem, mas o pipeline não os executa | Release 3 |

## 7. Decisão sobre a próxima release
Release 3: histórico do talhão e tela de cancelamento, testes de ponta a ponta e as US restantes do backlog, conforme a priorização do PO. A capacidade deve partir da velocity média das três primeiras sprints (cerca de 47 pontos).

**Responsável:** Antonio Lins
