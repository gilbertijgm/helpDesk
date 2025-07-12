package com.helpdesk.helpDesk.service.mappers;

import com.helpdesk.helpDesk.controller.dto.auth.UsuarioDTO;
import com.helpdesk.helpDesk.entities.Usuario;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UsuarioMapper {

    UsuarioDTO toDto(Usuario usuario);
}
