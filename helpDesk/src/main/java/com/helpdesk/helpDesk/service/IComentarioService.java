package com.helpdesk.helpDesk.service;

import com.helpdesk.helpDesk.controller.dto.comentario.ComentarioResponseDTO;
import com.helpdesk.helpDesk.controller.dto.comentario.ComentarioCreateDTO;

import java.util.List;

public interface IComentarioService {

    ComentarioResponseDTO crearComentario(ComentarioCreateDTO comentarioCreateDTO, Long idTicket);

    ComentarioResponseDTO modificarComentario(ComentarioCreateDTO comentarioCreateDTO, Long idComentario);

    ComentarioResponseDTO comentarioById(Long id);

    List<ComentarioResponseDTO> listarComentarios();

    void eliminarComentario(Long id);

}
