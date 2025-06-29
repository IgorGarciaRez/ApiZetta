package com.zetta.api.controller;

import com.zetta.api.dto.LoginRequest;
import com.zetta.api.dto.TokenResponse;
import com.zetta.api.service.JwtService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
public class AuthController {
    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private JwtService jwtService;

    @PostMapping("/login")
    public ResponseEntity<TokenResponse> login(@RequestBody LoginRequest login) {
        Authentication auth = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(login.email(), login.senha())
        );
        UserDetails user = (UserDetails) auth.getPrincipal();
        String token = jwtService.gerarToken(user);
        return ResponseEntity.ok(new TokenResponse(token));
    }
}
