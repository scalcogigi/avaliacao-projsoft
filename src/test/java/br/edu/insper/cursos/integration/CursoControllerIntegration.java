package br.edu.insper.cursos.integration;

import br.edu.insper.cursos.entity.Curso;
import br.edu.insper.cursos.repository.CursoRepository;
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
class CursoControllerIntegrationTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private CursoRepository repository;

    @BeforeEach
    void limparBanco() {
        repository.deleteAll();
    }

    @Test
    void getDeveFiltrarEEsconderDeletados() throws Exception {
        salvar("Java", false);
        salvar("Javascript", false);
        salvar("Java antigo", true);
        salvar("Python", false);

        mockMvc.perform(get("/cursos").param("nome", "java"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].deleted").value(false))
                .andExpect(jsonPath("$[1].deleted").value(false));
    }

    @Test
    void postDeveCriarCurso() throws Exception {
        String json = """
                {"nome":"Java","descricao":"Spring Boot","cargaHoraria":20}
                """;

        mockMvc.perform(post("/cursos")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").isNumber())
                .andExpect(jsonPath("$.nome").value("Java"))
                .andExpect(jsonPath("$.deleted").value(false));
    }

    @Test
    void deleteDeveMarcarCursoComoDeletado() throws Exception {
        Curso curso = salvar("Java", false);
        mockMvc.perform(delete("/cursos/{id}", curso.getId()))
                .andExpect(status().isNoContent());
        assert repository.findById(curso.getId()).orElseThrow().isDeleted();
    }

    private Curso salvar(String nome, boolean deleted) {
        Curso curso = new Curso();
        curso.setNome(nome);
        curso.setDescricao("Descrição");
        curso.setCargaHoraria(20);
        curso.setDeleted(deleted);
        return repository.save(curso);
    }
}

