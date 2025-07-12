package com.helpdesk.helpDesk.controller.dto.comentario;

import com.helpdesk.helpDesk.controller.dto.auth.UsuarioDTO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ComentarioResponseDTO {

    private Long idComentario;

    private String mensaje;

    private LocalDateTime fecha;

    // === Usuario que escribió el comentario (opcional pero recomendable)
    private UsuarioDTO nombreUsuario;
}
