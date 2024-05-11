package com.kpis.helper.domain;

import static com.kpis.helper.domain.ElectronicServicesTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.kpis.helper.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class ElectronicServicesTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(ElectronicServices.class);
        ElectronicServices electronicServices1 = getElectronicServicesSample1();
        ElectronicServices electronicServices2 = new ElectronicServices();
        assertThat(electronicServices1).isNotEqualTo(electronicServices2);

        electronicServices2.setId(electronicServices1.getId());
        assertThat(electronicServices1).isEqualTo(electronicServices2);

        electronicServices2 = getElectronicServicesSample2();
        assertThat(electronicServices1).isNotEqualTo(electronicServices2);
    }
}
