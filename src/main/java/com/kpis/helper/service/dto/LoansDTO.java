package com.kpis.helper.service.dto;

import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.Objects;

/**
 * A DTO for the {@link com.kpis.helper.domain.Loans} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class LoansDTO implements Serializable {

    private Long id;

    @NotNull
    private LocalDate recorded_date;

    private Integer total_items_borrowed;

    private Long turnover_rate;

    private Integer media_borrowed_at_least_once_percentage;

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

    public Integer getTotal_items_borrowed() {
        return total_items_borrowed;
    }

    public void setTotal_items_borrowed(Integer total_items_borrowed) {
        this.total_items_borrowed = total_items_borrowed;
    }

    public Long getTurnover_rate() {
        return turnover_rate;
    }

    public void setTurnover_rate(Long turnover_rate) {
        this.turnover_rate = turnover_rate;
    }

    public Integer getMedia_borrowed_at_least_once_percentage() {
        return media_borrowed_at_least_once_percentage;
    }

    public void setMedia_borrowed_at_least_once_percentage(Integer media_borrowed_at_least_once_percentage) {
        this.media_borrowed_at_least_once_percentage = media_borrowed_at_least_once_percentage;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof LoansDTO)) {
            return false;
        }

        LoansDTO loansDTO = (LoansDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, loansDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "LoansDTO{" +
            "id=" + getId() +
            ", recorded_date='" + getRecorded_date() + "'" +
            ", total_items_borrowed=" + getTotal_items_borrowed() +
            ", turnover_rate=" + getTurnover_rate() +
            ", media_borrowed_at_least_once_percentage=" + getMedia_borrowed_at_least_once_percentage() +
            "}";
    }
}
