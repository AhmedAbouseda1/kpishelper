package com.kpis.helper.service;

import com.kpis.helper.service.dto.LibraryDTO;
import java.util.List;
import java.util.Optional;

/**
 * Service Interface for managing {@link com.kpis.helper.domain.Library}.
 */
public interface LibraryService {
    /**
     * Save a library.
     *
     * @param libraryDTO the entity to save.
     * @return the persisted entity.
     */
    LibraryDTO save(LibraryDTO libraryDTO);

    /**
     * Updates a library.
     *
     * @param libraryDTO the entity to update.
     * @return the persisted entity.
     */
    LibraryDTO update(LibraryDTO libraryDTO);

    /**
     * Partially updates a library.
     *
     * @param libraryDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<LibraryDTO> partialUpdate(LibraryDTO libraryDTO);

    /**
     * Get all the libraries.
     *
     * @return the list of entities.
     */
    List<LibraryDTO> findAll();

    /**
     * Get the "id" library.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<LibraryDTO> findOne(Long id);

    /**
     * Delete the "id" library.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
