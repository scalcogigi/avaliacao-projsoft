package br.edu.insper.cursos.service;

import br.edu.insper.cursos.entity.Curso;
import br.edu.insper.cursos.repository.CursoRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
public class CursoService {
    private final CursoRepository repository;

    public CursoService(CursoRepository repository) {
        this.repository = repository;
    }

    public List<Curso> listar(String nome) {
        if (nome == null || nome.isBlank()) {
            return repository.findByDeletedFalse();
        }
        return repository.findByNomeStartingWithIgnoreCaseAndDeletedFalse(nome);
    }

    public Curso criar(Curso curso) {
        curso.setId(null);
        curso.setDeleted(false);
        return repository.save(curso);
    }

    public void deletar(Long id) {
        Curso curso = repository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Curso não encontrado"));
        curso.setDeleted(true);
        repository.save(curso);
    }
}

