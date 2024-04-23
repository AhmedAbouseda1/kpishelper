package com.kpis.helper.service;

import com.kpis.helper.service.dto.VisitorsDTO;
import java.util.Optional;

/**
 * Service Interface for managing {@link com.kpis.helper.domain.Visitors}.
 */
public interface VisitorsService {
    /**
     * Save a visitors.
     *
     * @param visitorsDTO the entity to save.
     * @return the persisted entity.
     */
    VisitorsDTO save(VisitorsDTO visitorsDTO);

    /**
     * Updates a visitors.
     *
     * @param visitorsDTO the entity to update.
     * @return the persisted entity.
     */
    VisitorsDTO update(VisitorsDTO visitorsDTO);

    /**
     * Partially updates a visitors.
     *
     * @param visitorsDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<VisitorsDTO> partialUpdate(VisitorsDTO visitorsDTO);

    /**
     * Get the "id" visitors.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<VisitorsDTO> findOne(Long id);

    /**
     * Delete the "id" visitors.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
