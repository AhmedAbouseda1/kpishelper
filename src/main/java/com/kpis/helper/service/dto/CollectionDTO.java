package com.kpis.helper.service.dto;

import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.Objects;

/**
 * A DTO for the {@link com.kpis.helper.domain.Collection} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class CollectionDTO implements Serializable {

    private Long id;

    private LocalDate date_recorded;

    @NotNull
    private Integer collection_size;

    @NotNull
    private Integer number_of_titles;

    private Integer stock_for_public_usage;

    private Integer titles_availability_for_population;

    private Integer titles_availability_for_active_members;

    private LibraryDTO library;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDate getDate_recorded() {
        return date_recorded;
    }

    public void setDate_recorded(LocalDate date_recorded) {
        this.date_recorded = date_recorded;
    }

    public Integer getCollection_size() {
        return collection_size;
    }

    public void setCollection_size(Integer collection_size) {
        this.collection_size = collection_size;
    }

    public Integer getNumber_of_titles() {
        return number_of_titles;
    }

    public void setNumber_of_titles(Integer number_of_titles) {
        this.number_of_titles = number_of_titles;
    }

    public Integer getStock_for_public_usage() {
        return stock_for_public_usage;
    }

    public void setStock_for_public_usage(Integer stock_for_public_usage) {
        this.stock_for_public_usage = stock_for_public_usage;
    }

    public Integer getTitles_availability_for_population() {
        return titles_availability_for_population;
    }

    public void setTitles_availability_for_population(Integer titles_availability_for_population) {
        this.titles_availability_for_population = titles_availability_for_population;
    }

    public Integer getTitles_availability_for_active_members() {
        return titles_availability_for_active_members;
    }

    public void setTitles_availability_for_active_members(Integer titles_availability_for_active_members) {
        this.titles_availability_for_active_members = titles_availability_for_active_members;
    }

    public LibraryDTO getLibrary() {
        return library;
    }

    public void setLibrary(LibraryDTO library) {
        this.library = library;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof CollectionDTO)) {
            return false;
        }

        CollectionDTO collectionDTO = (CollectionDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, collectionDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "CollectionDTO{" +
            "id=" + getId() +
            ", date_recorded='" + getDate_recorded() + "'" +
            ", collection_size=" + getCollection_size() +
            ", number_of_titles=" + getNumber_of_titles() +
            ", stock_for_public_usage=" + getStock_for_public_usage() +
            ", titles_availability_for_population=" + getTitles_availability_for_population() +
            ", titles_availability_for_active_members=" + getTitles_availability_for_active_members() +
            ", library=" + getLibrary() +
            "}";
    }
}
