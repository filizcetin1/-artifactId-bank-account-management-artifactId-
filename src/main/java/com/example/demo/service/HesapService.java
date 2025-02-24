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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class HesapService {

    private static final Logger logger = LoggerFactory.getLogger(HesapService.class);

    private final HesapRepository hesapRepository;
    private final HesapHareketiRepository hareketRepository;
    private final HesapMapper hesapMapper;

    public HesapService(HesapRepository hesapRepository, HesapHareketiRepository hareketRepository,
            HesapMapper hesapMapper) {
        this.hesapRepository = hesapRepository;
        this.hareketRepository = hareketRepository;
        this.hesapMapper = hesapMapper;
    }

    @Transactional
    public HesapDTO hesapOlustur(HesapDTO hesapDTO) {
        // Kimlik no kontrolü
        if (hesapRepository.existsByHesapSahipKimlikNo(hesapDTO.getHesapSahipKimlikNo())) {
            throw new BusinessException("Bu kimlik numarasına ait hesap zaten mevcut.");
        }

        Hesap hesap = hesapMapper.toEntity(hesapDTO);
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
                .orElseThrow(() -> new HesapNotFoundException("Hesap bulunamadı: " + id));

        if (hesap.getBakiye().compareTo(BigDecimal.ZERO) != 0) {
            throw new BusinessException("Bakiyesi olan hesap silinemez.");
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
                .collect(Collectors.toList());
    }

    @Transactional
    public void paraYatir(UUID hesapId, BigDecimal miktar) {
        logger.debug("Para yatırma işlemi başlatıldı. HesapId: {}, Miktar: {}", hesapId, miktar);
        try {
            if (miktar.compareTo(BigDecimal.ZERO) <= 0) {
                logger.error("Geçersiz miktar: {}", miktar);
                throw new BusinessException("Yatırılacak miktar sıfırdan büyük olmalıdır");
            }

            Hesap hesap = hesapRepository.findById(hesapId)
                    .orElseThrow(() -> {
                        logger.error("Hesap bulunamadı: {}", hesapId);
                        return new HesapNotFoundException("Hesap bulunamadı: " + hesapId);
                    });

            BigDecimal yeniBakiye = hesap.getBakiye().add(miktar);
            hesap.setBakiye(yeniBakiye);
            hesapRepository.save(hesap);
            logger.info("Para yatırma işlemi başarılı. HesapId: {}, Miktar: {}, Yeni Bakiye: {}", 
                    hesapId, miktar, yeniBakiye);

            HesapHareketi hareket = new HesapHareketi();
            hareket.setHesap(hesap);
            hareket.setHareketTuru(HareketTuru.PARA_YATIRMA);
            hareket.setMiktar(miktar);
            hareket.setIslemTarihi(LocalDateTime.now());
            hareket.setBakiye(yeniBakiye);
            hareket.setAciklama("Para yatırma işlemi");
            hareketRepository.save(hareket);
            logger.debug("Hesap hareketi kaydedildi. HesapId: {}, İşlem: {}", hesapId, hareket);
        } catch (Exception e) {
            logger.error("Para yatırma işlemi sırasında hata oluştu. HesapId: {}, Hata: {}", 
                    hesapId, e.getMessage(), e);
            throw e;
        }
    }

    @Transactional
    public void paraCek(UUID hesapId, BigDecimal miktar) {
        if (miktar.compareTo(BigDecimal.ZERO) <= 0) {
            throw new BusinessException("Çekilecek miktar sıfırdan büyük olmalıdır");
        }

        Hesap hesap = hesapRepository.findById(hesapId)
                .orElseThrow(() -> new HesapNotFoundException("Hesap bulunamadı: " + hesapId));

        if (hesap.getBakiye().compareTo(miktar) < 0) {
            throw new YetersizBakiyeException("Yetersiz bakiye");
        }

        BigDecimal yeniBakiye = hesap.getBakiye().subtract(miktar);
        hesap.setBakiye(yeniBakiye);
        hesapRepository.save(hesap);

        HesapHareketi hareket = new HesapHareketi();
        hareket.setHesap(hesap);
        hareket.setHareketTuru(HareketTuru.PARA_CEKME);
        hareket.setMiktar(miktar);
        hareket.setIslemTarihi(LocalDateTime.now());
        hareket.setBakiye(yeniBakiye);
        hareket.setAciklama("Para çekme işlemi");
        hareketRepository.save(hareket);
    }
}