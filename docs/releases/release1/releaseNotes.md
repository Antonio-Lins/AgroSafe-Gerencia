# AgroSafe: Release Notes, Release 1

### **Resultado:** Cadastros funcionando

## Novidades
### Acesso (EP01)
- Login com e-mail e senha (senhas armazenadas com BCrypt). [US-02]
- Dois perfis: **Gestor** e **Operador de campo**
- Rotas protegidas por perfil. [US-03]

### Cadastros (EP02)
- Cadastro, edição, listagem e remoção de **talhões**. [US-05 a US-08]
- Cadastro, edição, listagem e remoção de **defensivos**, com carência inteira maior que zero. [US-05 a US-08]
- Remoção de talhão e defensivo bloqueada quando houver aplicações associadas. [US-07]

### Base técnica
- Projeto Spring Boot + React, PostgreSQL, pipeline de CI com build e testes a cada PR. [US-01]


## Melhorias e correções
[PREENCHER: bugs corrigidos durante a sprint, com número do ticket]

## Limitações conhecidas
- Ainda não há registro de aplicação, colheita, painel, histórico ou cancelamento (Releases 2 e 3).
- A regra de bloqueio por aplicações só é exercitada de ponta a ponta a partir da Sprint 2.
- [PREENCHER outras]

## Decisões pendentes para a próxima release
- Aplicação retroativa lançada depois de uma colheita (US-11).
- Regra de colheita: data de hoje ou data informada (US-13).
- Editar a carência de um defensivo que já tem aplicações (US-09 e US-10).

## Como executar
- JDK: 21 ou superior
- Ferramenta de build: Maven
- execução pelo terminal: mvnw spring-boot:run (windows) ou ./mvnw spring-boot:run (Linux/ Mac OS)
