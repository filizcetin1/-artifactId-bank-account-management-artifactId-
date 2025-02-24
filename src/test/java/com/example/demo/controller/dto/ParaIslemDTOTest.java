package com.example.demo.controller.dto;

import org.junit.jupiter.api.Test;
import java.math.BigDecimal;
import static org.junit.jupiter.api.Assertions.*;

class ParaIslemDTOTest {

    @Test
    void testParaIslemDTO() {
        // Given
        ParaIslemDTO paraIslemDTO = new ParaIslemDTO();
        BigDecimal miktar = new BigDecimal("100.00");

        // When
        paraIslemDTO.setMiktar(miktar);

        // Then
        assertEquals(miktar, paraIslemDTO.getMiktar());
    }

    @Test
    void testParaIslemDTO_NullMiktar() {
        // Given
        ParaIslemDTO paraIslemDTO = new ParaIslemDTO();

        // When & Then
        assertNull(paraIslemDTO.getMiktar());
    }

    @Test
    void testParaIslemDTO_ToString() {
        // Given
        ParaIslemDTO paraIslemDTO = new ParaIslemDTO();
        BigDecimal miktar = new BigDecimal("100.00");
        paraIslemDTO.setMiktar(miktar);

        // When
        String toString = paraIslemDTO.toString();

        // Then
        assertTrue(toString.contains("miktar=" + miktar));
    }

    @Test
    void testParaIslemDTO_EqualsAndHashCode() {
        // Given
        ParaIslemDTO dto1 = new ParaIslemDTO();
        ParaIslemDTO dto2 = new ParaIslemDTO();
        BigDecimal miktar = new BigDecimal("100.00");

        dto1.setMiktar(miktar);
        dto2.setMiktar(miktar);

        // Then
        assertEquals(dto1, dto2);
        assertEquals(dto1.hashCode(), dto2.hashCode());
    }
}