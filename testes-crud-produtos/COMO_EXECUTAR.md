# Como Executar os Testes

Este documento fornece instruções detalhadas sobre como integrar e executar os testes unitários no seu projeto Spring Boot.

## Pré-requisitos

Antes de executar os testes, certifique-se de que você possui:

- **Java 11 ou superior** instalado
- **Maven** ou **Gradle** configurado no projeto
- **Spring Boot 2.5+** ou **3.x**
- Dependências de teste configuradas no `pom.xml` (Maven) ou `build.gradle` (Gradle)

## Configuração das Dependências

### Maven (pom.xml)

Adicione a seguinte dependência no seu arquivo `pom.xml`:

```xml
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-test</artifactId>
    <scope>test</scope>
</dependency>
```

Esta dependência inclui automaticamente:
- JUnit 5
- Mockito
- Spring Test (incluindo MockMvc)
- AssertJ
- Hamcrest

### Gradle (build.gradle)

Para projetos Gradle, adicione:

```gradle
testImplementation 'org.springframework.boot:spring-boot-starter-test'
```

## Estrutura de Diretórios

Os arquivos de teste devem ser colocados na estrutura de diretórios padrão do projeto:

```
seu-projeto/
├── src/
│   ├── main/
│   │   └── java/
│   │       └── br/com/anm/produtos/crud_produtos/
│   │           ├── controle/
│   │           │   └── ProdutoControle.java
│   │           ├── servico/
│   │           │   └── ProdutoServico.java
│   │           ├── repositorio/
│   │           │   └── ProdutoRepositorio.java
│   │           └── modelo/
│   │               ├── ProdutoModelo.java
│   │               └── RespostaModelo.java
│   └── test/
│       └── java/
│           └── br/com/anm/produtos/crud_produtos/
│               ├── controle/
│               │   └── ProdutoControleTest.java
│               └── servico/
│                   └── ProdutoServicoTest.java
```

## Copiando os Arquivos de Teste

1. Copie o arquivo `ProdutoServicoTest.java` para o diretório:
   ```
   src/test/java/br/com/anm/produtos/crud_produtos/servico/
   ```

2. Copie o arquivo `ProdutoControleTest.java` para o diretório:
   ```
   src/test/java/br/com/anm/produtos/crud_produtos/controle/
   ```

## Executando os Testes

### Via Maven

Para executar todos os testes do projeto:

```bash
mvn test
```

Para executar apenas os testes de uma classe específica:

```bash
mvn test -Dtest=ProdutoServicoTest
```

ou

```bash
mvn test -Dtest=ProdutoControleTest
```

Para executar um teste específico dentro de uma classe:

```bash
mvn test -Dtest=ProdutoServicoTest#deveCadastrarProdutoComSucesso
```

### Via Gradle

Para executar todos os testes:

```bash
./gradlew test
```

Para executar apenas os testes de uma classe específica:

```bash
./gradlew test --tests ProdutoServicoTest
```

ou

```bash
./gradlew test --tests ProdutoControleTest
```

### Via IDE (IntelliJ IDEA / Eclipse / VS Code)

**IntelliJ IDEA:**
1. Abra a classe de teste
2. Clique com o botão direito no nome da classe ou em um método de teste
3. Selecione "Run 'NomeDaClasse'" ou "Run 'nomeDoMetodo()'"

**Eclipse:**
1. Abra a classe de teste
2. Clique com o botão direito no arquivo
3. Selecione "Run As" → "JUnit Test"

**VS Code:**
1. Instale a extensão "Test Runner for Java"
2. Abra a classe de teste
3. Clique no ícone de "play" ao lado do nome da classe ou método

## Interpretando os Resultados

### Teste Bem-Sucedido

Quando todos os testes passam, você verá uma saída similar a:

```
[INFO] Tests run: 28, Failures: 0, Errors: 0, Skipped: 0
[INFO] BUILD SUCCESS
```

### Teste com Falha

Se algum teste falhar, você verá uma mensagem detalhada indicando:
- Qual teste falhou
- Qual foi o erro
- Valores esperados vs. valores obtidos

Exemplo:

```
[ERROR] Tests run: 28, Failures: 1, Errors: 0, Skipped: 0
[ERROR] deveCadastrarProdutoComSucesso  Time elapsed: 0.05 s  <<< FAILURE!
org.opentest4j.AssertionFailedError: 
Expected :CREATED
Actual   :BAD_REQUEST
```

## Relatório de Cobertura de Testes

### Maven com JaCoCo

Para gerar um relatório de cobertura de código, adicione o plugin JaCoCo no `pom.xml`:

```xml
<plugin>
    <groupId>org.jacoco</groupId>
    <artifactId>jacoco-maven-plugin</artifactId>
    <version>0.8.10</version>
    <executions>
        <execution>
            <goals>
                <goal>prepare-agent</goal>
            </goals>
        </execution>
        <execution>
            <id>report</id>
            <phase>test</phase>
            <goals>
                <goal>report</goal>
            </goals>
        </execution>
    </executions>
</plugin>
```

Execute:

```bash
mvn clean test jacoco:report
```

O relatório será gerado em: `target/site/jacoco/index.html`

### Gradle com JaCoCo

Adicione ao `build.gradle`:

```gradle
plugins {
    id 'jacoco'
}

jacoco {
    toolVersion = "0.8.10"
}

test {
    finalizedBy jacocoTestReport
}

jacocoTestReport {
    dependsOn test
    reports {
        html.enabled true
        xml.enabled false
        csv.enabled false
    }
}
```

Execute:

```bash
./gradlew test jacocoTestReport
```

O relatório será gerado em: `build/reports/jacoco/test/html/index.html`

## Solução de Problemas Comuns

### Erro: "Cannot resolve symbol 'MockitoExtension'"

**Solução:** Certifique-se de que a dependência `spring-boot-starter-test` está no `pom.xml` ou `build.gradle`.

### Erro: "No tests found"

**Solução:** Verifique se:
- Os métodos de teste estão anotados com `@Test`
- A classe de teste está no diretório `src/test/java`
- O nome da classe termina com "Test" (ex: `ProdutoServicoTest`)

### Erro: "MockMvc is null"

**Solução:** Certifique-se de que a classe de teste do controller está anotada com `@WebMvcTest(ProdutoControle.class)` e que o MockMvc está anotado com `@Autowired`.

### Erro: "NullPointerException" ao executar testes

**Solução:** Verifique se:
- Os mocks estão sendo criados corretamente com `@Mock`
- A classe testada está anotada com `@InjectMocks`
- A classe de teste está anotada com `@ExtendWith(MockitoExtension.class)`

## Boas Práticas

1. **Execute os testes frequentemente:** Rode os testes a cada alteração significativa no código.
2. **Mantenha os testes rápidos:** Testes unitários devem executar rapidamente. Se estiverem lentos, revise o uso de mocks.
3. **Não ignore testes falhando:** Se um teste falhar, corrija-o imediatamente ou investigue a causa.
4. **Mantenha os testes atualizados:** Sempre que alterar o código de produção, atualize os testes correspondentes.
5. **Escreva testes antes de corrigir bugs:** Quando encontrar um bug, escreva um teste que o reproduza antes de corrigi-lo.

## Recursos Adicionais

- [JUnit 5 User Guide](https://junit.org/junit5/docs/current/user-guide/)
- [Mockito Documentation](https://javadoc.io/doc/org.mockito/mockito-core/latest/org/mockito/Mockito.html)
- [Spring Boot Testing](https://docs.spring.io/spring-boot/docs/current/reference/html/features.html#features.testing)
- [Testing the Web Layer](https://spring.io/guides/gs/testing-web/)

---

**Dica:** Para facilitar a execução dos testes, considere configurar um atalho de teclado na sua IDE para executar os testes da classe atual ou do método sob o cursor.
