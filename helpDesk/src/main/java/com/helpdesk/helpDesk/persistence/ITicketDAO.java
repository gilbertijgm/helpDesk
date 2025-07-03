package com.helpdesk.helpDesk.persistence;

import com.helpdesk.helpDesk.entities.Ticket;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface ITicketDAO {

    //metodo post para guardar
    Ticket crearTicket(Ticket ticket);

    //metodo post para actualizr
    Ticket actualizarTicket(Ticket ticket);

    //metodo patch para actualizar parcialmente un recurso, en este caso solo el estado
    Ticket actualizarEstado(Ticket ticket);

    //metodo patch para asignar el ticket a un agente
    Ticket asignarTicketA(Ticket ticket);

    //metodo get para listar todas las tareas
    List<Ticket> listadoTicket();

    //metodo get para buscar por id
    Optional<Ticket> ticketPorId(Long id);

    //metodo get para listar todas las tareas, implementando paginacion y filtrado dinamico
    Page<Ticket> tickets(Pageable pageable);
    //String palabraClave, String estado, String prioridad, LocalDate fecha,
}
