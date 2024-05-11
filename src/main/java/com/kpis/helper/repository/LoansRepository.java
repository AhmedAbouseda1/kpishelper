package com.kpis.helper.repository;

import com.kpis.helper.domain.Loans;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the Loans entity.
 */
@SuppressWarnings("unused")
@Repository
public interface LoansRepository extends JpaRepository<Loans, Long> {}
