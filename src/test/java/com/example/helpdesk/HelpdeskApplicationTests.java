package com.example.helpdesk;

import com.example.helpdesk.domain.Ticket;
import com.example.helpdesk.domain.TicketPriority;
import com.example.helpdesk.domain.TicketStatus;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
class HelpdeskApplicationTests {

	private Ticket ticketAberto() {
		return new Ticket(
				"Erro ao logar",
				"Não consigo acessar minha conta",
				"Acesso",
				TicketPriority.MEDIUM
		);
	}

	private Ticket ticketEmAtendimento() {
		Ticket ticket = ticketAberto();
		ticket.iniciarAtendimento();
		return ticket;
	}

	private Ticket ticketResolvido() {
		Ticket ticket = ticketEmAtendimento();
		ticket.resolver();
		return ticket;
	}

	private Ticket ticketAguardandoUsuario() {
		Ticket ticket = ticketEmAtendimento();
		ticket.aguardarUsuario();
		return ticket;
	}

	private Ticket ticketFechado() {
		Ticket ticket = ticketResolvido();
		ticket.fechado();
		return ticket;
	}

	@Test
	@DisplayName("OPEN -> IN_PROGRESS ao iniciar atendimento")
	void deveIniciarAtendimentoQuandoOpen() {
		Ticket ticket = ticketAberto();

		ticket.iniciarAtendimento();

		assertEquals(TicketStatus.IN_PROGRESS, ticket.getStatus());
	}

	@Test
	@DisplayName("IN_PROGRESS -> RESOLVED ao resolver")
	void deveResolverQuandoInProgress() {
		Ticket ticket = ticketEmAtendimento();

		ticket.resolver();

		assertEquals(TicketStatus.RESOLVED, ticket.getStatus());
	}

	@Test
	@DisplayName("RESOLVED -> CLOSED ao fechar")
	void deveFecharQuandoResolved() {
		Ticket ticket = ticketResolvido();

		ticket.fechado();

		assertEquals(TicketStatus.CLOSED, ticket.getStatus());
	}

	@Test
	@DisplayName("WAITING_FOR_USER -> IN_PROGRESS ao retomar")
	void deveRetomarQuandoWaitingForUser() {
		Ticket ticket = ticketAguardandoUsuario();

		ticket.retomarAtendimento();

		assertEquals(TicketStatus.IN_PROGRESS, ticket.getStatus());
	}

	@Test
	@DisplayName("CLOSED não permite iniciar atendimento")
	void naoDeveIniciarAtendimentoQuandoClosed() {
		Ticket ticket = ticketFechado();

		IllegalStateException ex = assertThrows(
				IllegalStateException.class,
				ticket::iniciarAtendimento
		);

		assertEquals("Só é possível iniciar um ticket aberto", ex.getMessage());
		assertEquals(TicketStatus.CLOSED, ticket.getStatus());
	}

}
