package com.kpis.helper.service;

import com.kpis.helper.service.dto.ActivitiesDTO;
import java.util.List;
import java.util.Optional;

/**
 * Service Interface for managing {@link com.kpis.helper.domain.Activities}.
 */
public interface ActivitiesService {
    /**
     * Save a activities.
     *
     * @param activitiesDTO the entity to save.
     * @return the persisted entity.
     */
    ActivitiesDTO save(ActivitiesDTO activitiesDTO);

    /**
     * Updates a activities.
     *
     * @param activitiesDTO the entity to update.
     * @return the persisted entity.
     */
    ActivitiesDTO update(ActivitiesDTO activitiesDTO);

    /**
     * Partially updates a activities.
     *
     * @param activitiesDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<ActivitiesDTO> partialUpdate(ActivitiesDTO activitiesDTO);

    /**
     * Get all the activities.
     *
     * @return the list of entities.
     */
    List<ActivitiesDTO> findAll();

    /**
     * Get the "id" activities.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<ActivitiesDTO> findOne(Long id);

    /**
     * Delete the "id" activities.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
