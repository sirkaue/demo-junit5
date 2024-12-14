# Demo JUnit5
Repositório de estudo com o objetivo de praticar testes unitários e de integração utilizando **JUnit 5** no **Spring Boot**.
---
## Objetivo
O projeto **demo-junit5** tem como foco a implementação e execução de testes em diferentes camadas de uma aplicação Spring Boot:
- Teste unitário de **Services**;
- Teste de integração de **Repositories**.
---
## Tecnologias Utilizadas
- **Java 17**
- **Spring Boot**
- **JUnit 5**
- **Maven**
- **MySQL** (para o ambiente de produção simulado)
- **H2 Database** (perfil de testes para testes de integração)
---
## Testes Implementados
### 1. Testes Unitários de Services
- **Objetivo**: Validar a lógica de negócio presente nos **services** de forma isolada.
- **Ferramentas**: JUnit 5 e Mockito.
### 2. Testes de Integração de Repositories
- **Objetivo**: Validar a persistência e consulta de dados nos **repositories** utilizando o banco de dados em memória (**H2**) no perfil de teste.
- **Ferramentas**: JUnit 5 e Spring Boot Test.