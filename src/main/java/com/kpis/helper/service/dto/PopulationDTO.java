package com.kpis.helper.service.dto;

import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.Objects;

/**
 * A DTO for the {@link com.kpis.helper.domain.Population} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class PopulationDTO implements Serializable {

    private Long id;

    @NotNull
    private LocalDate date_recorded;

    @NotNull
    private Integer population;

    private Integer active_members;

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

    public Integer getPopulation() {
        return population;
    }

    public void setPopulation(Integer population) {
        this.population = population;
    }

    public Integer getActive_members() {
        return active_members;
    }

    public void setActive_members(Integer active_members) {
        this.active_members = active_members;
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
        if (!(o instanceof PopulationDTO)) {
            return false;
        }

        PopulationDTO populationDTO = (PopulationDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, populationDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "PopulationDTO{" +
            "id=" + getId() +
            ", date_recorded='" + getDate_recorded() + "'" +
            ", population=" + getPopulation() +
            ", active_members=" + getActive_members() +
            ", library=" + getLibrary() +
            "}";
    }
}
