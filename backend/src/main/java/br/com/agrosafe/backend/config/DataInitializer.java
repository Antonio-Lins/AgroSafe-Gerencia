package br.com.agrosafe.backend.config;

import br.com.agrosafe.backend.model.Perfil;
import br.com.agrosafe.backend.model.Usuario;
import br.com.agrosafe.backend.repository.UsuarioRepository;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.security.crypto.password.PasswordEncoder;

// Classe responsável por criar usuários de teste GESTOR e OPERADOR

@Configuration
//@Profile("demo")
public class DataInitializer {

    @Bean
    CommandLineRunner criarUsuarios(
            UsuarioRepository repository,
            PasswordEncoder encoder) {

        return args -> {
            if (!repository.existsByEmailIgnoreCase(
                    "gestor@agrosafe.com")) {

                repository.save(new Usuario(
                        "Gestor AgroSafe",
                        "gestor@agrosafe.com",
                        encoder.encode("Gestor@123"),
                        Perfil.GESTOR
                ));
            }

            if (!repository.existsByEmailIgnoreCase(
                    "operador@agrosafe.com")) {

                repository.save(new Usuario(
                        "Operador AgroSafe",
                        "operador@agrosafe.com",
                        encoder.encode("Operador@123"),
                        Perfil.OPERADOR
                ));
            }

            System.out.println(
                    "Usuarios de demonstracao inicializados!"
            );
        };
    }
}