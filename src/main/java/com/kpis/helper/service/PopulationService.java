package com.kpis.helper.service;

import com.kpis.helper.service.dto.PopulationDTO;
import java.util.Optional;

/**
 * Service Interface for managing {@link com.kpis.helper.domain.Population}.
 */
public interface PopulationService {
    /**
     * Save a population.
     *
     * @param populationDTO the entity to save.
     * @return the persisted entity.
     */
    PopulationDTO save(PopulationDTO populationDTO);

    /**
     * Updates a population.
     *
     * @param populationDTO the entity to update.
     * @return the persisted entity.
     */
    PopulationDTO update(PopulationDTO populationDTO);

    /**
     * Partially updates a population.
     *
     * @param populationDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<PopulationDTO> partialUpdate(PopulationDTO populationDTO);

    /**
     * Get the "id" population.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<PopulationDTO> findOne(Long id);

    /**
     * Delete the "id" population.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
