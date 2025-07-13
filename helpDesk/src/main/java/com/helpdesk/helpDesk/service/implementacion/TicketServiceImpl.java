package com.helpdesk.helpDesk.service.implementacion;

import com.helpdesk.helpDesk.controller.dto.ticket.AsignarTecnicoDTO;
import com.helpdesk.helpDesk.controller.dto.ticket.TicketCreateDTO;
import com.helpdesk.helpDesk.controller.dto.ticket.TicketDTO;
import com.helpdesk.helpDesk.controller.dto.ticket.TicketResponse;
import com.helpdesk.helpDesk.entities.*;
import com.helpdesk.helpDesk.entities.enums.Estado;
import com.helpdesk.helpDesk.entities.enums.Rol;
import com.helpdesk.helpDesk.exceptions.ResourceNotFoundException;
import com.helpdesk.helpDesk.exceptions.ServiceUtils;
import com.helpdesk.helpDesk.exceptions.UnauthorizedException;
import com.helpdesk.helpDesk.persistence.ICategoriaDAO;
import com.helpdesk.helpDesk.persistence.ITicketDAO;
import com.helpdesk.helpDesk.repository.UsuarioRepository;
import com.helpdesk.helpDesk.exceptions.ForbiddenAccessException;
import com.helpdesk.helpDesk.service.ITicketService;
import com.helpdesk.helpDesk.service.mappers.TicketMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class TicketServiceImpl implements ITicketService {

    private final UsuarioRepository usuarioRepository;
    private final ITicketDAO ticketDAO;
    private final ICategoriaDAO categoriaDAO;
    private final TicketMapper ticketMapper;

//    @Override
//    public TicketResponse crearTicket(TicketCreateDTO ticketDTO) {
//        Usuario usuario = getUsuarioAutenticado();
//
//            Categoria categoria = categoriaDAO.categoriaById(ticketDTO.getIdCategoria())
//                    .orElseThrow(() -> new UsernameNotFoundException("Categoria no encontrado"));
//
//
//        LocalDateTime hoy = LocalDateTime.now();
//
//        Ticket ticket = Ticket.builder()
//                .titulo(ticketDTO.getTitulo())
//                .descripcion(ticketDTO.getDescripcion())
//                .estado(Estado.ABIERTO)
//                .prioridad(ticketDTO.getPrioridad())
//                .fechaCreacion(hoy)
//                .creadoPor(usuario)
//                .categoria(categoria)
//                .build();
//
//        ticket = ticketDAO.crearTicket(ticket);
//
//        return TicketResponse.builder()
//                .idTicket(ticket.getIdTicket())
//                .titulo(ticket.getTitulo())
//                .descripcion(ticket.getDescripcion())
//                .estado(ticket.getEstado())
//                .prioridad(ticket.getPrioridad())
//                .fechaCreacion(ticket.getFechaCreacion())
//                .nombreUsuario(ticket.getCreadoPor() != null ? ticket.getCreadoPor().getUsername() : null)
//                .nombreCategoria(ticket.getCategoria() != null ? ticket.getCategoria().getNombreCategoria() : null)
//                .build();
//    }

    @Override
    public TicketResponse crearTicket(TicketCreateDTO ticketDTO) {
        // Obtenemos el usuario autenticado
        Usuario usuario = getUsuarioAutenticado();

        // Buscamos la categoría
        Categoria categoria = categoriaDAO.categoriaById(ticketDTO.getIdCategoria())
                .orElseThrow(() -> new UsernameNotFoundException("Categoria no encontrado"));
        //obtenemos la fecha de hoy
        LocalDateTime hoy = LocalDateTime.now();

        // Usamos el mapper para convertir el DTO a entidad
        Ticket ticket = ticketMapper.toEntity(ticketDTO);

        // Completamos campos que no vienen en el DTO
        ticket.setEstado(Estado.ABIERTO);
        ticket.setFechaCreacion(hoy);
        ticket.setCreadoPor(usuario);
        ticket.setCategoria(categoria);

        //Guardamos en la base de datos
        ticket = ticketDAO.crearTicket(ticket);

        return ticketMapper.toResponse(ticket);

    }
    @Override
    public TicketResponse actualizarTicket(TicketCreateDTO ticketDTO, Long id) {
       //buscamos el ticket a modificar por id recibido
        Ticket ticket  = ticketDAO.ticketPorId(id)
                .orElseThrow(() -> new ResourceNotFoundException("Ticket no encontrado con el id: " + id));

        // Buscamos la categoría que se desea asignar
        Categoria categoria = categoriaDAO.categoriaById(ticketDTO.getIdCategoria())
                .orElseThrow(() -> new UsernameNotFoundException("Categoria no encontrado"));


        //Mapeamos el dto recibido a la entidad, usando el metodo creado en el mapper
        //Esta línea actualiza el objeto original sin crear uno nuevo.
        // Es el enfoque más limpio, eficiente y seguro cuando querés actualizar parcialmente una entidad sin pisar todo.
        ticketMapper.uddateEntityFromDto(ticketDTO, ticket);

        //guadarmos la entidad actualizada
        Ticket ticketUpdate = ticketDAO.actualizarTicket(ticket);

        return ticketMapper.toResponse(ticketUpdate);
    }

    @Override
    public TicketResponse actualizarEstado(Long idTicket, String nuevoEstado) {
        //buscamos el ticket a modificar por id recibido
        Ticket ticket  = ticketDAO.ticketPorId(idTicket)
                .orElseThrow(() -> new ResourceNotFoundException("Ticket no encontrado con el id: " + idTicket));

        LocalDateTime hoy = LocalDateTime.now();
        Estado estadoEnum = Estado.valueOf(nuevoEstado);
//        Validación:
//        valueOf() lanza una excepción si el valor no coincide con ningún enum.
        ticket.setEstado(estadoEnum);

        if (estadoEnum == Estado.RESUELTO || estadoEnum == Estado.CERRADO){
            ticket.setFechaResolucion(hoy);
        }else {
            ticket.setFechaResolucion(null);
        }

        Ticket ticketActulizado = ticketDAO.actualizarEstado(ticket);

        return ticketMapper.toResponse(ticketActulizado);
    }

    @Override
    public TicketResponse asignarTicketA(Long idTicket, AsignarTecnicoDTO asignarTecnicoDTO) {
        //buscamos el ticket por id recibido
        Ticket ticket  = ticketDAO.ticketPorId(idTicket)
                .orElseThrow(() -> new ResourceNotFoundException("Ticket no encontrado con el id: " + idTicket));

        //buscamo el usario por id recibio
        Usuario usuario = usuarioRepository.findById(asignarTecnicoDTO.getIdTecnico())
                .orElseThrow(() -> new ResourceNotFoundException("Usuario no encontrado con el id: " +asignarTecnicoDTO.getIdTecnico()));

        //validar que usuario tenga rol de TECNICO
        if (!usuario.tieneRol(Rol.TECNICO)){
            throw new ForbiddenAccessException("El usuario no tiene rol de TECNICO");
        }
        ticket.setAsignadoA(usuario);
        ticket = ticketDAO.asignarTicketA(ticket);

        return ticketMapper.toResponse(ticket);
    }

    @Override
    public List<TicketResponse> listadoTicket() {
        //obetenemos los tickets
        List<Ticket> lista = ticketDAO.listadoTicket();

        //validamos si viene vacia la lista
        ServiceUtils.validateNotEmpty(lista, "No hay tickets registrados");


        return ticketMapper.toResponseList(lista);
    }

    @Override
    public TicketDTO ticketPorId(Long id) {
        Ticket ticket = ticketDAO.ticketPorId(id).orElseThrow(() -> new ResourceNotFoundException("Ticket no encontrada con el id: " + id));

        ticket.getComentarios().forEach(c -> {
            System.out.println("Comentario ID: " + c.getIdComentario());
            System.out.println("Autor: " + (c.getAutor() != null ? c.getAutor().getUsername() : "NULL"));
        });

        Usuario usuario = getUsuarioAutenticado();


        if (usuario.tieneRol(Rol.CLIENTE)) {
            if (!ticket.getCreadoPor().getIdUsuario().equals(usuario.getIdUsuario())) {
                throw new UnauthorizedException("No tienes permiso para ver este ticket.");
            }
        } else if (usuario.tieneRol(Rol.TECNICO)) {
            if (ticket.getAsignadoA() == null ||
                    !ticket.getAsignadoA().getIdUsuario().equals(usuario.getIdUsuario())) {
                throw new UnauthorizedException("No tienes permiso para ver este ticket.");
            }
        }
        // Si es ADMIN no se hace ninguna restricción

        return ticketDAO.ticketPorId(id)
                .map(ticketMapper::toDto)
                .orElseThrow(() -> new ResourceNotFoundException("Ticket no encontrada con el id: " + id));

    }

    @Override
    public Page<TicketResponse> tickets(Pageable pageable,
                                        String palabraClave,
                                        String estado,
                                        String prioridad,
                                        LocalDate fechaInicio,
                                        LocalDate fechaFin,
                                        Long idCreador,
                                        Long idTecnico,
                                        Long idCategoria) {

        Usuario usuario = getUsuarioAutenticado();

        // Determinar el rol y aplicar restricciones
        if (usuario.tieneRol(Rol.CLIENTE)) {
            // Cliente solo puede ver los tickets que él creó
            idCreador = usuario.getIdUsuario(); // Reemplaza lo que venga por query param
            idTecnico = null; // Evita que se apliquen otros filtros inadecuados
        } else if (usuario.tieneRol(Rol.TECNICO)) {
            // Técnico solo puede ver los tickets asignados a él
            idTecnico = usuario.getIdUsuario();
            idCreador = null;
        }

        //obtengo el listado de tickets paginados
        Page<Ticket> listadoPaginado = ticketDAO.tickets(pageable,palabraClave,estado,prioridad,fechaInicio,fechaFin,idCreador,idTecnico,idCategoria);

        //Convertimos las entidades en DTO usando el mapper creado con .getContent() que trae solo las tareas y no la informacion de paginacion
        List<TicketResponse> dtoList = ticketMapper.toResponseList(listadoPaginado.getContent());

        //validamos si viene vacia la lista
        ServiceUtils.validateNotEmpty(dtoList, "No hay tickets registrados");

        // Retornamos un nuevo Page con los DTOs y la info de paginación original
        return new PageImpl<>(dtoList, pageable, listadoPaginado.getTotalElements());
    }

    //Metodo para obetener del contexto el usuario logueado
    public Usuario getUsuarioAutenticado() {
        //En el backend, obtenés al usuario autenticado desde el contexto de Spring Security, así:
        String username = SecurityContextHolder.getContext().getAuthentication().getName();


        return usuarioRepository.findUserEntityByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("Usuario no encontrado"));
    }

}
