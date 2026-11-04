# Release 1: Review

**Projeto:** AgroSafe · **Release:** 1 · **Resultado demonstrável:** Cadastros funcionando
**Período:** 06/10/2026 a 29/10/2026 · **Data da review:** 29/10/2026
**Participantes:** João Gabriel(SM), Antonio Lins (QA), Joaquim Laureano (PO + DevOps)

## 1. Objetivo da release
Entregar a fundação do sistema e os cadastros base, para que as releases seguintes (aplicação, colheita com bloqueio, painel) tenham onde se apoiar.

## 2. Escopo comprometido × entregue
| Épico | USs | Pts sugeridos | Pts entregues [PREENCHER] | Situação |
|-------|-----|---------------|---------------------------|----------|
| EP01 Fundação e acesso | US-01 a US-04 | 26 | 26                        | |
| EP02 CRUD de talhões e defensivos | US-05 a US-08 | 23 | 23                        | |

Critério de pronto do épico: [PREENCHER conforme a definição do grupo]

## 3. Demonstração da release
Roteiro usado:  login Gestor → login Operador → rota proibida → CRUD talhão → CRUD defensivo
Ambiente:  local 

## 4. Requisitos atendidos
| Requisito | Descrição | Atendido? |
|-----------|-----------|-----------|
| RF01 | CRUD de talhão e defensivo (bloqueio de exclusão com aplicações) | Sim       |
| RNF03 | Autenticação, senha com BCrypt, perfis | Sim       |
| RNF02 | Layout responsivo (base, 360 px) | Não       | 

## 5. O que ficou de fora e por quê
Não foi feita a parte do banco de dados, optamos por implementar na próxima release

## 7. Decisão sobre a próxima release
Release 2: EP03, EP04 e EP05 (US-09 a US-19 e US-21), conforme o backlog. 

**Responsável:** João Gabriel