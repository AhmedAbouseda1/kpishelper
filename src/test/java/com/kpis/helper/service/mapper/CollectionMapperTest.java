package com.kpis.helper.service.mapper;

import static com.kpis.helper.domain.CollectionAsserts.*;
import static com.kpis.helper.domain.CollectionTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class CollectionMapperTest {

    private CollectionMapper collectionMapper;

    @BeforeEach
    void setUp() {
        collectionMapper = new CollectionMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getCollectionSample1();
        var actual = collectionMapper.toEntity(collectionMapper.toDto(expected));
        assertCollectionAllPropertiesEquals(expected, actual);
    }
}
