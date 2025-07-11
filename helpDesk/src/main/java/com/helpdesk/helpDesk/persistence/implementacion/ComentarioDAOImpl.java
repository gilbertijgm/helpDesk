package com.helpdesk.helpDesk.persistence.implementacion;

import com.helpdesk.helpDesk.entities.Comentario;
import com.helpdesk.helpDesk.persistence.IComentarioDAO;
import com.helpdesk.helpDesk.repository.ComentarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class ComentarioDAOImpl implements IComentarioDAO {

    private final ComentarioRepository comentarioRepository;

    @Override
    public Comentario crearComentario(Comentario comentario) {
        Comentario comen = comentarioRepository.save(comentario);

        return comen;
    }

    @Override
    public Comentario modificarComentario(Comentario comentario) {
        Comentario comen = comentarioRepository.save(comentario);

        return comen;
    }

    @Override
    public Optional<Comentario> comentarioById(Long id) {
        Optional<Comentario> comentario = comentarioRepository.findById(id);

        return comentario;
    }

    @Override
    public List<Comentario> listarComentarios() {
        List<Comentario> lista = comentarioRepository.findAll();

        return lista;
    }

    @Override
    public void eliminarComentario(Long id) {
        comentarioRepository.deleteById(id);
    }
}
