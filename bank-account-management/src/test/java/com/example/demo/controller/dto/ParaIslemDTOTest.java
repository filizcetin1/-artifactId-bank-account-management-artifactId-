package com.example.demo.controller.dto;

import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.math.BigDecimal;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class ParaIslemDTOTest {

    private Validator validator;
    private ParaIslemDTO paraIslemDTO;

    @BeforeEach
    void setUp() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
        paraIslemDTO = new ParaIslemDTO();
        paraIslemDTO.setHesapId(UUID.randomUUID());
    }

    @Test
    void gecerliParaIslemi() {
        paraIslemDTO.setMiktar(new BigDecimal("100.00"));
        assertTrue(validator.validate(paraIslemDTO).isEmpty());
    }

    @ParameterizedTest
    @ValueSource(strings = { "0.00", "-1.00", "-100.00" })
    void gecersizMiktar_SifirVeyaNegatif(String miktar) {
        paraIslemDTO.setMiktar(new BigDecimal(miktar));
        assertFalse(validator.validate(paraIslemDTO).isEmpty());
    }

    @Test
    void gecersizMiktar_Null() {
        paraIslemDTO.setMiktar(null);
        assertFalse(validator.validate(paraIslemDTO).isEmpty());
    }
}