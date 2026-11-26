
# AgroSafe — Release Notes da Release 2

**Projeto:** AgroSafe  
**Release:** 2 — Sprints 2 e 3  
**Período:** 02/11/2026 a 26/11/2026  
**Data de fechamento:** 26/11/2026  
**Situação:** Escopo funcional entregue — 23 histórias de usuário concluídas (91 story points únicos)

## 1. Visão geral

A Release 2 amplia o AgroSafe com as funcionalidades centrais de controle de aplicações de defensivos agrícolas e de colheitas. O sistema calcula a data de liberação de talhões a partir da carência dos produtos, preserva a maior data aplicável e impede o registro de colheitas durante a quarentena.

A entrega também integra PostgreSQL e Flyway, disponibiliza as primeiras interfaces React conectadas ao backend e incorpora cancelamentos justificados de aplicações e colheitas **pela API**. O incremento foi demonstrado em ambiente local com React, Spring Boot e PostgreSQL.

**Escopo:** 23 histórias concluídas, **91 de 91 story points** do escopo único da release. Na Sprint 2 foram entregues 37 de 40 pontos; a US-14, de 3 pontos, foi replanejada e concluída na Sprint 3, que entregou 54 de 54 pontos. Por isso, a soma dos planejamentos das sprints (94 pontos) não representa 94 pontos distintos de escopo.

## 2. Funcionalidades entregues

### 2.1 Aplicações e regras de carência — EP03

- **US-09 — Registro de aplicação:** cadastro da aplicação com talhão, defensivo, data e cálculo da data de liberação correspondente.
- **US-10 — Maior data de liberação:** a data do talhão reflete a liberação mais distante entre as aplicações ativas. Uma aplicação com prazo menor não antecipa a liberação.
- **US-11 — Validação da data:** aplicação com data futura é recusada; o registro com data atual ou passada segue as regras estabelecidas.
- **UI-02 — Tela de aplicações:** formulário web integrado ao backend para registrar aplicações de defensivos.

### 2.2 Colheitas e quarentena — EP04

- **US-12 — Registro de colheita:** cadastro de colheita informando talhão, data e quantidade.
- **US-13 — Bloqueio em quarentena:** o backend recusa colheitas anteriores à liberação; a operação é permitida na própria data de liberação.
- **US-14 — Indicação na interface:** botão **Colher** desabilitado quando o talhão está em quarentena, acompanhado da informação sobre a liberação. A história, adiada da Sprint 2, foi concluída na Sprint 3.
- **UI-03 — Interface de colheita:** disponibilização do fluxo de registro de colheita pela interface web, conforme demonstrado na Review.

### 2.3 PostgreSQL e migrações — EP05

- **US-15 — Ambiente de banco:** configuração do PostgreSQL para o projeto.
- **US-16 — Integração e estrutura:** persistência integrada ao backend, com evolução do esquema controlada por migrações Flyway.
- **Ambiente local:** uso de Docker Compose e configuração por variáveis de ambiente para facilitar a execução do banco.

### 2.4 Painel e acompanhamento dos talhões — EP06

- **US-17 a US-19 — Painel:** visualização do estado dos talhões com **cores e texto**, contadores e atalhos para operações do sistema, incluindo o acesso ao registro de aplicações.

### 2.5 Cancelamento justificado — EP07

- **US-20 — Cancelamento de aplicação:** operação disponível na API para o perfil Gestor, mediante motivo obrigatório. O registro original é preservado e recebe informações de auditoria: motivo, responsável e data/hora.
- **US-21 — Recálculo após cancelamento:** a data de liberação do talhão é recalculada considerando as aplicações não canceladas; as operações são executadas em contexto transacional.
- **US-22 — Cancelamento de colheita:** operação disponível na API, restrita ao Gestor, com motivo obrigatório e registro de responsável e data/hora, sem exclusão do histórico.

**Limite desta entrega:** os cancelamentos podem ser efetuados pela API, demonstrada via Swagger. A **tela de cancelamento e a visualização consolidada do histórico do talhão** foram reservadas para a Release 3.

### 2.6 Acesso e manutenção de cadastros — EP01 / EP02

- **UI-01 — Login integrado:** autenticação conectada ao backend e proteção das rotas conforme a sessão e o perfil do usuário.
- **UI-04 — Telas de talhões:** listagem, cadastro, edição e remoção conforme as permissões existentes.
- **UI-05 — Telas de defensivos e aplicações:** evolução dos formulários e cadastros, com tratamento das validações e recusas retornadas pelo backend.

As regras de autenticação, autorização e CRUD iniciadas na Release 1 passam a contar com interfaces utilizáveis e integradas ao backend.

## 3. Evolução técnica e qualidade

- **Integração contínua:** o GitHub Actions foi ampliado para os jobs de **backend** (Maven/JUnit/JaCoCo), **frontend** (lint e build) e **PostgreSQL/Flyway** (teste de migração em PostgreSQL 16). Os PRs da release passaram pelas verificações de CI.
- **Cobertura do backend:** medição JaCoCo de aproximadamente **86% de instruções** e **69% de desvios (*branches*)**. O pacote `colheita` apresentou **53% de cobertura de instruções**, área identificada para melhoria. Esses percentuais globais não representam a métrica específica de linhas dos serviços do Plano de Qualidade.
- **Testes automatizados:** a Review registra cerca de **111 métodos no backend** e **43 casos no frontend**. Os testes do frontend existem, mas **a execução deles não integra o CI desta release**; o job de frontend executa lint e build.
- **Consistência de datas:** padronização do relógio da aplicação para `America/Fortaleza`, evitando a dependência do fuso padrão do ambiente de execução.
- **Processo de desenvolvimento:** uso de branches, Pull Requests, revisão por outro integrante e verificações automatizadas antes da integração à `main`.

Resultados, metodologia e referências do pipeline estão documentados em [release2_pipeline.md](release2_pipeline.md). As execuções do CI podem ser consultadas no [GitHub Actions do AgroSafe](https://github.com/Antonio-Lins/AgroSafe-Gerencia/actions).

## 4. Demonstração e resultados da release

O incremento foi apresentado em ambiente local com o fluxo: login como Gestor → painel → registro de aplicação → visualização do talhão em quarentena e de sua liberação → botão de colheita desabilitado → registro de colheita em talhão livre → cancelamento justificado via Swagger → recálculo da liberação → manutenção de talhões e defensivos.

A Review registra **entrega integral do escopo único da Release 2 (91/91 pontos)**. As três histórias do fluxo de cancelamento foram concluídas no escopo acordado para a API, sem antecipar a interface de histórico prevista para a próxima release.

## 5. Limitações conhecidas e próximos passos

- **Cancelamento na interface:** a tela para cancelar aplicações e colheitas e a consulta visual ao histórico do talhão ficam para a **Release 3**. O cancelamento pela API já foi entregue.
- **Testes ponta a ponta:** cenários automatizados com Cucumber/Cypress, incluindo o cenário `@smoke`, não foram entregues nesta release e foram priorizados para a próxima.
- **Testes do frontend no CI:** apesar de haver testes no projeto, o workflow desta release executa lint e build, não a suíte automatizada do frontend.
- **Responsividade:** a validação manual documentada de todas as telas em **360 px** não foi confirmada nas atas de fechamento.
- **Cobertura:** o pacote `colheita` demanda ampliação de testes, segundo o relatório de qualidade da Review.
- **Drivers de qualidade:** **D2 (SCRUM-30), D4 (SCRUM-32) e D5 (SCRUM-33)** permanecem **Em desenvolvimento** no Jira devido a critérios adicionais de verificação. **São drivers de qualidade, não histórias de usuário pendentes**, e não alteram o resultado de 23 USs concluídas da release.
- **Padronização técnica:** a equipe identificou divergências na documentação da versão Java e decidiu uniformizá-las no início da Release 3.

## 6. Execução e documentação

O projeto é composto por frontend React, backend Spring Boot e PostgreSQL. Para executar o incremento em ambiente local, consulte as orientações e configurações atualizadas do repositório, inclusive os perfis de banco e as variáveis de ambiente necessárias.

- [Repositório AgroSafe-Gerencia](https://github.com/Antonio-Lins/AgroSafe-Gerencia)
- [GitHub Actions — execuções do CI](https://github.com/Antonio-Lins/AgroSafe-Gerencia/actions)
- [Workflow de CI na `main`](https://github.com/Antonio-Lins/AgroSafe-Gerencia/blob/main/.github/workflows/ci.yml)
- [Relatório de CI da Release 2](release2_pipeline.md)

## 7. Encerramento

A Release 2 entrega o fluxo funcional principal do AgroSafe: o registro das aplicações, o cálculo e a comunicação da liberação, o bloqueio de colheitas em quarentena e o cancelamento justificado pela API. A integração do banco de dados e a evolução das interfaces permitem demonstrar o produto além das operações isoladas do backend.

A Release 3 dará continuidade à experiência de uso e à verificação de qualidade, com foco na interface de cancelamento e histórico, nos testes ponta a ponta e na ampliação da automação de testes.
