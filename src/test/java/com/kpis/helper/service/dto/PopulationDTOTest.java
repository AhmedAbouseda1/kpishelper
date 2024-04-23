package com.kpis.helper.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.kpis.helper.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class PopulationDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(PopulationDTO.class);
        PopulationDTO populationDTO1 = new PopulationDTO();
        populationDTO1.setId(1L);
        PopulationDTO populationDTO2 = new PopulationDTO();
        assertThat(populationDTO1).isNotEqualTo(populationDTO2);
        populationDTO2.setId(populationDTO1.getId());
        assertThat(populationDTO1).isEqualTo(populationDTO2);
        populationDTO2.setId(2L);
        assertThat(populationDTO1).isNotEqualTo(populationDTO2);
        populationDTO1.setId(null);
        assertThat(populationDTO1).isNotEqualTo(populationDTO2);
    }
}
