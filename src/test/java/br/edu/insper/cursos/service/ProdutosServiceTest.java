package br.edu.insper.produtos.service;

import br.edu.insper.produtos.entity.produtos;
import br.edu.insper.produtos.repository.produtosRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ProdutosServiceTest {
    @Mock
    private ProdutosRepository repository;

    @InjectMocks
    private ProdutosService service;

    private Produtos produtos;

    @BeforeEach
    void preparar() {
        produtos = new Produtos();
        produtos.setId(1L);
        produtos.setNome("Java");
        produtos.setCargaHoraria(20);
    }

    @Test
    void deveListarNaoDeletadosSemFiltroNulo() {
        when(repository.findByDeletedFalse()).thenReturn(List.of(produtos));
        assertEquals(List.of(produtos), service.listar(null));
        verify(repository).findByDeletedFalse();
    }

    @Test
    void deveListarNaoDeletadosSemFiltroEmBranco() {
        when(repository.findByDeletedFalse()).thenReturn(List.of(produtos));
        assertEquals(List.of(produtos), service.listar("  "));
    }

    @Test
    void deveFiltrarPorInicioDoNome() {
        when(repository.findByNomeStartingWithIgnoreCaseAndDeletedFalse("ja")).thenReturn(List.of(produtos));
        assertEquals(List.of(produtos), service.listar("ja"));
        verify(repository).findByNomeStartingWithIgnoreCaseAndDeletedFalse("ja");
    }

    @Test
    void deveCriarComoNaoDeletadoESemIdFornecido() {
        produtos.setDeleted(true);
        when(repository.save(produtos)).thenReturn(produtos);
        Produtos salvo = service.criar(produtos);
        assertSame(produtos, salvo);
        assertNull(salvo.getId());
        assertFalse(salvo.isDeleted());
    }

    @Test
    void deveRealizarDelecaoLogica() {
        when(repository.findById(1L)).thenReturn(Optional.of(produtos));
        service.deletar(1L);
        assertTrue(produtos.isDeleted());
        verify(repository).save(produtos);
        verify(repository, never()).delete(any());
    }

    @Test
    void deveRetornar404QuandoprodutosNaoExiste() {
        when(repository.findById(99L)).thenReturn(Optional.empty());
        ResponseStatusException erro = assertThrows(ResponseStatusException.class, () -> service.deletar(99L));
        assertEquals(404, erro.getStatusCode().value());
        verify(repository, never()).save(any());
    }
}

