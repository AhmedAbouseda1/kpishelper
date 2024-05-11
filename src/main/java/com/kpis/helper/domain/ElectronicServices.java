package com.kpis.helper.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.LocalDate;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A ElectronicServices.
 */
@Entity
@Table(name = "electronic_services")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class ElectronicServices implements Serializable {

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
    @Column(name = "total_pcs_with_internet", nullable = false)
    private Integer total_pcs_with_internet;

    @Column(name = "pcs_with_internet_for_clients_only")
    private Integer pcs_with_internet_for_clients_only;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public ElectronicServices id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDate getRecorded_date() {
        return this.recorded_date;
    }

    public ElectronicServices recorded_date(LocalDate recorded_date) {
        this.setRecorded_date(recorded_date);
        return this;
    }

    public void setRecorded_date(LocalDate recorded_date) {
        this.recorded_date = recorded_date;
    }

    public Integer getTotal_pcs_with_internet() {
        return this.total_pcs_with_internet;
    }

    public ElectronicServices total_pcs_with_internet(Integer total_pcs_with_internet) {
        this.setTotal_pcs_with_internet(total_pcs_with_internet);
        return this;
    }

    public void setTotal_pcs_with_internet(Integer total_pcs_with_internet) {
        this.total_pcs_with_internet = total_pcs_with_internet;
    }

    public Integer getPcs_with_internet_for_clients_only() {
        return this.pcs_with_internet_for_clients_only;
    }

    public ElectronicServices pcs_with_internet_for_clients_only(Integer pcs_with_internet_for_clients_only) {
        this.setPcs_with_internet_for_clients_only(pcs_with_internet_for_clients_only);
        return this;
    }

    public void setPcs_with_internet_for_clients_only(Integer pcs_with_internet_for_clients_only) {
        this.pcs_with_internet_for_clients_only = pcs_with_internet_for_clients_only;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ElectronicServices)) {
            return false;
        }
        return getId() != null && getId().equals(((ElectronicServices) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ElectronicServices{" +
            "id=" + getId() +
            ", recorded_date='" + getRecorded_date() + "'" +
            ", total_pcs_with_internet=" + getTotal_pcs_with_internet() +
            ", pcs_with_internet_for_clients_only=" + getPcs_with_internet_for_clients_only() +
            "}";
    }
}
