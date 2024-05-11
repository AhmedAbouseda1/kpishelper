package com.kpis.helper.service.dto;

import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.Objects;

/**
 * A DTO for the {@link com.kpis.helper.domain.Activities} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class ActivitiesDTO implements Serializable {

    private Long id;

    @NotNull
    private LocalDate recorded_date;

    private Integer total_activities;

    private Integer total_participants;

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

    public Integer getTotal_activities() {
        return total_activities;
    }

    public void setTotal_activities(Integer total_activities) {
        this.total_activities = total_activities;
    }

    public Integer getTotal_participants() {
        return total_participants;
    }

    public void setTotal_participants(Integer total_participants) {
        this.total_participants = total_participants;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ActivitiesDTO)) {
            return false;
        }

        ActivitiesDTO activitiesDTO = (ActivitiesDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, activitiesDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ActivitiesDTO{" +
            "id=" + getId() +
            ", recorded_date='" + getRecorded_date() + "'" +
            ", total_activities=" + getTotal_activities() +
            ", total_participants=" + getTotal_participants() +
            "}";
    }
}
