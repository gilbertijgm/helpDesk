package com.helpdesk.helpDesk.controller;


import com.helpdesk.helpDesk.controller.dto.ticket.AsignarTecnicoDTO;
import com.helpdesk.helpDesk.controller.dto.ticket.TicketCreateDTO;
import com.helpdesk.helpDesk.controller.dto.ticket.TicketDTO;
import com.helpdesk.helpDesk.controller.dto.ticket.TicketResponse;
import com.helpdesk.helpDesk.response.ApiResponse;
import com.helpdesk.helpDesk.response.PagedResponse;
import com.helpdesk.helpDesk.response.Pagination;
import com.helpdesk.helpDesk.service.ITicketService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.LinkedHashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/ticket")
@RequiredArgsConstructor
public class TicketController {

    private final ITicketService ticketService;

    @PostMapping("/crear")
    public ResponseEntity<?> guardar(@RequestBody TicketCreateDTO ticketDTO ) throws URISyntaxException {

        TicketResponse ticket = ticketService.crearTicket(ticketDTO);

        ApiResponse<TicketResponse> response = new ApiResponse<>(201, "Ticket Creado con Exito" , ticket);

        return ResponseEntity.created(new URI("/api/task/" + ticketDTO.getIdTicket())).body(response);
    }

    @PutMapping("/actualizar/{id}")
    public ResponseEntity<?> actualizar(@PathVariable Long id, @RequestBody TicketCreateDTO ticketCreateDTO)throws URISyntaxException{
        TicketResponse ticketResponse = ticketService.actualizarTicket(ticketCreateDTO, id);

        ApiResponse<TicketResponse> response = new ApiResponse<>(200, "Ticket Actualizado con Exito", ticketResponse);

        return ResponseEntity.ok(response);
    }

    @PatchMapping("/asignarTecnico/{id}")
    public ResponseEntity<?> asignarTenico(@PathVariable Long id, @RequestBody AsignarTecnicoDTO asignarTecnicoDTO)throws URISyntaxException{
        TicketResponse ticketResponse = ticketService.asignarTicketA(id, asignarTecnicoDTO);

        ApiResponse<TicketResponse> response = new ApiResponse<>(200, "Ticket Asignado con exito", ticketResponse);

        return ResponseEntity.ok(response);
    }

    @GetMapping("/listado")
    public ResponseEntity<?> listado(){
        return ResponseEntity.ok(ticketService.listadoTicket());
    }

    @GetMapping("/tickets")
    public ResponseEntity<PagedResponse<TicketResponse>> tasks(@RequestParam(defaultValue = "0") int page, // Número de página (por defecto 0)
                                                               @RequestParam(defaultValue = "5")int size,  // Tamaño de página (por defecto 5 elementos por página)
                                                               HttpServletRequest request // Necesario para armar las URLs base en los enlaces
    ){
        // Creamos un objeto Pageable con la página y el tamaño
        Pageable pageable = PageRequest.of(page, size);

        //obtenemos la lista de tarea desde el servicio
        Page<TicketResponse> pageResult = ticketService.tickets(pageable);

        // Creamos el objeto con los datos de paginación
        Pagination pagination = new Pagination(
                pageResult.getNumber(),         // Página actual
                pageResult.getSize(),           // Tamaño de la página
                pageResult.getTotalPages(),     // Total de páginas
                pageResult.getTotalElements(),  // Total de elementos
                pageResult.hasNext(),           // ¿Hay una página siguiente?
                pageResult.hasPrevious()        // ¿Hay una página anterior?
        );

        // Obtenemos la URL base del request para construir los enlaces de navegación
        String baseUrl = request.getRequestURL().toString();

        // Creamos un mapa con los enlaces tipo HATEOAS manuales (next, previous, first, last)
        Map<String, String> links = new LinkedHashMap<>();
        links.put("firstPage", baseUrl + "?page=0&size=" + size);
        links.put("lastPage", baseUrl + "?page=" + (pageResult.getTotalPages() - 1) + "&size=" + size);
        links.put("nextPage", pageResult.hasNext() ? baseUrl + "?page=" + (page + 1) + "&size=" + size : null);
        links.put("previousPage", pageResult.hasPrevious() ? baseUrl + "?page=" + (page - 1) + "&size=" + size : null);

        // Creamos la respuesta final con todos los datos
        PagedResponse<TicketResponse> response = new PagedResponse<>(
                200,                // Código HTTP
                "Listado de Tareas",           // Mensaje
                pageResult.getContent(),       // Con .getContent() obtenés solo la lista de elementos sin los metadatos que trae Page.
                pagination,                    // Info de paginación
                links                          // Enlaces de navegación
        );

        return ResponseEntity.ok(response);
    }

    @GetMapping("/ticket/{id}")
    public ResponseEntity<ApiResponse<TicketDTO>> findById(@PathVariable Long id) {
        TicketDTO ticket = ticketService.ticketPorId(id); // lanza excepción si no existe
        ApiResponse<TicketDTO> response = new ApiResponse<>(200, "Ticket encontrada", ticket);
        return ResponseEntity.ok(response);
    }
}
