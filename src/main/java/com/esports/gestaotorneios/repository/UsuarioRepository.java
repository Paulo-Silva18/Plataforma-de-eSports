package com.esports.gestaotorneios.repository;

import com.esports.gestaotorneios.model.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UsuarioRepository extends JpaRepository<Usuario, Long> {
    
    // Método essencial para o Spring Security buscar o usuário no momento do login
    Optional<Usuario> findByUsername(String username);
}
