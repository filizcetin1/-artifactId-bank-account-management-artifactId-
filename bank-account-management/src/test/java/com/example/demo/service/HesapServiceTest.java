package com.example.demo.service;

import com.example.demo.controller.dto.HesapDTO;
import com.example.demo.entity.Hesap;
import com.example.demo.entity.HesapTuru;
import com.example.demo.exception.BusinessException;
import com.example.demo.exception.HesapNotFoundException;
import com.example.demo.exception.YetersizBakiyeException;
import com.example.demo.mapper.HesapMapper;
import com.example.demo.repository.HesapHareketiRepository;
import com.example.demo.repository.HesapRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class HesapServiceTest {

    @Mock
    private HesapRepository hesapRepository;

    @Mock
    private HesapHareketiRepository hareketRepository;

    @Mock
    private HesapMapper hesapMapper;

    @InjectMocks
    private HesapService hesapService;

    private Hesap testHesap;
    private HesapDTO testHesapDTO;
    private UUID hesapId;

    @BeforeEach
    void setUp() {
        hesapId = UUID.randomUUID();

        testHesap = new Hesap();
        testHesap.setId(hesapId);
        testHesap.setHesapNo("123456");
        testHesap.setBakiye(new BigDecimal("1000.00"));
        testHesap.setHesapSahipKimlikNo("12345678901");
        testHesap.setHesapSahipAd("Test");
        testHesap.setHesapSahipSoyad("User");
        testHesap.setHesapTuru(HesapTuru.VADELI);
        testHesap.setMusteriAdi("Test User");

        testHesapDTO = new HesapDTO();
        testHesapDTO.setId(hesapId);
        testHesapDTO.setHesapNo("123456");
        testHesapDTO.setBakiye(new BigDecimal("1000.00"));
        testHesapDTO.setHesapSahipKimlikNo("12345678901");
        testHesapDTO.setHesapSahipAd("Test");
        testHesapDTO.setHesapSahipSoyad("User");
        testHesapDTO.setHesapTuru(com.example.demo.model.HesapTuru.VADELI);
        testHesapDTO.setMusteriAdi("Test User");
    }

    @Test
    void hesapOlustur_BasariliDurum() {
        when(hesapRepository.existsByHesapNo(anyString())).thenReturn(false);
        when(hesapMapper.toEntity(any(HesapDTO.class))).thenReturn(testHesap);
        when(hesapRepository.save(any(Hesap.class))).thenReturn(testHesap);
        when(hesapMapper.toDTO(any(Hesap.class))).thenReturn(testHesapDTO);

        HesapDTO sonuc = hesapService.hesapOlustur(testHesapDTO);

        assertNotNull(sonuc);
        assertEquals(testHesapDTO.getHesapNo(), sonuc.getHesapNo());
        verify(hesapRepository).save(any(Hesap.class));
    }

    @Test
    void hesapOlustur_HesapNoMevcut_HataFirlatir() {
        when(hesapRepository.existsByHesapNo(anyString())).thenReturn(true);

        assertThrows(BusinessException.class, () -> hesapService.hesapOlustur(testHesapDTO));
    }

    @Test
    void paraYatir_BasariliDurum() {
        when(hesapRepository.findById(hesapId)).thenReturn(Optional.of(testHesap));

        BigDecimal yatirilacakMiktar = new BigDecimal("500.00");
        hesapService.paraYatir(hesapId, yatirilacakMiktar);

        verify(hesapRepository).save(any(Hesap.class));
        verify(hareketRepository).save(any());
    }

    @Test
    void paraYatir_MaksimumBakiyeAsimi_HataFirlatir() {
        when(hesapRepository.findById(hesapId)).thenReturn(Optional.of(testHesap));

        BigDecimal buyukMiktar = new BigDecimal("9999999.00");

        assertThrows(BusinessException.class, () -> hesapService.paraYatir(hesapId, buyukMiktar));
    }

    @Test
    void paraCek_BasariliDurum() {
        when(hesapRepository.findById(hesapId)).thenReturn(Optional.of(testHesap));

        BigDecimal cekilecekMiktar = new BigDecimal("500.00");
        hesapService.paraCek(hesapId, cekilecekMiktar);

        verify(hesapRepository).save(any(Hesap.class));
        verify(hareketRepository).save(any());
    }

    @Test
    void paraCek_YetersizBakiye_HataFirlatir() {
        when(hesapRepository.findById(hesapId)).thenReturn(Optional.of(testHesap));

        BigDecimal cekilecekMiktar = new BigDecimal("2000.00");

        assertThrows(YetersizBakiyeException.class, () -> hesapService.paraCek(hesapId, cekilecekMiktar));
    }

    @Test
    void hesapSil_BasariliDurum() {
        testHesap.setBakiye(BigDecimal.ZERO);
        when(hesapRepository.findById(hesapId)).thenReturn(Optional.of(testHesap));

        hesapService.hesapSil(hesapId);

        verify(hesapRepository).delete(testHesap);
    }

    @Test
    void hesapSil_BakiyeVar_HataFirlatir() {
        when(hesapRepository.findById(hesapId)).thenReturn(Optional.of(testHesap));

        assertThrows(BusinessException.class, () -> hesapService.hesapSil(hesapId));
    }

    @Test
    void tumHesaplariGetir_BasariliDurum() {
        when(hesapRepository.findAll()).thenReturn(Arrays.asList(testHesap));
        when(hesapMapper.toDTO(any(Hesap.class))).thenReturn(testHesapDTO);

        List<HesapDTO> sonuc = hesapService.tumHesaplariGetir();

        assertNotNull(sonuc);
        assertFalse(sonuc.isEmpty());
        assertEquals(1, sonuc.size());
        assertEquals(testHesapDTO.getHesapNo(), sonuc.get(0).getHesapNo());
    }

    @Test
    void hesapGuncelle_BasariliDurum() {
        when(hesapRepository.findById(hesapId)).thenReturn(Optional.of(testHesap));
        when(hesapRepository.save(any(Hesap.class))).thenReturn(testHesap);
        when(hesapMapper.toDTO(any(Hesap.class))).thenReturn(testHesapDTO);

        HesapDTO sonuc = hesapService.hesapGuncelle(hesapId, testHesapDTO);

        assertNotNull(sonuc);
        assertEquals(testHesapDTO.getHesapSahipAd(), sonuc.getHesapSahipAd());
        verify(hesapRepository).save(any(Hesap.class));
    }
}