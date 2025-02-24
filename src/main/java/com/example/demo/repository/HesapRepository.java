package com.example.demo.repository;

import com.example.demo.entity.Hesap;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.UUID;

public interface HesapRepository extends JpaRepository<Hesap, UUID> {
    boolean existsByHesapSahipKimlikNo(String hesapSahipKimlikNo);
}