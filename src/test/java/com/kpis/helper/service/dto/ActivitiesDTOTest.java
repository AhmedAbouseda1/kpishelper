package com.kpis.helper.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.kpis.helper.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class ActivitiesDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(ActivitiesDTO.class);
        ActivitiesDTO activitiesDTO1 = new ActivitiesDTO();
        activitiesDTO1.setId(1L);
        ActivitiesDTO activitiesDTO2 = new ActivitiesDTO();
        assertThat(activitiesDTO1).isNotEqualTo(activitiesDTO2);
        activitiesDTO2.setId(activitiesDTO1.getId());
        assertThat(activitiesDTO1).isEqualTo(activitiesDTO2);
        activitiesDTO2.setId(2L);
        assertThat(activitiesDTO1).isNotEqualTo(activitiesDTO2);
        activitiesDTO1.setId(null);
        assertThat(activitiesDTO1).isNotEqualTo(activitiesDTO2);
    }
}
