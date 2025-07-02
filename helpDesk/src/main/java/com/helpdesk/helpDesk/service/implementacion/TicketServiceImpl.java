package com.helpdesk.helpDesk.service.implementacion;

import com.helpdesk.helpDesk.controller.dto.ticket.TicketCreateDTO;
import com.helpdesk.helpDesk.controller.dto.ticket.TicketResponse;
import com.helpdesk.helpDesk.entities.Categoria;
import com.helpdesk.helpDesk.entities.Ticket;
import com.helpdesk.helpDesk.entities.Usuario;
import com.helpdesk.helpDesk.entities.enums.Estado;
import com.helpdesk.helpDesk.persistence.ICategoriaDAO;
import com.helpdesk.helpDesk.persistence.ITicketDAO;
import com.helpdesk.helpDesk.repository.CategoriaRepository;
import com.helpdesk.helpDesk.repository.UsuarioRepository;
import com.helpdesk.helpDesk.service.ITicketService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class TicketServiceImpl implements ITicketService {

    private final UsuarioRepository usuarioRepository;
    private final CategoriaRepository categoriaRepository;
    private final ITicketDAO ticketDAO;
    private final ICategoriaDAO categoriaDAO;

    @Override
    public TicketResponse crearTicket(TicketCreateDTO ticketDTO) {
        Usuario usuario = getUsuarioAutenticado();

            Categoria categoria = categoriaDAO.categoriaById(ticketDTO.getIdCategoria())
                    .orElseThrow(() -> new UsernameNotFoundException("Categoria no encontrado"));


        LocalDateTime hoy = LocalDateTime.now();

        Ticket ticket = Ticket.builder()
                .titulo(ticketDTO.getTitulo())
                .descripcion(ticketDTO.getDescripcion())
                .estado(Estado.ABIERTO)
                .prioridad(ticketDTO.getPrioridad())
                .fechaCreacion(hoy)
                .creadoPor(usuario)
                .categoria(categoria)
                .build();

        ticket = ticketDAO.crearTicket(ticket);

        return TicketResponse.builder()
                .idTicket(ticket.getIdTicket())
                .titulo(ticket.getTitulo())
                .descripcion(ticket.getDescripcion())
                .estado(ticket.getEstado())
                .prioridad(ticket.getPrioridad())
                .fechaCreacion(ticket.getFechaCreacion())
                .nombreUsuario(ticket.getCreadoPor() != null ? ticket.getCreadoPor().getUsername() : null)
                .nombreCategoria(ticket.getCategoria() != null ? ticket.getCategoria().getNombreCategoria() : null)
                .build();
    }

    @Override
    public TicketCreateDTO actualizarTicket(TicketCreateDTO ticket) {
        return null;
    }

    @Override
    public TicketCreateDTO actualizarEstado(TicketCreateDTO ticket) {
        return null;
    }

    @Override
    public TicketCreateDTO asignarTicketA(TicketCreateDTO ticket) {
        return null;
    }

    @Override
    public List<TicketCreateDTO> listadoTicket() {
        return List.of();
    }

    @Override
    public TicketCreateDTO ticketPorId(Long id) {
        return null;
    }

    @Override
    public Page<TicketCreateDTO> tickets(String palabraClave, String estado, String prioridad, LocalDate fecha, Pageable pageable) {
        return null;
    }

    //Metodo para obetener del contexto el usuario logueado
    public Usuario getUsuarioAutenticado() {
        //En el backend, obtenés al usuario autenticado desde el contexto de Spring Security, así:
        String username = SecurityContextHolder.getContext().getAuthentication().getName();


        return usuarioRepository.findUserEntityByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("Usuario no encontrado"));
    }

}
