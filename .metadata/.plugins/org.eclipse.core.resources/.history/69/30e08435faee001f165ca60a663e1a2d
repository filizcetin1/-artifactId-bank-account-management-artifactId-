package com.example.demo.controller.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.math.BigDecimal;
import java.util.UUID;

@Data
public class ParaIslemDTO {
    private UUID hesapId;
    @NotNull(message = "Miktar boş olamaz")
    @DecimalMin(value = "0.01", message = "Miktar 0'dan büyük olmalıdır")
    private BigDecimal miktar;
}