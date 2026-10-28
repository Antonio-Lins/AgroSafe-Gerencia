package br.com.agrosafe.backend.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.HttpStatusEntryPoint;

/**
 * Segurança base. Nesta US só o essencial: tudo exige autenticação, exceto o endpoint de teste.
 * O login real (US-02) e as restrições por perfil (US-03) serão construídos sobre esta base.
 */
@Configuration
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                // API consumida por SPA, sem cookies de sessão por enquanto
                .csrf(AbstractHttpConfigurer::disable)
                // Sem login ainda: responde 401 (e não 403) para quem não está autenticado
                .exceptionHandling(ex -> ex
                        .authenticationEntryPoint(new HttpStatusEntryPoint(HttpStatus.UNAUTHORIZED)))
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(HttpMethod.GET, "/api/ping").permitAll()
                        .anyRequest().authenticated());
        return http.build();
    }

    /** Senhas sempre com hash BCrypt (RNF03). */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}