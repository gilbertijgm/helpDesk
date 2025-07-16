package com.helpdesk.helpDesk.controller;

import com.helpdesk.helpDesk.controller.dto.categoria.CategoriaDTO;
import com.helpdesk.helpDesk.response.ApiResponse;
import com.helpdesk.helpDesk.service.ICategoriaService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.net.URISyntaxException;

@RestController
@RequestMapping("/api/categoria")
@RequiredArgsConstructor
@Tag(name = "Categoria", description = "Endpoint para las diferentes operaciones")
public class CategoriaController {

    private final ICategoriaService categoriaService;

    @Operation(
            summary = "Obtener lista de Categorias",
            description = ""
    )
    @GetMapping("/categorias")
    public ResponseEntity<?> findAll(){

        return ResponseEntity.ok(categoriaService.categorias());
    }

    @Operation(
            summary = "Obtener Categoria por ID",
            description = ""
    )
    @GetMapping("/categoria-id/{id}")
    public ResponseEntity<ApiResponse<CategoriaDTO>> categoriaById(@PathVariable Long id) {
        CategoriaDTO categoria = categoriaService.categoriaById(id); // lanza excepción si no existe
        ApiResponse<CategoriaDTO> response = new ApiResponse<>(200, "Categoria encontrada", categoria);
        return ResponseEntity.ok(response);
    }

    @Operation(
            summary = "Creacion de Comentario",
            description = ""
    )
    @PostMapping("/crear")
    public ResponseEntity<?> guardar(@RequestBody @Validated CategoriaDTO categoriaDTO ) throws URISyntaxException {

        CategoriaDTO categoria = categoriaService.guardarCategoria(categoriaDTO);

        ApiResponse<CategoriaDTO> response = new ApiResponse<>(201, "Categoria Creada con Exito" , categoria);

        return ResponseEntity.created(new URI("/api/task/" + categoriaDTO.getIdCategoria())).body(response);
    }

    @Operation(
            summary = "Eliminar Comentario",
            description = ""
    )
    @DeleteMapping("/eliminar/{id}")
    public ResponseEntity<?> deleteCategoria(@PathVariable Long id){
        categoriaService.deleteCategoria(id);

        return ResponseEntity.ok("Registro Eliminado");
    }
}
