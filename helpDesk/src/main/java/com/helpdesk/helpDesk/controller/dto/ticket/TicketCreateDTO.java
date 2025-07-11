package com.helpdesk.helpDesk.controller.dto.ticket;

import com.helpdesk.helpDesk.entities.Categoria;
import com.helpdesk.helpDesk.entities.Usuario;
import com.helpdesk.helpDesk.entities.enums.Estado;
import com.helpdesk.helpDesk.entities.enums.Prioridad;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class TicketCreateDTO {

    private Long idTicket;

    @NotBlank(message = "Campo requerido, Titulo no puede estar vacio")
    @Size(min = 2, max = 50)
    private String titulo;

    @NotBlank(message = "Campo requerido, Descripcion no puede estar vacio")
    @Size(min = 2, max = 250)
    private String descripcion;


    @NotNull(message = "Campo requerido, Estado no puede estar vacio")
    @Enumerated(EnumType.STRING)
    private Prioridad prioridad;

    @NotNull(message = "Campo requerido: La categoría es obligatoria")
    // Categoría del ticket
    private Long idCategoria;
}
