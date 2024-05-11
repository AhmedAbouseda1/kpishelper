package com.kpis.helper.service;

import com.kpis.helper.service.dto.CollectionDTO;
import java.util.List;
import java.util.Optional;

/**
 * Service Interface for managing {@link com.kpis.helper.domain.Collection}.
 */
public interface CollectionService {
    /**
     * Save a collection.
     *
     * @param collectionDTO the entity to save.
     * @return the persisted entity.
     */
    CollectionDTO save(CollectionDTO collectionDTO);

    /**
     * Updates a collection.
     *
     * @param collectionDTO the entity to update.
     * @return the persisted entity.
     */
    CollectionDTO update(CollectionDTO collectionDTO);

    /**
     * Partially updates a collection.
     *
     * @param collectionDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<CollectionDTO> partialUpdate(CollectionDTO collectionDTO);

    /**
     * Get all the collections.
     *
     * @return the list of entities.
     */
    List<CollectionDTO> findAll();

    /**
     * Get the "id" collection.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<CollectionDTO> findOne(Long id);

    public DashboardDTO getDashboardData() {
        DashboardDTO dashboardDTO = new DashboardDTO();
        
        dashboardDTO.setCollectionSize(collectionRepository.count());
        dashboardDTO.setCollectionRecordedDate(collectionRepository.findTopByOrderByRecordedDateDesc().getRecordedDate());
        
        dashboardDTO.setActivitiesRecordedDate(activitiesRepository.findMaxRecordedDate());
        dashboardDTO.setTotalActivities(activitiesRepository.count());
        dashboardDTO.setTotalParticipants(activitiesRepository.sumParticipants());
        
        return dashboardDTO;
    }
}
