package com.helpdesk.helpDesk.service;

import com.helpdesk.helpDesk.controller.dto.ticket.AsignarTecnicoDTO;
import com.helpdesk.helpDesk.controller.dto.ticket.TicketCreateDTO;
import com.helpdesk.helpDesk.controller.dto.ticket.TicketDTO;
import com.helpdesk.helpDesk.controller.dto.ticket.TicketResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDate;
import java.util.List;

public interface ITicketService {
    //metodo post para guardar
    TicketResponse crearTicket(TicketCreateDTO ticket);

    //metodo post para actualizr
    TicketResponse actualizarTicket(TicketCreateDTO ticket, Long idTecnico);

    //metodo patch para actualizar parcialmente un recurso, en este caso solo el estado
    TicketResponse actualizarEstado(Long idTicket, String nuevoEstado);

    //metodo patch para asignar el ticket a un agente
    TicketResponse asignarTicketA(Long idTicket, AsignarTecnicoDTO asignarTecnicoDTO);

    //metodo get para listar todas las tareas
    List<TicketResponse> listadoTicket();

    //metodo get para buscar por id
    TicketDTO ticketPorId(Long id);

    //metodo get para listar todas las tareas, implementando paginacion y filtrado dinamico
    Page<TicketResponse> tickets(
            Pageable pageable,
            String palabraClave,
            String estado,
            String prioridad,
            LocalDate fechaInicio,
            LocalDate fechaFin,
            Long idCreador,
            Long idTecnico,
            Long idCategoria
    );
    //String palabraClave, String estado, String prioridad, LocalDate fecha,
}