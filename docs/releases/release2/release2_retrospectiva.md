# Release 2: Retrospectiva

**Projeto:** AgroSafe · **Release:** 2 · **Data:** 26/11/2026 · **Facilitador:** Antonio Lins (SM)
**Participantes:** João Gabriel, Joaquim Laureano, Antonio Lins

## 1. Linha do tempo
| Data        | Evento |
|-------------|--------|
| 02/11/2026  | Início da Sprint 2 (aplicação, colheita e banco de dados) |
| 11/11/2026  | Review e Retrospectiva da Sprint 2: 37 de 40 pts, US-14 adiada |
| 16/11/2026  | Planning da Sprint 3 (54 pts, com as telas e o painel) |
| 26/11/2026  | Review e Retrospectiva da Sprint 3 e da Release 2: 54 de 54 pts |

## 2. O que funcionou
| # | Ponto |
|---|-------|
| 1 | Regras de negócio no backend antes da interface, o que deu base para as telas da Sprint 3 |
| 2 | Fluxo de branch com Pull Request, revisão por um colega e CI a cada PR |
| 3 | PostgreSQL com Flyway entregue e testado no CI, resolvendo a pendência da Release 1 |
| 4 | Primeira entrega com telas: o incremento deixou de ser demonstrável só pela API |
| 5 | Decisões de escopo registradas no planning (cancelamento pela API primeiro), evitando retrabalho |
| 6 | Meta da Sprint 3 cumprida mesmo com 35 % mais pontos que a Sprint 2 |

## 3. O que não funcionou
| # | Ponto | Causa raiz |
|---|-------|------------|
| 1 | Entregas concentradas no fim da Sprint 2 (nenhum ticket concluído na véspera) | PRs grandes e US de 8 pontos |
| 2 | Dependências entre US não mapeadas (US-12 e US-13 criaram a mesma base em paralelo) | Comunicação de impedimentos fora do Jira |
| 3 | Escopo da US-14 mudou no meio da Sprint 2 e a US foi adiada | Critério de aceite pouco claro e dependência da interface |
| 4 | Versão do Java divergente entre `pom.xml`, README, CI e release notes | Falta de padronização e de dono da tarefa |
| 5 | Testes de ponta a ponta não foram feitos e os testes do frontend não rodam no CI | Prioridade dada às funcionalidades |
| 6 | Cobertura baixa no pacote `colheita` (53 %) | Testes concentrados em aplicação, talhão e defensivo |

## 4. Aprendizados técnicos
- Cálculo da data de liberação e manutenção da maior data no talhão
- Transações no cancelamento e recálculo (`@Transactional`)
- PostgreSQL com Flyway e teste de migração no CI (Testcontainers/serviço `postgres:16`)
- React: rotas protegidas, contexto de autenticação, formulários e testes com Vitest
- GitHub Actions com múltiplos jobs (backend, frontend, PostgreSQL) e relatório JaCoCo

## 5. Aprendizados de processo
- Mapear dependências entre US antes de distribuir o trabalho
- Quebrar US grandes e abrir PRs menores
- Medir *velocity* e previsibilidade por sprint (49, 37 e 54 pontos)
- Registrar no planning as decisões de escopo e os riscos de dependência

## 6. Riscos de qualidade: como ficaram
| Atributo | Risco do plano | Mitigação prevista | Situação na release |
|----------|----------------|--------------------|---------------------|
| Confiabilidade | Data de liberação calculada errada ou talhão liberado antes do prazo | Testes unitários e de integração das regras (D2) | Cobre o risco |
| Integridade | Cancelamento e recálculo gravados em partes (D3) | Mesma transação | Cobre o risco |
| Auditabilidade | Registros apagados ou alterados (D4 e D5) | Cancelamento por `PATCH` com motivo obrigatório, sem `PUT`/`DELETE` | Cobre o risco |
| Usabilidade | Telas inadequadas ao celular (D7, D8 e D9) | Mobile-first, estado por cor e texto, atalhos no painel | Parcial: telas feitas, teste em 360 px *[QA confirmar]* |
| Testabilidade | Regressão sem teste de ponta a ponta | Cucumber/Cypress e `@smoke` | Não cobre o risco, passa para a Release 3 |

## 7. Decisões tomadas
- Entregar o cancelamento pela API na Release 2 e a tela na Release 3
- Incluir frontend (lint e build) e migrações Flyway no CI
- Padronizar a versão do Java no início da Release 3
- Priorizar o cenário `@smoke` e a execução dos testes do frontend no CI
- Planejar a Release 3 pela velocity média (cerca de 47 pontos) e confirmar a capacidade por pessoa

## 8. Métricas da release
| Sprint | Planejado | Entregue | Previsibilidade |
|--------|-----------|----------|-----------------|
| Sprint 2 | 40 | 37 | 92,5 % |
| Sprint 3 | 54 | 54 *(confirmar no Jira)* | 100 % |
| **Release 2** | **94** | **91** | **96,8 %** |

*A diferença de 3 pontos é a US-14, planejada na Sprint 2 e entregue na Sprint 3. Em escopo único, a release entregou 91 de 91 pontos.*
