package com.kpis.helper.repository;

import com.kpis.helper.domain.Activities;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import java.time.Instant;

/**
 * Spring Data JPA repository for the Activities entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ActivitiesRepository extends JpaRepository<Activities, Long> {
    @Query("select max(a.recordedDate) from Activities a")
    Instant findMaxRecordedDate();
}
