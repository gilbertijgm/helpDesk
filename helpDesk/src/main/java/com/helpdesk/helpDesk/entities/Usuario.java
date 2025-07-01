package com.helpdesk.helpDesk.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.helpdesk.helpDesk.entities.enums.Rol;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

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

    @Column(unique = true)
    private String username;
    private String password;

    private String  email;

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

    @ManyToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinTable(name = "user_roles", joinColumns = @JoinColumn(name = "user_id"), inverseJoinColumns = @JoinColumn(name = "role_id"))
    private Set<RoleEntity> roles = new HashSet<>();

    //atributos de springSecurity
    @Column(name = "is_enables")
    private boolean isEnabled;

    @Column(name = "account_No_Expired")
    private boolean accountNoExpired;

    @Column(name = "account_No_Locked")
    private boolean accountNoLocked;

    @Column(name = "credential_No_Expired")
    private boolean credentialNoExpired;


}
