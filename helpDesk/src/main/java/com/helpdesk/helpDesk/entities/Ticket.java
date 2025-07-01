package com.helpdesk.helpDesk.entities;


import com.fasterxml.jackson.annotation.JsonIgnore;
import com.helpdesk.helpDesk.entities.enums.Estado;
import com.helpdesk.helpDesk.entities.enums.Prioridad;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Ticket {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_ticket")
    private Long idTicket;

    private String titulo;

    private String descripcion;

    @Enumerated(EnumType.STRING)
    private Estado estado;

    @Enumerated(EnumType.STRING)
    private Prioridad prioridad;

    @Column(name = "fecha_creacion")
    private LocalDateTime fechaCreacion;

    @Column(name = "fecha_resolucion")
    private LocalDateTime fechaResolucion;

    //Quien creo el ticket
    @ManyToOne
    @JoinColumn(name = "creado_por_id")
    private Usuario creadoPor;

    //A quien le fue asignado
    @ManyToOne
    @JoinColumn(name = "asignado_a_id")
    private Usuario asignadoA;

    // Categoría del ticket
    @ManyToOne
    @JoinColumn(name = "categoria_id")
    private Categoria categoria;

    @OneToMany(mappedBy = "ticket",cascade = CascadeType.ALL, fetch = FetchType.LAZY, orphanRemoval = true)
    @Column(name = "comentarios")
    @JsonIgnore
    private List<Comentario> comentarios = new ArrayList<>();

}
