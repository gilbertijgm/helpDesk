package com.helpdesk.helpDesk.service.mappers;

import com.helpdesk.helpDesk.controller.dto.ticket.TicketCreateDTO;
import com.helpdesk.helpDesk.controller.dto.ticket.TicketDTO;
import com.helpdesk.helpDesk.controller.dto.ticket.TicketResponse;
import com.helpdesk.helpDesk.entities.Ticket;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import java.util.List;
//
@Mapper(componentModel = "spring", uses = {ComentarioMapper.class})
public interface TicketMapper {

    // Convertir DTO de creación a entidad
    Ticket toEntity(TicketCreateDTO dto);

    // Convertir entidad a respuesta
    @Mapping(target = "nombreUsuario", expression = "java(ticket.getCreadoPor() != null ? ticket.getCreadoPor().getUsername() : null)")
    @Mapping(target = "nombreCategoria", expression = "java(ticket.getCategoria() != null ? ticket.getCategoria().getNombreCategoria() : null)")
    @Mapping(target = "tecnicoAsignado", expression = "java(ticket.getAsignadoA() != null ? ticket.getAsignadoA().getUsername() : null)")
    @Mapping(target = "fechaResolucion", source = "fechaResolucion")
    TicketResponse toResponse(Ticket ticket);

    @Mapping(target = "nombreUsuario", expression = "java(ticket.getCreadoPor() != null ? ticket.getCreadoPor().getUsername() : null)")
    @Mapping(target = "nombreCategoria", expression = "java(ticket.getCategoria() != null ? ticket.getCategoria().getNombreCategoria() : null)")
    @Mapping(target = "tecnicoAsignado", expression = "java(ticket.getAsignadoA() != null ? ticket.getAsignadoA().getUsername() : null)")
    @Mapping(target = "fechaResolucion", source = "fechaResolucion")
    @Mapping(target = "comentarios", source = "comentarios")
    TicketDTO toDto(Ticket ticket);

    // Convertir lista de tickets a lista de respuestas
    List<TicketResponse> toResponseList(List<Ticket> tickets);

    @Mapping(target = "idTicket", ignore = true) //evita sobreescritura del ID  u otros campos sensibles
    void uddateEntityFromDto(TicketCreateDTO ticketCreateDTO, @MappingTarget Ticket entity);
}
