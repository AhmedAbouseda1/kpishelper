package com.kpis.helper.domain;

import static com.kpis.helper.domain.ActivitiesTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.kpis.helper.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class ActivitiesTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Activities.class);
        Activities activities1 = getActivitiesSample1();
        Activities activities2 = new Activities();
        assertThat(activities1).isNotEqualTo(activities2);

        activities2.setId(activities1.getId());
        assertThat(activities1).isEqualTo(activities2);

        activities2 = getActivitiesSample2();
        assertThat(activities1).isNotEqualTo(activities2);
    }
}
