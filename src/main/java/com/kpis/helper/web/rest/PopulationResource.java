package com.kpis.helper.web.rest;

import com.kpis.helper.repository.PopulationRepository;
import com.kpis.helper.service.PopulationQueryService;
import com.kpis.helper.service.PopulationService;
import com.kpis.helper.service.criteria.PopulationCriteria;
import com.kpis.helper.service.dto.PopulationDTO;
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
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.PaginationUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link com.kpis.helper.domain.Population}.
 */
@RestController
@RequestMapping("/api/populations")
public class PopulationResource {

    private final Logger log = LoggerFactory.getLogger(PopulationResource.class);

    private static final String ENTITY_NAME = "population";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final PopulationService populationService;

    private final PopulationRepository populationRepository;

    private final PopulationQueryService populationQueryService;

    public PopulationResource(
        PopulationService populationService,
        PopulationRepository populationRepository,
        PopulationQueryService populationQueryService
    ) {
        this.populationService = populationService;
        this.populationRepository = populationRepository;
        this.populationQueryService = populationQueryService;
    }

    /**
     * {@code POST  /populations} : Create a new population.
     *
     * @param populationDTO the populationDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new populationDTO, or with status {@code 400 (Bad Request)} if the population has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<PopulationDTO> createPopulation(@Valid @RequestBody PopulationDTO populationDTO) throws URISyntaxException {
        log.debug("REST request to save Population : {}", populationDTO);
        if (populationDTO.getId() != null) {
            throw new BadRequestAlertException("A new population cannot already have an ID", ENTITY_NAME, "idexists");
        }
        populationDTO = populationService.save(populationDTO);
        return ResponseEntity.created(new URI("/api/populations/" + populationDTO.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, populationDTO.getId().toString()))
            .body(populationDTO);
    }

    /**
     * {@code PUT  /populations/:id} : Updates an existing population.
     *
     * @param id the id of the populationDTO to save.
     * @param populationDTO the populationDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated populationDTO,
     * or with status {@code 400 (Bad Request)} if the populationDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the populationDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<PopulationDTO> updatePopulation(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody PopulationDTO populationDTO
    ) throws URISyntaxException {
        log.debug("REST request to update Population : {}, {}", id, populationDTO);
        if (populationDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, populationDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!populationRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        populationDTO = populationService.update(populationDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, populationDTO.getId().toString()))
            .body(populationDTO);
    }

    /**
     * {@code PATCH  /populations/:id} : Partial updates given fields of an existing population, field will ignore if it is null
     *
     * @param id the id of the populationDTO to save.
     * @param populationDTO the populationDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated populationDTO,
     * or with status {@code 400 (Bad Request)} if the populationDTO is not valid,
     * or with status {@code 404 (Not Found)} if the populationDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the populationDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<PopulationDTO> partialUpdatePopulation(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody PopulationDTO populationDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update Population partially : {}, {}", id, populationDTO);
        if (populationDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, populationDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!populationRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<PopulationDTO> result = populationService.partialUpdate(populationDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, populationDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /populations} : get all the populations.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of populations in body.
     */
    @GetMapping("")
    public ResponseEntity<List<PopulationDTO>> getAllPopulations(
        PopulationCriteria criteria,
        @org.springdoc.core.annotations.ParameterObject Pageable pageable
    ) {
        log.debug("REST request to get Populations by criteria: {}", criteria);

        Page<PopulationDTO> page = populationQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /populations/count} : count all the populations.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/count")
    public ResponseEntity<Long> countPopulations(PopulationCriteria criteria) {
        log.debug("REST request to count Populations by criteria: {}", criteria);
        return ResponseEntity.ok().body(populationQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /populations/:id} : get the "id" population.
     *
     * @param id the id of the populationDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the populationDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<PopulationDTO> getPopulation(@PathVariable("id") Long id) {
        log.debug("REST request to get Population : {}", id);
        Optional<PopulationDTO> populationDTO = populationService.findOne(id);
        return ResponseUtil.wrapOrNotFound(populationDTO);
    }

    /**
     * {@code DELETE  /populations/:id} : delete the "id" population.
     *
     * @param id the id of the populationDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePopulation(@PathVariable("id") Long id) {
        log.debug("REST request to delete Population : {}", id);
        populationService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
