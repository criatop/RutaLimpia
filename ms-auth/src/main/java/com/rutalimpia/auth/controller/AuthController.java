package com.rutalimpia.auth.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.rutalimpia.auth.dto.AuthResponse;
import com.rutalimpia.auth.dto.LoginRequest;
import com.rutalimpia.auth.dto.RegisterRequest;
import com.rutalimpia.auth.dto.TokenInfo;
import com.rutalimpia.auth.dto.UsuarioResponse;
import com.rutalimpia.auth.service.AuthService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/register")
    public ResponseEntity<UsuarioResponse> registrar(@Valid @RequestBody RegisterRequest req) {
        return ResponseEntity.status(HttpStatus.CREATED).body(authService.registrar(req));
    }

    @PostMapping("/login")
    public AuthResponse login(@Valid @RequestBody LoginRequest req) {
        return authService.login(req);
    }

    /** Lo puede usar el API Gateway (authorizer) para validar el JWT y conocer el rol. */
    @GetMapping("/validate")
    public TokenInfo validar(@RequestHeader(value = "Authorization", required = false) String authorization) {
        return authService.validar(authorization);
    }
}
