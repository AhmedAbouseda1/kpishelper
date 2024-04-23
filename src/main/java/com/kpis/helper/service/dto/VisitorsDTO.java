package com.kpis.helper.service.dto;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.Objects;

/**
 * A DTO for the {@link com.kpis.helper.domain.Visitors} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class VisitorsDTO implements Serializable {

    private Long id;

    private Integer total_visitors;

    private Integer website_visitors;

    private LocalDate recorded_date;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getTotal_visitors() {
        return total_visitors;
    }

    public void setTotal_visitors(Integer total_visitors) {
        this.total_visitors = total_visitors;
    }

    public Integer getWebsite_visitors() {
        return website_visitors;
    }

    public void setWebsite_visitors(Integer website_visitors) {
        this.website_visitors = website_visitors;
    }

    public LocalDate getRecorded_date() {
        return recorded_date;
    }

    public void setRecorded_date(LocalDate recorded_date) {
        this.recorded_date = recorded_date;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof VisitorsDTO)) {
            return false;
        }

        VisitorsDTO visitorsDTO = (VisitorsDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, visitorsDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "VisitorsDTO{" +
            "id=" + getId() +
            ", total_visitors=" + getTotal_visitors() +
            ", website_visitors=" + getWebsite_visitors() +
            ", recorded_date='" + getRecorded_date() + "'" +
            "}";
    }
}
