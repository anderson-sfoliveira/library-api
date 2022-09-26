<h1 align="center">
  Library API
</h1>

[![Build Status](https://app.travis-ci.com/anderson-sfoliveira/library-api.svg?branch=main)](https://app.travis-ci.com/anderson-sfoliveira/library-api)

<h4 align="center">
	🚧  Concluído  🚧
</h4>

Tabela de conteúdos
=================
<!--ts-->
* [Sobre o projeto](#-sobre-o-projeto)
* [Funcionalidades](#-funcionalidades)
* [Como executar o projeto](#-como-executar-o-projeto)
    * [Pré-requisitos](#pré-requisitos)
    * [Rodando a api](#-rodando-a-api)
* [Tecnologias](#-tecnologias)
* [Autor](#-autor)
* [Licença](#user-content--licença)
<!--te-->


## 💻 Sobre o projeto

Aqui você coloca o objetivo do projeto.

Exemplos:

Projeto desenvolvido durante o curso **Spring e Injeção de Dependências** oferecido pela [Algaworks](https://www.algaworks.com/).

Projeto desenvolvido durante a **NLW - Next Level Week** oferecida pela [Rocketseat](https://blog.rocketseat.com.br/primeira-next-level-week/).

---

## ⚙️ Funcionalidades (opcional)

- [x] Empresas ou entidades podem se cadastrar na plataforma web enviando:
    - [x] uma imagem do ponto de coleta
    - [x] nome da entidade, email e whatsapp
    - [x] e o endereço para que ele possa aparecer no mapa
    - [x] além de selecionar um ou mais ítens de coleta:
        - lâmpadas
        - pilhas e baterias
        - papéis e papelão
        - resíduos eletrônicos
        - resíduos orgânicos
        - óleo de cozinha

- [x] Os usuários tem acesso ao aplicativo móvel, onde podem:
    - [x] navegar pelo mapa para ver as instituições cadastradas
    - [x] entrar em contato com a entidade através do E-mail ou do WhatsApp

---

## 🚀 Como executar o projeto

### Pré-requisitos

Antes de começar, você vai precisar ter instalado em sua máquina o Java 11 e o [Maven](https://maven.apache.org/).
Além disto é bom ter um editor para trabalhar com o código, como o: [Intellij](https://www.jetbrains.com/pt-br/idea/).

#### 🎲 Rodando a API

```bash

# Clone este repositório
$ git clone https://github.com/anderson-sfoliveira/library-api.git

# Importe o projeto para dentro do Intellij.

# Inicie a aplicação.

# O servidor inciará na porta:8080

```
---

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

---

## 🦸🏾 Autor

<a href="https://www.linkedin.com/in/anderson-sfoliveira/">
 <img style="border-radius: 50%;" src="https://avatars.githubusercontent.com/u/2175235?s=400&u=432d3456eb62f2df111abdccd667976321f6f74a&v=4" width="100px;" alt=""/>
 <br />
 <sub><b>Anderson Oliveira</b></sub></a> <a href="https://www.linkedin.com/in/anderson-sfoliveira/" title="Anderson Oliveira"></a>
 <br />

[![Linkedin Badge](https://img.shields.io/badge/-Anderson-blue?style=flat-square&logo=Linkedin&logoColor=white&link=https://www.linkedin.com/in/anderson-sfoliveira/)](https://www.linkedin.com/in/anderson-sfoliveira/)
[![Gmail Badge](https://img.shields.io/badge/-anderson.sfoliveira@gmail.com-c14438?style=flat-square&logo=Gmail&logoColor=white&link=mailto:anderson.sfoliveira@gmail.com)](mailto:anderson.sfoliveira@gmail.com)

Feito com ❤️ por Anderson Oliveira 👋🏽 Entre em contato!

---

## 📝 Licença

Este projeto esta sobe a licença [MIT](./LICENSE).

Feito com ❤️ por Anderson Oliveira 👋🏽 Entre em contato!
