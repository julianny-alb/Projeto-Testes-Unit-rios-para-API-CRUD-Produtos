package br.com.anm.produtos.crud_produtos.servico;

import br.com.anm.produtos.crud_produtos.modelo.ProdutoModelo;
import br.com.anm.produtos.crud_produtos.modelo.RespostaModelo;
import br.com.anm.produtos.crud_produtos.repositorio.ProdutoRepositorio;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Classe de testes unitários para ProdutoServico
 * Utiliza Mockito para simular o comportamento do repositório
 */
@ExtendWith(MockitoExtension.class)
class ProdutoServicoTest {

    // Cria um mock (objeto falso) para o repositório
    // Todas as chamadas a este objeto serão controladas por nós no teste
    @Mock
    private ProdutoRepositorio pr;

    // Cria um mock para o modelo de resposta
    @Mock
    private RespostaModelo rm;

    // Injeta os mocks (@Mock) na instância do serviço que será testada
    @InjectMocks
    private ProdutoServico ps;

    // ==================== TESTES DO MÉTODO cadastrarAlterar() ====================

    @Test
    void deveCadastrarProdutoComSucesso() {
        // --- ARRANGE (Preparação) ---
        ProdutoModelo produto = new ProdutoModelo();
        produto.setNome("Notebook Gamer");
        produto.setMarca("Marca Famosa");

        // Quando o método 'save' do repositório for chamado com qualquer objeto ProdutoModelo,
        // ele deve retornar o mesmo objeto que foi passado
        when(pr.save(any(ProdutoModelo.class))).thenReturn(produto);

        // --- ACT (Ação) ---
        // Executamos o método que queremos testar
        ResponseEntity<?> resposta = ps.cadastrarAlterar(produto, "cadastrar");

        // --- ASSERT (Verificação) ---
        // Verificamos se o resultado é o esperado
        assertEquals(HttpStatus.CREATED, resposta.getStatusCode());
        assertEquals(produto, resposta.getBody());
        // Verifica se o método save foi chamado exatamente 1 vez
        verify(pr, times(1)).save(produto);
    }

    @Test
    void deveRetornarErroQuandoNomeProdutoEstiverVazio() {
        // --- ARRANGE ---
        ProdutoModelo produto = new ProdutoModelo();
        produto.setNome("");
        produto.setMarca("Marca Válida");

        // --- ACT ---
        ResponseEntity<?> resposta = ps.cadastrarAlterar(produto, "cadastrar");

        // --- ASSERT ---
        assertEquals(HttpStatus.BAD_REQUEST, resposta.getStatusCode());
        // Verificamos que o método save NUNCA foi chamado, pois a validação deve falhar antes
        verify(pr, never()).save(any(ProdutoModelo.class));
    }

    @Test
    void deveRetornarErroQuandoMarcaProdutoEstiverVazia() {
        // --- ARRANGE ---
        ProdutoModelo produto = new ProdutoModelo();
        produto.setNome("Nome Válido");
        produto.setMarca("");

        // --- ACT ---
        ResponseEntity<?> resposta = ps.cadastrarAlterar(produto, "cadastrar");

        // --- ASSERT ---
        assertEquals(HttpStatus.BAD_REQUEST, resposta.getStatusCode());
        verify(pr, never()).save(any(ProdutoModelo.class));
    }

    // ==================== TESTES DO MÉTODO alterar() ====================

    @Test
    void deveAlterarProdutoComSucesso() {
        // --- ARRANGE ---
        Long codigoProduto = 1L;
        ProdutoModelo produtoExistente = new ProdutoModelo();
        produtoExistente.setCodigo(codigoProduto);
        produtoExistente.setNome("Produto Antigo");
        produtoExistente.setMarca("Marca Antiga");

        ProdutoModelo produtoAtualizado = new ProdutoModelo();
        produtoAtualizado.setCodigo(codigoProduto);
        produtoAtualizado.setNome("Produto Atualizado");
        produtoAtualizado.setMarca("Marca Atualizada");

        // Simula que o produto existe no banco de dados
        when(pr.findById(codigoProduto)).thenReturn(Optional.of(produtoExistente));
        // Simula que o save retorna o produto atualizado
        when(pr.save(any(ProdutoModelo.class))).thenReturn(produtoAtualizado);

        // --- ACT ---
        ResponseEntity<?> resposta = ps.cadastrarAlterar(produtoAtualizado, "alterar");

        // --- ASSERT ---
        assertEquals(HttpStatus.OK, resposta.getStatusCode());
        assertEquals(produtoAtualizado, resposta.getBody());
        verify(pr, times(1)).findById(codigoProduto);
        verify(pr, times(1)).save(produtoAtualizado);
    }

    @Test
    void deveRetornarErroQuandoTentarAlterarProdutoInexistente() {
        // --- ARRANGE ---
        Long codigoProduto = 999L;
        ProdutoModelo produto = new ProdutoModelo();
        produto.setCodigo(codigoProduto);
        produto.setNome("Produto Válido");
        produto.setMarca("Marca Válida");

        // Simula que o produto NÃO existe no banco de dados
        when(pr.findById(codigoProduto)).thenReturn(Optional.empty());

        // --- ACT ---
        ResponseEntity<?> resposta = ps.cadastrarAlterar(produto, "alterar");

        // --- ASSERT ---
        assertEquals(HttpStatus.NOT_FOUND, resposta.getStatusCode());
        verify(pr, times(1)).findById(codigoProduto);
        // Verifica que o save nunca foi chamado, pois o produto não existe
        verify(pr, never()).save(any(ProdutoModelo.class));
    }

    @Test
    void deveRetornarErroQuandoAlterarComNomeVazio() {
        // --- ARRANGE ---
        Long codigoProduto = 1L;
        ProdutoModelo produto = new ProdutoModelo();
        produto.setCodigo(codigoProduto);
        produto.setNome("");
        produto.setMarca("Marca Válida");

        // --- ACT ---
        ResponseEntity<?> resposta = ps.cadastrarAlterar(produto, "alterar");

        // --- ASSERT ---
        assertEquals(HttpStatus.BAD_REQUEST, resposta.getStatusCode());
        verify(pr, never()).findById(any());
        verify(pr, never()).save(any(ProdutoModelo.class));
    }

    // ==================== TESTES DO MÉTODO remover() ====================

    @Test
    void deveRemoverProdutoComSucesso() {
        // --- ARRANGE ---
        Long codigoProduto = 1L;
        ProdutoModelo produtoExistente = new ProdutoModelo();
        produtoExistente.setCodigo(codigoProduto);
        produtoExistente.setNome("Produto a ser removido");
        produtoExistente.setMarca("Marca Qualquer");

        // Simula que o produto existe no banco de dados
        when(pr.findById(codigoProduto)).thenReturn(Optional.of(produtoExistente));
        // O método deleteById não retorna nada, então não precisamos configurar um retorno
        doNothing().when(pr).deleteById(codigoProduto);

        // --- ACT ---
        ResponseEntity<?> resposta = ps.remover(codigoProduto);

        // --- ASSERT ---
        assertEquals(HttpStatus.OK, resposta.getStatusCode());
        assertNotNull(resposta.getBody());
        verify(pr, times(1)).findById(codigoProduto);
        verify(pr, times(1)).deleteById(codigoProduto);
    }

    @Test
    void deveRetornarErroQuandoTentarRemoverProdutoInexistente() {
        // --- ARRANGE ---
        Long codigoProduto = 999L;

        // Simula que o produto NÃO existe no banco de dados
        when(pr.findById(codigoProduto)).thenReturn(Optional.empty());

        // --- ACT ---
        ResponseEntity<?> resposta = ps.remover(codigoProduto);

        // --- ASSERT ---
        assertEquals(HttpStatus.NOT_FOUND, resposta.getStatusCode());
        verify(pr, times(1)).findById(codigoProduto);
        // Verifica que deleteById nunca foi chamado, pois o produto não existe
        verify(pr, never()).deleteById(any());
    }

    @Test
    void deveRetornarErroQuandoCodigoProdutoForNulo() {
        // --- ARRANGE ---
        Long codigoProduto = null;

        // --- ACT ---
        ResponseEntity<?> resposta = ps.remover(codigoProduto);

        // --- ASSERT ---
        assertEquals(HttpStatus.BAD_REQUEST, resposta.getStatusCode());
        verify(pr, never()).findById(any());
        verify(pr, never()).deleteById(any());
    }

    @Test
    void deveRetornarErroQuandoCodigoProdutoForZero() {
        // --- ARRANGE ---
        Long codigoProduto = 0L;

        // --- ACT ---
        ResponseEntity<?> resposta = ps.remover(codigoProduto);

        // --- ASSERT ---
        assertEquals(HttpStatus.BAD_REQUEST, resposta.getStatusCode());
        verify(pr, never()).findById(any());
        verify(pr, never()).deleteById(any());
    }

    @Test
    void deveRetornarErroQuandoCodigoProdutoForNegativo() {
        // --- ARRANGE ---
        Long codigoProduto = -1L;

        // --- ACT ---
        ResponseEntity<?> resposta = ps.remover(codigoProduto);

        // --- ASSERT ---
        assertEquals(HttpStatus.BAD_REQUEST, resposta.getStatusCode());
        verify(pr, never()).findById(any());
        verify(pr, never()).deleteById(any());
    }
}
