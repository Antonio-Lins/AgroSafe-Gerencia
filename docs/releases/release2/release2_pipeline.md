
# Release 2 — Relatório do Pipeline de Integração Contínua (CI)

**Projeto:** AgroSafe  
**Release:** 2 — Sprints 2 e 3  
**Data do relatório:** 26/11/2026  
**Responsável:** João Gabriel — PO e DevOps da Sprint 3  
**Ferramenta:** GitHub Actions  
**Fonte de evidências:** [GitHub Actions — AgroSafe-Gerencia](https://github.com/Antonio-Lins/AgroSafe-Gerencia/actions)

## 1. Objetivo

Documentar a configuração, a evolução e os resultados do processo de integração contínua do AgroSafe na Release 2. O pipeline executa verificações automatizadas para reduzir a entrada de alterações com falhas na branch `main`, apoiar a revisão de Pull Requests e disponibilizar evidências técnicas de qualidade.

## 2. Configuração do pipeline

O workflow está versionado em [`.github/workflows/ci.yml`](https://github.com/Antonio-Lins/AgroSafe-Gerencia/blob/main/.github/workflows/ci.yml) e é acionado por eventos de Pull Request e por pushes na branch `main`.

| Job | Ambiente e ferramentas | Verificações executadas |
|---|---|---|
| **backend** | Java Temurin 25, Maven, JUnit 5 e JaCoCo | Compilação, testes automatizados e verificação do backend com `./mvnw -B clean verify`; geração do relatório de cobertura JaCoCo. |
| **frontend** | Node.js 22, npm, ESLint e Vite | Instalação de dependências com `npm ci`, análise estática com `npm run lint` e compilação com `npm run build`. |
| **postgresql** | PostgreSQL 16 e Flyway | Inicialização de banco de testes e execução de `FlywayMigrationIT` com o perfil `migrationtest`, utilizando `./mvnw -B -Dtest=FlywayMigrationIT test`. |

As credenciais utilizadas pelo banco de teste são próprias do ambiente de CI, sem necessidade de publicar senhas de produção no repositório.

## 3. Evolução do CI nas Sprints 2 e 3

### Sprint 2 — CI do backend

Na Sprint 2, o processo de integração contínua era concentrado no backend Java. Em paralelo, foram desenvolvidas a configuração do PostgreSQL e a integração do Flyway ao projeto. A retrospectiva identificou a necessidade de expandir o pipeline para validar também o frontend e as migrações do banco.

### Sprint 3 — Ampliação e integração

Na Sprint 3, o workflow foi ampliado para contemplar os três jobs: **backend, frontend e postgresql**. Também foram incorporadas a execução de lint e build do frontend e a produção de relatórios JaCoCo.

A alteração foi integrada à `main` pelo [PR #31 — CI, qualidade e integração](https://github.com/Antonio-Lins/AgroSafe-Gerencia/pull/31), associado à execução **CI #60**, em 26/11/2026. A correção de padronização do fuso horário nas regras de carência foi integrada pelo [PR #33](https://github.com/Antonio-Lins/AgroSafe-Gerencia/pull/33), associada à execução **CI #64**.

A equipe confirmou que **todos os Pull Requests da Release 2 passaram nas verificações de CI**, não tendo sido integrado um PR com checks reprovados.

## 4. Resultados das execuções

A apuração das execuções identificadas entre **CI #24 e CI #68** contempla 45 workflows da Release 2. A página geral do Actions indicava 66 execuções históricas no repositório; por isso, os números desta seção têm como universo as **45 execuções identificadas no recorte da release**, e não representam uma auditoria das 66 execuções históricas.

| Indicador | Sprint 2 — CI #24 a #42 | Sprint 3 — CI #43 a #68 | Total do recorte |
|---|---:|---:|---:|
| Execuções contabilizadas | 19 | 26 | **45** |
| Execuções aprovadas | 19 | 26 | **45** |
| Execuções reprovadas no recorte | 0 | 0 | **0** |
| Taxa de aprovação no recorte | 100% | 100% | **100%** |
| Duração média | 35,3 s | 42,6 s | **39,5 s** |
| Maior duração | 53 s | 1 min 25 s | **1 min 25 s** |

As execuções da Sprint 2 identificadas concentram-se entre 10 e 12/11/2026; as da Sprint 3, entre 17 e 26/11/2026. O aumento da duração média da Sprint 3 é compatível com a ampliação das verificações, mas os dados disponíveis não permitem atribuir quantitativamente esse aumento a um job específico.

### 4.1 Indicador Q-06 — CI na branch `main`

No recorte apurado, **22 execuções ocorreram na `main`**, sendo nove da Sprint 2 e treze da Sprint 3. Todas as 22 foram aprovadas:

**Taxa de sucesso observada na `main` = 22 / 22 × 100 = 100%.**

O Plano de Qualidade estabelece meta de **90% ou mais** de execuções aprovadas na `main` (mínimo aceitável de 80%). **O recorte apurado atende à meta Q-06.** A afirmação se restringe às 22 execuções identificadas na `main` durante o levantamento.

### 4.2 Resultado do pipeline ampliado

A ampliação foi incorporada à `main` na execução **CI #60**. As execuções **CI #60 a CI #68** apresentaram resultado geral aprovado. Dessa forma, há evidência de execução bem-sucedida do workflow após a inclusão dos novos jobs.

A aprovação geral do workflow é utilizada como evidência do resultado da execução; os detalhes individuais dos jobs podem ser consultados diretamente na página do GitHub Actions.

## 5. Cobertura automatizada — JaCoCo

O JaCoCo foi incorporado ao processo Maven para apoiar a análise de cobertura dos testes automatizados. Em medição local realizada durante a preparação da release, foram obtidos os seguintes resultados para o backend como um todo:

| Indicador | Resultado |
|---|---:|
| Cobertura de **instruções** do backend | **86%** — 1.596 de 1.853 instruções |
| Cobertura de **branches** do backend | **69%** — 36 de 52 branches |

A métrica **Q-01** do Plano de Qualidade corresponde especificamente à **cobertura de linhas da camada de serviços**, cuja meta é pelo menos 70% (mínimo aceitável de 60%). **Essa métrica específica não foi apurada neste relatório**; portanto, os 86% de instruções globais não são apresentados como comprovação da Q-01.

A cobertura deve continuar sendo acompanhada, especialmente em cenários de cancelamento e preservação de histórico, nos quais ainda existem verificações previstas no Plano de Riscos.

## 6. Ajustes técnicos e ações de melhoria

| Tema | Ação executada e resultado |
|---|---|
| **Frontend no CI** | Inclusão de verificação ESLint e build Vite antes da integração de alterações. |
| **Compatibilidade do ESLint** | Adequação da versão e da configuração do ESLint aos plugins utilizados pelo frontend. |
| **Migrações do banco** | Inclusão de teste de integração Flyway com PostgreSQL 16 no pipeline. |
| **Perfil de migração** | Ajuste da configuração de teste para usar o dialeto PostgreSQL, coerente com o banco executado no ambiente de integração. |
| **Cobertura JaCoCo** | Geração de relatório de cobertura pelo Maven, permitindo acompanhamento de áreas menos testadas. |
| **Fuso horário** | Integração da padronização `America/Fortaleza` por meio do PR #33, contribuindo para o tratamento do risco de liberação antecipada. |

As verificações de risco ainda não automatizadas, como teste específico de rollback com falha forçada e recusa de operações históricas indevidas, permanecem registradas no acompanhamento do Plano de Riscos. A aprovação do CI não significa que esses cenários tenham sido executados.

## 7. Evidências e rastreabilidade

| Evidência | Referência |
|---|---|
| **Execuções e resultados do GitHub Actions** | https://github.com/Antonio-Lins/AgroSafe-Gerencia/actions |
| **Workflow versionado na `main`** | https://github.com/Antonio-Lins/AgroSafe-Gerencia/blob/main/.github/workflows/ci.yml |
| **PR #31 — ampliação do pipeline** | https://github.com/Antonio-Lins/AgroSafe-Gerencia/pull/31 |
| **PR #33 — padronização do fuso horário** | https://github.com/Antonio-Lins/AgroSafe-Gerencia/pull/33 |

Os resultados podem ser auditados na página do GitHub Actions, consultando os workflows e seus respectivos jobs. Caso o repositório seja privado, a avaliação depende de acesso autorizado ao GitHub.

## 8. Conclusão

A Release 2 consolidou o uso do GitHub Actions como mecanismo de verificação das alterações do AgroSafe. O processo, inicialmente concentrado no backend, foi expandido na Sprint 3 para incluir validações do frontend e testes de migração com PostgreSQL/Flyway, além de relatórios de cobertura JaCoCo.

No recorte de **45 execuções identificadas**, todas foram aprovadas, com **taxa de sucesso de 100%**, tempo médio de **39,5 segundos** e maior duração de **1 minuto e 25 segundos**. Na branch `main`, as **22 execuções contabilizadas** também foram aprovadas, atendendo à meta Q-06 no período apurado. A equipe confirmou ainda a aprovação das verificações de CI dos Pull Requests da release.

O resultado demonstra a adoção consistente de integração contínua e uma evolução relevante da infraestrutura de qualidade do projeto. Permanecem como oportunidades de melhoria o acompanhamento da cobertura de linhas dos serviços (Q-01) e a ampliação dos testes automatizados para cenários específicos de riscos identificados pela equipe.
