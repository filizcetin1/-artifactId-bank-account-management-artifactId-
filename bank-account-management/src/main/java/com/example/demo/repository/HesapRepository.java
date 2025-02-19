package com.example.demo.repository;

import com.example.demo.entity.Hesap;
import com.example.demo.model.HesapTuru;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface HesapRepository extends JpaRepository<Hesap, UUID> {
    boolean existsByHesapSahipKimlikNoAndHesapTuru(String hesapSahipKimlikNo, HesapTuru hesapTuru);

    boolean existsByHesapNo(String hesapNo);

    Hesap findByHesapNo(String hesapNo);
}