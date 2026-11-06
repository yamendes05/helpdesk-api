package com.example.helpdesk.service;

import com.example.helpdesk.domain.Ticket;
import com.example.helpdesk.domain.TicketPriority;
import com.example.helpdesk.exception.TicketNotFoundException;
import com.example.helpdesk.repository.TicketRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TicketService {

    private final TicketRepository ticketRepository;

    public TicketService(TicketRepository ticketRepository) {
        this.ticketRepository = ticketRepository;
    }

    public Ticket criarTicket(
            String title,
            String description,
            String category,
            TicketPriority priority) {

        Ticket ticket = new Ticket(title, description, category, priority);

        return ticketRepository.save(ticket);
    }
    public Ticket buscarId(Long id) {
        return ticketRepository.findById(id)
                .orElseThrow(() -> new TicketNotFoundException(
                        "Ticket não encontrado com o id: " + id
                ));
    }
    public List<Ticket> listarTickets() {

        return ticketRepository.findAll();

    }

}