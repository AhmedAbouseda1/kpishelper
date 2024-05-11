package com.kpis.helper.service.dto;

import java.time.Instant;

public class DashboardDTO {
    private Long collectionSize;
    private Instant collectionRecordedDate;
    private Instant activitiesRecordedDate;
    private Long totalActivities;
    private Long totalParticipants;

    public Long getCollectionSize() {
        return collectionSize;
    }

    public void setCollectionSize(Long collectionSize) {
        this.collectionSize = collectionSize;
    }

    public Instant getCollectionRecordedDate() {
        return collectionRecordedDate;
    }

    public void setCollectionRecordedDate(Instant collectionRecordedDate) {
        this.collectionRecordedDate = collectionRecordedDate;
    }

    public Instant getActivitiesRecordedDate() {
        return activitiesRecordedDate;
    }

    public void setActivitiesRecordedDate(Instant activitiesRecordedDate) {
        this.activitiesRecordedDate = activitiesRecordedDate;
    }

    public Long getTotalActivities() {
        return totalActivities;
    }

    public void setTotalActivities(Long totalActivities) {
        this.totalActivities = totalActivities;
    }

    public Long getTotalParticipants() {
        return totalParticipants;
    }

    public void setTotalParticipants(Long totalParticipants) {
        this.totalParticipants = totalParticipants;
    }
}