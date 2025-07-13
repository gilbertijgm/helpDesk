package com.helpdesk.helpDesk.controller.dto.comentario;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ComentarioCreateDTO {

    @NotBlank(message = "Campo requerido, mensaje no puede estar vacio")
    @Size(min = 2, max = 250)
    private String mensaje;
}
