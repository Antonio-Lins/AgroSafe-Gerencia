#  AgroSafe

**Sistema de Gestão e Controle de Carência Agrícola**

O AgroSafe é um sistema desenvolvido para auxiliar no gerenciamento de propriedades agrícolas, com foco no controle da aplicação de defensivos e no acompanhamento dos períodos de carência necessários para a realização segura das colheitas.

O sistema permite cadastrar talhões e defensivos agrícolas, registrar aplicações, calcular datas de liberação e controlar operações de colheita, respeitando as regras de negócio estabelecidas.

>  **Status:** Em desenvolvimento.

##  Objetivo

Facilitar o controle dos períodos de carência de defensivos agrícolas, garantindo maior rastreabilidade das aplicações e evitando o registro de colheitas antes das datas permitidas.

##  Contexto Acadêmico

O **AgroSafe** é um projeto acadêmico desenvolvido como parte das atividades da disciplina de **Gerência de Projetos**, do curso de Ciência da Computação.

Além do desenvolvimento de uma solução tecnológica para o setor agrícola, o projeto tem como objetivo aplicar, na prática, conceitos de planejamento, organização, acompanhamento e gestão de projetos de software.

###  Gerenciamento do Projeto

Para auxiliar na organização e no acompanhamento das atividades, a equipe utiliza o **Jira** como ferramenta de gerenciamento do projeto.

A ferramenta é utilizada para:

- Organizar e acompanhar as tarefas de desenvolvimento.
- Gerenciar histórias de usuário (*User Stories*).
- Distribuir responsabilidades entre os integrantes.
- Acompanhar o progresso das funcionalidades.
- Registrar e monitorar atividades pendentes, em andamento e concluídas.

O desenvolvimento é realizado de maneira colaborativa, utilizando **Git e GitHub** para versionamento do código, gerenciamento de branches e integração das funcionalidades por meio de *Pull Requests*.

Dessa forma, o AgroSafe busca integrar conhecimentos técnicos de desenvolvimento de software com práticas de gerenciamento de projetos, proporcionando uma experiência próxima à realidade do desenvolvimento profissional.

## ⚙ Tecnologias utilizadas

- **Java 21** — Linguagem de programação.
- **Spring Boot 4.1.2** — Framework principal do backend.
- **Spring Web MVC** — Desenvolvimento da API REST.
- **Spring Data JPA** — Persistência de dados.
- **Spring Security** — Autenticação e autorização.
- **PostgreSQL / H2** — Bancos de dados utilizados no projeto.
- **Maven** — Gerenciamento de dependências.
- **Swagger UI / OpenAPI** — Documentação e testes interativos da API.
- **JUnit e ferramentas de teste do Spring** — Testes automatizados.

##  Funcionalidades

O projeto possui as seguintes funcionalidades:

- Cadastro e gerenciamento de talhões agrícolas.
- Cadastro e gerenciamento de defensivos.
- Registro de aplicações de defensivos.
- Cálculo automático do período de carência.
- Controle das datas de liberação dos talhões.
- Registro de colheitas com validação das regras de carência.
- Cancelamento justificado de registros.
- Autenticação de usuários.
- Controle de acesso por perfil (Gestor e Operador).
- Documentação interativa da API com Swagger.

*As funcionalidades estão em diferentes etapas de implementação e validação.*

##  Estrutura do projeto

O backend segue uma arquitetura organizada em camadas:

- `controller` — Recebimento e tratamento das requisições HTTP.
- `service` — Implementação das regras de negócio.
- `repository` — Acesso e persistência dos dados.
- `model` — Representação das entidades.
- `security` — Configurações de autenticação e autorização.

##  Como executar o projeto

### Pré-requisitos

- Java 21
- Maven ou Maven Wrapper
- IntelliJ IDEA ou outra IDE compatível
- Banco de dados configurado conforme o ambiente

### Executando o backend

1. Clone o repositório do projeto.
2. Abra a pasta `backend` na IDE.
3. Aguarde o Maven carregar as dependências.
4. Configure o banco de dados conforme as propriedades do projeto.
5. Execute a classe principal do Spring Boot.

Por padrão, a API estará disponível em:

`http://localhost:8080`

##  Documentação da API — Swagger

O AgroSafe utiliza Swagger UI para documentação e testes dos endpoints REST.

Com o backend em execução, acesse:

**Swagger UI:**

`http://localhost:8080/swagger-ui.html`

**Documentação OpenAPI (JSON):**

`http://localhost:8080/v3/api-docs`

A interface permite visualizar os endpoints, enviar requisições HTTP e consultar os códigos de resposta da API.

Algumas operações exigem autenticação e permissões específicas.

##  Segurança

O sistema utiliza Spring Security com autenticação por sessão e controle de acesso baseado em perfis.

- **Gestor:** responsável pelas operações administrativas e funcionalidades restritas.
- **Operador:** responsável pelas operações permitidas ao perfil operacional.

As senhas são protegidas utilizando BCrypt.

##  Testes

O projeto utiliza ferramentas de testes do ecossistema Spring Boot, incluindo JUnit.

Os testes automatizados buscam verificar as regras de negócio, a autenticação e as permissões de acesso.

O Swagger UI também pode ser utilizado para testes manuais dos endpoints disponíveis.

##  Status do desenvolvimento

O AgroSafe está em desenvolvimento acadêmico, com implementação incremental das funcionalidades e integração contínua

## Equipe de Desenvolvimento

 **[Antonio Lins](https://github.com/Antonio-Lins)** \
 **[João Gabriel Caetano](https://github.com/21jann12)**\
 **[Joaquim Laureano ](https://github.com/JoaquimLG)** 
