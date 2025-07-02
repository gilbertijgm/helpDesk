package com.helpdesk.helpDesk.persistence;

import com.helpdesk.helpDesk.entities.Categoria;

import java.util.List;
import java.util.Optional;

public interface ICategoriaDAO {

    //Metodo para listar todos los recursos
    List<Categoria> categorias();

    //Metodo para buescar recurso por id
    Optional<Categoria> categoriaById(Long id);

    //Metodo para guardar
    Categoria guardarCategoria(Categoria task);


    //Metodo para eliminar
    void deleteCategoria(Long id);
}
