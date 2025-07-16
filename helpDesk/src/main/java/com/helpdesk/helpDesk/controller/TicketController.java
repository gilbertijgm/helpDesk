package com.helpdesk.helpDesk.controller;


import com.helpdesk.helpDesk.controller.dto.ticket.*;
import com.helpdesk.helpDesk.response.ApiResponse;
import com.helpdesk.helpDesk.response.PagedResponse;
import com.helpdesk.helpDesk.response.Pagination;
import com.helpdesk.helpDesk.service.ITicketService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.net.URISyntaxException;
import java.time.LocalDate;
import java.util.LinkedHashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/ticket")
@RequiredArgsConstructor
@Tag(name = "Tickets", description = "Endpoint para las diferentes operaciones")
public class TicketController {

    private final ITicketService ticketService;
    @Operation(
            summary = "Creacion de Ticket",
            description = ""
    )
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "201", description = "Ticket creado exitosamente"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "Datos inválidos"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "401", description = "No autorizado"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "500", description = "Ocurrió un error inesperado")
    })
    @PostMapping("/crear")
    public ResponseEntity<?> guardar(@RequestBody @Validated  TicketCreateDTO ticketDTO ) throws URISyntaxException {

        TicketResponse ticket = ticketService.crearTicket(ticketDTO);

        ApiResponse<TicketResponse> response = new ApiResponse<>(201, "Ticket Creado con Exito" , ticket);

        return ResponseEntity.created(new URI("/api/task/" + ticketDTO.getIdTicket())).body(response);
    }

    @Operation(
            summary = "Actualizacion de Ticket",
            description = ""
    )
    @PutMapping("/actualizar/{id}")
    public ResponseEntity<?> actualizar(@PathVariable Long id, @RequestBody TicketCreateDTO ticketCreateDTO)throws URISyntaxException{
        TicketResponse ticketResponse = ticketService.actualizarTicket(ticketCreateDTO, id);

        ApiResponse<TicketResponse> response = new ApiResponse<>(200, "Ticket Actualizado con Exito", ticketResponse);

        return ResponseEntity.ok(response);
    }

    @Operation(
            summary = "Asignacion de tecnico",
            description = "Asigna a un tecnico para la resolucion del ticket"
    )
    @PatchMapping("/asignarTecnico/{id}")
    public ResponseEntity<?> asignarTecnico(@PathVariable Long id, @RequestBody AsignarTecnicoDTO asignarTecnicoDTO)throws URISyntaxException{
        TicketResponse ticketResponse = ticketService.asignarTicketA(id, asignarTecnicoDTO);

        ApiResponse<TicketResponse> response = new ApiResponse<>(200, "Ticket Asignado con exito", ticketResponse);

        return ResponseEntity.ok(response);
    }

    @Operation(
            summary = "Actualiza parcialmente un Ticket",
            description = "Actualiza o modifica el estado de un Ticket"
    )
    @PatchMapping("/actualizarEstado/{id}")
    public ResponseEntity<?> actualizarEstado(@PathVariable Long id, @RequestBody ActualizarEstadoDTO nuevoEstado)throws URISyntaxException{
        TicketResponse ticketResponse = ticketService.actualizarEstado(id, nuevoEstado.getNuevoEstado());

        ApiResponse<TicketResponse> response = new ApiResponse<>(200, "Estado actualizado con exito", ticketResponse);

        return ResponseEntity.ok(response);
    }

//    @CrossOrigin(origins = "http://localhost:5173",
//            allowedHeaders = "*",
//            methods = {RequestMethod.GET, RequestMethod.POST,
//                    RequestMethod.PUT, RequestMethod.DELETE, RequestMethod.OPTIONS},
//            allowCredentials = "true")
    @GetMapping("/listado")
    public ResponseEntity<?> listado(){
        return ResponseEntity.ok(ticketService.listadoTicket());
    }


    @Operation(
            summary = "Obtiene todos los tickets",
            description = "Lista tickets con filtros opcionales como estado, prioridad, etc. Incluyendo paginacion y HATEOAS." +
                    "Ademas tiene filtracion por roles: cliente solo puede ver los tickets que creo," +
                    "tecnico solo puede acceder a los ticket asignados a el, administrador puede ver todos los tickets"
    )
    @GetMapping("/tickets")
    public ResponseEntity<PagedResponse<TicketResponse>> listarTickets(@RequestParam(defaultValue = "0") int page, // Número de página (por defecto 0)
                                                               @RequestParam(defaultValue = "5")int size,// Tamaño de página (por defecto 5 elementos por página)
                                                               @RequestParam(required = false) String palabraClave,
                                                               @RequestParam(required = false) String estado,
                                                               @RequestParam(required = false) String prioridad,
                                                               @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)  LocalDate fechaInicio,
                                                               @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fechaFin,
                                                               @RequestParam(required = false) Long idCreador,
                                                               @RequestParam(required = false) Long idTecnico,
                                                               @RequestParam(required = false) Long idCategoria,
                                                               HttpServletRequest request // Necesario para armar las URLs base en los enlaces
    ){
        //validacion de parametros de entrada
        int pageNumber = Math.max(0, page); //minimo de pagina 0
        int pageSize = Math.min(Math.max(1, size), 100); //Maximo 100 resultados por pagina

        // Creamos un objeto Pageable con la página y el tamaño
        Pageable pageable = PageRequest.of(pageNumber, pageSize);

        //obtenemos la lista de tarea desde el servicio
        Page<TicketResponse> pageResult = ticketService.tickets(pageable,palabraClave,estado,prioridad,fechaInicio,fechaFin,idCreador,idTecnico,idCategoria);

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

        String queryParams = request.getQueryString() != null ? "&" + request.getQueryString().replaceAll("page=\\d+", "") : "";

        // Creamos un mapa con los enlaces tipo HATEOAS manuales (next, previous, first, last)
        Map<String, String> links = new LinkedHashMap<>();
        links.put("firstPage", baseUrl + "?page=0&size=" + size + queryParams);
        links.put("lastPage", baseUrl + "?page=" + (pageResult.getTotalPages() - 1) + "&size=" + size + queryParams);
        links.put("nextPage", pageResult.hasNext() ? baseUrl + "?page=" + (page + 1) + "&size=" + size + queryParams : null);
        links.put("previousPage", pageResult.hasPrevious() ? baseUrl + "?page=" + (page - 1) + "&size=" + size + queryParams : null);

        //creamos mensaje personalizado
        String mensaje = pageResult.getTotalElements() == 0 ? "No se encontraron tickets registrados" : "Listado de Tickets";
        // Creamos la respuesta final con todos los datos
        PagedResponse<TicketResponse> response = new PagedResponse<>(
                200,                // Código HTTP
                mensaje,           // Mensaje
                pageResult.getContent(),       // Con .getContent() obtenés solo la lista de elementos sin los metadatos que trae Page.
                pagination,                    // Info de paginación
                links                          // Enlaces de navegación
        );

        return ResponseEntity.ok(response);
    }


    @Operation(
            summary = "Obtiene ticket por ID",
            description = "Obtiene ticked por ID, los filtra por roles: cliente solo puede ver los tickets que creo," +
                    "tecnico solo puede acceder a los ticket asignados a el, administrador puede ver todos los tickets."
    )
    @GetMapping("/ticket/{id}")
    public ResponseEntity<ApiResponse<TicketDTO>> findById(@PathVariable Long id) {
        TicketDTO ticket = ticketService.ticketPorId(id); // lanza excepción si no existe
        ApiResponse<TicketDTO> response = new ApiResponse<>(200, "Ticket encontrada", ticket);
        return ResponseEntity.ok(response);
    }
}
