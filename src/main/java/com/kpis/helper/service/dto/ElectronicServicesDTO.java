package com.kpis.helper.service.dto;

import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.Objects;

/**
 * A DTO for the {@link com.kpis.helper.domain.ElectronicServices} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class ElectronicServicesDTO implements Serializable {

    private Long id;

    @NotNull
    private LocalDate recorded_date;

    @NotNull
    @Min(value = 1)
    private Integer total_pcs_with_internet;

    private Integer pcs_with_internet_for_clients_only;

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

    public Integer getTotal_pcs_with_internet() {
        return total_pcs_with_internet;
    }

    public void setTotal_pcs_with_internet(Integer total_pcs_with_internet) {
        this.total_pcs_with_internet = total_pcs_with_internet;
    }

    public Integer getPcs_with_internet_for_clients_only() {
        return pcs_with_internet_for_clients_only;
    }

    public void setPcs_with_internet_for_clients_only(Integer pcs_with_internet_for_clients_only) {
        this.pcs_with_internet_for_clients_only = pcs_with_internet_for_clients_only;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ElectronicServicesDTO)) {
            return false;
        }

        ElectronicServicesDTO electronicServicesDTO = (ElectronicServicesDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, electronicServicesDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ElectronicServicesDTO{" +
            "id=" + getId() +
            ", recorded_date='" + getRecorded_date() + "'" +
            ", total_pcs_with_internet=" + getTotal_pcs_with_internet() +
            ", pcs_with_internet_for_clients_only=" + getPcs_with_internet_for_clients_only() +
            "}";
    }
}
