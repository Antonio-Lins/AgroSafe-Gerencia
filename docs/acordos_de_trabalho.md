# Acordos de Trabalho (Team Agreements) - AgroSafe

Estes acordos de convivência e engenharia foram estabelecidos pela equipa para garantir a qualidade do processo, o respeito mútuo e a conformidade com as diretrizes de entrega do projeto.

## 1. Comunicação e Transparência
* **Canais Oficiais:** Toda a comunicação síncrona e assíncrona deve ocorrer nos canais oficiais do grupo.
* **Impedimentos:** Se qualquer elemento da equipa ficar bloqueado numa tarefa por mais de algumas horas, deve registar o impedimento imediatamente no Jira (como comentário ou *issue*) e avisar no grupo para atuação rápida do Scrum Master.

## 2. Padrões de Desenvolvimento e Git
* **Proibido Commits na Main:** É estritamente proibido fazer commits diretamente na branch `main`. Todo o código deve passar por uma branch de feature.
* **Pull Requests (PRs):** Nenhuma alteração pode ser integrada sem a abertura de um PR e a aprovação prévia de, pelo menos, um colega da equipa.
* **Versões e Configurações:** As versões de tecnologias (como Java e dependências) e as variáveis de ambiente devem ser estritamente respeitadas conforme documentado no README para evitar quebras no pipeline de CI.

## 3. Compromisso com as Cerimónias
* **Pontualidade:** Todos os membros devem esforçar-se para comparecer às reuniões (Planning, Review e Retrospectiva) no horário estipulado, respeitando rigorosamente o *timebox* definido.
* **Qualidade e Testes:** Os critérios de aceitação e os testes automatizados (Cucumber, Cypress e cenários `@smoke`) devem acompanhar as entregas e as evidências devem ser devidamente registadas.

### Gestão de Impedimentos
* **Regra:** Se qualquer membro da equipa ficar bloqueado numa tarefa por mais de 2 horas, deve obrigatoriamente abrir um comentário na *issue* do Jira detalhando o bloqueio e avisar o Scrum Master imediatamente[cite: 2].
* **Visibilidade:** O cartão afetado deve ser sinalizado visualmente no quadro do Jira para garantir transparência total no burndown[cite: 2].