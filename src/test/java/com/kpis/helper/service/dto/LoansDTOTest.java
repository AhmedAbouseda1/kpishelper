package com.kpis.helper.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.kpis.helper.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class LoansDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(LoansDTO.class);
        LoansDTO loansDTO1 = new LoansDTO();
        loansDTO1.setId(1L);
        LoansDTO loansDTO2 = new LoansDTO();
        assertThat(loansDTO1).isNotEqualTo(loansDTO2);
        loansDTO2.setId(loansDTO1.getId());
        assertThat(loansDTO1).isEqualTo(loansDTO2);
        loansDTO2.setId(2L);
        assertThat(loansDTO1).isNotEqualTo(loansDTO2);
        loansDTO1.setId(null);
        assertThat(loansDTO1).isNotEqualTo(loansDTO2);
    }
}
