package br.edu.insper.cursos.service;

import br.edu.insper.cursos.entity.Curso;
import br.edu.insper.cursos.repository.CursoRepository;
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
class CursoServiceTest {
    @Mock
    private CursoRepository repository;

    @InjectMocks
    private CursoService service;

    private Curso curso;

    @BeforeEach
    void preparar() {
        curso = new Curso();
        curso.setId(1L);
        curso.setNome("Java");
        curso.setCargaHoraria(20);
    }

    @Test
    void deveListarNaoDeletadosSemFiltroNulo() {
        when(repository.findByDeletedFalse()).thenReturn(List.of(curso));
        assertEquals(List.of(curso), service.listar(null));
        verify(repository).findByDeletedFalse();
    }

    @Test
    void deveListarNaoDeletadosSemFiltroEmBranco() {
        when(repository.findByDeletedFalse()).thenReturn(List.of(curso));
        assertEquals(List.of(curso), service.listar("  "));
    }

    @Test
    void deveFiltrarPorInicioDoNome() {
        when(repository.findByNomeStartingWithIgnoreCaseAndDeletedFalse("ja")).thenReturn(List.of(curso));
        assertEquals(List.of(curso), service.listar("ja"));
        verify(repository).findByNomeStartingWithIgnoreCaseAndDeletedFalse("ja");
    }

    @Test
    void deveCriarComoNaoDeletadoESemIdFornecido() {
        curso.setDeleted(true);
        when(repository.save(curso)).thenReturn(curso);
        Curso salvo = service.criar(curso);
        assertSame(curso, salvo);
        assertNull(salvo.getId());
        assertFalse(salvo.isDeleted());
    }

    @Test
    void deveRealizarDelecaoLogica() {
        when(repository.findById(1L)).thenReturn(Optional.of(curso));
        service.deletar(1L);
        assertTrue(curso.isDeleted());
        verify(repository).save(curso);
        verify(repository, never()).delete(any());
    }

    @Test
    void deveRetornar404QuandoCursoNaoExiste() {
        when(repository.findById(99L)).thenReturn(Optional.empty());
        ResponseStatusException erro = assertThrows(ResponseStatusException.class, () -> service.deletar(99L));
        assertEquals(404, erro.getStatusCode().value());
        verify(repository, never()).save(any());
    }
}

