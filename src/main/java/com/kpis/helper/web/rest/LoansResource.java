package com.kpis.helper.web.rest;

import com.kpis.helper.repository.LoansRepository;
import com.kpis.helper.service.LoansService;
import com.kpis.helper.service.dto.LoansDTO;
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
 * REST controller for managing {@link com.kpis.helper.domain.Loans}.
 */
@RestController
@RequestMapping("/api/loans")
public class LoansResource {

    private final Logger log = LoggerFactory.getLogger(LoansResource.class);

    private static final String ENTITY_NAME = "loans";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final LoansService loansService;

    private final LoansRepository loansRepository;

    public LoansResource(LoansService loansService, LoansRepository loansRepository) {
        this.loansService = loansService;
        this.loansRepository = loansRepository;
    }

    /**
     * {@code POST  /loans} : Create a new loans.
     *
     * @param loansDTO the loansDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new loansDTO, or with status {@code 400 (Bad Request)} if the loans has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<LoansDTO> createLoans(@Valid @RequestBody LoansDTO loansDTO) throws URISyntaxException {
        log.debug("REST request to save Loans : {}", loansDTO);
        if (loansDTO.getId() != null) {
            throw new BadRequestAlertException("A new loans cannot already have an ID", ENTITY_NAME, "idexists");
        }
        loansDTO = loansService.save(loansDTO);
        return ResponseEntity.created(new URI("/api/loans/" + loansDTO.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, loansDTO.getId().toString()))
            .body(loansDTO);
    }

    /**
     * {@code PUT  /loans/:id} : Updates an existing loans.
     *
     * @param id the id of the loansDTO to save.
     * @param loansDTO the loansDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated loansDTO,
     * or with status {@code 400 (Bad Request)} if the loansDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the loansDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<LoansDTO> updateLoans(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody LoansDTO loansDTO
    ) throws URISyntaxException {
        log.debug("REST request to update Loans : {}, {}", id, loansDTO);
        if (loansDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, loansDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!loansRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        loansDTO = loansService.update(loansDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, loansDTO.getId().toString()))
            .body(loansDTO);
    }

    /**
     * {@code PATCH  /loans/:id} : Partial updates given fields of an existing loans, field will ignore if it is null
     *
     * @param id the id of the loansDTO to save.
     * @param loansDTO the loansDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated loansDTO,
     * or with status {@code 400 (Bad Request)} if the loansDTO is not valid,
     * or with status {@code 404 (Not Found)} if the loansDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the loansDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<LoansDTO> partialUpdateLoans(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody LoansDTO loansDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update Loans partially : {}, {}", id, loansDTO);
        if (loansDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, loansDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!loansRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<LoansDTO> result = loansService.partialUpdate(loansDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, loansDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /loans} : get all the loans.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of loans in body.
     */
    @GetMapping("")
    public List<LoansDTO> getAllLoans() {
        log.debug("REST request to get all Loans");
        return loansService.findAll();
    }

    /**
     * {@code GET  /loans/:id} : get the "id" loans.
     *
     * @param id the id of the loansDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the loansDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<LoansDTO> getLoans(@PathVariable("id") Long id) {
        log.debug("REST request to get Loans : {}", id);
        Optional<LoansDTO> loansDTO = loansService.findOne(id);
        return ResponseUtil.wrapOrNotFound(loansDTO);
    }

    /**
     * {@code DELETE  /loans/:id} : delete the "id" loans.
     *
     * @param id the id of the loansDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteLoans(@PathVariable("id") Long id) {
        log.debug("REST request to delete Loans : {}", id);
        loansService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
