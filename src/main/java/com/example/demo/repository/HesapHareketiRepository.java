package com.example.demo.repository;

import com.example.demo.entity.HesapHareketi;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface HesapHareketiRepository extends JpaRepository<HesapHareketi, UUID> {
}