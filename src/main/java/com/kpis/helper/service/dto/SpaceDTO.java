package com.kpis.helper.service.dto;

import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.Objects;

/**
 * A DTO for the {@link com.kpis.helper.domain.Space} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class SpaceDTO implements Serializable {

    private Long id;

    @NotNull
    private LocalDate recorded_date;

    @Min(value = 1L)
    private Long square_meters_available;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDate getRecorded_date() {
        return recorded_date;
    }

    public void setRecorded_date(LocalDate recorded_date) {
        this.recorded_date = recorded_date;
    }

    public Long getSquare_meters_available() {
        return square_meters_available;
    }

    public void setSquare_meters_available(Long square_meters_available) {
        this.square_meters_available = square_meters_available;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof SpaceDTO)) {
            return false;
        }

        SpaceDTO spaceDTO = (SpaceDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, spaceDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "SpaceDTO{" +
            "id=" + getId() +
            ", recorded_date='" + getRecorded_date() + "'" +
            ", square_meters_available=" + getSquare_meters_available() +
            "}";
    }
}
