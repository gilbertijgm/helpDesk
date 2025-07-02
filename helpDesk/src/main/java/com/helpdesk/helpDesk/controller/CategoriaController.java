package com.helpdesk.helpDesk.controller;

import com.helpdesk.helpDesk.controller.dto.categoria.CategoriaDTO;
import com.helpdesk.helpDesk.response.ApiResponse;
import com.helpdesk.helpDesk.service.ICategoriaService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.net.URISyntaxException;

@RestController
@RequestMapping("/api/categoria")
@RequiredArgsConstructor
public class CategoriaController {

    private final ICategoriaService categoriaService;

    @GetMapping("/categorias")
    public ResponseEntity<?> findAll(){

        return ResponseEntity.ok(categoriaService.categorias());
    }

    @GetMapping("/categoria-id/{id}")
    public ResponseEntity<ApiResponse<CategoriaDTO>> categoriaById(@PathVariable Long id) {
        CategoriaDTO categoria = categoriaService.categoriaById(id); // lanza excepción si no existe
        ApiResponse<CategoriaDTO> response = new ApiResponse<>(200, "Categoria encontrada", categoria);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/crear")
    public ResponseEntity<?> guardar(@RequestBody CategoriaDTO categoriaDTO ) throws URISyntaxException {

        CategoriaDTO categoria = categoriaService.guardarCategoria(categoriaDTO);

        ApiResponse<CategoriaDTO> response = new ApiResponse<>(201, "Categoria Creada con Exito" , categoria);

        return ResponseEntity.created(new URI("/api/task/" + categoriaDTO.getIdCategoria())).body(response);
    }

    @DeleteMapping("/eliminar/{id}")
    public ResponseEntity<?> deleteCategoria(@PathVariable Long id){
        categoriaService.deleteCategoria(id);

        return ResponseEntity.ok("Registro Eliminado");
    }
}
