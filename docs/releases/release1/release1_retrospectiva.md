# Release 1: Retrospectiva

**Projeto:** AgroSafe · **Release:** 1 · **Data:** 29/10/2026 · **Facilitador:**
**Participantes:** João Gabriel, Joaquim Laureano, Antonio Lins


## 1. Linha do tempo
| Data        | Evento |
|-------------|--------|
| 08/10/2026  | Início do projeto/ambiente |
| 20/10/2026  | Planning da Sprint 1 |
| 29/10/2026  | Review | |

## 2. O que funcionou
| # | Ponto                              |
|---|------------------------------------|
| 1 | Divisão de trabalho por US         | 
| 2 | organização das USs no jira        |
| 3 | reuniões para definição do backlog |
## 3. O que não funcionou
| # | Ponto                                     | Causa raiz                    |
|---|-------------------------------------------|-------------------------------|
| 1 | erros de versão no projeto                | falta de comunicação          |
| 2 | Demora na configuração do backlog no jira | O time nunca havia usado Jira |

## 4. Aprendizados técnicos
- Spring Boot 
- Spring Security / BCrypt / perfis
- CI e testes (JUnit 5, Mockito, @SpringBootTest)

## 5. Aprendizados de processo
- Definition of Ready / Done
- Uso do Jira (épicos, USs, releases por versão)

## 6. Riscos de qualidade: como ficaram
| Atributo | Risco do plano | Mitigação prevista | Situação na release                      |
|----------|----------------|--------------------|------------------------------------------|
| Confiabilidade | Validações do CRUD (carência inteira > 0, código único) | Testes unitários e de integração | Cobre o risco                            |
| Auditabilidade | Endpoints PUT/DELETE indevidos em registros | Não expor esses endpoints; decisão de projeto | Cobre o risco                            |
| Usabilidade | Telas inadequadas ao celular | Mobile-first, teste em 360 px | Não foi feito, focamos apenas no backend |


## 7. Decisões tomadas
- Foco no backend
- Não fazer frontend nessa release
- implementar banco de dados na próxima sprint
