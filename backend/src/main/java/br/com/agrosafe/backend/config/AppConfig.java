package br.com.agrosafe.backend.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.Clock;
import java.time.ZoneId;

@Configuration
public class AppConfig {

    /**
     * Relógio injetável. As regras de carência dependem de "hoje" (RN01, RN04, RN07)
     * injetar o Clock permite testá-las com datas fixas
     */
    @Bean
    public Clock clock() {
        return Clock.system(ZoneId.of("America/Fortaleza"));
    }
}