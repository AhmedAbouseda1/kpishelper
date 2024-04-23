package com.kpis.helper.domain;

import static com.kpis.helper.domain.CollectionTestSamples.*;
import static com.kpis.helper.domain.LibraryTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.kpis.helper.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class CollectionTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Collection.class);
        Collection collection1 = getCollectionSample1();
        Collection collection2 = new Collection();
        assertThat(collection1).isNotEqualTo(collection2);

        collection2.setId(collection1.getId());
        assertThat(collection1).isEqualTo(collection2);

        collection2 = getCollectionSample2();
        assertThat(collection1).isNotEqualTo(collection2);
    }

    @Test
    void libraryTest() throws Exception {
        Collection collection = getCollectionRandomSampleGenerator();
        Library libraryBack = getLibraryRandomSampleGenerator();

        collection.setLibrary(libraryBack);
        assertThat(collection.getLibrary()).isEqualTo(libraryBack);

        collection.library(null);
        assertThat(collection.getLibrary()).isNull();
    }
}
