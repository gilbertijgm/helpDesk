package com.helpdesk.helpDesk.repository;

import com.helpdesk.helpDesk.entities.Ticket;
import com.helpdesk.helpDesk.entities.enums.Estado;
import com.helpdesk.helpDesk.entities.enums.Prioridad;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;


// Esta clase es la implementación de la interfaz personalizada TaskRepositoryCustom
@Repository // Marcamos esta clase como componente de repositorio para que Spring la detecte
public class TicketRepositoryImpl implements TicketRepositoryCustom{


    @PersistenceContext // Inyectamos el EntityManager para trabajar con la Criteria API
    private EntityManager entityManager;

    @Override
    public Page<Ticket> buscarTicketsConFiltros(Pageable pageable, String palabraClave, String estado, String prioridad, LocalDate fechaInicio, LocalDate fechaFin, Long idCreador, Long idTecnico, Long idCategoria) {

        // ============================
        // CONSULTA PRINCIPAL (traer datos)
        // ============================

        CriteriaBuilder cb = entityManager.getCriteriaBuilder(); // Obtenemos el constructor de criterios
        CriteriaQuery<Ticket> query = cb.createQuery(Ticket.class); // Creamos una consulta que devuelve objetos
        Root<Ticket> ticket = query.from(Ticket.class); // Definimos la entidad base de la consulta (FROM Entidad)
        List<Predicate> predicates = new ArrayList<>(); // Lista que acumulará los filtros dinámicos (WHERE)

        // Aquí vamos a ir agregando los filtros uno por uno...

        // ============================
        // FILTRO POR PALABRA CLAVE (título o descripción)
        // Validamos que la palabra clave no sea nula ni vacía
        if (palabraClave != null && !palabraClave.isBlank()) {
            // Creamos predicado tipo LIKE para el campo 'titulo', convirtiendo todo a minúsculas
            Predicate tituloLike = cb.like(cb.lower(ticket.get("titulo")), "%" + palabraClave.toLowerCase() + "%");
            // Creamos predicado tipo LIKE para el campo 'descripcion', también en minúsculas
            Predicate descripcionLike = cb.like(cb.lower(ticket.get("descripcion")), "%" + palabraClave.toLowerCase() + "%");
            // Combinamos ambos con un OR, ya que queremos encontrar coincidencias en cualquiera de los dos campos
            predicates.add(cb.or(tituloLike, descripcionLike));
        }

        // ============================
        // FILTRO POR ESTADO DEL TICKET
        // Validamos que se haya enviado un estado y que no esté vacío
        if (estado != null && !estado.isBlank()) {
            // Convertimos el valor recibido (String) al enum correspondiente
            // Nota: esto puede lanzar una excepción si el valor no existe en el enum
            Predicate estadoEqual = cb.equal(ticket.get("estado"), Estado.valueOf(estado.toUpperCase()));
            // Agregamos el predicado a la lista
            predicates.add(estadoEqual);
        }

        // ============================
        // FILTRO POR PRIORIDAD DEL TICKET
        // Validamos que se haya enviado una prioridad y que no esté vacía
        if (prioridad != null && !prioridad.isBlank()) {
            // Convertimos el valor recibido (String) al enum correspondiente
            Predicate prioridadEqual = cb.equal(ticket.get("prioridad"), Prioridad.valueOf(prioridad.toUpperCase()));
            // Agregamos el predicado a la lista de filtros principales
            predicates.add(prioridadEqual);
        }

        // ============================
        // FILTRO POR RANGO DE FECHAS (fechaCreacion entre fechaInicio y fechaFin)
        // Si se especificaron ambas fechas (inicio y fin)
        //Nota técnica: Usamos .atStartOfDay() y .atTime(23, 59, 59) para cubrir todo el día completo en el rango.
        if (fechaInicio != null && fechaFin != null) {
            predicates.add(cb.between(ticket.get("fechaCreacion"), fechaInicio.atStartOfDay(), fechaFin.atTime(23, 59, 59)));
        }
        // Si solo se especificó la fecha de inicio
        else if (fechaInicio != null) {
            predicates.add(cb.greaterThanOrEqualTo(ticket.get("fechaCreacion"), fechaInicio.atStartOfDay()));
        }
        // Si solo se especificó la fecha de fin
        else if (fechaFin != null) {
            predicates.add(cb.lessThanOrEqualTo(ticket.get("fechaCreacion"), fechaFin.atTime(23, 59, 59)));
        }

        // ============================
        // FILTRO POR ID DE USUARIO CREADOR (creadoPor.idUsuario)
        if (idCreador != null) {
            // Verificamos que el objeto creadoPor no sea null y luego accedemos a su id
            predicates.add(cb.equal(ticket.get("creadoPor").get("idUsuario"), idCreador));
        }

        // ============================
        // FILTRO POR ID DEL TÉCNICO ASIGNADO (asignadoA.idUsuario)
        if (idTecnico != null) {
            // Accedemos a la propiedad 'asignadoA' (el técnico asignado) y su idUsuario para filtrar
            predicates.add(cb.equal(ticket.get("asignadoA").get("idUsuario"), idTecnico));
        }
        // ============================
        // FILTRO POR ID DE LA CATEGORÍA (categoria.idCategoria)
        if (idCategoria != null) {
            // Accedemos a la propiedad 'categoria' y filtramos por su idCategoria
            predicates.add(cb.equal(ticket.get("categoria").get("idCategoria"), idCategoria));
        }

        // Aplicamos todos los filtros acumulados con AND
        query.where(cb.and(predicates.toArray(new Predicate[0])));

        // Builder para campo prioridad: ALTA = 1, MEDIA = 2, BAJA = 3
        Expression<Integer> prioridadOrdenada = cb.<Integer>selectCase()
                .when(cb.equal(ticket.get("prioridad"), Prioridad.ALTA), 1)
                .when(cb.equal(ticket.get("prioridad"), Prioridad.MEDIA), 2)
                .when(cb.equal(ticket.get("prioridad"), Prioridad.BAJA), 3)
                .otherwise(4);

        // Aplicamos orden por prioridad (ascendente → 1 primero = ALTA)
        Order ordenPorPrioridad = cb.asc(prioridadOrdenada);
        // Orden secundario: fecha de creación descendente
        Order ordenPorFecha = cb.desc(ticket.get("fechaCreacion"));
        // Agregamos ambos órdenes
        query.orderBy(ordenPorPrioridad, ordenPorFecha);// Por si aparece una prioridad inesperada

        // ============================
        // EJECUTAMOS LA CONSULTA PRINCIPAL CON PAGINACIÓN
        // ============================

        // Creamos el objeto TypedQuery a partir del CriteriaQuery construido
        TypedQuery<Ticket> typedQuery = entityManager.createQuery(query);

        // Configuramos la paginación:
        // - offset: cantidad de registros a omitir (según la página actual)
        // - maxResults: cantidad máxima de registros a devolver por página
        typedQuery.setFirstResult((int) pageable.getOffset());         // Ejemplo: página 2, size 10 → offset = 20
        typedQuery.setMaxResults(pageable.getPageSize());              // Tamaño de página definido por el cliente

        // Ejecutamos la consulta y obtenemos los resultados paginados
        List<Ticket> resultList = typedQuery.getResultList();

        // ============================
        // CONSULTA DE CONTEO (para saber cuántos registros en total hay con los filtros aplicados)
        // ============================

        CriteriaQuery<Long> countQuery = cb.createQuery(Long.class); // Creamos consulta de tipo Long (conteo)
        Root<Ticket> countRoot = countQuery.from(Ticket.class); // Nueva raíz, no se puede usar la anterior
        List<Predicate> countPredicates = new ArrayList<>(); // Lista de filtros (de nuevo, pero para count)

        // ========== CONSULTA DE CONTEO ==========

        if (palabraClave != null && !palabraClave.isBlank()) {
            // Predicado LIKE para 'titulo' en la consulta de conteo
            Predicate tituloLikeCount = cb.like(cb.lower(countRoot.get("titulo")), "%" + palabraClave.toLowerCase() + "%");
            // Predicado LIKE para 'descripcion' en la consulta de conteo
            Predicate descripcionLikeCount = cb.like(cb.lower(countRoot.get("descripcion")), "%" + palabraClave.toLowerCase() + "%");
            // OR lógico entre ambos campos
            countPredicates.add(cb.or(tituloLikeCount, descripcionLikeCount));
        }
        //  ========== Repetimos para el conteo ==========
        if (estado != null && !estado.isBlank()) {
            Predicate estadoEqualCount = cb.equal(countRoot.get("estado"), Estado.valueOf(estado.toUpperCase()));
            countPredicates.add(estadoEqualCount);
        }
        // ========== Repetimos para el conteo ==========
        if (prioridad != null && !prioridad.isBlank()) {
            Predicate prioridadEqualCount = cb.equal(countRoot.get("prioridad"), Prioridad.valueOf(prioridad.toUpperCase()));
            countPredicates.add(prioridadEqualCount);
        }
        // ========== Repetimos para el conteo ==========
        if (fechaInicio != null && fechaFin != null) {
            countPredicates.add(cb.between(countRoot.get("fechaCreacion"), fechaInicio.atStartOfDay(), fechaFin.atTime(23, 59, 59)));
        } else if (fechaInicio != null) {
            countPredicates.add(cb.greaterThanOrEqualTo(countRoot.get("fechaCreacion"), fechaInicio.atStartOfDay()));
        } else if (fechaFin != null) {
            countPredicates.add(cb.lessThanOrEqualTo(countRoot.get("fechaCreacion"), fechaFin.atTime(23, 59, 59)));
        }
        // ========== Repetimos para la consulta de conteo ==========
        if (idCreador != null) {
            countPredicates.add(cb.equal(countRoot.get("creadoPor").get("idUsuario"), idCreador));
        }
        // ========== Repetimos para la consulta de conteo ==========
        if (idTecnico != null) {
            countPredicates.add(cb.equal(countRoot.get("asignadoA").get("idUsuario"), idTecnico));
        }
        // ========== Repetimos para la consulta de conteo ==========
        if (idCategoria != null) {
            countPredicates.add(cb.equal(countRoot.get("categoria").get("idCategoria"), idCategoria));
        }

        // Definimos la operación de conteo con todos los filtros aplicados
        countQuery.select(cb.count(countRoot)).where(cb.and(countPredicates.toArray(new Predicate[0])));

        // Ejecutamos la consulta y obtenemos el total de resultados
        Long total = entityManager.createQuery(countQuery).getSingleResult();

        // ============================
        // RETORNAMOS LA PÁGINA RESULTANTE
        // ============================
        // Devolvemos un PageImpl con la lista de tareas, la paginación original, y el total
        return new PageImpl<>(resultList, pageable, total);
    }
}
