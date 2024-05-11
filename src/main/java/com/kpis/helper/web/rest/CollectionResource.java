package com.kpis.helper.web.rest;

import com.kpis.helper.repository.CollectionRepository;
import com.kpis.helper.service.CollectionService;
import com.kpis.helper.service.dto.CollectionDTO;
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
 * REST controller for managing {@link com.kpis.helper.domain.Collection}.
 */
@RestController
@RequestMapping("/api/collections")
public class CollectionResource {

    private final Logger log = LoggerFactory.getLogger(CollectionResource.class);

    private static final String ENTITY_NAME = "collection";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final CollectionService collectionService;
    
    private final CollectionRepository collectionRepository;
    
    private final ActivitiesRepository activitiesRepository;
    
    public CollectionResource(CollectionService collectionService, CollectionRepository collectionRepository, ActivitiesRepository activitiesRepository) {
        this.collectionService = collectionService;
        this.collectionRepository = collectionRepository;
        this.activitiesRepository = activitiesRepository;
    }
    
    @GetMapping("/api/dashboard")
    public DashboardDTO getDashboardData() {
        log.debug("REST request to get dashboard data");
        return collectionService.getDashboardData();
    }

    /**
     * {@code POST  /collections} : Create a new collection.
     *
     * @param collectionDTO the collectionDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new collectionDTO, or with status {@code 400 (Bad Request)} if the collection has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<CollectionDTO> createCollection(@Valid @RequestBody CollectionDTO collectionDTO) throws URISyntaxException {
        log.debug("REST request to save Collection : {}", collectionDTO);
        if (collectionDTO.getId() != null) {
            throw new BadRequestAlertException("A new collection cannot already have an ID", ENTITY_NAME, "idexists");
        }
        collectionDTO = collectionService.save(collectionDTO);
        return ResponseEntity.created(new URI("/api/collections/" + collectionDTO.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, collectionDTO.getId().toString()))
            .body(collectionDTO);
    }

    /**
     * {@code PUT  /collections/:id} : Updates an existing collection.
     *
     * @param id the id of the collectionDTO to save.
     * @param collectionDTO the collectionDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated collectionDTO,
     * or with status {@code 400 (Bad Request)} if the collectionDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the collectionDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<CollectionDTO> updateCollection(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody CollectionDTO collectionDTO
    ) throws URISyntaxException {
        log.debug("REST request to update Collection : {}, {}", id, collectionDTO);
        if (collectionDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, collectionDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!collectionRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        collectionDTO = collectionService.update(collectionDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, collectionDTO.getId().toString()))
            .body(collectionDTO);
    }

    /**
     * {@code PATCH  /collections/:id} : Partial updates given fields of an existing collection, field will ignore if it is null
     *
     * @param id the id of the collectionDTO to save.
     * @param collectionDTO the collectionDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated collectionDTO,
     * or with status {@code 400 (Bad Request)} if the collectionDTO is not valid,
     * or with status {@code 404 (Not Found)} if the collectionDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the collectionDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<CollectionDTO> partialUpdateCollection(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody CollectionDTO collectionDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update Collection partially : {}, {}", id, collectionDTO);
        if (collectionDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, collectionDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!collectionRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<CollectionDTO> result = collectionService.partialUpdate(collectionDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, collectionDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /collections} : get all the collections.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of collections in body.
     */
    @GetMapping("")
    public List<CollectionDTO> getAllCollections() {
        log.debug("REST request to get all Collections");
        return collectionService.findAll();
    }

    /**
     * {@code GET  /collections/:id} : get the "id" collection.
     *
     * @param id the id of the collectionDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the collectionDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<CollectionDTO> getCollection(@PathVariable("id") Long id) {
        log.debug("REST request to get Collection : {}", id);
        Optional<CollectionDTO> collectionDTO = collectionService.findOne(id);
        return ResponseUtil.wrapOrNotFound(collectionDTO);
    }

    /**
     * {@code DELETE  /collections/:id} : delete the "id" collection.
     *
     * @param id the id of the collectionDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCollection(@PathVariable("id") Long id) {
        log.debug("REST request to delete Collection : {}", id);
        collectionService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
