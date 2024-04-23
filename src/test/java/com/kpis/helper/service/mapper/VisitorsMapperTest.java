package com.kpis.helper.service.mapper;

import static com.kpis.helper.domain.VisitorsAsserts.*;
import static com.kpis.helper.domain.VisitorsTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class VisitorsMapperTest {

    private VisitorsMapper visitorsMapper;

    @BeforeEach
    void setUp() {
        visitorsMapper = new VisitorsMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getVisitorsSample1();
        var actual = visitorsMapper.toEntity(visitorsMapper.toDto(expected));
        assertVisitorsAllPropertiesEquals(expected, actual);
    }
}
