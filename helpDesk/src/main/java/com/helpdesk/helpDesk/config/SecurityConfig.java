package com.helpdesk.helpDesk.config;

import com.helpdesk.helpDesk.config.filter.JwtTokenValidator;
import com.helpdesk.helpDesk.service.UserDetailsServiceImpl;
import com.helpdesk.helpDesk.utils.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

@Configuration // Indicamos que esta clase define configuraciones para el contexto de Spring
@EnableWebSecurity // Habilita la seguridad web en Spring Security
@EnableMethodSecurity // Permite usar anotaciones como @PreAuthorize y @Secured a nivel de métodos
@RequiredArgsConstructor // Genera un constructor con todos los campos marcados como final
public class SecurityConfig {

    // Inyectamos la utilidad que maneja JWT (generación y validación de tokens)
    private final JwtUtil jwtUtils;

    // Bean que define la cadena de filtros de seguridad (SecurityFilterChain)
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
        return httpSecurity
                // Desactiva CSRF porque se trabaja con JWT (stateless)
                .csrf(csrf -> csrf.disable())

                // Activa autenticación básica (útil para pruebas con Postman, por ejemplo)
                .httpBasic(Customizer.withDefaults())

                // Configura que las sesiones no se almacenan (modo stateless)
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))

                // Define las reglas de autorización por tipo de solicitud y endpoint
                .authorizeHttpRequests(http -> {
                    // ===== ENDPOINTS PÚBLICOS =====
                    http.requestMatchers(HttpMethod.POST, "/auth/login").permitAll();      // Acceso libre a POST /auth/login

                    // ===== ENDPOINTS PRIVADOS (requieren roles) =====
                    http.requestMatchers(HttpMethod.POST, "/auth/signup")
                            .hasAnyRole("ADMIN");          // Solo accesible para rol admin

                    // ==== ENDPOINT DE TICKETS ======
                    http.requestMatchers(HttpMethod.POST, "/api/ticket/crear")
                            .hasAnyRole("ADMIN","CLIENTE","TECNICO");

                    // ==== ENDPOINT DE CATEGORIA ======
                    http.requestMatchers(HttpMethod.GET, "/api/categoria/categorias")
                            .permitAll();
                    http.requestMatchers(HttpMethod.GET, "/api/categoria/categoria-id/{id}")
                            .permitAll();
                    http.requestMatchers(HttpMethod.POST, "/api/categoria/crear")
                            .hasAnyRole("ADMIN");
                    http.requestMatchers(HttpMethod.DELETE, "/api/categoria/eliminar/{id}")
                            .hasAnyRole("ADMIN");
                })

                // Agrega un filtro personalizado que valida el JWT antes de que llegue al filtro básico de autenticación
                .addFilterBefore(new JwtTokenValidator(jwtUtils), BasicAuthenticationFilter.class)

                // Construye y devuelve el filtro configurado
                .build();
    }

    /*
     * Bean que expone el AuthenticationManager, encargado de autenticar usuarios.
     * Se obtiene desde la configuración interna de Spring.
     */
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

    /*
     * Bean que define cómo se debe autenticar un usuario (fuente de datos, codificación, etc.)
     */
    @Bean
    public AuthenticationProvider authenticationProvider(UserDetailsServiceImpl userDetailsService){
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider(); // Autenticador basado en datos de usuario persistidos
        provider.setPasswordEncoder(passwordEncoder()); // Configura el codificador de contraseñas
        provider.setUserDetailsService(userDetailsService); // Inyecta el servicio que carga los usuarios desde la base de datos

        return provider;
    }

    /*
     * Bean que proporciona el codificador de contraseñas.
     * BCrypt es un algoritmo seguro y ampliamente usado para proteger contraseñas.
     */
    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }
}
