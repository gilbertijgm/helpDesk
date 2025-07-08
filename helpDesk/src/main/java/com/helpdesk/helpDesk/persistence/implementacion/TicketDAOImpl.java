package com.helpdesk.helpDesk.persistence.implementacion;

import com.helpdesk.helpDesk.entities.Ticket;
import com.helpdesk.helpDesk.persistence.ITicketDAO;
import com.helpdesk.helpDesk.repository.TicketRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class TicketDAOImpl implements ITicketDAO {

    private final TicketRepository ticketRepository;
    @Override
    public Ticket crearTicket(Ticket ticket) {
        return ticketRepository.save(ticket);
    }

    @Override
    public Ticket actualizarTicket(Ticket ticket) {
        return ticketRepository.save(ticket);
    }

    @Override
    public Ticket actualizarEstado(Ticket ticket) {
        return ticketRepository.save(ticket);
    }

    @Override
    public Ticket asignarTicketA(Ticket ticket) {
        return ticketRepository.save(ticket);
    }

    @Override
    public List<Ticket> listadoTicket() {
        List<Ticket> lista = ticketRepository.findAll();

        return lista;
    }

    @Override
    public Optional<Ticket> ticketPorId(Long id) {


        return ticketRepository.findById(id);
    }

    //String palabraClave, String estado, String prioridad, LocalDate fecha,
    @Override
    public Page<Ticket> tickets(
                                Pageable pageable,
                                String palabraClave,
                                String estado,
                                String prioridad,
                                LocalDate fechaInicio,
                                LocalDate fechaFin,
                                Long idCreador,
                                Long idTecnico,
                                Long idCategoria) {

        Page<Ticket> lista = ticketRepository.buscarTicketsConFiltros(
                pageable,
                palabraClave,
                estado,
                prioridad,
                fechaInicio,
                fechaFin,
                idCreador,
                idTecnico,
                idCategoria
        );

        return lista;
    }
}
