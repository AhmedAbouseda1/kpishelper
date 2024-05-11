package com.kpis.helper.web.rest;

import com.kpis.helper.repository.ActivitiesRepository;
import com.kpis.helper.service.ActivitiesService;
import com.kpis.helper.service.dto.ActivitiesDTO;
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
 * REST controller for managing {@link com.kpis.helper.domain.Activities}.
 */
@RestController
@RequestMapping("/api/activities")
public class ActivitiesResource {

    private final Logger log = LoggerFactory.getLogger(ActivitiesResource.class);

    private static final String ENTITY_NAME = "activities";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final ActivitiesService activitiesService;

    private final ActivitiesRepository activitiesRepository;

    public ActivitiesResource(ActivitiesService activitiesService, ActivitiesRepository activitiesRepository) {
        this.activitiesService = activitiesService;
        this.activitiesRepository = activitiesRepository;
    }

    /**
     * {@code POST  /activities} : Create a new activities.
     *
     * @param activitiesDTO the activitiesDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new activitiesDTO, or with status {@code 400 (Bad Request)} if the activities has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<ActivitiesDTO> createActivities(@Valid @RequestBody ActivitiesDTO activitiesDTO) throws URISyntaxException {
        log.debug("REST request to save Activities : {}", activitiesDTO);
        if (activitiesDTO.getId() != null) {
            throw new BadRequestAlertException("A new activities cannot already have an ID", ENTITY_NAME, "idexists");
        }
        activitiesDTO = activitiesService.save(activitiesDTO);
        return ResponseEntity.created(new URI("/api/activities/" + activitiesDTO.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, activitiesDTO.getId().toString()))
            .body(activitiesDTO);
    }

    /**
     * {@code PUT  /activities/:id} : Updates an existing activities.
     *
     * @param id the id of the activitiesDTO to save.
     * @param activitiesDTO the activitiesDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated activitiesDTO,
     * or with status {@code 400 (Bad Request)} if the activitiesDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the activitiesDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<ActivitiesDTO> updateActivities(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody ActivitiesDTO activitiesDTO
    ) throws URISyntaxException {
        log.debug("REST request to update Activities : {}, {}", id, activitiesDTO);
        if (activitiesDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, activitiesDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!activitiesRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        activitiesDTO = activitiesService.update(activitiesDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, activitiesDTO.getId().toString()))
            .body(activitiesDTO);
    }

    /**
     * {@code PATCH  /activities/:id} : Partial updates given fields of an existing activities, field will ignore if it is null
     *
     * @param id the id of the activitiesDTO to save.
     * @param activitiesDTO the activitiesDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated activitiesDTO,
     * or with status {@code 400 (Bad Request)} if the activitiesDTO is not valid,
     * or with status {@code 404 (Not Found)} if the activitiesDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the activitiesDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<ActivitiesDTO> partialUpdateActivities(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody ActivitiesDTO activitiesDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update Activities partially : {}, {}", id, activitiesDTO);
        if (activitiesDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, activitiesDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!activitiesRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<ActivitiesDTO> result = activitiesService.partialUpdate(activitiesDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, activitiesDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /activities} : get all the activities.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of activities in body.
     */
    @GetMapping("")
    public List<ActivitiesDTO> getAllActivities() {
        log.debug("REST request to get all Activities");
        return activitiesService.findAll();
    }

    /**
     * {@code GET  /activities/:id} : get the "id" activities.
     *
     * @param id the id of the activitiesDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the activitiesDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<ActivitiesDTO> getActivities(@PathVariable("id") Long id) {
        log.debug("REST request to get Activities : {}", id);
        Optional<ActivitiesDTO> activitiesDTO = activitiesService.findOne(id);
        return ResponseUtil.wrapOrNotFound(activitiesDTO);
    }

    /**
     * {@code DELETE  /activities/:id} : delete the "id" activities.
     *
     * @param id the id of the activitiesDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteActivities(@PathVariable("id") Long id) {
        log.debug("REST request to delete Activities : {}", id);
        activitiesService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
