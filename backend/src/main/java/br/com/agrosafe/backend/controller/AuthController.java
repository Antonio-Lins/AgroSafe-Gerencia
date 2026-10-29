package br.com.agrosafe.backend.controller;

import br.com.agrosafe.backend.dto.request.LoginRequest;
import br.com.agrosafe.backend.dto.response.UsuarioResponse;
import br.com.agrosafe.backend.service.UsuarioService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.context.SecurityContextHolderStrategy;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;
import org.springframework.security.web.context.SecurityContextRepository;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

/**
 * Login por JSON com sessão (cookie). O front-end (React) deve enviar as
 * requisições com credenciais (cookies) habilitadas.
 *
 * - POST /api/auth/login  : autentica e abre a sessão
 * - POST /api/auth/logout : encerra a sessão
 * - GET  /api/auth/me     : devolve o usuário logado (nome e perfil)
 */
@RestController
@RequestMapping("/api/auth")
public class AuthController {

    /** Mensagem única: não revela se o e-mail existe (evita enumeração de usuários). */
    private static final String CREDENCIAIS_INVALIDAS = "E-mail ou senha inválidos.";

    private final AuthenticationManager authenticationManager;
    private final UsuarioService usuarioService;

    private final SecurityContextRepository securityContextRepository = new HttpSessionSecurityContextRepository();
    private final SecurityContextHolderStrategy securityContextHolderStrategy =
            SecurityContextHolder.getContextHolderStrategy();
    private final SecurityContextLogoutHandler logoutHandler = new SecurityContextLogoutHandler();

    public AuthController(AuthenticationManager authenticationManager, UsuarioService usuarioService) {
        this.authenticationManager = authenticationManager;
        this.usuarioService = usuarioService;
    }

    @PostMapping("/login")
    public ResponseEntity<Object> login(@Valid @RequestBody LoginRequest request,
                                        HttpServletRequest httpRequest,
                                        HttpServletResponse httpResponse) {
        Authentication autenticacao;
        try {
            autenticacao = authenticationManager.authenticate(
                    UsernamePasswordAuthenticationToken.unauthenticated(request.email().strip(), request.senha()));
        } catch (AuthenticationException ex) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(ProblemDetail.forStatusAndDetail(HttpStatus.UNAUTHORIZED, CREDENCIAIS_INVALIDAS));
        }

        // Proteção contra fixação de sessão: troca o id de uma sessão que já existia.
        if (httpRequest.getSession(false) != null) {
            httpRequest.changeSessionId();
        }

        SecurityContext contexto = securityContextHolderStrategy.createEmptyContext();
        contexto.setAuthentication(autenticacao);
        securityContextHolderStrategy.setContext(contexto);
        securityContextRepository.saveContext(contexto, httpRequest, httpResponse);

        return ResponseEntity.ok(usuarioService.buscarPorEmail(autenticacao.getName()));
    }

    @PostMapping("/logout")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void logout(HttpServletRequest request, HttpServletResponse response) {
        logoutHandler.logout(request, response, SecurityContextHolder.getContext().getAuthentication());
    }

    @GetMapping("/me")
    public UsuarioResponse me(Authentication authentication) {
        return usuarioService.buscarPorEmail(authentication.getName());
    }
}
