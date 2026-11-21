package br.com.agrosafe.backend.auth.dto;

import br.com.agrosafe.backend.usuario.Perfil;
import br.com.agrosafe.backend.usuario.Usuario;

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
