package com.example.helpdesk.dtos;

import com.example.helpdesk.domain.Ticket;
import com.example.helpdesk.domain.TicketPriority;
import com.example.helpdesk.domain.TicketStatus;

import java.time.LocalDateTime;

public record TicketResponse(
        Long id,
        String title,
        String description,
        String category,
        TicketPriority priority,
        TicketStatus status,
        LocalDateTime createdAt
) {
    public static TicketResponse de(Ticket ticket) {
        return new TicketResponse(
                ticket.getId(),
                ticket.getTitle(),
                ticket.getDescription(),
                ticket.getCategory(),
                ticket.getPriority(),
                ticket.getStatus(),
                ticket.getCreatedAt()
        );
    }

}
