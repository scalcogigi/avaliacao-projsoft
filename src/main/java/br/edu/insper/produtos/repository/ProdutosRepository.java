package br.edu.insper.produtos.repository;

import br.edu.insper.produtos.entity.Produtos;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProdutosRepository extends JpaRepository<Produtos, Long> {
    List<Produtos> findByDeletedFalse();
    List<Produtos> findByNomeStartingWithIgnoreCaseAndDeletedFalse(String nome);
}

