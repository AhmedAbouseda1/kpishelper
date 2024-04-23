package com.kpis.helper.domain;

import static org.assertj.core.api.Assertions.assertThat;

public class VisitorsAsserts {

    /**
     * Asserts that the entity has all properties (fields/relationships) set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertVisitorsAllPropertiesEquals(Visitors expected, Visitors actual) {
        assertVisitorsAutoGeneratedPropertiesEquals(expected, actual);
        assertVisitorsAllUpdatablePropertiesEquals(expected, actual);
    }

    /**
     * Asserts that the entity has all updatable properties (fields/relationships) set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertVisitorsAllUpdatablePropertiesEquals(Visitors expected, Visitors actual) {
        assertVisitorsUpdatableFieldsEquals(expected, actual);
        assertVisitorsUpdatableRelationshipsEquals(expected, actual);
    }

    /**
     * Asserts that the entity has all the auto generated properties (fields/relationships) set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertVisitorsAutoGeneratedPropertiesEquals(Visitors expected, Visitors actual) {
        assertThat(expected)
            .as("Verify Visitors auto generated properties")
            .satisfies(e -> assertThat(e.getId()).as("check id").isEqualTo(actual.getId()));
    }

    /**
     * Asserts that the entity has all the updatable fields set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertVisitorsUpdatableFieldsEquals(Visitors expected, Visitors actual) {
        assertThat(expected)
            .as("Verify Visitors relevant properties")
            .satisfies(e -> assertThat(e.getTotal_visitors()).as("check total_visitors").isEqualTo(actual.getTotal_visitors()))
            .satisfies(e -> assertThat(e.getWebsite_visitors()).as("check website_visitors").isEqualTo(actual.getWebsite_visitors()))
            .satisfies(e -> assertThat(e.getRecorded_date()).as("check recorded_date").isEqualTo(actual.getRecorded_date()));
    }

    /**
     * Asserts that the entity has all the updatable relationships set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertVisitorsUpdatableRelationshipsEquals(Visitors expected, Visitors actual) {}
}
