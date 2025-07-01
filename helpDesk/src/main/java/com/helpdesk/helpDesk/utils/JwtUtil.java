package com.helpdesk.helpDesk.utils;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.Claim;
import com.auth0.jwt.interfaces.DecodedJWT;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Component;


import java.util.Date;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

@Component
// Esta clase será gestionada como un componente por Spring (se puede inyectar con @Autowired o @RequiredArgsConstructor)
public class JwtUtil {
    // Clave secreta (privada) para firmar y verificar el JWT (viene del application.properties)
    @Value("${security.jwt.key.private}")
    private String privateKey;

    // Nombre del generador del token (Issuer), puede ser el nombre de la app o empresa
    @Value("${security.jwt.user.generator}")
    private String userGenerator;

    /**
     * Genera un token JWT a partir de la información de autenticación.
     */
    public String createToken(Authentication authentication){
        // Creamos el algoritmo de firma HMAC con la clave privada
        Algorithm algorithm = Algorithm.HMAC256(this.privateKey);

        // Obtenemos el nombre del usuario autenticado
        String username = authentication.getPrincipal().toString();

        // Convertimos las autoridades a un string separado por comas
        String authorities = authentication.getAuthorities()
                .stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.joining(","));

        // Creamos el token JWT con claims personalizados
        String jwtToken = JWT.create()
                .withIssuer(this.userGenerator)                            // Quién genera el token
                .withSubject(username)                                     // A quién pertenece el token (usuario)
                .withClaim("authorities", authorities)                     // Roles y permisos
                .withIssuedAt(new Date())                                  // Fecha de emisión
                .withExpiresAt(new Date(System.currentTimeMillis() + 1800000)) // Expira en 30 minutos
                .withJWTId(UUID.randomUUID().toString())                   // ID único del token
                .withNotBefore(new Date(System.currentTimeMillis()))       // El token es válido desde ahora
                .sign(algorithm);                                          // Firmamos el token

        return jwtToken; // Devolvemos el token generado
    }

    /**
     * Valida que un token JWT sea válido (firma, emisor, etc.).
     * Si es válido, devuelve su contenido decodificado.
     */
    public DecodedJWT validateToken(String token){
        try {
            // Creamos el algoritmo con la misma clave usada para firmar
            Algorithm algorithm = Algorithm.HMAC256(this.privateKey);

            // Creamos el verificador del token
            JWTVerifier verifier = JWT.require(algorithm)
                    .withIssuer(this.userGenerator) // Debe coincidir el emisor
                    .build();

            // Verificamos la validez del token y lo decodificamos
            DecodedJWT decodedJWT = verifier.verify(token);

            return decodedJWT;
        } catch (JWTVerificationException exception){
            // Si el token no es válido (firma incorrecta, expirado, etc.)
            throw new JWTVerificationException("Token invalid, not Authorized");
        }
    }

    /**
     * Extrae el nombre de usuario (subject) desde un token ya validado.
     */
    public String extractUsername(DecodedJWT decodedJWT){
        return decodedJWT.getSubject().toString();
    }

    /**
     * Devuelve un claim específico (atributo) del token.
     * Ej: roles, nombre, email, etc.
     */
    public Claim getSpecificClaim(DecodedJWT decodedJWT, String claimName){
        return decodedJWT.getClaim(claimName);
    }

    /**
     * Devuelve todos los claims (atributos) contenidos en el token.
     */
    public Map<String, Claim> returnAllClaims(DecodedJWT decodedJWT){
        return decodedJWT.getClaims();
    }
}
