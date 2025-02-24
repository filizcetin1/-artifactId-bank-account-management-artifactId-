package com.example.demo.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.UUID;

@Entity
@Table(name = "hesap")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Hesap {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(name = "hesap_sahip_kimlik_no", nullable = false)
    private String hesapSahipKimlikNo;

    @Column(name = "hesap_sahip_ad", nullable = false)
    private String hesapSahipAd;

    @Column(name = "hesap_sahip_soyad", nullable = false)
    private String hesapSahipSoyad;

    @Enumerated(EnumType.STRING)
    @Column(name = "hesap_turu", nullable = false)
    private HesapTuru hesapTuru;

    @Column(name = "bakiye", precision = 9, scale = 2, nullable = false)
    private BigDecimal bakiye = BigDecimal.ZERO;

    @Column(name = "hesap_no", nullable = false)
    private String hesapNo;

    @Column(name = "musteri_adi", nullable = false)
    private String musteriAdi;
}