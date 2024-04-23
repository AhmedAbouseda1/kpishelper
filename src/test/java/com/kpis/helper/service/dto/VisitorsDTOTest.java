package com.kpis.helper.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.kpis.helper.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class VisitorsDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(VisitorsDTO.class);
        VisitorsDTO visitorsDTO1 = new VisitorsDTO();
        visitorsDTO1.setId(1L);
        VisitorsDTO visitorsDTO2 = new VisitorsDTO();
        assertThat(visitorsDTO1).isNotEqualTo(visitorsDTO2);
        visitorsDTO2.setId(visitorsDTO1.getId());
        assertThat(visitorsDTO1).isEqualTo(visitorsDTO2);
        visitorsDTO2.setId(2L);
        assertThat(visitorsDTO1).isNotEqualTo(visitorsDTO2);
        visitorsDTO1.setId(null);
        assertThat(visitorsDTO1).isNotEqualTo(visitorsDTO2);
    }
}
