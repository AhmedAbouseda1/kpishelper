package com.kpis.helper.service.mapper;

import static com.kpis.helper.domain.ActivitiesAsserts.*;
import static com.kpis.helper.domain.ActivitiesTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class ActivitiesMapperTest {

    private ActivitiesMapper activitiesMapper;

    @BeforeEach
    void setUp() {
        activitiesMapper = new ActivitiesMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getActivitiesSample1();
        var actual = activitiesMapper.toEntity(activitiesMapper.toDto(expected));
        assertActivitiesAllPropertiesEquals(expected, actual);
    }
}
