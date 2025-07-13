package com.helpdesk.helpDesk.controller.dto.ticket;

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
public class ActualizarEstadoDTO {

    @NotBlank(message = "Campo requerido, Estado no puede estar vacio")
    @Size(min = 2, max = 15)
    private String nuevoEstado;
}
