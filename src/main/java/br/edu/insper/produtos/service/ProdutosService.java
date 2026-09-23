package br.edu.insper.produtos.service;

import br.edu.insper.produtos.entity.Produtos;
import br.edu.insper.produtos.repository.ProdutosRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
public class ProdutosService {
    private final ProdutosRepository repository;

    public ProdutosService(ProdutosRepository repository) {
        this.repository = repository;
    }

    public List<Produtos> listar(String nome) {
        if (nome == null || nome.isBlank()) {
            return repository.findByDeletedFalse();
        }
        return repository.findByNomeStartingWithIgnoreCaseAndDeletedFalse(nome);
    }

    public Produtos criar(Produtos produtos) {
        produtos.setId(null);
        produtos.setDeleted(false);
        return repository.save(produtos);
    }

    public void deletar(Long id) {
        Produtos produtos = repository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Produto não encontrado"));
        produtos.setDeleted(true);
        repository.save(produtos);
    }
}

