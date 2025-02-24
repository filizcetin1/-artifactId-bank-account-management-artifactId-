package com.example.demo.mapper;

import com.example.demo.controller.dto.HesapDTO;
import com.example.demo.entity.Hesap;
import com.example.demo.model.HesapTuru;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
public class HesapMapper {

    public HesapDTO toDTO(Hesap entity) {
        if (entity == null) {
            return null;
        }

        HesapDTO dto = new HesapDTO();
        dto.setId(entity.getId());
        dto.setHesapNo(entity.getHesapNo());
        dto.setBakiye(entity.getBakiye());
        dto.setHesapSahipKimlikNo(entity.getHesapSahipKimlikNo());
        dto.setHesapSahipAd(entity.getHesapSahipAd());
        dto.setHesapSahipSoyad(entity.getHesapSahipSoyad());
        dto.setHesapTuru(convertToModelHesapTuru(entity.getHesapTuru()));
        dto.setMusteriAdi(entity.getMusteriAdi());
        return dto;
    }

    public Hesap toEntity(HesapDTO dto) {
        if (dto == null) {
            return null;
        }

        Hesap entity = new Hesap();
        entity.setId(dto.getId());
        entity.setHesapNo(dto.getHesapNo());
        entity.setBakiye(dto.getBakiye() != null ? dto.getBakiye() : BigDecimal.ZERO);
        entity.setHesapSahipKimlikNo(dto.getHesapSahipKimlikNo());
        entity.setHesapSahipAd(dto.getHesapSahipAd());
        entity.setHesapSahipSoyad(dto.getHesapSahipSoyad());
        entity.setHesapTuru(convertToEntityHesapTuru(dto.getHesapTuru()));
        entity.setMusteriAdi(dto.getMusteriAdi());
        return entity;
    }

    private HesapTuru convertToModelHesapTuru(com.example.demo.entity.HesapTuru entityTuru) {
        if (entityTuru == null) {
            return null;
        }
        return HesapTuru.valueOf(entityTuru.name());
    }

    private com.example.demo.entity.HesapTuru convertToEntityHesapTuru(HesapTuru modelTuru) {
        if (modelTuru == null) {
            return null;
        }
        return com.example.demo.entity.HesapTuru.valueOf(modelTuru.name());
    }
}