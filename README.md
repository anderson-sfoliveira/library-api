<h1 align="center">
  Library API
</h1>

[![Heroku Deployment](https://github.com/anderson-sfoliveira/library-api/actions/workflows/heroku-deployment.yml/badge.svg)](https://github.com/anderson-sfoliveira/library-api/actions/workflows/heroku-deployment.yml)
[![codecov](https://codecov.io/gh/anderson-sfoliveira/library-api/branch/main/graph/badge.svg?token=FEC5XBGPHJ)](https://codecov.io/gh/anderson-sfoliveira/library-api)
[![Build Status](https://app.travis-ci.com/anderson-sfoliveira/library-api.svg?branch=main)](https://app.travis-ci.com/anderson-sfoliveira/library-api)

Tabela de conteúdos
=================
<!--ts-->
* [Sobre o projeto](#-sobre-o-projeto)
* [Funcionalidades](#-funcionalidades)
* [Links](#links)
* [Como executar o projeto](#-como-executar-o-projeto)
    * [Pré-requisitos](#pré-requisitos)
    * [Rodando a api](#-rodando-a-api)
* [Tecnologias](#-tecnologias)
* [Autor](#-autor)
* [Licença](#user-content--licença)
<!--te-->


## 💻 Sobre o projeto

Desenvolvido durante o curso **Design de API's RestFul com Spring Boot, TDD e o novo JUnit5** oferecido pela [Udemy](https://www.udemy.com/course/design-de-apis-restful-com-tdd-spring-boot-e-junit-5/).

Esta API realiza o controle de uma biblioteca.

O projeto foi desenvolvido utilizando as técnicas TDD e BDD onde os testes são escritos antes mesmo dos códigos.
Foram criados testes unitários e de integração com as bibliotecas Junit5, Mockito e Assertj, que fazem parte do Spring Boot Starter Test.

Usamos o H2 como nosso banco de dados em memória, o lombok para reduzir código boilerplate e modelmapper para conversão de DTOs.

Criamos um serviço de agendamento de tarefas para enviar e-mail com Spring Boot Starter Mail para o clientes com livro emprestado a mais de 3 dias.
Utilizamos o Mailtrap para simular e-mails.

O Springdoc - OpenAPI 3 foi utilizado para gerar automaticamente a documentação da API junto com algumas anotações Swagger.

Utilizamos o Spring Boot Starter Actuator para monitorar as informações e status da aplicação.

A biblioteca Spring Boot Admin foi utilizada para fazer a integração com a aplicação [admin-apps](https://github.com/anderson-sfoliveira/admin-apps) que criamos para fornecer uma interface administrativa para monitorar aplicativos Spring Boot.
Neste outro aplicativo são demonstradas as informações coletadas pelo Spring Boot Starter Actuator.

O Jacoco foi a ferramenta escolhida para realizar a cobertura de código, usada para medir quantas linhas do nosso código são testadas.

Para a integração contínua foi ensinado no curso como utilizar o Travis CI para testar o build, as classes de testes e compartilhar com o Codecov os resultados da cobertura de código.
Porém, o Travis CI não está conseguindo compartilhar os resultados da cobertura de código com o Codecov devido ao com Java 11.

Por conta do problema acima, utilizei o Git Actions para realizar a integração e entrega contínua (CI/CD).
Criei um workflow "Heroku Deployment" onde é testado o build da aplicação, são executadas todas as classes de testes, o resultado da cobertura de código é compartilhado com o Codecov e é realizado o deploy da aplicação no Heroku.

No Codecov podemos visualizar a análise da cobertura de código e podemos usar o Insomnia para testar os nossos endpoints.


## ⚙️ Funcionalidades

- [x] API de livro:
  - [x] Cadastrar um novo livro enviado o título, autor e isbn do livro;
  - [x] Atualizar o titulo e autor do livro através do id;
  - [x] Deletar um livro através do id;
  - [x] Obter detalhes de um livro através do id;
  - [x] Obter uma lista de livros através de parâmetros;
  - [x] Obter uma lista de empréstimos através do id do livro.

- [x] API de empréstimos:
  - [x] Cadastrar o empréstimo de um livro para um cliente;
  - [x] Registrar a devolução do empréstimo;
  - [x] Obter uma lista de empréstimos através de parâmetros (customer e isbn livro).


## Links

-   **[URL da API](https://library-api-mycloud.herokuapp.com/)**
-   **[Documentação API](https://library-api-mycloud.herokuapp.com/swagger-ui/index.html)**
-   **[Travis CI](https://app.travis-ci.com/github/anderson-sfoliveira/library-api/branches)**
-   **[Codecov](https://app.codecov.io/gh/anderson-sfoliveira/library-api)**
-   **[GitHub da aplicação admin-apps](https://github.com/anderson-sfoliveira/admin-apps)**


## 🚀 Como executar o projeto

### Pré-requisitos

Antes de começar, você vai precisar ter instalado em sua máquina o Java 11 e o [Maven](https://maven.apache.org/).

Além disto é bom ter um editor de código, como o: [Intellij](https://www.jetbrains.com/pt-br/idea/). E também o
[Insomnia REST](https://insomnia.rest/) para testar os endpoints.

#### 🎲 Rodando a API

```bash

# Clone este repositório
$ git clone https://github.com/anderson-sfoliveira/library-api.git

# Importe o projeto para dentro do Intellij.

# Inicie a aplicação.

# O servidor inciará na porta:8080

# http://localhost:9090/actuator

# http://localhost:8080/swagger-ui/index.html

```


## 🛠 Tecnologias


#### **Bibliotecas utilizadas na API**

-   **Java 11**
-   **Spring Boot 2.7.3**
-   **Spring Boot Starter Web**
-   **Spring Boot Starter Data JPA**
-   **Spring Boot Starter Mail**
-   **Spring Boot Starter Validation**
-   **Spring Boot Starter Test**
-   **Spring Boot Starter Actuator**
-   **Spring Boot Devtools**
-   **[H2 Database Engine](https://www.h2database.com/html/main.html)**
-   **[Lombok](https://projectlombok.org/)**
-   **[ModelMapper (version 3.0.0)](http://modelmapper.org/)**
-   **[Springdoc - OpenAPI 3 (version 1.6.11)](https://springdoc.org/)**
-   **[Spring Boot Admin (version 2.7.5)](https://github.com/codecentric/spring-boot-admin)**
-   **[JaCoCo - Java Code Coverage Library](https://www.jacoco.org/jacoco/trunk/index.html)**

> Veja o arquivo [pom.xml](https://github.com/anderson-sfoliveira/library-api/blob/main/pom.xml)

#### **Serviços**

-   **[GitHub](https://github.com/)**
-   **[Travis CI](https://www.travis-ci.com/)** - Serviço de integração contínua distribuído e disponível na nuvem, utilizado para criar e testar projetos de software hospedados no GitHub.
-   **[Codecov](https://about.codecov.io/)** - Fornece ferramentas altamente integradas para desenvolvedores obter visibilidade acionável em sua cobertura de código.
Desde que seu código tenha testes e sua ferramenta de cobertura (usamos o Jacoco) possa gerar resultados de cobertura em um formato compatível.
Para utilizar o Codecov adicione comandos no seu arquivo de configuração de CI (no nosso caso o arquivo .travis.yml) para instalar Codecov como ferramenta de relatório para o seu CI, que enviará o arquivo do relatório para Codecov.
Quando criar um PR (pull request), o seu CI irá enviar o relatório de cobertura de código para Codecov.
-   **[Mailtrap](https://mailtrap.io/)** - Ferramenta gratuita para testar envio de e-mails.
Essa ferramenta é extremamente útil em ambiente local de desenvolvimento, porque o envio de e-mail fica centralizado e através da ferramenta é muito mais fácil analisar o conteúdo do e-mail.
-   **[CronMaker](http://www.cronmaker.com/)**


## 🦸🏾 Autor

<a href="https://www.linkedin.com/in/anderson-sfoliveira/">
 <img style="border-radius: 50%;" src="https://avatars.githubusercontent.com/u/2175235?s=400&u=432d3456eb62f2df111abdccd667976321f6f74a&v=4" width="100px;" alt=""/>
 <br />
 <sub><b>Anderson Oliveira</b></sub></a> <a href="https://www.linkedin.com/in/anderson-sfoliveira/" title="Anderson Oliveira"></a>
 <br />

[![Linkedin Badge](https://img.shields.io/badge/-Anderson-blue?style=flat-square&logo=Linkedin&logoColor=white&link=https://www.linkedin.com/in/anderson-sfoliveira/)](https://www.linkedin.com/in/anderson-sfoliveira/)
[![Gmail Badge](https://img.shields.io/badge/-anderson.sfoliveira@gmail.com-c14438?style=flat-square&logo=Gmail&logoColor=white&link=mailto:anderson.sfoliveira@gmail.com)](mailto:anderson.sfoliveira@gmail.com)

Feito com ❤️ por Anderson Oliveira 👋🏽 Entre em contato!


## 📝 Licença

Este projeto esta sobe a licença [MIT](./LICENSE).

Feito com ❤️ por Anderson Oliveira 👋🏽 Entre em contato!
