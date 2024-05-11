package com.kpis.helper.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.LocalDate;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A Staff.
 */
@Entity
@Table(name = "staff")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Staff implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "recorded_date", nullable = false, unique = true)
    private LocalDate recorded_date;

    @NotNull
    @Min(value = 1)
    @Column(name = "number_of_staff", nullable = false)
    private Integer number_of_staff;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Staff id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDate getRecorded_date() {
        return this.recorded_date;
    }

    public Staff recorded_date(LocalDate recorded_date) {
        this.setRecorded_date(recorded_date);
        return this;
    }

    public void setRecorded_date(LocalDate recorded_date) {
        this.recorded_date = recorded_date;
    }

    public Integer getNumber_of_staff() {
        return this.number_of_staff;
    }

    public Staff number_of_staff(Integer number_of_staff) {
        this.setNumber_of_staff(number_of_staff);
        return this;
    }

    public void setNumber_of_staff(Integer number_of_staff) {
        this.number_of_staff = number_of_staff;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Staff)) {
            return false;
        }
        return getId() != null && getId().equals(((Staff) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Staff{" +
            "id=" + getId() +
            ", recorded_date='" + getRecorded_date() + "'" +
            ", number_of_staff=" + getNumber_of_staff() +
            "}";
    }
}
