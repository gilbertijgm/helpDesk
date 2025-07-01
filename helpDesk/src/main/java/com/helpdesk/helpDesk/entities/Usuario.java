package com.helpdesk.helpDesk.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.helpdesk.helpDesk.entities.enums.Rol;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Usuario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_usuario")
    private Long idUsuario;

    @Column(name = "nombre_usuario")
    private String nombreUsuario;

    private String  email;

    @Enumerated(EnumType.STRING)
    private Rol rol;

    private boolean activo;

    // Tickets creados por el usuario (cliente)
    @OneToMany(mappedBy = "creadoPor", cascade = CascadeType.ALL, fetch = FetchType.LAZY, orphanRemoval = true)
    @Column(name = "tickets_creadosPor")
    @JsonIgnore
    private List<Ticket> listaTicketsCreados = new ArrayList<>();

    // Tickets asignados a este usuario (agente, admin)
    @OneToMany(mappedBy = "asignadoA", cascade = CascadeType.ALL, fetch = FetchType.LAZY, orphanRemoval = true)
    @Column(name = "tickets_asignadosA")
    @JsonIgnore
    private List<Ticket> listaTicketsAsignadosA = new ArrayList<>();
}
