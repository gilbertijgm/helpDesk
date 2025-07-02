package com.helpdesk.helpDesk.repository;

import com.helpdesk.helpDesk.entities.Categoria;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CategoriaRepository extends JpaRepository<Categoria, Long> {

    Optional<Categoria> findCategoriaByNombreCategoria(String nombreCategoria);
}
