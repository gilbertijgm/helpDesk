package com.helpdesk.helpDesk.controller.dto.categoria;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CategoriaDTO {

    private Long idCategoria;

    private String nombreCategoria;

    private String descripcion;
}
