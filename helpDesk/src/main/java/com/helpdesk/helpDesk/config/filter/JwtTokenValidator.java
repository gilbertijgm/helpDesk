package com.helpdesk.helpDesk.config.filter;

import com.auth0.jwt.interfaces.DecodedJWT;
import com.helpdesk.helpDesk.utils.JwtUtil;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Collection;

@RequiredArgsConstructor // Genera constructor con los atributos finales (en este caso, jwtUtils)
public class  JwtTokenValidator extends OncePerRequestFilter {
    // Clase personalizada que extiende OncePerRequestFilter: asegura que el filtro se ejecuta solo una vez por solicitud

    private final JwtUtil jwtUtils; // Utilidad encargada de validar y procesar el token JWT

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {
        // Este método se ejecuta en cada solicitud HTTP entrante, antes de llegar al controlador

        // Extraemos el encabezado Authorization de la solicitud
        String jwtToken = request.getHeader(HttpHeaders.AUTHORIZATION);

        // Si el token existe (no es null)
        if (jwtToken != null) {
            // Quitamos el prefijo "Bearer " (los primeros 7 caracteres) para obtener solo el token
            jwtToken = jwtToken.substring(7);

            // Validamos el token y obtenemos su contenido decodificado
            DecodedJWT decodedJWT = jwtUtils.validateToken(jwtToken);

            // Extraemos el nombre de usuario del token
            String username = jwtUtils.extractUsername(decodedJWT);

            // Extraemos las autoridades (roles/permisos) desde un claim personalizado llamado "authorities"
            String stringAuthorities = jwtUtils.getSpecificClaim(decodedJWT, "authorities").asString();

            // Convertimos la lista de roles en formato string (separada por comas) a objetos GrantedAuthority
            Collection<? extends GrantedAuthority> authorities =
                    AuthorityUtils.commaSeparatedStringToAuthorityList(stringAuthorities);

            // Obtenemos el contexto de seguridad actual
            SecurityContext context = SecurityContextHolder.getContext();

            // Creamos una autenticación basada en el usuario extraído y sus roles, sin necesidad de contraseña
            Authentication authentication =
                    new UsernamePasswordAuthenticationToken(username, null, authorities);

            // Establecemos esa autenticación en el contexto actual
            context.setAuthentication(authentication);

            // Finalmente, volvemos a establecer el contexto con la autenticación aplicada
            SecurityContextHolder.setContext(context);
        }

        // Pasamos la solicitud al siguiente filtro en la cadena
        filterChain.doFilter(request, response);
    }


}