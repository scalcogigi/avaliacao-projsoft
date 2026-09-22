package br.edu.insper.cursos.repository;

import br.edu.insper.cursos.entity.Curso;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CursoRepository extends JpaRepository<Curso, Long> {
    List<Curso> findByDeletedFalse();
    List<Curso> findByNomeStartingWithIgnoreCaseAndDeletedFalse(String nome);
}

