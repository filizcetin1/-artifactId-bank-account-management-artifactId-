package com.example.demo.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "hesap_hareketi")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class HesapHareketi {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "hesap_id", nullable = false)
    private Hesap hesap;

    @Column(name = "hareket_turu", nullable = false, length = 20)
    @Enumerated(EnumType.STRING)
    private HareketTuru hareketTuru;

    @Column(name = "miktar", nullable = false, precision = 9, scale = 2)
    private BigDecimal miktar;

    @Column(name = "islem_tarihi", nullable = false)
    private LocalDateTime islemTarihi;

    @Column(name = "aciklama", length = 255)
    private String aciklama;

    @Column(name = "bakiye", nullable = false, precision = 9, scale = 2)
    private BigDecimal bakiye;
}