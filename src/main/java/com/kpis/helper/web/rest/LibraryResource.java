package com.kpis.helper.web.rest;

import com.kpis.helper.repository.LibraryRepository;
import com.kpis.helper.service.LibraryService;
import com.kpis.helper.service.dto.LibraryDTO;
import com.kpis.helper.web.rest.errors.BadRequestAlertException;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link com.kpis.helper.domain.Library}.
 */
@RestController
@RequestMapping("/api/libraries")
public class LibraryResource {

    private final Logger log = LoggerFactory.getLogger(LibraryResource.class);

    private static final String ENTITY_NAME = "library";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final LibraryService libraryService;

    private final LibraryRepository libraryRepository;

    public LibraryResource(LibraryService libraryService, LibraryRepository libraryRepository) {
        this.libraryService = libraryService;
        this.libraryRepository = libraryRepository;
    }

    /**
     * {@code POST  /libraries} : Create a new library.
     *
     * @param libraryDTO the libraryDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new libraryDTO, or with status {@code 400 (Bad Request)} if the library has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<LibraryDTO> createLibrary(@Valid @RequestBody LibraryDTO libraryDTO) throws URISyntaxException {
        log.debug("REST request to save Library : {}", libraryDTO);
        if (libraryDTO.getId() != null) {
            throw new BadRequestAlertException("A new library cannot already have an ID", ENTITY_NAME, "idexists");
        }
        libraryDTO = libraryService.save(libraryDTO);
        return ResponseEntity.created(new URI("/api/libraries/" + libraryDTO.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, libraryDTO.getId().toString()))
            .body(libraryDTO);
    }

    /**
     * {@code PUT  /libraries/:id} : Updates an existing library.
     *
     * @param id the id of the libraryDTO to save.
     * @param libraryDTO the libraryDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated libraryDTO,
     * or with status {@code 400 (Bad Request)} if the libraryDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the libraryDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<LibraryDTO> updateLibrary(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody LibraryDTO libraryDTO
    ) throws URISyntaxException {
        log.debug("REST request to update Library : {}, {}", id, libraryDTO);
        if (libraryDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, libraryDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!libraryRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        libraryDTO = libraryService.update(libraryDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, libraryDTO.getId().toString()))
            .body(libraryDTO);
    }

    /**
     * {@code PATCH  /libraries/:id} : Partial updates given fields of an existing library, field will ignore if it is null
     *
     * @param id the id of the libraryDTO to save.
     * @param libraryDTO the libraryDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated libraryDTO,
     * or with status {@code 400 (Bad Request)} if the libraryDTO is not valid,
     * or with status {@code 404 (Not Found)} if the libraryDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the libraryDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<LibraryDTO> partialUpdateLibrary(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody LibraryDTO libraryDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update Library partially : {}, {}", id, libraryDTO);
        if (libraryDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, libraryDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!libraryRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<LibraryDTO> result = libraryService.partialUpdate(libraryDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, libraryDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /libraries} : get all the libraries.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of libraries in body.
     */
    @GetMapping("")
    public List<LibraryDTO> getAllLibraries() {
        log.debug("REST request to get all Libraries");
        return libraryService.findAll();
    }

    /**
     * {@code GET  /libraries/:id} : get the "id" library.
     *
     * @param id the id of the libraryDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the libraryDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<LibraryDTO> getLibrary(@PathVariable("id") Long id) {
        log.debug("REST request to get Library : {}", id);
        Optional<LibraryDTO> libraryDTO = libraryService.findOne(id);
        return ResponseUtil.wrapOrNotFound(libraryDTO);
    }

    /**
     * {@code DELETE  /libraries/:id} : delete the "id" library.
     *
     * @param id the id of the libraryDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteLibrary(@PathVariable("id") Long id) {
        log.debug("REST request to delete Library : {}", id);
        libraryService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
