# Plano de Qualidade - AgroSafe

## 1. Objetivo
Garantir a conformidade do software desenvolvido com os requisitos especificados, assegurando estabilidade, legibilidade de código, ausência de defeitos críticos e rastreabilidade total das entregas.

## 2. Critérios de Qualidade e Práticas Adotadas
* **Automação de Testes:** Utilização de testes automatizados integrados ao pipeline de CI, contemplando testes unitários, cenários em arquivos `.feature` (Cucumber), testes *End-to-End* (Cypress) e um cenário de fumo (`@smoke`)[cite: 3].
* **Gestão de Defeitos:** Todos os defeitos encontrados devem ser obrigatoriamente registados através do modelo de bug, vinculados à respetiva User Story e ao Pull Request correspondente[cite: 3].
* **Revisão de Código (Code Review):** Nenhum código pode ser integrado à branch principal (`main`) sem passar por um Pull Request e possuir a aprovação formal de outro membro da equipa[cite: 3].
* **Evidências de Execução:** A execução de testes deve gerar registos formais utilizando o modelo de *Evidência de Teste*[cite: 3].