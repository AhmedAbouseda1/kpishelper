package com.kpis.helper.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.LocalDate;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A Activities.
 */
@Entity
@Table(name = "activities")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Activities implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "recorded_date", nullable = false, unique = true)
    private LocalDate recorded_date;

    @Column(name = "total_activities")
    private Integer total_activities;

    @Column(name = "total_participants")
    private Integer total_participants;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Activities id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDate getRecorded_date() {
        return this.recorded_date;
    }

    public Activities recorded_date(LocalDate recorded_date) {
        this.setRecorded_date(recorded_date);
        return this;
    }

    public void setRecorded_date(LocalDate recorded_date) {
        this.recorded_date = recorded_date;
    }

    public Integer getTotal_activities() {
        return this.total_activities;
    }

    public Activities total_activities(Integer total_activities) {
        this.setTotal_activities(total_activities);
        return this;
    }

    public void setTotal_activities(Integer total_activities) {
        this.total_activities = total_activities;
    }

    public Integer getTotal_participants() {
        return this.total_participants;
    }

    public Activities total_participants(Integer total_participants) {
        this.setTotal_participants(total_participants);
        return this;
    }

    public void setTotal_participants(Integer total_participants) {
        this.total_participants = total_participants;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Activities)) {
            return false;
        }
        return getId() != null && getId().equals(((Activities) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Activities{" +
            "id=" + getId() +
            ", recorded_date='" + getRecorded_date() + "'" +
            ", total_activities=" + getTotal_activities() +
            ", total_participants=" + getTotal_participants() +
            "}";
    }
}
