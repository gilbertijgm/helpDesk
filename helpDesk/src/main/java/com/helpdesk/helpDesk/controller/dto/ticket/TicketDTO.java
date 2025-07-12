package com.helpdesk.helpDesk.controller.dto.ticket;

import com.helpdesk.helpDesk.controller.dto.auth.UsuarioDTO;
import com.helpdesk.helpDesk.controller.dto.categoria.CategoriaDTO;
import com.helpdesk.helpDesk.controller.dto.comentario.ComentarioResponseDTO;
import com.helpdesk.helpDesk.entities.Usuario;
import com.helpdesk.helpDesk.entities.enums.Estado;
import com.helpdesk.helpDesk.entities.enums.Prioridad;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class TicketDTO {

    private Long idTicket;

    private String titulo;

    private String descripcion;

    @Enumerated(EnumType.STRING)
    private Estado estado;

    @Enumerated(EnumType.STRING)
    private Prioridad prioridad;


    private LocalDateTime fechaCreacion;

    private LocalDateTime fechaResolucion;


    private UsuarioDTO nombreUsuario;
    private UsuarioDTO tecnicoAsignado;
    private CategoriaDTO nombreCategoria;

    private List<ComentarioResponseDTO> comentarios;
}
