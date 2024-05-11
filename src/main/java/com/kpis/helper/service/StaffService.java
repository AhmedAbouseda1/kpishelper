package com.kpis.helper.service;

import com.kpis.helper.service.dto.StaffDTO;
import java.util.List;
import java.util.Optional;

/**
 * Service Interface for managing {@link com.kpis.helper.domain.Staff}.
 */
public interface StaffService {
    /**
     * Save a staff.
     *
     * @param staffDTO the entity to save.
     * @return the persisted entity.
     */
    StaffDTO save(StaffDTO staffDTO);

    /**
     * Updates a staff.
     *
     * @param staffDTO the entity to update.
     * @return the persisted entity.
     */
    StaffDTO update(StaffDTO staffDTO);

    /**
     * Partially updates a staff.
     *
     * @param staffDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<StaffDTO> partialUpdate(StaffDTO staffDTO);

    /**
     * Get all the staff.
     *
     * @return the list of entities.
     */
    List<StaffDTO> findAll();

    /**
     * Get the "id" staff.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<StaffDTO> findOne(Long id);

    /**
     * Delete the "id" staff.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
