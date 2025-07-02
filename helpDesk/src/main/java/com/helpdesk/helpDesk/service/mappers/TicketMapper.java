package com.helpdesk.helpDesk.service.mappers;

import com.helpdesk.helpDesk.controller.dto.categoria.CategoriaDTO;
import com.helpdesk.helpDesk.controller.dto.ticket.TicketCreateDTO;
import com.helpdesk.helpDesk.controller.dto.ticket.TicketResponse;
import com.helpdesk.helpDesk.entities.Categoria;
import com.helpdesk.helpDesk.entities.Ticket;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface TicketMapper {

    // Método para convertir entidad a DTO
    TicketCreateDTO toDto(Ticket ticket);

    // Método para convertir DTO a entidad
    Ticket toEntity(TicketResponse ticketResponse);

    // También podés mapear listas automáticamente
    List<TicketCreateDTO> toDtoList(List<Ticket> ticket);
    List<Ticket> toEntityList(List<TicketResponse> dtos);
}
