package com.helpdesk.helpDesk.repository;

import com.helpdesk.helpDesk.entities.Ticket;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TicketRepository extends JpaRepository<Ticket, Long> {

    //Metodo para obtener todas las tickets aplicando paginacion
    Page<Ticket> findAll(Pageable pageable);
}
