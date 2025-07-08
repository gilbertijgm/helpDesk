package com.helpdesk.helpDesk.repository;

import com.helpdesk.helpDesk.entities.Ticket;
import org.springframework.cglib.core.Local;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDate;

public interface TicketRepositoryCustom {

    // Implementamos la firma del método que permite filtrar tareas según múltiples criterios opcionales incluyendo la paginacion
    Page<Ticket> buscarTicketsConFiltros(
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
}
