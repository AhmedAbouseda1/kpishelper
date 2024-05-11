package com.kpis.helper.service.mapper;

import static com.kpis.helper.domain.SpaceAsserts.*;
import static com.kpis.helper.domain.SpaceTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class SpaceMapperTest {

    private SpaceMapper spaceMapper;

    @BeforeEach
    void setUp() {
        spaceMapper = new SpaceMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getSpaceSample1();
        var actual = spaceMapper.toEntity(spaceMapper.toDto(expected));
        assertSpaceAllPropertiesEquals(expected, actual);
    }
}
