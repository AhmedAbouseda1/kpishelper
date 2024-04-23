package com.kpis.helper.service.criteria;

import java.io.Serializable;
import java.util.Objects;
import java.util.Optional;
import org.springdoc.core.annotations.ParameterObject;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.*;

/**
 * Criteria class for the {@link com.kpis.helper.domain.Visitors} entity. This class is used
 * in {@link com.kpis.helper.web.rest.VisitorsResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /visitors?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
@ParameterObject
@SuppressWarnings("common-java:DuplicatedBlocks")
public class VisitorsCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private IntegerFilter total_visitors;

    private IntegerFilter website_visitors;

    private LocalDateFilter recorded_date;

    private Boolean distinct;

    public VisitorsCriteria() {}

    public VisitorsCriteria(VisitorsCriteria other) {
        this.id = other.optionalId().map(LongFilter::copy).orElse(null);
        this.total_visitors = other.optionalTotal_visitors().map(IntegerFilter::copy).orElse(null);
        this.website_visitors = other.optionalWebsite_visitors().map(IntegerFilter::copy).orElse(null);
        this.recorded_date = other.optionalRecorded_date().map(LocalDateFilter::copy).orElse(null);
        this.distinct = other.distinct;
    }

    @Override
    public VisitorsCriteria copy() {
        return new VisitorsCriteria(this);
    }

    public LongFilter getId() {
        return id;
    }

    public Optional<LongFilter> optionalId() {
        return Optional.ofNullable(id);
    }

    public LongFilter id() {
        if (id == null) {
            setId(new LongFilter());
        }
        return id;
    }

    public void setId(LongFilter id) {
        this.id = id;
    }

    public IntegerFilter getTotal_visitors() {
        return total_visitors;
    }

    public Optional<IntegerFilter> optionalTotal_visitors() {
        return Optional.ofNullable(total_visitors);
    }

    public IntegerFilter total_visitors() {
        if (total_visitors == null) {
            setTotal_visitors(new IntegerFilter());
        }
        return total_visitors;
    }

    public void setTotal_visitors(IntegerFilter total_visitors) {
        this.total_visitors = total_visitors;
    }

    public IntegerFilter getWebsite_visitors() {
        return website_visitors;
    }

    public Optional<IntegerFilter> optionalWebsite_visitors() {
        return Optional.ofNullable(website_visitors);
    }

    public IntegerFilter website_visitors() {
        if (website_visitors == null) {
            setWebsite_visitors(new IntegerFilter());
        }
        return website_visitors;
    }

    public void setWebsite_visitors(IntegerFilter website_visitors) {
        this.website_visitors = website_visitors;
    }

    public LocalDateFilter getRecorded_date() {
        return recorded_date;
    }

    public Optional<LocalDateFilter> optionalRecorded_date() {
        return Optional.ofNullable(recorded_date);
    }

    public LocalDateFilter recorded_date() {
        if (recorded_date == null) {
            setRecorded_date(new LocalDateFilter());
        }
        return recorded_date;
    }

    public void setRecorded_date(LocalDateFilter recorded_date) {
        this.recorded_date = recorded_date;
    }

    public Boolean getDistinct() {
        return distinct;
    }

    public Optional<Boolean> optionalDistinct() {
        return Optional.ofNullable(distinct);
    }

    public Boolean distinct() {
        if (distinct == null) {
            setDistinct(true);
        }
        return distinct;
    }

    public void setDistinct(Boolean distinct) {
        this.distinct = distinct;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final VisitorsCriteria that = (VisitorsCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(total_visitors, that.total_visitors) &&
            Objects.equals(website_visitors, that.website_visitors) &&
            Objects.equals(recorded_date, that.recorded_date) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, total_visitors, website_visitors, recorded_date, distinct);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "VisitorsCriteria{" +
            optionalId().map(f -> "id=" + f + ", ").orElse("") +
            optionalTotal_visitors().map(f -> "total_visitors=" + f + ", ").orElse("") +
            optionalWebsite_visitors().map(f -> "website_visitors=" + f + ", ").orElse("") +
            optionalRecorded_date().map(f -> "recorded_date=" + f + ", ").orElse("") +
            optionalDistinct().map(f -> "distinct=" + f + ", ").orElse("") +
        "}";
    }
}
