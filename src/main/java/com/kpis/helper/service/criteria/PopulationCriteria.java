package com.kpis.helper.service.criteria;

import java.io.Serializable;
import java.util.Objects;
import java.util.Optional;
import org.springdoc.core.annotations.ParameterObject;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.*;

/**
 * Criteria class for the {@link com.kpis.helper.domain.Population} entity. This class is used
 * in {@link com.kpis.helper.web.rest.PopulationResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /populations?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
@ParameterObject
@SuppressWarnings("common-java:DuplicatedBlocks")
public class PopulationCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private LocalDateFilter date_recorded;

    private IntegerFilter population;

    private IntegerFilter active_members;

    private LongFilter libraryId;

    private Boolean distinct;

    public PopulationCriteria() {}

    public PopulationCriteria(PopulationCriteria other) {
        this.id = other.optionalId().map(LongFilter::copy).orElse(null);
        this.date_recorded = other.optionalDate_recorded().map(LocalDateFilter::copy).orElse(null);
        this.population = other.optionalPopulation().map(IntegerFilter::copy).orElse(null);
        this.active_members = other.optionalActive_members().map(IntegerFilter::copy).orElse(null);
        this.libraryId = other.optionalLibraryId().map(LongFilter::copy).orElse(null);
        this.distinct = other.distinct;
    }

    @Override
    public PopulationCriteria copy() {
        return new PopulationCriteria(this);
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

    public LocalDateFilter getDate_recorded() {
        return date_recorded;
    }

    public Optional<LocalDateFilter> optionalDate_recorded() {
        return Optional.ofNullable(date_recorded);
    }

    public LocalDateFilter date_recorded() {
        if (date_recorded == null) {
            setDate_recorded(new LocalDateFilter());
        }
        return date_recorded;
    }

    public void setDate_recorded(LocalDateFilter date_recorded) {
        this.date_recorded = date_recorded;
    }

    public IntegerFilter getPopulation() {
        return population;
    }

    public Optional<IntegerFilter> optionalPopulation() {
        return Optional.ofNullable(population);
    }

    public IntegerFilter population() {
        if (population == null) {
            setPopulation(new IntegerFilter());
        }
        return population;
    }

    public void setPopulation(IntegerFilter population) {
        this.population = population;
    }

    public IntegerFilter getActive_members() {
        return active_members;
    }

    public Optional<IntegerFilter> optionalActive_members() {
        return Optional.ofNullable(active_members);
    }

    public IntegerFilter active_members() {
        if (active_members == null) {
            setActive_members(new IntegerFilter());
        }
        return active_members;
    }

    public void setActive_members(IntegerFilter active_members) {
        this.active_members = active_members;
    }

    public LongFilter getLibraryId() {
        return libraryId;
    }

    public Optional<LongFilter> optionalLibraryId() {
        return Optional.ofNullable(libraryId);
    }

    public LongFilter libraryId() {
        if (libraryId == null) {
            setLibraryId(new LongFilter());
        }
        return libraryId;
    }

    public void setLibraryId(LongFilter libraryId) {
        this.libraryId = libraryId;
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
        final PopulationCriteria that = (PopulationCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(date_recorded, that.date_recorded) &&
            Objects.equals(population, that.population) &&
            Objects.equals(active_members, that.active_members) &&
            Objects.equals(libraryId, that.libraryId) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, date_recorded, population, active_members, libraryId, distinct);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "PopulationCriteria{" +
            optionalId().map(f -> "id=" + f + ", ").orElse("") +
            optionalDate_recorded().map(f -> "date_recorded=" + f + ", ").orElse("") +
            optionalPopulation().map(f -> "population=" + f + ", ").orElse("") +
            optionalActive_members().map(f -> "active_members=" + f + ", ").orElse("") +
            optionalLibraryId().map(f -> "libraryId=" + f + ", ").orElse("") +
            optionalDistinct().map(f -> "distinct=" + f + ", ").orElse("") +
        "}";
    }
}
