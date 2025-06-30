package com.zetta.api.service;

import com.zetta.api.model.UsuarioModel;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.util.Date;

@Service
public class JwtService {
    private static final String SECRET_KEY = "apiZetta2025-ProjetoTodoList-IgorGarciaRezende";

    public String gerarToken(UserDetails user) {
        UsuarioModel usuario = (UsuarioModel) user;

        return Jwts.builder()
                .setSubject(usuario.getEmail())
                .claim("role", usuario.getCargo().name()) // Adiciona o cargo como claim
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60)) // 1h
                .signWith(Keys.hmacShaKeyFor(SECRET_KEY.getBytes()), SignatureAlgorithm.HS256)
                .compact();
    }

    public String extrairEmail(String token){
        return Jwts.parserBuilder()
                .setSigningKey(Keys.hmacShaKeyFor(SECRET_KEY.getBytes()))
                .build()
                .parseClaimsJws(token)
                .getBody()
                .getSubject();
    }

    public boolean tokenExpirado(String token){
        return Jwts.parserBuilder()
                .setSigningKey(Keys.hmacShaKeyFor(SECRET_KEY.getBytes()))
                .build()
                .parseClaimsJws(token)
                .getBody()
                .getExpiration().before(new Date());
    }

    public boolean validarToken(String token, UserDetails user){
        final String email = extrairEmail(token);
        return email.equals(user.getUsername()) && !tokenExpirado(token);
    }
}
