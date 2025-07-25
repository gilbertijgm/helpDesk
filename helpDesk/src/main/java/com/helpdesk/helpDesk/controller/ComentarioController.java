package com.helpdesk.helpDesk.controller;

import com.helpdesk.helpDesk.controller.dto.comentario.ComentarioResponseDTO;
import com.helpdesk.helpDesk.controller.dto.comentario.ComentarioCreateDTO;
import com.helpdesk.helpDesk.controller.dto.ticket.TicketResponse;
import com.helpdesk.helpDesk.response.ApiResponse;
import com.helpdesk.helpDesk.response.PagedResponse;
import com.helpdesk.helpDesk.response.Pagination;
import com.helpdesk.helpDesk.service.IComentarioService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.LinkedHashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/comentario")
@RequiredArgsConstructor
@Tag(name = "Comentario", description = "Endpoint para las diferentes operaciones")
public class ComentarioController {

    private final IComentarioService comentarioService;

    @Operation(
            summary = "Creacion de Comentario",
            description = ""
    )
    @PostMapping("/crear/{idTicket}")
    public ResponseEntity<?> crearComentario(@PathVariable Long idTicket,
                                             @RequestBody @Validated ComentarioCreateDTO comentarioDTO) throws URISyntaxException {

        ComentarioResponseDTO comentario = comentarioService.crearComentario(comentarioDTO, idTicket);

        ApiResponse<ComentarioResponseDTO> response = new ApiResponse<>(201, "Comentario creado con exito.", comentario);

        return ResponseEntity.created(new URI("/api/comentario/crear/")).body(response);
    }

    @Operation(
            summary = "Modificacion de Comentario",
            description = ""
    )
    @PutMapping("/modificar/{idComentario}")
    public ResponseEntity<?> modificarComentario(@PathVariable Long idComentario, @RequestBody ComentarioCreateDTO comentarioDTO){
        ComentarioResponseDTO comentario = comentarioService.modificarComentario(comentarioDTO, idComentario);

        ApiResponse<ComentarioResponseDTO> response = new ApiResponse<>(200, "Comentario modificado con exito.", comentario);

        return ResponseEntity.ok(response);
    }

    @Operation(
            summary = "Eliminar Comentario",
            description = ""
    )
    @DeleteMapping("/eliminar/{id}")
    public ResponseEntity<?> eliminarComentario(@PathVariable Long id){

        comentarioService.eliminarComentario(id);

        return ResponseEntity.ok("Comentario Eliminado");
    }
    @Operation(
            summary = "Obtener Comentario por ID",
            description = ""
    )
    @GetMapping("/byId/{id}")
    public ResponseEntity<?> comentario(@PathVariable Long id){
        ComentarioResponseDTO comen = comentarioService.comentarioById(id);

        return ResponseEntity.ok(comen);
    }


}
