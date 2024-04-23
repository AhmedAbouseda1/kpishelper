package com.kpis.helper.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.LocalDate;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A Collection.
 */
@Entity
@Table(name = "collection")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Collection implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @Column(name = "date_recorded")
    private LocalDate date_recorded;

    @NotNull
    @Column(name = "collection_size", nullable = false)
    private Integer collection_size;

    @NotNull
    @Column(name = "number_of_titles", nullable = false)
    private Integer number_of_titles;

    @Column(name = "stock_for_public_usage")
    private Integer stock_for_public_usage;

    @Column(name = "titles_availability_for_population")
    private Integer titles_availability_for_population;

    @Column(name = "titles_availability_for_active_members")
    private Integer titles_availability_for_active_members;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(value = { "populations", "collections" }, allowSetters = true)
    private Library library;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Collection id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDate getDate_recorded() {
        return this.date_recorded;
    }

    public Collection date_recorded(LocalDate date_recorded) {
        this.setDate_recorded(date_recorded);
        return this;
    }

    public void setDate_recorded(LocalDate date_recorded) {
        this.date_recorded = date_recorded;
    }

    public Integer getCollection_size() {
        return this.collection_size;
    }

    public Collection collection_size(Integer collection_size) {
        this.setCollection_size(collection_size);
        return this;
    }

    public void setCollection_size(Integer collection_size) {
        this.collection_size = collection_size;
    }

    public Integer getNumber_of_titles() {
        return this.number_of_titles;
    }

    public Collection number_of_titles(Integer number_of_titles) {
        this.setNumber_of_titles(number_of_titles);
        return this;
    }

    public void setNumber_of_titles(Integer number_of_titles) {
        this.number_of_titles = number_of_titles;
    }

    public Integer getStock_for_public_usage() {
        return this.stock_for_public_usage;
    }

    public Collection stock_for_public_usage(Integer stock_for_public_usage) {
        this.setStock_for_public_usage(stock_for_public_usage);
        return this;
    }

    public void setStock_for_public_usage(Integer stock_for_public_usage) {
        this.stock_for_public_usage = stock_for_public_usage;
    }

    public Integer getTitles_availability_for_population() {
        return this.titles_availability_for_population;
    }

    public Collection titles_availability_for_population(Integer titles_availability_for_population) {
        this.setTitles_availability_for_population(titles_availability_for_population);
        return this;
    }

    public void setTitles_availability_for_population(Integer titles_availability_for_population) {
        this.titles_availability_for_population = titles_availability_for_population;
    }

    public Integer getTitles_availability_for_active_members() {
        return this.titles_availability_for_active_members;
    }

    public Collection titles_availability_for_active_members(Integer titles_availability_for_active_members) {
        this.setTitles_availability_for_active_members(titles_availability_for_active_members);
        return this;
    }

    public void setTitles_availability_for_active_members(Integer titles_availability_for_active_members) {
        this.titles_availability_for_active_members = titles_availability_for_active_members;
    }

    public Library getLibrary() {
        return this.library;
    }

    public void setLibrary(Library library) {
        this.library = library;
    }

    public Collection library(Library library) {
        this.setLibrary(library);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Collection)) {
            return false;
        }
        return getId() != null && getId().equals(((Collection) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Collection{" +
            "id=" + getId() +
            ", date_recorded='" + getDate_recorded() + "'" +
            ", collection_size=" + getCollection_size() +
            ", number_of_titles=" + getNumber_of_titles() +
            ", stock_for_public_usage=" + getStock_for_public_usage() +
            ", titles_availability_for_population=" + getTitles_availability_for_population() +
            ", titles_availability_for_active_members=" + getTitles_availability_for_active_members() +
            "}";
    }
}
