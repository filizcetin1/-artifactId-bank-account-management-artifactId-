package com.example.demo.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "hesap_hareketi")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class HesapHareketi {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "hesap_id", nullable = false)
    private Hesap hesap;

    @Column(name = "islem_tarihi", nullable = false)
    private LocalDateTime islemTarihi;

    @Enumerated(EnumType.STRING)
    @Column(name = "hareket_turu", nullable = false)
    private HareketTuru hareketTuru;

    @Column(name = "miktar", precision = 9, scale = 2, nullable = false)
    private BigDecimal miktar;
}