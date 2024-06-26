package com.kpis.helper.domain;

import static org.assertj.core.api.Assertions.assertThat;

public class LoansAsserts {

    /**
     * Asserts that the entity has all properties (fields/relationships) set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertLoansAllPropertiesEquals(Loans expected, Loans actual) {
        assertLoansAutoGeneratedPropertiesEquals(expected, actual);
        assertLoansAllUpdatablePropertiesEquals(expected, actual);
    }

    /**
     * Asserts that the entity has all updatable properties (fields/relationships) set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertLoansAllUpdatablePropertiesEquals(Loans expected, Loans actual) {
        assertLoansUpdatableFieldsEquals(expected, actual);
        assertLoansUpdatableRelationshipsEquals(expected, actual);
    }

    /**
     * Asserts that the entity has all the auto generated properties (fields/relationships) set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertLoansAutoGeneratedPropertiesEquals(Loans expected, Loans actual) {
        assertThat(expected)
            .as("Verify Loans auto generated properties")
            .satisfies(e -> assertThat(e.getId()).as("check id").isEqualTo(actual.getId()));
    }

    /**
     * Asserts that the entity has all the updatable fields set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertLoansUpdatableFieldsEquals(Loans expected, Loans actual) {
        assertThat(expected)
            .as("Verify Loans relevant properties")
            .satisfies(e -> assertThat(e.getRecorded_date()).as("check recorded_date").isEqualTo(actual.getRecorded_date()))
            .satisfies(
                e -> assertThat(e.getTotal_items_borrowed()).as("check total_items_borrowed").isEqualTo(actual.getTotal_items_borrowed())
            )
            .satisfies(e -> assertThat(e.getTurnover_rate()).as("check turnover_rate").isEqualTo(actual.getTurnover_rate()))
            .satisfies(
                e ->
                    assertThat(e.getMedia_borrowed_at_least_once_percentage())
                        .as("check media_borrowed_at_least_once_percentage")
                        .isEqualTo(actual.getMedia_borrowed_at_least_once_percentage())
            );
    }

    /**
     * Asserts that the entity has all the updatable relationships set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertLoansUpdatableRelationshipsEquals(Loans expected, Loans actual) {}
}
