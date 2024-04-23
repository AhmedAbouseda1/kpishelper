package com.kpis.helper.repository;

import com.kpis.helper.domain.Collection;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the Collection entity.
 */
@SuppressWarnings("unused")
@Repository
public interface CollectionRepository extends JpaRepository<Collection, Long> {}
