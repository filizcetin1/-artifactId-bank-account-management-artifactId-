package com.example.demo.controller;

import com.example.demo.controller.dto.HesapDTO;
import com.example.demo.controller.dto.ParaIslemDTO;
import com.example.demo.entity.HesapTuru;
import com.example.demo.service.HesapService;
import com.example.demo.config.TestConfig;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(HesapController.class)
@ActiveProfiles("test")
@Import({ TestConfig.class })
class HesapControllerTest {

    @MockBean
    private HesapService hesapService;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    private HesapDTO testHesapDTO;
    private UUID hesapId;

    @BeforeEach
    void setUp() {
        hesapId = UUID.randomUUID();
        testHesapDTO = new HesapDTO();
        testHesapDTO.setId(hesapId);
        testHesapDTO.setHesapSahipKimlikNo("12345678901");
        testHesapDTO.setHesapSahipAd("Test");
        testHesapDTO.setHesapSahipSoyad("User");
        testHesapDTO.setHesapTuru(HesapTuru.VADELI);
        testHesapDTO.setBakiye(new BigDecimal("1000.00"));
    }

    @Test
    void hesapOlustur_BasariliDurum() throws Exception {
        when(hesapService.hesapOlustur(any(HesapDTO.class))).thenReturn(testHesapDTO);

        mockMvc.perform(post("/api/hesaplar")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(testHesapDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.hesapSahipKimlikNo").value(testHesapDTO.getHesapSahipKimlikNo()))
                .andExpect(jsonPath("$.bakiye").value(1000.0));
    }

    @Test
    void paraYatir_BasariliDurum() throws Exception {
        ParaIslemDTO paraIslemDTO = new ParaIslemDTO();
        paraIslemDTO.setMiktar(new BigDecimal("500.00"));

        mockMvc.perform(post("/api/hesaplar/{id}/para-yatir", hesapId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(paraIslemDTO)))
                .andExpect(status().isOk());
    }

    @Test
    void tumHesaplariGetir_BasariliDurum() throws Exception {
        when(hesapService.tumHesaplariGetir())
                .thenReturn(Arrays.asList(testHesapDTO));

        mockMvc.perform(get("/api/hesaplar"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].hesapSahipKimlikNo").value(testHesapDTO.getHesapSahipKimlikNo()));
    }

    @Test
    void hesapGetir_BasariliDurum() throws Exception {
        when(hesapService.hesapGetir(hesapId)).thenReturn(testHesapDTO);

        mockMvc.perform(get("/api/hesaplar/{id}", hesapId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.hesapSahipKimlikNo").value(testHesapDTO.getHesapSahipKimlikNo()));
    }
}