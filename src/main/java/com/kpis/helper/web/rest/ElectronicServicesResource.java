package com.kpis.helper.web.rest;

import com.kpis.helper.repository.ElectronicServicesRepository;
import com.kpis.helper.service.ElectronicServicesService;
import com.kpis.helper.service.dto.ElectronicServicesDTO;
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
 * REST controller for managing {@link com.kpis.helper.domain.ElectronicServices}.
 */
@RestController
@RequestMapping("/api/electronic-services")
public class ElectronicServicesResource {

    private final Logger log = LoggerFactory.getLogger(ElectronicServicesResource.class);

    private static final String ENTITY_NAME = "electronicServices";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final ElectronicServicesService electronicServicesService;

    private final ElectronicServicesRepository electronicServicesRepository;

    public ElectronicServicesResource(
        ElectronicServicesService electronicServicesService,
        ElectronicServicesRepository electronicServicesRepository
    ) {
        this.electronicServicesService = electronicServicesService;
        this.electronicServicesRepository = electronicServicesRepository;
    }

    /**
     * {@code POST  /electronic-services} : Create a new electronicServices.
     *
     * @param electronicServicesDTO the electronicServicesDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new electronicServicesDTO, or with status {@code 400 (Bad Request)} if the electronicServices has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<ElectronicServicesDTO> createElectronicServices(@Valid @RequestBody ElectronicServicesDTO electronicServicesDTO)
        throws URISyntaxException {
        log.debug("REST request to save ElectronicServices : {}", electronicServicesDTO);
        if (electronicServicesDTO.getId() != null) {
            throw new BadRequestAlertException("A new electronicServices cannot already have an ID", ENTITY_NAME, "idexists");
        }
        electronicServicesDTO = electronicServicesService.save(electronicServicesDTO);
        return ResponseEntity.created(new URI("/api/electronic-services/" + electronicServicesDTO.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, electronicServicesDTO.getId().toString()))
            .body(electronicServicesDTO);
    }

    /**
     * {@code PUT  /electronic-services/:id} : Updates an existing electronicServices.
     *
     * @param id the id of the electronicServicesDTO to save.
     * @param electronicServicesDTO the electronicServicesDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated electronicServicesDTO,
     * or with status {@code 400 (Bad Request)} if the electronicServicesDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the electronicServicesDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<ElectronicServicesDTO> updateElectronicServices(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody ElectronicServicesDTO electronicServicesDTO
    ) throws URISyntaxException {
        log.debug("REST request to update ElectronicServices : {}, {}", id, electronicServicesDTO);
        if (electronicServicesDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, electronicServicesDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!electronicServicesRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        electronicServicesDTO = electronicServicesService.update(electronicServicesDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, electronicServicesDTO.getId().toString()))
            .body(electronicServicesDTO);
    }

    /**
     * {@code PATCH  /electronic-services/:id} : Partial updates given fields of an existing electronicServices, field will ignore if it is null
     *
     * @param id the id of the electronicServicesDTO to save.
     * @param electronicServicesDTO the electronicServicesDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated electronicServicesDTO,
     * or with status {@code 400 (Bad Request)} if the electronicServicesDTO is not valid,
     * or with status {@code 404 (Not Found)} if the electronicServicesDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the electronicServicesDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<ElectronicServicesDTO> partialUpdateElectronicServices(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody ElectronicServicesDTO electronicServicesDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update ElectronicServices partially : {}, {}", id, electronicServicesDTO);
        if (electronicServicesDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, electronicServicesDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!electronicServicesRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<ElectronicServicesDTO> result = electronicServicesService.partialUpdate(electronicServicesDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, electronicServicesDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /electronic-services} : get all the electronicServices.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of electronicServices in body.
     */
    @GetMapping("")
    public List<ElectronicServicesDTO> getAllElectronicServices() {
        log.debug("REST request to get all ElectronicServices");
        return electronicServicesService.findAll();
    }

    /**
     * {@code GET  /electronic-services/:id} : get the "id" electronicServices.
     *
     * @param id the id of the electronicServicesDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the electronicServicesDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<ElectronicServicesDTO> getElectronicServices(@PathVariable("id") Long id) {
        log.debug("REST request to get ElectronicServices : {}", id);
        Optional<ElectronicServicesDTO> electronicServicesDTO = electronicServicesService.findOne(id);
        return ResponseUtil.wrapOrNotFound(electronicServicesDTO);
    }

    /**
     * {@code DELETE  /electronic-services/:id} : delete the "id" electronicServices.
     *
     * @param id the id of the electronicServicesDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteElectronicServices(@PathVariable("id") Long id) {
        log.debug("REST request to delete ElectronicServices : {}", id);
        electronicServicesService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
