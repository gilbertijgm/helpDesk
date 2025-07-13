package com.helpdesk.helpDesk.utils;

import com.helpdesk.helpDesk.entities.Usuario;
import com.helpdesk.helpDesk.repository.UsuarioRepository;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class AuthUtil {

    private final UsuarioRepository usuarioRepository;
    private static UsuarioRepository staticUsuarioRepository;

    @PostConstruct
    public void init(){
        // Cargamos el repository en una variable estática para poder usarlo desde métodos estáticos
        staticUsuarioRepository = usuarioRepository;
    }

    //Metodo para obetener del contexto el usuario logueado
    public Usuario getUsuarioAutenticado() {
        //obtenemos al usuario autenticado desde el contexto de Spring Security, así:
        String username = SecurityContextHolder.getContext().getAuthentication().getName();


        return staticUsuarioRepository.findUserEntityByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("Usuario no encontrado"));
    }

}
