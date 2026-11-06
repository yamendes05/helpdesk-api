package com.example.helpdesk;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;

import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
class TicketControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    private static final String TICKET_JSON = """
            {
              "title": "Erro de login",
              "description": "Não consigo acessar minha conta",
              "category": "Acesso",
              "priority": "HIGH"
            }
            """;

    @Test
    @DisplayName("Cria ticket e retorna os dados com HTTP 201")
    void deveCriarTicket() throws Exception {
        mockMvc.perform(post("/tickets")
                        .contentType("application/json")
                        .content(TICKET_JSON))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.title").value("Erro de login"))
                .andExpect(jsonPath("$.priority").value("HIGH"))
                .andExpect(jsonPath("$.status").value("OPEN"));
    }

    @Test
    @DisplayName("Lista tickets com HTTP 200")
    void deveListarTickets() throws Exception {
        mockMvc.perform(post("/tickets")
                        .contentType("application/json")
                        .content(TICKET_JSON))
                .andExpect(status().isCreated());

        mockMvc.perform(get("/tickets"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].title").value("Erro de login"));
    }

    @Test
    @DisplayName("Busca ticket pelo ID com HTTP 200")
    void deveBuscarTicketPorId() throws Exception {
        String location = mockMvc.perform(post("/tickets")
                        .contentType("application/json")
                        .content(TICKET_JSON))
                .andExpect(status().isCreated())
                .andReturn().getResponse().getHeader("Location");

        long id = Long.parseLong(location.substring(location.lastIndexOf('/') + 1));

        mockMvc.perform(get("/tickets/{id}", id))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(id))
                .andExpect(jsonPath("$.title").value("Erro de login"));
    }

    @Test
    @DisplayName("Retorna HTTP 404 quando o ticket não existe")
    void deveRetornar404ParaTicketInexistente() throws Exception {
        mockMvc.perform(get("/tickets/999999"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.mensagem")
                        .value("Ticket não encontrado com o id: 999999"))
                .andExpect(jsonPath("$.status").value(404));
    }

    @Test
    @DisplayName("Retorna HTTP 400 quando o título está vazio")
    void deveRetornar400ParaTicketInvalido() throws Exception {
        String json = """
                {
                  "title": "",
                  "description": "Não consigo acessar minha conta",
                  "category": "Acesso",
                  "priority": "HIGH"
                }
                """;

        mockMvc.perform(post("/tickets")
                        .contentType("application/json")
                        .content(json))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.erro").value("Erro de validação"))
                .andExpect(jsonPath("$.mensagens.title")
                        .value("O título é obrigatório"));
    }
}
