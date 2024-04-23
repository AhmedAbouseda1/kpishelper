package com.kpis.helper.domain;

import static com.kpis.helper.domain.LibraryTestSamples.*;
import static com.kpis.helper.domain.PopulationTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.kpis.helper.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class PopulationTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Population.class);
        Population population1 = getPopulationSample1();
        Population population2 = new Population();
        assertThat(population1).isNotEqualTo(population2);

        population2.setId(population1.getId());
        assertThat(population1).isEqualTo(population2);

        population2 = getPopulationSample2();
        assertThat(population1).isNotEqualTo(population2);
    }

    @Test
    void libraryTest() throws Exception {
        Population population = getPopulationRandomSampleGenerator();
        Library libraryBack = getLibraryRandomSampleGenerator();

        population.setLibrary(libraryBack);
        assertThat(population.getLibrary()).isEqualTo(libraryBack);

        population.library(null);
        assertThat(population.getLibrary()).isNull();
    }
}
