package br.edu.insper.produtos.controller;

import br.edu.insper.produtos.entity.Curso;
import br.edu.insper.produtos.service.ProdutosService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/produtos")
public class ProdutosController {
    private final ProdutosService service;

    public ProdutosController(ProdutosService service) {
        this.service = service;
    }

    @GetMapping
    public List<Produtos> listar(@RequestParam(required = false) String nome) {
        return service.listar(nome);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Produtos criar(@Valid @RequestBody Produtos produtos) {
        return service.criar(produtos);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deletar(@PathVariable Long id) {
        service.deletar(id);
    }
}

