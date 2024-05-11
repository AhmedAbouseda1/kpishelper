package com.kpis.helper.service.mapper;

import static com.kpis.helper.domain.LoansAsserts.*;
import static com.kpis.helper.domain.LoansTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class LoansMapperTest {

    private LoansMapper loansMapper;

    @BeforeEach
    void setUp() {
        loansMapper = new LoansMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getLoansSample1();
        var actual = loansMapper.toEntity(loansMapper.toDto(expected));
        assertLoansAllPropertiesEquals(expected, actual);
    }
}
