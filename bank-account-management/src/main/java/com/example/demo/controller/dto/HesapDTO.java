package com.example.demo.controller.dto;

import lombok.Data;
import java.math.BigDecimal;
import java.util.UUID;
import com.example.demo.model.HesapTuru;

@Data
public class HesapDTO {
    private UUID id;
    private String hesapNo;
    private BigDecimal bakiye;
    private String musteriAdi;
    private String hesapSahipKimlikNo; // Account holder's identification number
    private String hesapSahipAd;
    private String hesapSahipSoyad;
    private HesapTuru hesapTuru;
}