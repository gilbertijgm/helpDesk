package com.helpdesk.helpDesk.config;

import com.helpdesk.helpDesk.entities.Ticket;
import com.helpdesk.helpDesk.entities.Usuario;
import com.helpdesk.helpDesk.entities.enums.Rol;
import com.helpdesk.helpDesk.persistence.ITicketDAO;
import com.helpdesk.helpDesk.repository.UsuarioRepository;
import com.helpdesk.helpDesk.service.ITicketService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component("ticketSecurity")
@RequiredArgsConstructor
public class TicketSecurity {

    private final UsuarioRepository usuarioRepository;
    private final ITicketDAO ticketDAO;

    //Metodo para obetener del contexto el usuario logueado
    public Usuario getUsuarioAutenticado() {
        //En el backend, obtenés al usuario autenticado desde el contexto de Spring Security, así:
        String username = SecurityContextHolder.getContext().getAuthentication().getName();

        System.out.println("Buscando usuario autenticado: " + username);

        return usuarioRepository.findUserEntityByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("Usuario no encontrado"));
    }

    public boolean puedeListarTickets() {
        Usuario u = getUsuarioAutenticado();
        return u.tieneRol(Rol.ADMIN) || u.tieneRol(Rol.CLIENTE) || u.tieneRol(Rol.TECNICO);
    }

    public boolean puedeVerTicket(Long ticketId) {
        Usuario usuario = getUsuarioAutenticado();

        Optional<Ticket> optional = ticketDAO.ticketPorId(ticketId);
        if (optional.isEmpty()) return false;

        Ticket ticket = optional.get();

        if (usuario.tieneRol(Rol.ADMIN)) return true;

        if (usuario.tieneRol(Rol.CLIENTE) &&
                ticket.getCreadoPor().getIdUsuario().equals(usuario.getIdUsuario())) return true;

        if (usuario.tieneRol(Rol.TECNICO) &&
                ticket.getAsignadoA() != null &&
                ticket.getAsignadoA().getIdUsuario().equals(usuario.getIdUsuario())) return true;

        return false;
    }

}
