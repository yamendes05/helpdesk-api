package com.example.helpdesk.domain;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "ticket")
public class Ticket {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String description;

    @Column
    private String category;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private TicketPriority priority;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private TicketStatus status;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    // Construtor padrão obrigatório pelo JPA
    protected Ticket() {
    }

    // Construtor principal para criação de novos tickets
    public Ticket(String title, String description, String category, TicketPriority priority) {
        validarDadosObrigatorios(title, description);

        this.title = title;
        this.description = description;
        this.category = category;
        this.priority = priority;
        this.status = TicketStatus.OPEN;
        this.createdAt = LocalDateTime.now();
    }

    // Regras de Negócio / Transições de Estado
    public void iniciarAtendimento() {
        if (this.status != TicketStatus.OPEN) {
            throw new IllegalStateException("Só é possível iniciar um ticket aberto");
        }
        this.status = TicketStatus.IN_PROGRESS;
    }

    public void resolver() {
        if (this.status != TicketStatus.IN_PROGRESS) {
            throw new IllegalStateException("Só é possível resolver um atendimento em andamento");
        }
        this.status = TicketStatus.RESOLVED;
    }

    public void fechado() {
        if (this.status != TicketStatus.RESOLVED) {
            throw new IllegalStateException("Só é possível fechar um atendimento que está resolvido");
        }
        this.status = TicketStatus.CLOSED;
    }

    public void aguardarUsuario() {
        if (this.status != TicketStatus.IN_PROGRESS) {
            throw new IllegalStateException("Só é possível aguardar usuário se o ticket estiver em andamento");
        }
        this.status = TicketStatus.WAITING_FOR_USER;
    }

    public void retomarAtendimento() {
        if (this.status != TicketStatus.WAITING_FOR_USER) {
            throw new IllegalStateException("Só é possível retomar o ticket se ele estiver aguardando usuário");
        }
        this.status = TicketStatus.IN_PROGRESS;
    }

    private void validarDadosObrigatorios(String title, String description) {
        if (title == null || title.isBlank()) {
            throw new IllegalArgumentException("Título é obrigatório");
        }
        if (description == null || description.isBlank()) {
            throw new IllegalArgumentException("Descrição é obrigatória");
        }
    }

    // Getters e Setters
    public Long getId() {
        return id;
    }

    private void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        validarDadosObrigatorios(title, this.description);
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        validarDadosObrigatorios(this.title, description);
        this.description = description;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public TicketPriority getPriority() {
        return priority;
    }

    public void setPriority(TicketPriority priority) {
        this.priority = priority;
    }

    public TicketStatus getStatus() {
        return status;
    }

    private void setStatus(TicketStatus status) {
        this.status = status;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    private void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    @Override
    public String toString() {
        return "Ticket{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", description='" + description + '\'' +
                ", category='" + category + '\'' +
                ", priority=" + priority +
                ", status=" + status +
                ", createdAt=" + createdAt +
                '}';
    }
}
