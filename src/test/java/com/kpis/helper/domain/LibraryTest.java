package com.kpis.helper.domain;

import static com.kpis.helper.domain.CollectionTestSamples.*;
import static com.kpis.helper.domain.LibraryTestSamples.*;
import static com.kpis.helper.domain.PopulationTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.kpis.helper.web.rest.TestUtil;
import java.util.HashSet;
import java.util.Set;
import org.junit.jupiter.api.Test;

class LibraryTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Library.class);
        Library library1 = getLibrarySample1();
        Library library2 = new Library();
        assertThat(library1).isNotEqualTo(library2);

        library2.setId(library1.getId());
        assertThat(library1).isEqualTo(library2);

        library2 = getLibrarySample2();
        assertThat(library1).isNotEqualTo(library2);
    }

    @Test
    void populationTest() throws Exception {
        Library library = getLibraryRandomSampleGenerator();
        Population populationBack = getPopulationRandomSampleGenerator();

        library.addPopulation(populationBack);
        assertThat(library.getPopulations()).containsOnly(populationBack);
        assertThat(populationBack.getLibrary()).isEqualTo(library);

        library.removePopulation(populationBack);
        assertThat(library.getPopulations()).doesNotContain(populationBack);
        assertThat(populationBack.getLibrary()).isNull();

        library.populations(new HashSet<>(Set.of(populationBack)));
        assertThat(library.getPopulations()).containsOnly(populationBack);
        assertThat(populationBack.getLibrary()).isEqualTo(library);

        library.setPopulations(new HashSet<>());
        assertThat(library.getPopulations()).doesNotContain(populationBack);
        assertThat(populationBack.getLibrary()).isNull();
    }

    @Test
    void collectionTest() throws Exception {
        Library library = getLibraryRandomSampleGenerator();
        Collection collectionBack = getCollectionRandomSampleGenerator();

        library.addCollection(collectionBack);
        assertThat(library.getCollections()).containsOnly(collectionBack);
        assertThat(collectionBack.getLibrary()).isEqualTo(library);

        library.removeCollection(collectionBack);
        assertThat(library.getCollections()).doesNotContain(collectionBack);
        assertThat(collectionBack.getLibrary()).isNull();

        library.collections(new HashSet<>(Set.of(collectionBack)));
        assertThat(library.getCollections()).containsOnly(collectionBack);
        assertThat(collectionBack.getLibrary()).isEqualTo(library);

        library.setCollections(new HashSet<>());
        assertThat(library.getCollections()).doesNotContain(collectionBack);
        assertThat(collectionBack.getLibrary()).isNull();
    }
}
