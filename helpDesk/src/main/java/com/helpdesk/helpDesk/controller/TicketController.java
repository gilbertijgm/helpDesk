package com.helpdesk.helpDesk.controller;


import com.helpdesk.helpDesk.controller.dto.ticket.TicketCreateDTO;
import com.helpdesk.helpDesk.controller.dto.ticket.TicketResponse;
import com.helpdesk.helpDesk.response.ApiResponse;
import com.helpdesk.helpDesk.service.ITicketService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;
import java.net.URISyntaxException;

@RestController
@RequestMapping("/api/ticket")
@RequiredArgsConstructor
public class TicketController {

    private final ITicketService ticketService;

    @PostMapping("/crear")
    public ResponseEntity<?> guardar(@RequestBody TicketCreateDTO ticketDTO ) throws URISyntaxException {

        TicketResponse ticket = ticketService.crearTicket(ticketDTO);

        ApiResponse<TicketResponse> response = new ApiResponse<>(201, "Ticket Creado con Exito" , ticket);

        return ResponseEntity.created(new URI("/api/task/" + ticketDTO.getIdTicket())).body(response);
    }
}
