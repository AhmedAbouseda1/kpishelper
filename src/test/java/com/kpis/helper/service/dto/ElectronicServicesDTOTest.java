package com.kpis.helper.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.kpis.helper.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class ElectronicServicesDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(ElectronicServicesDTO.class);
        ElectronicServicesDTO electronicServicesDTO1 = new ElectronicServicesDTO();
        electronicServicesDTO1.setId(1L);
        ElectronicServicesDTO electronicServicesDTO2 = new ElectronicServicesDTO();
        assertThat(electronicServicesDTO1).isNotEqualTo(electronicServicesDTO2);
        electronicServicesDTO2.setId(electronicServicesDTO1.getId());
        assertThat(electronicServicesDTO1).isEqualTo(electronicServicesDTO2);
        electronicServicesDTO2.setId(2L);
        assertThat(electronicServicesDTO1).isNotEqualTo(electronicServicesDTO2);
        electronicServicesDTO1.setId(null);
        assertThat(electronicServicesDTO1).isNotEqualTo(electronicServicesDTO2);
    }
}
