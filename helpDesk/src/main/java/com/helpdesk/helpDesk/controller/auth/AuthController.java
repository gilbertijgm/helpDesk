package com.helpdesk.helpDesk.controller.auth;

import com.helpdesk.helpDesk.controller.dto.auth.AuthCreateUserRequest;
import com.helpdesk.helpDesk.controller.dto.auth.AuthLoginRequest;
import com.helpdesk.helpDesk.controller.dto.auth.AuthResponse;
import com.helpdesk.helpDesk.service.UserDetailsServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final UserDetailsServiceImpl userDetailsService;

    //endpoint para registrar un nuevo usuario
    @PostMapping("/signup")
    public ResponseEntity<AuthResponse> register(@RequestBody @Validated AuthCreateUserRequest authCreateUser){
        return new ResponseEntity<>(this.userDetailsService.createUser(authCreateUser), HttpStatus.CREATED);
    }

    //endpoint para inicio de sesion
    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@RequestBody @Validated AuthLoginRequest userRequest){
        return new ResponseEntity<>(this.userDetailsService.loginUser(userRequest), HttpStatus.OK);
    }
}
