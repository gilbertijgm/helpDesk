package com.helpdesk.helpDesk.controller.dto.comentario;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ComentarioCreateDTO {

    private String mensaje;
}
