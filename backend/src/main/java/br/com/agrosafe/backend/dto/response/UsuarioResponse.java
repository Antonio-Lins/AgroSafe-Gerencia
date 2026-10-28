package br.com.agrosafe.backend.dto.response;

import br.com.agrosafe.backend.model.Perfil;
import br.com.agrosafe.backend.model.Usuario;

/** Nunca inclui a senha nem o hash. */
public record UsuarioResponse(
        Long id,
        String nome,
        String email,
        Perfil perfil
) {
    public static UsuarioResponse de(Usuario usuario) {
        return new UsuarioResponse(
                usuario.getId(),
                usuario.getNome(),
                usuario.getEmail(),
                usuario.getPerfil());
    }
}
