package com.helpdesk.helpDesk.service;

import com.helpdesk.helpDesk.controller.dto.ticket.TicketCreateDTO;
import com.helpdesk.helpDesk.controller.dto.ticket.TicketResponse;
import com.helpdesk.helpDesk.entities.Ticket;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface ITicketService {
    //metodo post para guardar
    TicketResponse crearTicket(TicketCreateDTO ticket);

    //metodo post para actualizr
    TicketCreateDTO actualizarTicket(TicketCreateDTO ticket);

    //metodo patch para actualizar parcialmente un recurso, en este caso solo el estado
    TicketCreateDTO actualizarEstado(TicketCreateDTO ticket);

    //metodo patch para asignar el ticket a un agente
    TicketCreateDTO asignarTicketA(TicketCreateDTO ticket);

    //metodo get para listar todas las tareas
    List<TicketCreateDTO> listadoTicket();

    //metodo get para buscar por id
    TicketCreateDTO ticketPorId(Long id);

    //metodo get para listar todas las tareas, implementando paginacion y filtrado dinamico
    Page<TicketCreateDTO> tickets(String palabraClave, String estado, String prioridad, LocalDate fecha, Pageable pageable);
}