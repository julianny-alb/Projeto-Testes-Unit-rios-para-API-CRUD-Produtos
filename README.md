
# 🧪 Testes Unitários para API CRUD de Produtos (Spring Boot)

Este repositório contém a implementação completa dos testes unitários para as camadas de Serviço e Controle de uma API CRUD (Create, Read, Update, Delete) de produtos, desenvolvida utilizando **Java** e **Spring Boot**.

O objetivo deste projeto é demonstrar a aplicação de boas práticas de teste de software, garantindo a qualidade e a manutenibilidade do código.

---

## 🛠️ Tecnologias e Ferramentas

*   **Linguagem:** Java
*   **Framework:** Spring Boot
*   **Testes Unitários:** JUnit 5
*   **Mocks:** Mockito
*   **Testes de Controller:** Spring Test (MockMvc)
*   **Padrão de Teste:** Arrange-Act-Assert (AAA)

---

## 🎯 Cobertura de Testes

Os testes cobrem todas as operações essenciais da API, tanto na camada de lógica de negócio (`ProdutoServico`) quanto na camada de interface HTTP (`ProdutoControle`).

| Camada | Classe Testada | Operações Cobertas | Total de Testes |
| :--- | :--- | :--- | :--- |
| **Serviço** | `ProdutoServicoTest.java` | Cadastro, Alteração e Remoção | 13 |
| **Controle** | `ProdutoControleTest.java` | GET /listar, POST /cadastrar, PUT /alterar, DELETE /remover/{codigo} | 15 |
| **TOTAL** | **2 classes** | **CRUD Completo** | **28 Testes** |

---

## 📁 Estrutura do Repositório

Os arquivos de teste estão organizados na estrutura de pacotes padrão do Java:

```
seu-projeto/src/test/java/br/com/anm/produtos/crud_produtos/
├── controle/
│   └── ProdutoControleTest.java  # Testes dos Endpoints HTTP (MockMvc)
└── servico/
    └── ProdutoServicoTest.java   # Testes da Lógica de Negócio (Mockito)
```

---

## 🚀 Como Executar os Testes

Para rodar todos os testes, certifique-se de que seu projeto está configurado com as dependências de teste do Spring Boot.

### Via Terminal (Maven)

```bash
mvn test
```

### Via Terminal (Gradle)

```bash
./gradlew test
```

### Via IDE

Abra as classes `ProdutoServicoTest.java` e `ProdutoControleTest.java` e use a função "Run" da sua IDE (IntelliJ, VS Code, Eclipse) para executar os testes.

---

## 📄 Documentação e Relatório

Para uma análise detalhada da metodologia, resultados e conclusão do trabalho, consulte o relatório técnico:

*   **`Relatorio_Testes_Unitarios.md`**: Relatório completo do projeto.
*   **`COMO_EXECUTAR.md`**: Guia detalhado de configuração e execução.
