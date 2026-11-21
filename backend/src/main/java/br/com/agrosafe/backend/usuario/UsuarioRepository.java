package br.com.agrosafe.backend.usuario;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UsuarioRepository extends JpaRepository<Usuario, Long> {

    Optional<Usuario> findByEmailIgnoreCase(String email);

    /** Usado pela carga inicial (US-04) para não duplicar usuários a cada reinício. */
    boolean existsByEmailIgnoreCase(String email);
}
