package com.example.demo.integration;

import com.example.demo.controller.dto.HesapDTO;
import com.example.demo.controller.dto.ParaIslemDTO;
import com.example.demo.model.HesapTuru;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.math.BigDecimal;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@AutoConfigureMockMvc
class HesapIntegrationTest {

        @Autowired
        private MockMvc mockMvc;

        @Autowired
        private ObjectMapper objectMapper;

        @Test
        void hesapOlusturVeParaYatir() throws Exception {
                // Hesap oluştur
                HesapDTO hesapDTO = new HesapDTO();
                hesapDTO.setHesapNo("TEST123");
                hesapDTO.setBakiye(BigDecimal.ZERO);
                hesapDTO.setHesapSahipKimlikNo("12345678901");
                hesapDTO.setHesapSahipAd("Test");
                hesapDTO.setHesapSahipSoyad("User");
                hesapDTO.setHesapTuru(HesapTuru.VADELI);
                hesapDTO.setMusteriAdi("Test User");

                String requestBody = objectMapper.writeValueAsString(hesapDTO);
                System.out.println("Request Body: " + requestBody); // Debug için

                MvcResult hesapResult = mockMvc.perform(post("/api/hesaplar")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(requestBody))
                                .andExpect(status().isOk())
                                .andReturn();

                String responseBody = hesapResult.getResponse().getContentAsString();
                System.out.println("Response Body: " + responseBody); // Debug için

                HesapDTO olusturulanHesap = objectMapper.readValue(responseBody, HesapDTO.class);
                assertNotNull(olusturulanHesap.getId());
                assertNotNull(olusturulanHesap.getHesapNo());

                // Para yatır
                ParaIslemDTO paraIslemDTO = new ParaIslemDTO();
                paraIslemDTO.setMiktar(new BigDecimal("500.00"));

                mockMvc.perform(post("/api/hesaplar/{id}/para-yatir", olusturulanHesap.getId())
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(paraIslemDTO)))
                                .andExpect(status().isOk());
        }
}