package com.helpdesk.helpDesk.repository;

import com.helpdesk.helpDesk.entities.Ticket;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface TicketRepository extends JpaRepository<Ticket, Long>, TicketRepositoryCustom {

    //Metodo para obtener todas las tickets aplicando paginacion
    Page<Ticket> findAll(Pageable pageable);

    @EntityGraph(attributePaths = {"comentarios", "comentarios.autor"})
    @Query("SELECT t FROM Ticket t WHERE t.id = :id")
    Optional<Ticket> findByIdWithComentarios(@Param("id") Long id);

}
