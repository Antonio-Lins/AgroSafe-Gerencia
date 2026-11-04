# Registo e Matriz de Riscos - AgroSafe

Este documento consolida os riscos identificados no projeto, a sua probabilidade, impacto e as respetivas ações de mitigação, sendo revisto obrigatoriamente ao fim de cada sprint[cite: 2].

| ID | Descrição do Risco | Categoria | Probabilidade | Impacto | Ação de Mitigação | Status Atual |
| :--- | :--- | :--- | :--- | :--- | :--- | :--- |
| **R01** | Instabilidade ou atraso na configuração do ambiente de testes[cite: 2] | Técnico | Média | Alto | Integrar ferramentas de CI desde o início | Em monitorização |
| **R02** | Dificuldade na migração do banco de dados para PostgreSQL[cite: 1] | Técnico | Alta | Alto | Realizar testes prévios em ambiente isolado | Crítico / Em aberto |
| **R03** | Falta de clareza nos critérios de aceitação das User Stories | Processo | Média | Médio | Refinamento ativo com o Product Owner antes da Planning | Controlado |
| **R04** | Atraso na entrega das tarefas por sobrecarga de um membro | Gestão | Média | Alto | Redistribuição de tarefas e acompanhamento de velocity | Controlado |
| **R05** | Divergência de versões de ferramentas (ex: Java no CI vs README)[cite: 3] | Técnico | Baixa | Alto | Padronizar versões no README e arquivos de configuração | Mitigado |
| **R06** | Ausência de registo de evidências de testes[cite: 3] | Qualidade | Alta | Médio | Utilizar obrigatoriamente o modelo de evidência de teste | Em aberto |
| **R07** | Falha na comunicação síncrona de impedimentos[cite: 2] | Processo | Média | Alto | Obrigatoriedade de registar bloqueios no Jira imediatamente[cite: 2] | Controlado |
| **R08** | Incompatibilidade ou problemas com os testes automatizados (Cucumber/Cypress)[cite: 3] | Técnico | Média | Alto | Alinhamento técnico prévio com a frente de DevOps/QA | Em aberto |
| **R09** | Escopo desalinhado com as expectativas da disciplina | Negócio | Baixa | Alto | Validação contínua do backlog com base no feedback da R1 | Controlado |