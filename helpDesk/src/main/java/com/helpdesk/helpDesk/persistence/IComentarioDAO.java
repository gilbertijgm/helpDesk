package com.helpdesk.helpDesk.persistence;

import com.helpdesk.helpDesk.entities.Comentario;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;

import java.util.List;
import java.util.Optional;

public interface IComentarioDAO {

    Comentario crearComentario(Comentario comentario);

    Comentario modificarComentario(Comentario comentario);
    


    Optional<Comentario> comentarioById(Long id);

    List<Comentario> listarComentarios();

    void eliminarComentario(Long id);

}
