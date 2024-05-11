package com.kpis.helper.service;

import com.kpis.helper.service.dto.LoansDTO;
import java.util.List;
import java.util.Optional;

/**
 * Service Interface for managing {@link com.kpis.helper.domain.Loans}.
 */
public interface LoansService {
    /**
     * Save a loans.
     *
     * @param loansDTO the entity to save.
     * @return the persisted entity.
     */
    LoansDTO save(LoansDTO loansDTO);

    /**
     * Updates a loans.
     *
     * @param loansDTO the entity to update.
     * @return the persisted entity.
     */
    LoansDTO update(LoansDTO loansDTO);

    /**
     * Partially updates a loans.
     *
     * @param loansDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<LoansDTO> partialUpdate(LoansDTO loansDTO);

    /**
     * Get all the loans.
     *
     * @return the list of entities.
     */
    List<LoansDTO> findAll();

    /**
     * Get the "id" loans.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<LoansDTO> findOne(Long id);

    /**
     * Delete the "id" loans.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
