package br.com.anm.produtos.crud_produtos.controle;

import br.com.anm.produtos.crud_produtos.modelo.ProdutoModelo;
import br.com.anm.produtos.crud_produtos.servico.ProdutoServico;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.List;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Classe de testes unitários para ProdutoControle
 * Utiliza @WebMvcTest para testar apenas a camada de controle
 * MockMvc simula requisições HTTP sem precisar subir o servidor
 */
@WebMvcTest(ProdutoControle.class)
class ProdutoControleTest {

    // MockMvc é a principal ferramenta para testar controllers
    // Ele simula as chamadas HTTP
    @Autowired
    private MockMvc mockMvc;

    // @MockBean cria um mock do serviço e o adiciona ao contexto do Spring para o teste
    // Assim, o ProdutoControle usará nosso mock em vez do serviço real
    @MockBean
    private ProdutoServico ps;

    // Utilitário para converter objetos Java para JSON e vice-versa
    @Autowired
    private ObjectMapper objectMapper;

    // ==================== TESTES DO ENDPOINT GET /listar ====================

    @Test
    void deveListarTodosOsProdutos() throws Exception {
        // --- ARRANGE ---
        ProdutoModelo p1 = new ProdutoModelo();
        p1.setCodigo(1L);
        p1.setNome("Celular");
        p1.setMarca("Marca X");

        ProdutoModelo p2 = new ProdutoModelo();
        p2.setCodigo(2L);
        p2.setNome("Televisão");
        p2.setMarca("Marca Y");

        List<ProdutoModelo> produtos = Arrays.asList(p1, p2);

        // Configuramos o mock do serviço para retornar nossa lista de produtos quando o
        // método listar for chamado
        when(ps.listar()).thenReturn(produtos);

        // --- ACT & ASSERT ---
        mockMvc.perform(get("/listar")) // Simula uma requisição GET para "/listar"
                .andExpect(status().isOk()) // Esperamos que o status da resposta seja 200 OK
                .andExpect(content().contentType(MediaType.APPLICATION_JSON)) // O conteúdo deve ser JSON
                .andExpect(jsonPath("$[0].nome").value("Celular")) // Verifica o nome do primeiro produto na lista
                .andExpect(jsonPath("$[1].nome").value("Televisão")); // Verifica o nome do segundo produto
    }

    @Test
    void deveRetornarListaVaziaQuandoNaoHouverProdutos() throws Exception {
        // --- ARRANGE ---
        List<ProdutoModelo> produtosVazios = Arrays.asList();

        when(ps.listar()).thenReturn(produtosVazios);

        // --- ACT & ASSERT ---
        mockMvc.perform(get("/listar"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$").isEmpty()); // Verifica que o array JSON está vazio
    }

    // ==================== TESTES DO ENDPOINT POST /cadastrar ====================

    @Test
    void deveCadastrarUmNovoProduto() throws Exception {
        // --- ARRANGE ---
        ProdutoModelo produto = new ProdutoModelo();
        produto.setNome("Novo Produto");
        produto.setMarca("Nova Marca");

        // Simulamos o comportamento do serviço: ao receber um produto e a ação "cadastrar",
        // ele deve retornar uma ResponseEntity com status 201 CREATED
        when(ps.cadastrarAlterar(any(ProdutoModelo.class), eq("cadastrar")))
                .thenReturn(ResponseEntity.status(HttpStatus.CREATED).body(produto));

        // --- ACT & ASSERT ---
        mockMvc.perform(post("/cadastrar") // Simula uma requisição POST
                        .contentType(MediaType.APPLICATION_JSON) // Define o tipo de conteúdo do corpo da requisição
                        .content(objectMapper.writeValueAsString(produto))) // Converte o objeto produto para uma string JSON
                .andExpect(status().isCreated()) // Esperamos que o status seja 201 CREATED
                .andExpect(jsonPath("$.nome").value("Novo Produto")); // Verificamos o corpo da resposta
    }

    @Test
    void deveRetornarErroQuandoCadastrarProdutoComNomeVazio() throws Exception {
        // --- ARRANGE ---
        ProdutoModelo produto = new ProdutoModelo();
        produto.setNome("");
        produto.setMarca("Marca Válida");

        when(ps.cadastrarAlterar(any(ProdutoModelo.class), eq("cadastrar")))
                .thenReturn(ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Nome do produto é obrigatório"));

        // --- ACT & ASSERT ---
        mockMvc.perform(post("/cadastrar")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(produto)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void deveRetornarErroQuandoCadastrarProdutoComMarcaVazia() throws Exception {
        // --- ARRANGE ---
        ProdutoModelo produto = new ProdutoModelo();
        produto.setNome("Nome Válido");
        produto.setMarca("");

        when(ps.cadastrarAlterar(any(ProdutoModelo.class), eq("cadastrar")))
                .thenReturn(ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Marca do produto é obrigatória"));

        // --- ACT & ASSERT ---
        mockMvc.perform(post("/cadastrar")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(produto)))
                .andExpect(status().isBadRequest());
    }

    // ==================== TESTES DO ENDPOINT PUT /alterar ====================

    @Test
    void deveAlterarProdutoExistente() throws Exception {
        // --- ARRANGE ---
        Long codigoProduto = 1L;
        ProdutoModelo produtoAtualizado = new ProdutoModelo();
        produtoAtualizado.setCodigo(codigoProduto);
        produtoAtualizado.setNome("Produto Atualizado");
        produtoAtualizado.setMarca("Marca Atualizada");

        when(ps.cadastrarAlterar(any(ProdutoModelo.class), eq("alterar")))
                .thenReturn(ResponseEntity.status(HttpStatus.OK).body(produtoAtualizado));

        // --- ACT & ASSERT ---
        mockMvc.perform(put("/alterar") // Simula uma requisição PUT
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(produtoAtualizado)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nome").value("Produto Atualizado"))
                .andExpect(jsonPath("$.marca").value("Marca Atualizada"));
    }

    @Test
    void deveRetornarErroQuandoAlterarProdutoInexistente() throws Exception {
        // --- ARRANGE ---
        Long codigoProduto = 999L;
        ProdutoModelo produto = new ProdutoModelo();
        produto.setCodigo(codigoProduto);
        produto.setNome("Produto Válido");
        produto.setMarca("Marca Válida");

        when(ps.cadastrarAlterar(any(ProdutoModelo.class), eq("alterar")))
                .thenReturn(ResponseEntity.status(HttpStatus.NOT_FOUND).body("Produto não encontrado"));

        // --- ACT & ASSERT ---
        mockMvc.perform(put("/alterar")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(produto)))
                .andExpect(status().isNotFound());
    }

    @Test
    void deveRetornarErroQuandoAlterarComNomeVazio() throws Exception {
        // --- ARRANGE ---
        ProdutoModelo produto = new ProdutoModelo();
        produto.setCodigo(1L);
        produto.setNome("");
        produto.setMarca("Marca Válida");

        when(ps.cadastrarAlterar(any(ProdutoModelo.class), eq("alterar")))
                .thenReturn(ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Nome do produto é obrigatório"));

        // --- ACT & ASSERT ---
        mockMvc.perform(put("/alterar")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(produto)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void deveRetornarErroQuandoAlterarComMarcaVazia() throws Exception {
        // --- ARRANGE ---
        ProdutoModelo produto = new ProdutoModelo();
        produto.setCodigo(1L);
        produto.setNome("Nome Válido");
        produto.setMarca("");

        when(ps.cadastrarAlterar(any(ProdutoModelo.class), eq("alterar")))
                .thenReturn(ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Marca do produto é obrigatória"));

        // --- ACT & ASSERT ---
        mockMvc.perform(put("/alterar")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(produto)))
                .andExpect(status().isBadRequest());
    }

    // ==================== TESTES DO ENDPOINT DELETE /remover/{codigo} ====================

    @Test
    void deveRemoverProdutoExistente() throws Exception {
        // --- ARRANGE ---
        Long codigoProduto = 1L;

        when(ps.remover(codigoProduto))
                .thenReturn(ResponseEntity.status(HttpStatus.OK).body("Produto removido com sucesso"));

        // --- ACT & ASSERT ---
        mockMvc.perform(delete("/remover/{codigo}", codigoProduto)) // Simula uma requisição DELETE
                .andExpect(status().isOk());

        // Verifica que o método remover foi chamado exatamente 1 vez com o código correto
        verify(ps, times(1)).remover(codigoProduto);
    }

    @Test
    void deveRetornarErroQuandoRemoverProdutoInexistente() throws Exception {
        // --- ARRANGE ---
        Long codigoProduto = 999L;

        when(ps.remover(codigoProduto))
                .thenReturn(ResponseEntity.status(HttpStatus.NOT_FOUND).body("Produto não encontrado"));

        // --- ACT & ASSERT ---
        mockMvc.perform(delete("/remover/{codigo}", codigoProduto))
                .andExpect(status().isNotFound());

        verify(ps, times(1)).remover(codigoProduto);
    }

    @Test
    void deveRetornarErroQuandoRemoverComCodigoInvalido() throws Exception {
        // --- ARRANGE ---
        Long codigoProduto = 0L;

        when(ps.remover(codigoProduto))
                .thenReturn(ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Código inválido"));

        // --- ACT & ASSERT ---
        mockMvc.perform(delete("/remover/{codigo}", codigoProduto))
                .andExpect(status().isBadRequest());

        verify(ps, times(1)).remover(codigoProduto);
    }

    @Test
    void deveRetornarErroQuandoRemoverComCodigoNegativo() throws Exception {
        // --- ARRANGE ---
        Long codigoProduto = -1L;

        when(ps.remover(codigoProduto))
                .thenReturn(ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Código inválido"));

        // --- ACT & ASSERT ---
        mockMvc.perform(delete("/remover/{codigo}", codigoProduto))
                .andExpect(status().isBadRequest());

        verify(ps, times(1)).remover(codigoProduto);
    }

    @Test
    void devePermitirRemoverMultiplosProdutos() throws Exception {
        // --- ARRANGE ---
        Long codigo1 = 1L;
        Long codigo2 = 2L;

        when(ps.remover(codigo1))
                .thenReturn(ResponseEntity.status(HttpStatus.OK).body("Produto 1 removido"));
        when(ps.remover(codigo2))
                .thenReturn(ResponseEntity.status(HttpStatus.OK).body("Produto 2 removido"));

        // --- ACT & ASSERT ---
        mockMvc.perform(delete("/remover/{codigo}", codigo1))
                .andExpect(status().isOk());

        mockMvc.perform(delete("/remover/{codigo}", codigo2))
                .andExpect(status().isOk());

        verify(ps, times(1)).remover(codigo1);
        verify(ps, times(1)).remover(codigo2);
    }
}
