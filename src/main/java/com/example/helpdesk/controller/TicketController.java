package com.example.helpdesk.controller;

import com.example.helpdesk.dtos.TicketRequest;
import com.example.helpdesk.dtos.TicketResponse;
import com.example.helpdesk.service.TicketService;

import jakarta.validation.Valid;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/tickets")
public class TicketController {

    private final TicketService service;

    // Injeção de dependência
    public TicketController(TicketService service) {
        this.service = service;
    }

    // Buscar ticket por ID
    @GetMapping("/{id}")
    public TicketResponse buscar(@PathVariable Long id) {

        return TicketResponse.de(
                service.buscarId(id)
        );
    }

    // Listar todos os tickets
    @GetMapping
    public List<TicketResponse> listar() {

        return service.listarTickets()
                .stream()
                .map(TicketResponse::de)
                .toList();
    }

    // Criar novo ticket
    @PostMapping
    public ResponseEntity<TicketResponse> criar(
            @Valid @RequestBody TicketRequest request) {

        var ticket = service.criarTicket(
                request.title(),
                request.description(),
                request.category(),
                request.priority()
        );

        TicketResponse criado = TicketResponse.de(ticket);

        URI uri = URI.create("/tickets/" + criado.id());

        return ResponseEntity.created(uri).body(criado);
    }
}