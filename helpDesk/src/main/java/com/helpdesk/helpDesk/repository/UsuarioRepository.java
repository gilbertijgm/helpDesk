package com.helpdesk.helpDesk.repository;

import com.helpdesk.helpDesk.entities.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UsuarioRepository extends JpaRepository<Usuario, Long> {

    Optional<Usuario> findUserEntityByUsername(String username);
}
