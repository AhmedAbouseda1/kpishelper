package com.kpis.helper.service.mapper;

import static com.kpis.helper.domain.PopulationAsserts.*;
import static com.kpis.helper.domain.PopulationTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class PopulationMapperTest {

    private PopulationMapper populationMapper;

    @BeforeEach
    void setUp() {
        populationMapper = new PopulationMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getPopulationSample1();
        var actual = populationMapper.toEntity(populationMapper.toDto(expected));
        assertPopulationAllPropertiesEquals(expected, actual);
    }
}
