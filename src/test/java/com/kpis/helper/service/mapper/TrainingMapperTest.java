package com.kpis.helper.service.mapper;

import static com.kpis.helper.domain.TrainingAsserts.*;
import static com.kpis.helper.domain.TrainingTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class TrainingMapperTest {

    private TrainingMapper trainingMapper;

    @BeforeEach
    void setUp() {
        trainingMapper = new TrainingMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getTrainingSample1();
        var actual = trainingMapper.toEntity(trainingMapper.toDto(expected));
        assertTrainingAllPropertiesEquals(expected, actual);
    }
}
