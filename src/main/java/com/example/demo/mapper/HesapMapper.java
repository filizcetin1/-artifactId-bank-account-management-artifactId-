package com.example.demo.mapper;

import com.example.demo.controller.dto.HesapDTO;
import com.example.demo.entity.Hesap;
import com.example.demo.entity.HesapTuru;
import org.springframework.stereotype.Component;

@Component
public class HesapMapper {

    public HesapDTO toDTO(Hesap entity) {
        if (entity == null) {
            return null;
        }

        HesapDTO dto = new HesapDTO();
        dto.setId(entity.getId());
        dto.setHesapSahipKimlikNo(entity.getHesapSahipKimlikNo());
        dto.setHesapSahipAd(entity.getHesapSahipAd());
        dto.setHesapSahipSoyad(entity.getHesapSahipSoyad());
        dto.setHesapTuru(entity.getHesapTuru());
        dto.setBakiye(entity.getBakiye());
        return dto;
    }

    public Hesap toEntity(HesapDTO dto) {
        if (dto == null) {
            return null;
        }

        Hesap entity = new Hesap();
        entity.setId(dto.getId());
        entity.setHesapSahipKimlikNo(dto.getHesapSahipKimlikNo());
        entity.setHesapSahipAd(dto.getHesapSahipAd());
        entity.setHesapSahipSoyad(dto.getHesapSahipSoyad());
        entity.setHesapTuru(dto.getHesapTuru());
        entity.setBakiye(dto.getBakiye());
        return entity;
    }
}