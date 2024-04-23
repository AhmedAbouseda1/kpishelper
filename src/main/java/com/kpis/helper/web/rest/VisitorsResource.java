package com.kpis.helper.web.rest;

import com.kpis.helper.repository.VisitorsRepository;
import com.kpis.helper.service.VisitorsQueryService;
import com.kpis.helper.service.VisitorsService;
import com.kpis.helper.service.criteria.VisitorsCriteria;
import com.kpis.helper.service.dto.VisitorsDTO;
import com.kpis.helper.web.rest.errors.BadRequestAlertException;
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
 * REST controller for managing {@link com.kpis.helper.domain.Visitors}.
 */
@RestController
@RequestMapping("/api/visitors")
public class VisitorsResource {

    private final Logger log = LoggerFactory.getLogger(VisitorsResource.class);

    private static final String ENTITY_NAME = "visitors";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final VisitorsService visitorsService;

    private final VisitorsRepository visitorsRepository;

    private final VisitorsQueryService visitorsQueryService;

    public VisitorsResource(
        VisitorsService visitorsService,
        VisitorsRepository visitorsRepository,
        VisitorsQueryService visitorsQueryService
    ) {
        this.visitorsService = visitorsService;
        this.visitorsRepository = visitorsRepository;
        this.visitorsQueryService = visitorsQueryService;
    }

    /**
     * {@code POST  /visitors} : Create a new visitors.
     *
     * @param visitorsDTO the visitorsDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new visitorsDTO, or with status {@code 400 (Bad Request)} if the visitors has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<VisitorsDTO> createVisitors(@RequestBody VisitorsDTO visitorsDTO) throws URISyntaxException {
        log.debug("REST request to save Visitors : {}", visitorsDTO);
        if (visitorsDTO.getId() != null) {
            throw new BadRequestAlertException("A new visitors cannot already have an ID", ENTITY_NAME, "idexists");
        }
        visitorsDTO = visitorsService.save(visitorsDTO);
        return ResponseEntity.created(new URI("/api/visitors/" + visitorsDTO.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, visitorsDTO.getId().toString()))
            .body(visitorsDTO);
    }

    /**
     * {@code PUT  /visitors/:id} : Updates an existing visitors.
     *
     * @param id the id of the visitorsDTO to save.
     * @param visitorsDTO the visitorsDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated visitorsDTO,
     * or with status {@code 400 (Bad Request)} if the visitorsDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the visitorsDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<VisitorsDTO> updateVisitors(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody VisitorsDTO visitorsDTO
    ) throws URISyntaxException {
        log.debug("REST request to update Visitors : {}, {}", id, visitorsDTO);
        if (visitorsDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, visitorsDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!visitorsRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        visitorsDTO = visitorsService.update(visitorsDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, visitorsDTO.getId().toString()))
            .body(visitorsDTO);
    }

    /**
     * {@code PATCH  /visitors/:id} : Partial updates given fields of an existing visitors, field will ignore if it is null
     *
     * @param id the id of the visitorsDTO to save.
     * @param visitorsDTO the visitorsDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated visitorsDTO,
     * or with status {@code 400 (Bad Request)} if the visitorsDTO is not valid,
     * or with status {@code 404 (Not Found)} if the visitorsDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the visitorsDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<VisitorsDTO> partialUpdateVisitors(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody VisitorsDTO visitorsDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update Visitors partially : {}, {}", id, visitorsDTO);
        if (visitorsDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, visitorsDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!visitorsRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<VisitorsDTO> result = visitorsService.partialUpdate(visitorsDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, visitorsDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /visitors} : get all the visitors.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of visitors in body.
     */
    @GetMapping("")
    public ResponseEntity<List<VisitorsDTO>> getAllVisitors(
        VisitorsCriteria criteria,
        @org.springdoc.core.annotations.ParameterObject Pageable pageable
    ) {
        log.debug("REST request to get Visitors by criteria: {}", criteria);

        Page<VisitorsDTO> page = visitorsQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /visitors/count} : count all the visitors.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/count")
    public ResponseEntity<Long> countVisitors(VisitorsCriteria criteria) {
        log.debug("REST request to count Visitors by criteria: {}", criteria);
        return ResponseEntity.ok().body(visitorsQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /visitors/:id} : get the "id" visitors.
     *
     * @param id the id of the visitorsDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the visitorsDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<VisitorsDTO> getVisitors(@PathVariable("id") Long id) {
        log.debug("REST request to get Visitors : {}", id);
        Optional<VisitorsDTO> visitorsDTO = visitorsService.findOne(id);
        return ResponseUtil.wrapOrNotFound(visitorsDTO);
    }

    /**
     * {@code DELETE  /visitors/:id} : delete the "id" visitors.
     *
     * @param id the id of the visitorsDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteVisitors(@PathVariable("id") Long id) {
        log.debug("REST request to delete Visitors : {}", id);
        visitorsService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
