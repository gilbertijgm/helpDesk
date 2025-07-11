package com.helpdesk.helpDesk.service.mappers;

import com.helpdesk.helpDesk.controller.dto.comentario.ComentarioResponseDTO;
import com.helpdesk.helpDesk.controller.dto.comentario.ComentarioCreateDTO;
import com.helpdesk.helpDesk.entities.Comentario;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import java.util.List;

@Mapper(componentModel = "spring")
public interface ComentarioMapper {

    // Convertir DTO de creación a entidad
    Comentario toEntity(ComentarioCreateDTO dto);

    // Convertir entidad a respuesta
    @Mapping(target = "nombreAutor", expression = "java(comentario.getAutor() != null ? comentario.getAutor().getUsername() : null)")
    ComentarioResponseDTO toResponse(Comentario comentario);


    // Convertir lista de tickets a lista de respuestas
    List<ComentarioResponseDTO> toResponseList(List<Comentario> comentarios);

    @Mapping(target = "idComentario", ignore = true) //evita sobreescritura del ID  u otros campos sensibles
    void uddateEntityFromDto(ComentarioCreateDTO comentarioCreateDTO, @MappingTarget Comentario entity);
}
