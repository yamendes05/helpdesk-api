package com.example.helpdesk;

import com.example.helpdesk.domain.Ticket;
import com.example.helpdesk.domain.TicketPriority;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import static com.example.helpdesk.domain.TicketPriority.LOW;

@SpringBootApplication
public class HelpdeskApplication {

	public static void main(String[] args) {
		SpringApplication.run(HelpdeskApplication.class, args);


	}
}