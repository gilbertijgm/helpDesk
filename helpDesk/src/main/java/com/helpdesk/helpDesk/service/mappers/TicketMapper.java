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
@Mapper(componentModel = "spring", uses = {ComentarioMapper.class, CategoriaMapper.class, UsuarioMapper.class})
public interface TicketMapper {

    // Convertir DTO de creación a entidad
    Ticket toEntity(TicketCreateDTO dto);

    // Convertir entidad a respuesta
    @Mapping(source = "creadoPor", target = "nombreUsuario")
    @Mapping(source = "asignadoA", target = "tecnicoAsignado")
    @Mapping(source = "categoria", target = "nombreCategoria")
    @Mapping(target = "fechaResolucion", source = "fechaResolucion")
    TicketResponse toResponse(Ticket ticket);

    // Convertir entidad a DTO completo (usado para detalle)
    @Mapping(source = "creadoPor", target = "nombreUsuario")
    @Mapping(source = "asignadoA", target = "tecnicoAsignado")
    @Mapping(source = "categoria", target = "nombreCategoria")
    TicketDTO toDto(Ticket ticket);

    // Convertir lista de tickets a lista de respuestas
    List<TicketResponse> toResponseList(List<Ticket> tickets);

    @Mapping(target = "idTicket", ignore = true) //evita sobreescritura del ID  u otros campos sensibles
    void uddateEntityFromDto(TicketCreateDTO ticketCreateDTO, @MappingTarget Ticket entity);
}
