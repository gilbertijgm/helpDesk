package com.helpdesk.helpDesk.service.mappers;

import com.helpdesk.helpDesk.controller.dto.categoria.CategoriaDTO;
import com.helpdesk.helpDesk.entities.Categoria;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface CategoriaMapper {

    // Método para convertir entidad a DTO
    CategoriaDTO toDto(Categoria categoria);

    // Método para convertir DTO a entidad
    Categoria toEntity(CategoriaDTO dto);

    // También podés mapear listas automáticamente
    List<CategoriaDTO> toDtoList(List<Categoria> tasks);
    List<Categoria> toEntityList(List<CategoriaDTO> dtos);
}
