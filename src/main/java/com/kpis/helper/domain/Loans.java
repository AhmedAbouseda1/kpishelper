package com.kpis.helper.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.LocalDate;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A Loans.
 */
@Entity
@Table(name = "loans")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Loans implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "recorded_date", nullable = false, unique = true)
    private LocalDate recorded_date;

    @Column(name = "total_items_borrowed")
    private Integer total_items_borrowed;

    @Column(name = "turnover_rate")
    private Long turnover_rate;

    @Column(name = "media_borrowed_at_least_once_percentage")
    private Integer media_borrowed_at_least_once_percentage;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Loans id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDate getRecorded_date() {
        return this.recorded_date;
    }

    public Loans recorded_date(LocalDate recorded_date) {
        this.setRecorded_date(recorded_date);
        return this;
    }

    public void setRecorded_date(LocalDate recorded_date) {
        this.recorded_date = recorded_date;
    }

    public Integer getTotal_items_borrowed() {
        return this.total_items_borrowed;
    }

    public Loans total_items_borrowed(Integer total_items_borrowed) {
        this.setTotal_items_borrowed(total_items_borrowed);
        return this;
    }

    public void setTotal_items_borrowed(Integer total_items_borrowed) {
        this.total_items_borrowed = total_items_borrowed;
    }

    public Long getTurnover_rate() {
        return this.turnover_rate;
    }

    public Loans turnover_rate(Long turnover_rate) {
        this.setTurnover_rate(turnover_rate);
        return this;
    }

    public void setTurnover_rate(Long turnover_rate) {
        this.turnover_rate = turnover_rate;
    }

    public Integer getMedia_borrowed_at_least_once_percentage() {
        return this.media_borrowed_at_least_once_percentage;
    }

    public Loans media_borrowed_at_least_once_percentage(Integer media_borrowed_at_least_once_percentage) {
        this.setMedia_borrowed_at_least_once_percentage(media_borrowed_at_least_once_percentage);
        return this;
    }

    public void setMedia_borrowed_at_least_once_percentage(Integer media_borrowed_at_least_once_percentage) {
        this.media_borrowed_at_least_once_percentage = media_borrowed_at_least_once_percentage;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Loans)) {
            return false;
        }
        return getId() != null && getId().equals(((Loans) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Loans{" +
            "id=" + getId() +
            ", recorded_date='" + getRecorded_date() + "'" +
            ", total_items_borrowed=" + getTotal_items_borrowed() +
            ", turnover_rate=" + getTurnover_rate() +
            ", media_borrowed_at_least_once_percentage=" + getMedia_borrowed_at_least_once_percentage() +
            "}";
    }
}
