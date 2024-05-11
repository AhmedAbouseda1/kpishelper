package com.kpis.helper.service;

import com.kpis.helper.service.dto.TrainingDTO;
import java.util.List;
import java.util.Optional;

/**
 * Service Interface for managing {@link com.kpis.helper.domain.Training}.
 */
public interface TrainingService {
    /**
     * Save a training.
     *
     * @param trainingDTO the entity to save.
     * @return the persisted entity.
     */
    TrainingDTO save(TrainingDTO trainingDTO);

    /**
     * Updates a training.
     *
     * @param trainingDTO the entity to update.
     * @return the persisted entity.
     */
    TrainingDTO update(TrainingDTO trainingDTO);

    /**
     * Partially updates a training.
     *
     * @param trainingDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<TrainingDTO> partialUpdate(TrainingDTO trainingDTO);

    /**
     * Get all the trainings.
     *
     * @return the list of entities.
     */
    List<TrainingDTO> findAll();

    /**
     * Get the "id" training.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<TrainingDTO> findOne(Long id);

    /**
     * Delete the "id" training.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
