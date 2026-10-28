package br.com.agrosafe.backend.controller;

import br.com.agrosafe.backend.dto.response.PingResponse;
import br.com.agrosafe.backend.service.PingService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.time.Instant;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
class PingControllerTest {

    @Mock
    private PingService pingService;

    @InjectMocks
    private PingController controller;

    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(controller).build();
    }

    @Test
    void getPingResponde200ComStatusOk() throws Exception {
        when(pingService.ping())
                .thenReturn(new PingResponse("ok", "agrosafe-backend", Instant.parse("2026-10-28T12:00:00Z")));

        mockMvc.perform(get("/api/ping"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("ok"))
                .andExpect(jsonPath("$.aplicacao").value("agrosafe-backend"));
    }
}