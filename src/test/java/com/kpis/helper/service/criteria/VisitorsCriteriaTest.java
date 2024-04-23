package com.kpis.helper.service.criteria;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.function.BiFunction;
import java.util.function.Function;
import org.assertj.core.api.Condition;
import org.junit.jupiter.api.Test;

class VisitorsCriteriaTest {

    @Test
    void newVisitorsCriteriaHasAllFiltersNullTest() {
        var visitorsCriteria = new VisitorsCriteria();
        assertThat(visitorsCriteria).is(criteriaFiltersAre(filter -> filter == null));
    }

    @Test
    void visitorsCriteriaFluentMethodsCreatesFiltersTest() {
        var visitorsCriteria = new VisitorsCriteria();

        setAllFilters(visitorsCriteria);

        assertThat(visitorsCriteria).is(criteriaFiltersAre(filter -> filter != null));
    }

    @Test
    void visitorsCriteriaCopyCreatesNullFilterTest() {
        var visitorsCriteria = new VisitorsCriteria();
        var copy = visitorsCriteria.copy();

        assertThat(visitorsCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(filter -> filter == null)),
            criteria -> assertThat(criteria).isEqualTo(visitorsCriteria)
        );
    }

    @Test
    void visitorsCriteriaCopyDuplicatesEveryExistingFilterTest() {
        var visitorsCriteria = new VisitorsCriteria();
        setAllFilters(visitorsCriteria);

        var copy = visitorsCriteria.copy();

        assertThat(visitorsCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(filter -> filter != null)),
            criteria -> assertThat(criteria).isEqualTo(visitorsCriteria)
        );
    }

    @Test
    void toStringVerifier() {
        var visitorsCriteria = new VisitorsCriteria();

        assertThat(visitorsCriteria).hasToString("VisitorsCriteria{}");
    }

    private static void setAllFilters(VisitorsCriteria visitorsCriteria) {
        visitorsCriteria.id();
        visitorsCriteria.total_visitors();
        visitorsCriteria.website_visitors();
        visitorsCriteria.recorded_date();
        visitorsCriteria.distinct();
    }

    private static Condition<VisitorsCriteria> criteriaFiltersAre(Function<Object, Boolean> condition) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId()) &&
                condition.apply(criteria.getTotal_visitors()) &&
                condition.apply(criteria.getWebsite_visitors()) &&
                condition.apply(criteria.getRecorded_date()) &&
                condition.apply(criteria.getDistinct()),
            "every filter matches"
        );
    }

    private static Condition<VisitorsCriteria> copyFiltersAre(VisitorsCriteria copy, BiFunction<Object, Object, Boolean> condition) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId(), copy.getId()) &&
                condition.apply(criteria.getTotal_visitors(), copy.getTotal_visitors()) &&
                condition.apply(criteria.getWebsite_visitors(), copy.getWebsite_visitors()) &&
                condition.apply(criteria.getRecorded_date(), copy.getRecorded_date()) &&
                condition.apply(criteria.getDistinct(), copy.getDistinct()),
            "every filter matches"
        );
    }
}
