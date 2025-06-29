package com.zetta.api.repository;

import com.zetta.api.model.UsuarioModel;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UsuarioRepository extends JpaRepository<UsuarioModel,Long> {
    Optional<UsuarioModel> findByEmail(String email);
    Optional<UsuarioModel> findById(Long id);
}
