package com.kpis.helper.service.dto;

import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.Objects;

/**
 * A DTO for the {@link com.kpis.helper.domain.Training} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class TrainingDTO implements Serializable {

    private Long id;

    @NotNull
    private LocalDate recorded_date;

    private Integer total_courses;

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

    public Integer getTotal_courses() {
        return total_courses;
    }

    public void setTotal_courses(Integer total_courses) {
        this.total_courses = total_courses;
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
        if (!(o instanceof TrainingDTO)) {
            return false;
        }

        TrainingDTO trainingDTO = (TrainingDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, trainingDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "TrainingDTO{" +
            "id=" + getId() +
            ", recorded_date='" + getRecorded_date() + "'" +
            ", total_courses=" + getTotal_courses() +
            ", total_participants=" + getTotal_participants() +
            "}";
    }
}
