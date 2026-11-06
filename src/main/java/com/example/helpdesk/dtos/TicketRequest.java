package com.example.helpdesk.dtos;

import com.example.helpdesk.domain.TicketPriority;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
public record TicketRequest(
        @NotBlank(message = "O título é obrigatório")
        String title,

        @NotBlank(message = "A descrição é obrigatória")
        String description,

        @NotBlank(message = "A categoria é obrigatória")
        String category,

        @NotNull(message = "A prioridade é obrigatória")
        TicketPriority priority
) {
}

