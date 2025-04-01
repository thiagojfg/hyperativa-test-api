package com.hyperativa.controllers;

import com.hyperativa.helpers.JwtUtil;
import com.hyperativa.services.card.CardService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.context.bean.override.mockito.MockitoSpyBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Collections;

import static org.mockito.ArgumentMatchers.any;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@ActiveProfiles("test")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
class CardControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoSpyBean
    private JwtUtil jwtUtil;

    @MockitoSpyBean
    private UserDetailsService userDetailsService;

    @MockitoBean
    private CardService cardService;

    @Test
    void shouldReturn403IfNotAuthenticated() throws Exception {
        mockMvc.perform(put("/api/cards")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isForbidden());
    }

    @Test
    void shouldReturnOk() throws Exception {
        String cardNumber = "1234123413241241";

        UserDetails user = new User("user", "pass", Collections.emptyList());
        Mockito.doReturn(true).when(jwtUtil).validateToken(any(), any());
        Mockito.doReturn("user").when(jwtUtil).extractUsername(any());
        Mockito.doReturn(user).when(userDetailsService).loadUserByUsername(any());
        Mockito.when(cardService.saveCardNumber(cardNumber)).thenReturn(1L);

        mockMvc.perform(put("/api/cards?cardNumber=" + cardNumber)
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", "Bearer token"))
                .andExpect(status().isOk());
    }
}