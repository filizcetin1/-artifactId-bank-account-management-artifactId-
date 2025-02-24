package com.example.demo.service;

import com.example.demo.controller.dto.HesapDTO;
import com.example.demo.entity.Hesap;
import com.example.demo.entity.HesapHareketi;
import com.example.demo.entity.HareketTuru;
import com.example.demo.exception.BusinessException;
import com.example.demo.exception.HesapNotFoundException;
import com.example.demo.exception.YetersizBakiyeException;
import com.example.demo.mapper.HesapMapper;
import com.example.demo.repository.HesapHareketiRepository;
import com.example.demo.repository.HesapRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class HesapService {
    private final HesapRepository hesapRepository;
    private final HesapHareketiRepository hareketRepository;
    private final HesapMapper hesapMapper;

    private static final BigDecimal MAX_BAKIYE = new BigDecimal("9999999.00");

    @Transactional
    public HesapDTO hesapOlustur(HesapDTO hesapDTO) {
        if (hesapDTO.getHesapNo() == null || hesapDTO.getHesapNo().trim().isEmpty()) {
            throw new BusinessException("Hesap numarası boş olamaz");
        }

        if (hesapRepository.existsByHesapNo(hesapDTO.getHesapNo())) {
            throw new BusinessException("Bu hesap numarası zaten kullanımda");
        }

        Hesap hesap = hesapMapper.toEntity(hesapDTO);

        if (hesap.getBakiye() == null) {
            hesap.setBakiye(BigDecimal.ZERO);
        }

        if (hesap.getHesapNo() == null || hesap.getHesapSahipKimlikNo() == null ||
                hesap.getHesapTuru() == null) {
            throw new BusinessException("Zorunlu alanlar boş olamaz");
        }

        hesap = hesapRepository.save(hesap);
        return hesapMapper.toDTO(hesap);
    }

    @Transactional
    public HesapDTO hesapGuncelle(UUID id, HesapDTO hesapDTO) {
        Hesap hesap = hesapRepository.findById(id)
                .orElseThrow(() -> new BusinessException("Hesap bulunamadı"));

        hesap.setHesapSahipAd(hesapDTO.getHesapSahipAd());
        hesap.setHesapSahipSoyad(hesapDTO.getHesapSahipSoyad());

        hesap = hesapRepository.save(hesap);
        return hesapMapper.toDTO(hesap);
    }

    @Transactional
    public void hesapSil(UUID id) {
        Hesap hesap = hesapRepository.findById(id)
                .orElseThrow(() -> new BusinessException("Hesap bulunamadı"));

        if (hesap.getBakiye().compareTo(BigDecimal.ZERO) > 0) {
            throw new BusinessException("Bakiyesi olan hesap silinemez");
        }

        hesapRepository.delete(hesap);
    }

    @Transactional(readOnly = true)
    public HesapDTO hesapGetir(UUID id) {
        Hesap hesap = hesapRepository.findById(id)
                .orElseThrow(() -> new HesapNotFoundException("Hesap bulunamadı: " + id));
        return hesapMapper.toDTO(hesap);
    }

    @Transactional(readOnly = true)
    public List<HesapDTO> tumHesaplariGetir() {
        return hesapRepository.findAll().stream()
                .map(hesapMapper::toDTO)
                .toList();
    }

    @Transactional
    public void paraYatir(UUID hesapId, BigDecimal miktar) {
        Hesap hesap = hesapRepository.findById(hesapId)
                .orElseThrow(() -> new HesapNotFoundException("Hesap bulunamadı: " + hesapId));

        BigDecimal yeniBakiye = hesap.getBakiye().add(miktar);
        if (yeniBakiye.compareTo(MAX_BAKIYE) > 0) {
            throw new BusinessException("Maksimum bakiye sınırı aşılamaz: " + MAX_BAKIYE);
        }

        hesap.setBakiye(yeniBakiye);
        hesapRepository.save(hesap);

        kaydetHareket(hesap, HareketTuru.PARA_YATIRMA, miktar);
    }

    @Transactional
    public void paraCek(UUID hesapId, BigDecimal miktar) {
        Hesap hesap = hesapRepository.findById(hesapId)
                .orElseThrow(() -> new HesapNotFoundException("Hesap bulunamadı: " + hesapId));
        if (hesap.getBakiye().compareTo(miktar) < 0) {
            throw new YetersizBakiyeException("Yetersiz bakiye");
        }
        hesap.setBakiye(hesap.getBakiye().subtract(miktar));
        hesapRepository.save(hesap);

        kaydetHareket(hesap, HareketTuru.PARA_CEKME, miktar);
    }

    private void kaydetHareket(Hesap hesap, HareketTuru hareketTuru, BigDecimal miktar) {
        HesapHareketi hareket = new HesapHareketi();
        hareket.setHesap(hesap);
        hareket.setIslemTarihi(LocalDateTime.now());
        hareket.setHareketTuru(hareketTuru);
        hareket.setMiktar(miktar);
        hareketRepository.save(hareket);
    }
}