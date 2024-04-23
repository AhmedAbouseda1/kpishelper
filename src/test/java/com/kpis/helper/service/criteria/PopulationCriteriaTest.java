package com.kpis.helper.service.criteria;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.function.BiFunction;
import java.util.function.Function;
import org.assertj.core.api.Condition;
import org.junit.jupiter.api.Test;

class PopulationCriteriaTest {

    @Test
    void newPopulationCriteriaHasAllFiltersNullTest() {
        var populationCriteria = new PopulationCriteria();
        assertThat(populationCriteria).is(criteriaFiltersAre(filter -> filter == null));
    }

    @Test
    void populationCriteriaFluentMethodsCreatesFiltersTest() {
        var populationCriteria = new PopulationCriteria();

        setAllFilters(populationCriteria);

        assertThat(populationCriteria).is(criteriaFiltersAre(filter -> filter != null));
    }

    @Test
    void populationCriteriaCopyCreatesNullFilterTest() {
        var populationCriteria = new PopulationCriteria();
        var copy = populationCriteria.copy();

        assertThat(populationCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(filter -> filter == null)),
            criteria -> assertThat(criteria).isEqualTo(populationCriteria)
        );
    }

    @Test
    void populationCriteriaCopyDuplicatesEveryExistingFilterTest() {
        var populationCriteria = new PopulationCriteria();
        setAllFilters(populationCriteria);

        var copy = populationCriteria.copy();

        assertThat(populationCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(filter -> filter != null)),
            criteria -> assertThat(criteria).isEqualTo(populationCriteria)
        );
    }

    @Test
    void toStringVerifier() {
        var populationCriteria = new PopulationCriteria();

        assertThat(populationCriteria).hasToString("PopulationCriteria{}");
    }

    private static void setAllFilters(PopulationCriteria populationCriteria) {
        populationCriteria.id();
        populationCriteria.date_recorded();
        populationCriteria.population();
        populationCriteria.active_members();
        populationCriteria.libraryId();
        populationCriteria.distinct();
    }

    private static Condition<PopulationCriteria> criteriaFiltersAre(Function<Object, Boolean> condition) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId()) &&
                condition.apply(criteria.getDate_recorded()) &&
                condition.apply(criteria.getPopulation()) &&
                condition.apply(criteria.getActive_members()) &&
                condition.apply(criteria.getLibraryId()) &&
                condition.apply(criteria.getDistinct()),
            "every filter matches"
        );
    }

    private static Condition<PopulationCriteria> copyFiltersAre(PopulationCriteria copy, BiFunction<Object, Object, Boolean> condition) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId(), copy.getId()) &&
                condition.apply(criteria.getDate_recorded(), copy.getDate_recorded()) &&
                condition.apply(criteria.getPopulation(), copy.getPopulation()) &&
                condition.apply(criteria.getActive_members(), copy.getActive_members()) &&
                condition.apply(criteria.getLibraryId(), copy.getLibraryId()) &&
                condition.apply(criteria.getDistinct(), copy.getDistinct()),
            "every filter matches"
        );
    }
}
