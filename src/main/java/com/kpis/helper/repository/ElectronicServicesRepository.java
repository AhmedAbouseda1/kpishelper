package com.kpis.helper.repository;

import com.kpis.helper.domain.ElectronicServices;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the ElectronicServices entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ElectronicServicesRepository extends JpaRepository<ElectronicServices, Long> {}
