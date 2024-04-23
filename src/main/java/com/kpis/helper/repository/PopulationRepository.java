package com.kpis.helper.repository;

import com.kpis.helper.domain.Population;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the Population entity.
 */
@SuppressWarnings("unused")
@Repository
public interface PopulationRepository extends JpaRepository<Population, Long>, JpaSpecificationExecutor<Population> {}
