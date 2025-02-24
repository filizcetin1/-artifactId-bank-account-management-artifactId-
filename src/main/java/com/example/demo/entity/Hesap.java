package com.example.demo.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "hesap")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Hesap {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    @Column(name = "hesap_sahip_kimlik_no", nullable = false, unique = true, length = 11)
    private String hesapSahipKimlikNo;

    @Column(name = "hesap_sahip_ad", length = 50)
    private String hesapSahipAd;

    @Column(name = "hesap_sahip_soyad", length = 50)
    private String hesapSahipSoyad;

    @Enumerated(EnumType.STRING)
    @Column(name = "hesap_turu", length = 20)
    private HesapTuru hesapTuru;

    @Column(name = "bakiye", precision = 9, scale = 2)
    private BigDecimal bakiye = BigDecimal.ZERO;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }
}