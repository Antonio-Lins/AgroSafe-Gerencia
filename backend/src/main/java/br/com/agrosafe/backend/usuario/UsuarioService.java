package br.com.agrosafe.backend.usuario;

import br.com.agrosafe.backend.auth.dto.UsuarioResponse;
import br.com.agrosafe.backend.shared.exception.ResourceNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UsuarioService {

    private final UsuarioRepository usuarioRepository;

    public UsuarioService(UsuarioRepository usuarioRepository) {
        this.usuarioRepository = usuarioRepository;
    }

    @Transactional(readOnly = true)
    public UsuarioResponse buscarPorEmail(String email) {
        return usuarioRepository.findByEmailIgnoreCase(email.strip())
                .map(UsuarioResponse::de)
                .orElseThrow(() -> new ResourceNotFoundException("Usuário não encontrado."));
    }
}
