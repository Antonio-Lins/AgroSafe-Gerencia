# Ata da Sprint Review: Sprint 1

**Projeto:** AgroSafe · **Release:** 1 · **Sprint:** 1
**Data da review:** 29/10/2026 
**Participantes:** João Gabriel(SM), Joaquim Laureano (PO), Antonio Lins(QA)


## 1. Meta da Sprint
Entregar a fundação e os cadastros: login com perfis (Gestor e Operador), proteção de rotas por perfil e CRUD de talhões e defensivos.


## 2. Escopo planejado × entregue

| US | Título | Épico | Pts | Status    |
|----|--------|-------|-----|-----------|
| US-01 | Configurar projeto, banco e pipeline (técnica) | EP01 | 13  | Parcial   |
| US-02 | Fazer login | EP01 | 5   | Concluído |
| US-03 | Restringir acesso por perfil | EP01 | 5   | concluído |
| US-04 | Carregar perfis Gestor e Operador na carga inicial | EP01 | 3   | concluído |
| US-05 | Cadastrar talhão e defensivo | EP02 | 8   | concluído |
| US-06 | Editar talhão e defensivo | EP02 | 5   | concluído |
| US-07 | Remover talhão e defensivo (bloqueado se houver aplicações) | EP02 | 5   | concluído |
| US-08 | Listar talhões e defensivos | EP02 | 5   | concluído |


## 3. Demonstração
O que foi mostrado:

- Login com Gestor e com Operador
- Rota protegida negando acesso ao Operador
- Cadastro, edição, listagem e remoção de talhão
- Cadastro, edição, listagem e remoção de defensivo

## 4. Conferência do Definition of Done
Para cada US marcada como concluída, confirmar:

- [ ] Critérios de aceite (Gherkin) atendidos, com cenários de sucesso e de exceção
- [ ] Testes automatizados escritos e passando
- [ ] Revisão por Pull Request aprovada
- [ ] Integrado sem quebrar o build 
- [ ] Demonstrada na review
- [ ] Cenários de exceção cobertos por teste
- [ ] Endpoints documentados


## 5. Itens não concluídos e destino
| US/Item                        | Motivo                               | Destino  |
|--------------------------------|--------------------------------------|----------|
| estruturação do banco de dados | decisão do time                      | Sprint 2 |
| frontend                       | decisão do time ( priorizar backend) | Sprint 2 |


## 6. Próximos passos
Sprint 2 (planejada): EP03 (US-09 a US-11) com 16 pts sugeridos, mais EP04 (US-12 a US-14) com 16 pts sugeridos, total sugerido de 32 pts.

