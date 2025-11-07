# Projeto de Testes Unitários para API CRUD de Produtos

Este projeto contém os testes unitários para as camadas de serviço e controle de uma API CRUD (Create, Read, Update, Delete) de produtos, desenvolvida com Java e Spring Boot.

## Estrutura do Projeto

O projeto está organizado da seguinte forma:

/testes-crud-produtos
|-- /controle
|   `-- ProdutoControleTest.java  # Testes para a camada de Controller (endpoints HTTP)

|-- /servico
|   `-- ProdutoServicoTest.java   # Testes para a camada de Serviço (regras de negócio)

`-- README.md                     # Este arquivo

### Camada de Serviço (`ProdutoServicoTest.java`)

Neste arquivo, os testes são focados em validar as regras de negócio da aplicação, como validações de campos, lógica de cadastro, alteração e remoção de produtos. Utilizamos o **Mockito** para "mockar" (simular) o comportamento do `ProdutoRepositorio`, isolando a camada de serviço da camada de acesso a dados.

### Camada de Controle (`ProdutoControleTest.java`)

Aqui, o foco é testar os endpoints da API, ou seja, a interface HTTP da aplicação. Utilizamos o **MockMvc** para simular requisições HTTP (GET, POST, PUT, DELETE) e verificar se as respostas (status HTTP, corpo da resposta em JSON) estão corretas. O `ProdutoServico` é "mockado" com `@MockBean` para que os testes da camada de controle não dependam da implementação real da camada de serviço.

## Ferramentas Utilizadas

*   **JUnit 5:** Framework padrão para a escrita de testes em Java.
*   **Mockito:** Biblioteca para a criação de objetos de teste "falsos" (mocks), essencial para o isolamento de componentes nos testes unitários.
*   **Spring Test (MockMvc):** Ferramenta do ecossistema Spring para testar os controllers de forma focada, simulando o comportamento de requisições HTTP sem a necessidade de iniciar um servidor web completo.

## Sumário Executivo

Este relatório apresenta a implementação completa de testes unitários para uma API CRUD de produtos desenvolvida com Java e Spring Boot. O trabalho abrange testes para as camadas de serviço e controle, seguindo as melhores práticas de teste de software e utilizando ferramentas consolidadas no ecossistema Java, como JUnit 5, Mockito e Spring Test.

O objetivo principal foi garantir a qualidade, manutenibilidade e confiabilidade do software através da criação de uma suíte de testes abrangente que valida tanto as regras de negócio quanto a interface HTTP da aplicação.

## Introdução

Testes unitários representam a base da pirâmide de testes de software. Eles permitem verificar o comportamento de pequenas unidades de código de forma isolada, facilitando a identificação precoce de bugs e promovendo um design de código mais limpo e modular. Em aplicações Spring Boot, é fundamental testar separadamente as diferentes camadas da arquitetura, como a camada de serviço (que contém a lógica de negócio) e a camada de controle (que expõe os endpoints HTTP).

Este trabalho foi desenvolvido seguindo o guia prático fornecido, que estabelece as bases para a implementação de testes em um projeto CRUD de produtos. Além dos testes exemplificados no guia, foram implementados testes adicionais para completar a cobertura das operações CRUD, incluindo testes para os métodos de alteração e remoção de produtos.

## Metodologia

A implementação dos testes seguiu o padrão **Arrange-Act-Assert (AAA)**, uma estrutura amplamente utilizada na escrita de testes unitários que organiza cada teste em três seções distintas:

1. **Arrange (Preparação):** Configuração do cenário de teste, incluindo a criação de objetos, definição de valores e configuração de mocks.
2. **Act (Ação):** Execução do método ou operação que está sendo testada.
3. **Assert (Verificação):** Validação dos resultados obtidos, comparando-os com os resultados esperados.

### Ferramentas e Frameworks Utilizados

As seguintes ferramentas foram empregadas na implementação dos testes:

| Ferramenta | Versão | Propósito |
|------------|--------|-----------|
| **JUnit 5** | 5.x | Framework padrão para escrita de testes em Java |
| **Mockito** | 4.x | Biblioteca para criação de objetos mock e simulação de comportamentos |
| **Spring Test** | 3.x | Módulo do Spring para testes, incluindo MockMvc para testes de controllers |
| **AssertJ** | 3.x | Biblioteca para asserções fluentes e expressivas |
| **Spring Boot Starter Test** | 3.x | Dependência que agrupa todas as bibliotecas de teste necessárias |

### Estratégia de Teste

A estratégia adotada focou em duas camadas principais da aplicação:

**Camada de Serviço (`ProdutoServico`):** Testes que validam as regras de negócio, como validações de campos obrigatórios, lógica de cadastro, alteração e remoção de produtos. Nesta camada, o repositório foi mockado para isolar a lógica de negócio da camada de persistência.

**Camada de Controle (`ProdutoControle`):** Testes que verificam o comportamento dos endpoints HTTP, incluindo status de resposta, formato do conteúdo (JSON) e estrutura dos dados retornados. Nesta camada, o serviço foi mockado para focar exclusivamente no comportamento do controller.

## Implementação dos Testes

### Testes da Camada de Serviço

A classe `ProdutoServicoTest` contém um total de **13 testes** que cobrem os três principais métodos da camada de serviço: `cadastrarAlterar()`, `alterar()` e `remover()`.

#### Testes do Método `cadastrarAlterar()`

Este método é responsável por cadastrar novos produtos no sistema. Os seguintes cenários foram testados:

1. **Cadastro com sucesso:** Verifica se um produto com nome e marca válidos é cadastrado corretamente, retornando status HTTP 201 (CREATED).
2. **Erro quando o nome está vazio:** Valida que a aplicação rejeita produtos sem nome, retornando status HTTP 400 (BAD_REQUEST).
3. **Erro quando a marca está vazia:** Valida que a aplicação rejeita produtos sem marca, retornando status HTTP 400 (BAD_REQUEST).

#### Testes do Método `alterar()`

Este método permite a atualização de produtos existentes. Os cenários testados incluem:

1. **Alteração com sucesso:** Verifica se um produto existente pode ser atualizado corretamente, retornando status HTTP 200 (OK).
2. **Erro ao tentar alterar produto inexistente:** Valida que a aplicação retorna status HTTP 404 (NOT_FOUND) quando se tenta alterar um produto que não existe no banco de dados.
3. **Erro quando o nome está vazio:** Garante que a validação de campos obrigatórios também se aplica na operação de alteração.

#### Testes do Método `remover()`

Este método é responsável pela exclusão de produtos. Os testes implementados cobrem:

1. **Remoção com sucesso:** Verifica se um produto existente pode ser removido corretamente, retornando status HTTP 200 (OK).
2. **Erro ao tentar remover produto inexistente:** Valida que a aplicação retorna status HTTP 404 (NOT_FOUND) quando se tenta remover um produto que não existe.
3. **Erro quando o código é nulo:** Garante que a aplicação rejeita tentativas de remoção sem fornecer um código válido.
4. **Erro quando o código é zero:** Valida que códigos inválidos (como zero) são rejeitados.
5. **Erro quando o código é negativo:** Garante que códigos negativos são tratados como inválidos.

### Testes da Camada de Controle

A classe `ProdutoControleTest` contém um total de **15 testes** que cobrem os quatro principais endpoints da API: GET `/listar`, POST `/cadastrar`, PUT `/alterar` e DELETE `/remover/{codigo}`.

#### Testes do Endpoint GET `/listar`

1. **Listar todos os produtos:** Verifica se o endpoint retorna corretamente uma lista de produtos em formato JSON com status HTTP 200 (OK).
2. **Retornar lista vazia:** Valida o comportamento quando não há produtos cadastrados, esperando uma lista vazia em formato JSON.

#### Testes do Endpoint POST `/cadastrar`

1. **Cadastrar um novo produto:** Verifica se o endpoint aceita um produto válido e retorna status HTTP 201 (CREATED).
2. **Erro ao cadastrar com nome vazio:** Valida que o endpoint rejeita produtos sem nome, retornando status HTTP 400 (BAD_REQUEST).
3. **Erro ao cadastrar com marca vazia:** Valida que o endpoint rejeita produtos sem marca, retornando status HTTP 400 (BAD_REQUEST).

#### Testes do Endpoint PUT `/alterar`

1. **Alterar produto existente:** Verifica se o endpoint atualiza corretamente um produto existente, retornando status HTTP 200 (OK).
2. **Erro ao alterar produto inexistente:** Valida que o endpoint retorna status HTTP 404 (NOT_FOUND) quando se tenta alterar um produto que não existe.
3. **Erro ao alterar com nome vazio:** Garante que as validações de campos obrigatórios são aplicadas na alteração.
4. **Erro ao alterar com marca vazia:** Valida que produtos sem marca são rejeitados na alteração.

#### Testes do Endpoint DELETE `/remover/{codigo}`

1. **Remover produto existente:** Verifica se o endpoint remove corretamente um produto existente, retornando status HTTP 200 (OK).
2. **Erro ao remover produto inexistente:** Valida que o endpoint retorna status HTTP 404 (NOT_FOUND) quando se tenta remover um produto que não existe.
3. **Erro ao remover com código inválido (zero):** Garante que códigos inválidos são rejeitados.
4. **Erro ao remover com código negativo:** Valida que códigos negativos são tratados como inválidos.
5. **Permitir remover múltiplos produtos:** Verifica que o endpoint pode ser chamado múltiplas vezes para remover diferentes produtos.

## Padrões e Boas Práticas Aplicadas

Durante a implementação dos testes, foram seguidas diversas boas práticas de engenharia de software:

### Isolamento de Componentes

Cada teste foi projetado para ser independente e isolado. Na camada de serviço, o repositório foi mockado para que os testes não dependessem de um banco de dados real. Na camada de controle, o serviço foi mockado para que os testes focassem exclusivamente no comportamento do controller.

### Nomenclatura Descritiva

Os nomes dos métodos de teste seguem um padrão descritivo que deixa claro o que está sendo testado e qual é o resultado esperado. Por exemplo, `deveCadastrarProdutoComSucesso()` e `deveRetornarErroQuandoNomeProdutoEstiverVazio()` são autoexplicativos.

### Cobertura de Cenários

Os testes cobrem tanto os cenários de sucesso (happy path) quanto os cenários de erro (sad path). Isso garante que a aplicação se comporta corretamente em situações normais e também trata adequadamente situações excepcionais.

### Verificação de Interações

Além de validar os resultados retornados pelos métodos, os testes também verificam se as interações esperadas com os mocks ocorreram. Por exemplo, após um cadastro bem-sucedido, verificamos se o método `save()` do repositório foi chamado exatamente uma vez.

### Documentação Inline

Cada teste contém comentários explicativos que detalham as três fases do padrão AAA (Arrange, Act, Assert), facilitando a compreensão do código por outros desenvolvedores.

## Cobertura de Testes

A suíte de testes implementada oferece uma cobertura abrangente das funcionalidades principais da aplicação:

| Camada | Classe Testada | Número de Testes | Métodos Cobertos |
|--------|----------------|------------------|------------------|
| Serviço | `ProdutoServico` | 13 | `cadastrarAlterar()`, `alterar()`, `remover()` |
| Controle | `ProdutoControle` | 15 | GET `/listar`, POST `/cadastrar`, PUT `/alterar`, DELETE `/remover/{codigo}` |
| **Total** | **2 classes** | **28 testes** | **7 operações** |

### Análise de Cobertura por Operação

**Operação CREATE (Cadastrar):**
- 3 testes na camada de serviço
- 3 testes na camada de controle
- Total: 6 testes

**Operação READ (Listar):**
- 2 testes na camada de controle
- Total: 2 testes

**Operação UPDATE (Alterar):**
- 3 testes na camada de serviço
- 4 testes na camada de controle
- Total: 7 testes

**Operação DELETE (Remover):**
- 5 testes na camada de serviço
- 5 testes na camada de controle
- Total: 10 testes

## Benefícios da Implementação de Testes

A implementação desta suíte de testes traz diversos benefícios para o projeto:

### Detecção Precoce de Bugs

Os testes automatizados permitem identificar problemas no código durante o desenvolvimento, antes que eles cheguem à produção. Isso reduz significativamente os custos de correção de bugs, pois é mais fácil e barato corrigir um problema detectado durante o desenvolvimento do que em produção.

### Facilitação da Refatoração

Com uma suíte de testes abrangente, os desenvolvedores podem refatorar o código com confiança, sabendo que qualquer alteração que quebre a funcionalidade existente será imediatamente detectada pelos testes.

### Documentação Viva

Os testes servem como documentação executável do comportamento esperado da aplicação. Ao ler os testes, outros desenvolvedores podem entender rapidamente como cada componente deve funcionar e quais são os casos de uso suportados.

### Melhoria do Design do Código

Escrever código testável geralmente leva a um design mais limpo e modular. A necessidade de mockar dependências força os desenvolvedores a pensar em termos de interfaces e inversão de dependências, resultando em código mais desacoplado e manutenível.

### Confiança nas Mudanças

Com testes automatizados, a equipe de desenvolvimento pode fazer alterações no código com maior confiança, sabendo que os testes irão alertá-los se algo quebrar. Isso acelera o ciclo de desenvolvimento e reduz o medo de fazer mudanças necessárias.

## Desafios e Soluções

Durante a implementação dos testes, alguns desafios foram encontrados e superados:

### Desafio 1: Configuração do Mockito

**Problema:** Inicialmente, houve dificuldade em configurar corretamente os mocks do Mockito, especialmente em relação à injeção de dependências.

**Solução:** Utilizou-se a anotação `@ExtendWith(MockitoExtension.class)` para integrar o Mockito com o JUnit 5, e as anotações `@Mock` e `@InjectMocks` para criar e injetar os mocks automaticamente.

### Desafio 2: Simulação de Requisições HTTP

**Problema:** Testar os controllers sem iniciar um servidor web completo parecia complexo.

**Solução:** O MockMvc do Spring Test resolve esse problema perfeitamente, permitindo simular requisições HTTP de forma leve e rápida, focando apenas na camada de controle.

### Desafio 3: Validação de Respostas JSON

**Problema:** Verificar a estrutura e os valores dos objetos JSON retornados pelos endpoints.

**Solução:** Utilizou-se o método `jsonPath()` do MockMvc, que permite navegar pela estrutura JSON usando expressões de caminho, facilitando a validação de campos específicos.

## Próximos Passos

Embora a suíte de testes implementada seja abrangente, existem oportunidades para expandir ainda mais a cobertura de testes:

### Testes de Integração

Implementar testes de integração que validem o funcionamento conjunto de todas as camadas da aplicação, incluindo a interação real com o banco de dados. Esses testes complementariam os testes unitários, garantindo que os componentes funcionam corretamente quando integrados.

### Testes de Validação de Dados

Adicionar testes mais detalhados para validações de campos, como limites de tamanho de strings, formatos de dados e regras de negócio mais complexas.

### Testes de Performance

Implementar testes que verifiquem o desempenho da aplicação sob diferentes cargas, identificando possíveis gargalos antes que eles afetem os usuários em produção.

### Testes de Segurança

Adicionar testes que verifiquem aspectos de segurança, como autenticação, autorização e proteção contra ataques comuns (SQL injection, XSS, etc.).

### Integração Contínua

Configurar um pipeline de CI/CD que execute automaticamente todos os testes a cada commit, garantindo que o código sempre esteja em um estado funcional.

## Conclusão

A implementação de testes unitários para a API CRUD de produtos demonstrou ser um investimento valioso para a qualidade e manutenibilidade do software. Através da utilização de ferramentas consolidadas como JUnit 5, Mockito e Spring Test, foi possível criar uma suíte de testes abrangente que cobre os principais cenários de uso da aplicação.

Os 28 testes implementados cobrem tanto os casos de sucesso quanto os casos de erro, garantindo que a aplicação se comporta corretamente em diferentes situações. A adoção de boas práticas, como o padrão Arrange-Act-Assert, nomenclatura descritiva e isolamento de componentes, resultou em testes claros, manuteníveis e confiáveis.

Este trabalho não apenas atende aos requisitos estabelecidos no guia prático, mas também vai além, implementando testes adicionais para os métodos de alteração e remoção, completando a cobertura das operações CRUD. A experiência adquirida neste projeto serve como base sólida para a aplicação de práticas de teste em projetos futuros, contribuindo para o desenvolvimento de software de alta qualidade.
