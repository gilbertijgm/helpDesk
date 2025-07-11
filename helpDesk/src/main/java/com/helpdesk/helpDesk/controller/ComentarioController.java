package com.helpdesk.helpDesk.controller;

import com.helpdesk.helpDesk.controller.dto.comentario.ComentarioResponseDTO;
import com.helpdesk.helpDesk.controller.dto.comentario.ComentarioCreateDTO;
import com.helpdesk.helpDesk.response.ApiResponse;
import com.helpdesk.helpDesk.service.IComentarioService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.net.URISyntaxException;

@RestController
@RequestMapping("/api/comentario")
@RequiredArgsConstructor
public class ComentarioController {

    private final IComentarioService comentarioService;

    @PostMapping("/crear/{idTicket}")
    public ResponseEntity<?> crearComentario(@PathVariable Long idTicket,
                                             @RequestBody ComentarioCreateDTO comentarioDTO) throws URISyntaxException {

        ComentarioResponseDTO comentario = comentarioService.crearComentario(comentarioDTO, idTicket);

        ApiResponse<ComentarioResponseDTO> response = new ApiResponse<>(201, "Comentario creado con exito.", comentario);

        return ResponseEntity.created(new URI("/api/comentario/crear/")).body(response);
    }

    @PutMapping("/modificar/{idComentario}")
    public ResponseEntity<?> modificarComentario(@PathVariable Long idComentario, @RequestBody ComentarioCreateDTO comentarioDTO){
        ComentarioResponseDTO comentario = comentarioService.modificarComentario(comentarioDTO, idComentario);

        ApiResponse<ComentarioResponseDTO> response = new ApiResponse<>(200, "Comentario modificado con exito.", comentario);

        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/eliminar/{id}")
    public ResponseEntity<?> eliminarComentario(@PathVariable Long id){

        comentarioService.eliminarComentario(id);

        return ResponseEntity.ok("Comentario Eliminado");
    }

    @GetMapping("/byId/{id}")
    public ResponseEntity<?> comentario(@PathVariable Long id){
        ComentarioResponseDTO comen = comentarioService.comentarioById(id);

        return ResponseEntity.ok(comen);
    }
}
