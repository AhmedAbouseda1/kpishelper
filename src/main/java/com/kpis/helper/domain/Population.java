package com.kpis.helper.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.LocalDate;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A Population.
 */
@Entity
@Table(name = "population")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Population implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "date_recorded", nullable = false)
    private LocalDate date_recorded;

    @NotNull
    @Column(name = "population", nullable = false)
    private Integer population;

    @Column(name = "active_members")
    private Integer active_members;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(value = { "populations", "collections" }, allowSetters = true)
    private Library library;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Population id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDate getDate_recorded() {
        return this.date_recorded;
    }

    public Population date_recorded(LocalDate date_recorded) {
        this.setDate_recorded(date_recorded);
        return this;
    }

    public void setDate_recorded(LocalDate date_recorded) {
        this.date_recorded = date_recorded;
    }

    public Integer getPopulation() {
        return this.population;
    }

    public Population population(Integer population) {
        this.setPopulation(population);
        return this;
    }

    public void setPopulation(Integer population) {
        this.population = population;
    }

    public Integer getActive_members() {
        return this.active_members;
    }

    public Population active_members(Integer active_members) {
        this.setActive_members(active_members);
        return this;
    }

    public void setActive_members(Integer active_members) {
        this.active_members = active_members;
    }

    public Library getLibrary() {
        return this.library;
    }

    public void setLibrary(Library library) {
        this.library = library;
    }

    public Population library(Library library) {
        this.setLibrary(library);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Population)) {
            return false;
        }
        return getId() != null && getId().equals(((Population) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Population{" +
            "id=" + getId() +
            ", date_recorded='" + getDate_recorded() + "'" +
            ", population=" + getPopulation() +
            ", active_members=" + getActive_members() +
            "}";
    }
}
