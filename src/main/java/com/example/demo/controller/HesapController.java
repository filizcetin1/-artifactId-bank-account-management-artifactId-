package com.example.demo.controller;

import com.example.demo.controller.dto.HesapDTO;
import com.example.demo.controller.dto.ParaIslemDTO;
import com.example.demo.service.HesapService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/hesaplar")
@RequiredArgsConstructor
@Tag(name = "Hesap İşlemleri", description = "Hesap yönetimi ve para işlemleri")
public class HesapController {
    private static final Logger logger = LoggerFactory.getLogger(HesapController.class);

    private final HesapService hesapService;

    @PostMapping
    @Operation(summary = "Yeni hesap oluştur")
    public ResponseEntity<HesapDTO> hesapOlustur(@Valid @RequestBody HesapDTO hesapDTO) {
        return ResponseEntity.ok(hesapService.hesapOlustur(hesapDTO));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Hesap bilgilerini güncelle")
    public ResponseEntity<HesapDTO> hesapGuncelle(@PathVariable UUID id, @Valid @RequestBody HesapDTO hesapDTO) {
        return ResponseEntity.ok(hesapService.hesapGuncelle(id, hesapDTO));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Hesabı sil")
    public ResponseEntity<Void> hesapSil(@PathVariable UUID id) {
        hesapService.hesapSil(id);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/{id}")
    @Operation(summary = "Hesap detaylarını getir")
    public ResponseEntity<HesapDTO> hesapGetir(@PathVariable UUID id) {
        return ResponseEntity.ok(hesapService.hesapGetir(id));
    }

    @GetMapping
    @Operation(summary = "Tüm hesapları listele")
    public ResponseEntity<List<HesapDTO>> tumHesaplariGetir() {
        return ResponseEntity.ok(hesapService.tumHesaplariGetir());
    }

    @PostMapping("/{id}/para-yatir")
    @Operation(summary = "Hesaba para yatır")
    public ResponseEntity<Void> paraYatir(@PathVariable UUID id, @RequestBody ParaIslemDTO paraIslemDTO) {
        logger.info("Para yatırma isteği alındı. HesapId: {}, Miktar: {}", id, paraIslemDTO.getMiktar());
        try {
            hesapService.paraYatir(id, paraIslemDTO.getMiktar());
            logger.info("Para yatırma işlemi başarıyla tamamlandı. HesapId: {}", id);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            logger.error("Para yatırma işlemi başarısız. HesapId: {}, Hata: {}", id, e.getMessage(), e);
            throw e;
        }
    }

    @PostMapping("/{id}/para-cek")
    @Operation(summary = "Hesaptan para çek")
    public ResponseEntity<Void> paraCek(@PathVariable UUID id, @Valid @RequestBody ParaIslemDTO paraIslemDTO) {
        hesapService.paraCek(id, paraIslemDTO.getMiktar());
        return ResponseEntity.ok().build();
    }
}