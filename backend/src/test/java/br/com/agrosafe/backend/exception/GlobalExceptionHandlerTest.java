package br.com.agrosafe.backend.exception;

import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;

import static org.junit.jupiter.api.Assertions.assertEquals;

class GlobalExceptionHandlerTest {

    private final GlobalExceptionHandler handler = new GlobalExceptionHandler();

    @Test
    void violacaoDeRegraUsaOStatusInformado() {
        ProblemDetail resposta = handler.handleBusiness(
                new BusinessException("Talhão em quarentena", HttpStatus.CONFLICT));

        assertEquals(409, resposta.getStatus());
        assertEquals("Talhão em quarentena", resposta.getDetail());
    }

    @Test
    void violacaoDeRegraSemStatusUsa422() {
        ProblemDetail resposta = handler.handleBusiness(new BusinessException("Dado inválido"));

        assertEquals(422, resposta.getStatus());
    }

    @Test
    void recursoNaoEncontradoRetorna404() {
        ProblemDetail resposta = handler.handleNotFound(new ResourceNotFoundException("Talhão não existe"));

        assertEquals(404, resposta.getStatus());
    }
}