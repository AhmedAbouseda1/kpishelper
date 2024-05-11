package com.kpis.helper.domain;

import static com.kpis.helper.domain.SpaceTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.kpis.helper.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class SpaceTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Space.class);
        Space space1 = getSpaceSample1();
        Space space2 = new Space();
        assertThat(space1).isNotEqualTo(space2);

        space2.setId(space1.getId());
        assertThat(space1).isEqualTo(space2);

        space2 = getSpaceSample2();
        assertThat(space1).isNotEqualTo(space2);
    }
}
