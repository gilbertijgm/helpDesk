package com.helpdesk.helpDesk.service;

import com.helpdesk.helpDesk.controller.dto.categoria.CategoriaDTO;
import com.helpdesk.helpDesk.entities.Categoria;

import java.util.List;
import java.util.Optional;

public interface ICategoriaService {

    //Metodo para listar todos los recursos
    List<CategoriaDTO> categorias();

    //Metodo para buescar recurso por id
    CategoriaDTO categoriaById(Long id);

    //Metodo para guardar
    CategoriaDTO guardarCategoria(CategoriaDTO categoriaDTO);

    //Metodo para eliminar
    void deleteCategoria(Long id);
}
