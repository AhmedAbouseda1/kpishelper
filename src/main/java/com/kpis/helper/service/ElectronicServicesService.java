package com.kpis.helper.service;

import com.kpis.helper.service.dto.ElectronicServicesDTO;
import java.util.List;
import java.util.Optional;

/**
 * Service Interface for managing {@link com.kpis.helper.domain.ElectronicServices}.
 */
public interface ElectronicServicesService {
    /**
     * Save a electronicServices.
     *
     * @param electronicServicesDTO the entity to save.
     * @return the persisted entity.
     */
    ElectronicServicesDTO save(ElectronicServicesDTO electronicServicesDTO);

    /**
     * Updates a electronicServices.
     *
     * @param electronicServicesDTO the entity to update.
     * @return the persisted entity.
     */
    ElectronicServicesDTO update(ElectronicServicesDTO electronicServicesDTO);

    /**
     * Partially updates a electronicServices.
     *
     * @param electronicServicesDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<ElectronicServicesDTO> partialUpdate(ElectronicServicesDTO electronicServicesDTO);

    /**
     * Get all the electronicServices.
     *
     * @return the list of entities.
     */
    List<ElectronicServicesDTO> findAll();

    /**
     * Get the "id" electronicServices.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<ElectronicServicesDTO> findOne(Long id);

    /**
     * Delete the "id" electronicServices.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
