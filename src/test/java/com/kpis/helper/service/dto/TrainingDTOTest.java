package com.kpis.helper.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.kpis.helper.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class TrainingDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(TrainingDTO.class);
        TrainingDTO trainingDTO1 = new TrainingDTO();
        trainingDTO1.setId(1L);
        TrainingDTO trainingDTO2 = new TrainingDTO();
        assertThat(trainingDTO1).isNotEqualTo(trainingDTO2);
        trainingDTO2.setId(trainingDTO1.getId());
        assertThat(trainingDTO1).isEqualTo(trainingDTO2);
        trainingDTO2.setId(2L);
        assertThat(trainingDTO1).isNotEqualTo(trainingDTO2);
        trainingDTO1.setId(null);
        assertThat(trainingDTO1).isNotEqualTo(trainingDTO2);
    }
}
