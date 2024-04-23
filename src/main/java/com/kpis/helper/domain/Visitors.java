package com.kpis.helper.domain;

import jakarta.persistence.*;
import java.io.Serializable;
import java.time.LocalDate;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A Visitors.
 */
@Entity
@Table(name = "visitors")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Visitors implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @Column(name = "total_visitors")
    private Integer total_visitors;

    @Column(name = "website_visitors")
    private Integer website_visitors;

    @Column(name = "recorded_date")
    private LocalDate recorded_date;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Visitors id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getTotal_visitors() {
        return this.total_visitors;
    }

    public Visitors total_visitors(Integer total_visitors) {
        this.setTotal_visitors(total_visitors);
        return this;
    }

    public void setTotal_visitors(Integer total_visitors) {
        this.total_visitors = total_visitors;
    }

    public Integer getWebsite_visitors() {
        return this.website_visitors;
    }

    public Visitors website_visitors(Integer website_visitors) {
        this.setWebsite_visitors(website_visitors);
        return this;
    }

    public void setWebsite_visitors(Integer website_visitors) {
        this.website_visitors = website_visitors;
    }

    public LocalDate getRecorded_date() {
        return this.recorded_date;
    }

    public Visitors recorded_date(LocalDate recorded_date) {
        this.setRecorded_date(recorded_date);
        return this;
    }

    public void setRecorded_date(LocalDate recorded_date) {
        this.recorded_date = recorded_date;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Visitors)) {
            return false;
        }
        return getId() != null && getId().equals(((Visitors) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Visitors{" +
            "id=" + getId() +
            ", total_visitors=" + getTotal_visitors() +
            ", website_visitors=" + getWebsite_visitors() +
            ", recorded_date='" + getRecorded_date() + "'" +
            "}";
    }
}
