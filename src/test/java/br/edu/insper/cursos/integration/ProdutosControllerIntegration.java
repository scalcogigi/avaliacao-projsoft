package br.edu.insper.produtos.integration;

import br.edu.insper.produtos.entity.Produtos;
import br.edu.insper.produtos.repository.ProdutosRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class ProdutosControllerIntegrationTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ProdutosRepository repository;

    @BeforeEach
    void limparBanco() {
        repository.deleteAll();
    }

    @Test // teste para a criação de novos produtos
    void getDeveFiltrarEEsconderDeletados() throws Exception {
        salvar("Java", false);
        salvar("Javascript", false);
        salvar("Java antigo", true);
        salvar("Python", false);

        mockMvc.perform(get("/produtos").param("nome", "java"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].deleted").value(false))
                .andExpect(jsonPath("$[1].deleted").value(false));
    }

    @Test
    void postDeveCriarProdutos() throws Exception {
        String json = """
                {"nome":"Java","descricao":"Spring Boot","cargaHoraria":20}
                """;

        mockMvc.perform(post("/produtos")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").isNumber())
                .andExpect(jsonPath("$.nome").value("Java"))
                .andExpect(jsonPath("$.deleted").value(false));
    }

    @Test
    void deleteDeveMarcarProdutoComoDeletado() throws Exception {
        Produtos produtos = salvar("Java", false);
        mockMvc.perform(delete("/produtos/{id}", produtos.getId()))
                .andExpect(status().isNoContent());
        assert repository.findById(produtos.getId()).orElseThrow().isDeleted();
    }

    private Produtos salvar(String nome, boolean deleted) {
        Produtos produtos = new Produtos();
        produtos.setNome(nome);
        produtos.setDescricao("Descrição");
        // produtos.setPreco(preco);
        // produtos.setQuantidade(quantidade);
        produtos.setDeleted(deleted);
        return repository.save(produtos);
    }
}

