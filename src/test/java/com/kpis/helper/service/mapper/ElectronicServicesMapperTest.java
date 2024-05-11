package com.kpis.helper.service.mapper;

import static com.kpis.helper.domain.ElectronicServicesAsserts.*;
import static com.kpis.helper.domain.ElectronicServicesTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class ElectronicServicesMapperTest {

    private ElectronicServicesMapper electronicServicesMapper;

    @BeforeEach
    void setUp() {
        electronicServicesMapper = new ElectronicServicesMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getElectronicServicesSample1();
        var actual = electronicServicesMapper.toEntity(electronicServicesMapper.toDto(expected));
        assertElectronicServicesAllPropertiesEquals(expected, actual);
    }
}
