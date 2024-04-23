package com.kpis.helper.domain;

import static com.kpis.helper.domain.VisitorsTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.kpis.helper.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class VisitorsTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Visitors.class);
        Visitors visitors1 = getVisitorsSample1();
        Visitors visitors2 = new Visitors();
        assertThat(visitors1).isNotEqualTo(visitors2);

        visitors2.setId(visitors1.getId());
        assertThat(visitors1).isEqualTo(visitors2);

        visitors2 = getVisitorsSample2();
        assertThat(visitors1).isNotEqualTo(visitors2);
    }
}
