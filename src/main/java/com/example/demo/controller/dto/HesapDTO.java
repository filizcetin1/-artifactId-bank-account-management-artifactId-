package com.example.demo.controller.dto;

import lombok.Data;
import java.math.BigDecimal;
import java.util.UUID;
import com.example.demo.entity.HesapTuru;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class HesapDTO {
    private UUID id;
    private String hesapSahipKimlikNo;
    private String hesapSahipAd;
    private String hesapSahipSoyad;
    private HesapTuru hesapTuru;
    private BigDecimal bakiye;
}