package br.com.agrosafe.backend.security;

import br.com.agrosafe.backend.model.Usuario;
import br.com.agrosafe.backend.repository.UsuarioRepository;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Carrega o usuário pelo e-mail para o Spring Security.
 * O Perfil vira a autoridade ROLE_GESTOR ou ROLE_OPERADOR, que é o que o
 * hasRole('GESTOR') dos controllers (US-03) espera.
 */
@Service
public class UsuarioDetailsService implements UserDetailsService {

    private final UsuarioRepository usuarioRepository;

    public UsuarioDetailsService(UsuarioRepository usuarioRepository) {
        this.usuarioRepository = usuarioRepository;
    }

    @Override
    @Transactional(readOnly = true)
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Usuario usuario = usuarioRepository.findByEmailIgnoreCase(email.strip())
                .orElseThrow(() -> new UsernameNotFoundException("Usuário não encontrado."));

        return User.withUsername(usuario.getEmail())
                .password(usuario.getSenhaHash())
                .roles(usuario.getPerfil().name())
                .build();
    }
}
