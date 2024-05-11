package com.kpis.helper.domain;

import static com.kpis.helper.domain.LoansTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.kpis.helper.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class LoansTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Loans.class);
        Loans loans1 = getLoansSample1();
        Loans loans2 = new Loans();
        assertThat(loans1).isNotEqualTo(loans2);

        loans2.setId(loans1.getId());
        assertThat(loans1).isEqualTo(loans2);

        loans2 = getLoansSample2();
        assertThat(loans1).isNotEqualTo(loans2);
    }
}
