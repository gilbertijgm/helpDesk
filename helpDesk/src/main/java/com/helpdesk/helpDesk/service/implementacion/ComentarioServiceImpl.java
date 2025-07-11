package com.helpdesk.helpDesk.service.implementacion;

import com.helpdesk.helpDesk.controller.dto.comentario.ComentarioResponseDTO;
import com.helpdesk.helpDesk.controller.dto.comentario.ComentarioCreateDTO;
import com.helpdesk.helpDesk.entities.Comentario;
import com.helpdesk.helpDesk.entities.Ticket;
import com.helpdesk.helpDesk.entities.Usuario;
import com.helpdesk.helpDesk.exceptions.ResourceNotFoundException;
import com.helpdesk.helpDesk.persistence.IComentarioDAO;
import com.helpdesk.helpDesk.persistence.ITicketDAO;
import com.helpdesk.helpDesk.repository.UsuarioRepository;
import com.helpdesk.helpDesk.service.IComentarioService;
import com.helpdesk.helpDesk.service.mappers.ComentarioMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ComentarioServiceImpl implements IComentarioService {

    private final IComentarioDAO comentarioDAO;
    private final UsuarioRepository usuarioRepository;
    private final ITicketDAO ticketDAO;
    private final ComentarioMapper comentarioMapper;

    @Override
    public ComentarioResponseDTO crearComentario(ComentarioCreateDTO comentarioCreateDTO, Long idTicket) {
        //obtenemos el usuario autenticado
        Usuario usuario = getUsuarioAutenticado();

        //obtenemos el ticket
        Ticket ticket = ticketDAO.ticketPorId(idTicket)
                .orElseThrow(() -> new ResourceNotFoundException("Ticket no encontrado con el id: "+idTicket));

        //obtenemos la fecha
        LocalDateTime hoy = LocalDateTime.now();

        //Mapeamos el DTO a entidad
        Comentario comentario = comentarioMapper.toEntity(comentarioCreateDTO);

        //completamos campos que no vienen del dto
        comentario.setFecha(hoy);
        comentario.setTicket(ticket);
        comentario.setAutor(usuario);

        //guardamos en la bd
        comentario = comentarioDAO.crearComentario(comentario);

        return comentarioMapper.toResponse(comentario);
    }

    @Override
    public ComentarioResponseDTO modificarComentario(ComentarioCreateDTO comentarioCreateDTO, Long idComentario) {
        //buscamos el comentario a modificar
        Comentario comentario = comentarioDAO.comentarioById(idComentario)
                .orElseThrow(() -> new ResourceNotFoundException("Comentario no encotrado con el id: "+ idComentario));

        comentarioMapper.uddateEntityFromDto(comentarioCreateDTO, comentario);

        Comentario comen = comentarioDAO.modificarComentario(comentario);

        return comentarioMapper.toResponse(comen);
    }

    @Override
    public ComentarioResponseDTO comentarioById(Long id) {
        Comentario comentario = comentarioDAO.comentarioById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Comentario no encontrado con el id: "+id));



        ComentarioResponseDTO comen = comentarioMapper.toResponse(comentario);

        return comen;
    }

    @Override
    public List<ComentarioResponseDTO> listarComentarios() {
        return List.of();
    }

    @Override
    public void eliminarComentario(Long id) {
        comentarioDAO.eliminarComentario(id);
    }

    //Metodo para obetener del contexto el usuario logueado
    public Usuario getUsuarioAutenticado() {
        //En el backend, obtenés al usuario autenticado desde el contexto de Spring Security, así:
        String username = SecurityContextHolder.getContext().getAuthentication().getName();


        return usuarioRepository.findUserEntityByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("Usuario no encontrado"));
    }
}
