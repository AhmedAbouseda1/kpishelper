package com.kpis.helper.service.dto;

import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.Objects;

/**
 * A DTO for the {@link com.kpis.helper.domain.Staff} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class StaffDTO implements Serializable {

    private Long id;

    @NotNull
    private LocalDate recorded_date;

    @NotNull
    @Min(value = 1)
    private Integer number_of_staff;

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

    public Integer getNumber_of_staff() {
        return number_of_staff;
    }

    public void setNumber_of_staff(Integer number_of_staff) {
        this.number_of_staff = number_of_staff;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof StaffDTO)) {
            return false;
        }

        StaffDTO staffDTO = (StaffDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, staffDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "StaffDTO{" +
            "id=" + getId() +
            ", recorded_date='" + getRecorded_date() + "'" +
            ", number_of_staff=" + getNumber_of_staff() +
            "}";
    }
}
