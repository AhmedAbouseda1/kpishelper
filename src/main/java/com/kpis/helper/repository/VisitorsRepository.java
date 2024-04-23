package com.kpis.helper.repository;

import com.kpis.helper.domain.Visitors;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the Visitors entity.
 */
@SuppressWarnings("unused")
@Repository
public interface VisitorsRepository extends JpaRepository<Visitors, Long>, JpaSpecificationExecutor<Visitors> {}
