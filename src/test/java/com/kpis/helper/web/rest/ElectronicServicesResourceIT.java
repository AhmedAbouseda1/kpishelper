package com.kpis.helper.web.rest;

import static com.kpis.helper.domain.ElectronicServicesAsserts.*;
import static com.kpis.helper.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.kpis.helper.IntegrationTest;
import com.kpis.helper.domain.ElectronicServices;
import com.kpis.helper.repository.ElectronicServicesRepository;
import com.kpis.helper.service.dto.ElectronicServicesDTO;
import com.kpis.helper.service.mapper.ElectronicServicesMapper;
import jakarta.persistence.EntityManager;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

/**
 * Integration tests for the {@link ElectronicServicesResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class ElectronicServicesResourceIT {

    private static final LocalDate DEFAULT_RECORDED_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_RECORDED_DATE = LocalDate.now(ZoneId.systemDefault());

    private static final Integer DEFAULT_TOTAL_PCS_WITH_INTERNET = 1;
    private static final Integer UPDATED_TOTAL_PCS_WITH_INTERNET = 2;

    private static final Integer DEFAULT_PCS_WITH_INTERNET_FOR_CLIENTS_ONLY = 1;
    private static final Integer UPDATED_PCS_WITH_INTERNET_FOR_CLIENTS_ONLY = 2;

    private static final String ENTITY_API_URL = "/api/electronic-services";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private ElectronicServicesRepository electronicServicesRepository;

    @Autowired
    private ElectronicServicesMapper electronicServicesMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restElectronicServicesMockMvc;

    private ElectronicServices electronicServices;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ElectronicServices createEntity(EntityManager em) {
        ElectronicServices electronicServices = new ElectronicServices()
            .recorded_date(DEFAULT_RECORDED_DATE)
            .total_pcs_with_internet(DEFAULT_TOTAL_PCS_WITH_INTERNET)
            .pcs_with_internet_for_clients_only(DEFAULT_PCS_WITH_INTERNET_FOR_CLIENTS_ONLY);
        return electronicServices;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ElectronicServices createUpdatedEntity(EntityManager em) {
        ElectronicServices electronicServices = new ElectronicServices()
            .recorded_date(UPDATED_RECORDED_DATE)
            .total_pcs_with_internet(UPDATED_TOTAL_PCS_WITH_INTERNET)
            .pcs_with_internet_for_clients_only(UPDATED_PCS_WITH_INTERNET_FOR_CLIENTS_ONLY);
        return electronicServices;
    }

    @BeforeEach
    public void initTest() {
        electronicServices = createEntity(em);
    }

    @Test
    @Transactional
    void createElectronicServices() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the ElectronicServices
        ElectronicServicesDTO electronicServicesDTO = electronicServicesMapper.toDto(electronicServices);
        var returnedElectronicServicesDTO = om.readValue(
            restElectronicServicesMockMvc
                .perform(
                    post(ENTITY_API_URL)
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(om.writeValueAsBytes(electronicServicesDTO))
                )
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            ElectronicServicesDTO.class
        );

        // Validate the ElectronicServices in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedElectronicServices = electronicServicesMapper.toEntity(returnedElectronicServicesDTO);
        assertElectronicServicesUpdatableFieldsEquals(
            returnedElectronicServices,
            getPersistedElectronicServices(returnedElectronicServices)
        );
    }

    @Test
    @Transactional
    void createElectronicServicesWithExistingId() throws Exception {
        // Create the ElectronicServices with an existing ID
        electronicServices.setId(1L);
        ElectronicServicesDTO electronicServicesDTO = electronicServicesMapper.toDto(electronicServices);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restElectronicServicesMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(electronicServicesDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ElectronicServices in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkRecorded_dateIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        electronicServices.setRecorded_date(null);

        // Create the ElectronicServices, which fails.
        ElectronicServicesDTO electronicServicesDTO = electronicServicesMapper.toDto(electronicServices);

        restElectronicServicesMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(electronicServicesDTO))
            )
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkTotal_pcs_with_internetIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        electronicServices.setTotal_pcs_with_internet(null);

        // Create the ElectronicServices, which fails.
        ElectronicServicesDTO electronicServicesDTO = electronicServicesMapper.toDto(electronicServices);

        restElectronicServicesMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(electronicServicesDTO))
            )
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllElectronicServices() throws Exception {
        // Initialize the database
        electronicServicesRepository.saveAndFlush(electronicServices);

        // Get all the electronicServicesList
        restElectronicServicesMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(electronicServices.getId().intValue())))
            .andExpect(jsonPath("$.[*].recorded_date").value(hasItem(DEFAULT_RECORDED_DATE.toString())))
            .andExpect(jsonPath("$.[*].total_pcs_with_internet").value(hasItem(DEFAULT_TOTAL_PCS_WITH_INTERNET)))
            .andExpect(jsonPath("$.[*].pcs_with_internet_for_clients_only").value(hasItem(DEFAULT_PCS_WITH_INTERNET_FOR_CLIENTS_ONLY)));
    }

    @Test
    @Transactional
    void getElectronicServices() throws Exception {
        // Initialize the database
        electronicServicesRepository.saveAndFlush(electronicServices);

        // Get the electronicServices
        restElectronicServicesMockMvc
            .perform(get(ENTITY_API_URL_ID, electronicServices.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(electronicServices.getId().intValue()))
            .andExpect(jsonPath("$.recorded_date").value(DEFAULT_RECORDED_DATE.toString()))
            .andExpect(jsonPath("$.total_pcs_with_internet").value(DEFAULT_TOTAL_PCS_WITH_INTERNET))
            .andExpect(jsonPath("$.pcs_with_internet_for_clients_only").value(DEFAULT_PCS_WITH_INTERNET_FOR_CLIENTS_ONLY));
    }

    @Test
    @Transactional
    void getNonExistingElectronicServices() throws Exception {
        // Get the electronicServices
        restElectronicServicesMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingElectronicServices() throws Exception {
        // Initialize the database
        electronicServicesRepository.saveAndFlush(electronicServices);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the electronicServices
        ElectronicServices updatedElectronicServices = electronicServicesRepository.findById(electronicServices.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedElectronicServices are not directly saved in db
        em.detach(updatedElectronicServices);
        updatedElectronicServices
            .recorded_date(UPDATED_RECORDED_DATE)
            .total_pcs_with_internet(UPDATED_TOTAL_PCS_WITH_INTERNET)
            .pcs_with_internet_for_clients_only(UPDATED_PCS_WITH_INTERNET_FOR_CLIENTS_ONLY);
        ElectronicServicesDTO electronicServicesDTO = electronicServicesMapper.toDto(updatedElectronicServices);

        restElectronicServicesMockMvc
            .perform(
                put(ENTITY_API_URL_ID, electronicServicesDTO.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(electronicServicesDTO))
            )
            .andExpect(status().isOk());

        // Validate the ElectronicServices in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedElectronicServicesToMatchAllProperties(updatedElectronicServices);
    }

    @Test
    @Transactional
    void putNonExistingElectronicServices() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        electronicServices.setId(longCount.incrementAndGet());

        // Create the ElectronicServices
        ElectronicServicesDTO electronicServicesDTO = electronicServicesMapper.toDto(electronicServices);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restElectronicServicesMockMvc
            .perform(
                put(ENTITY_API_URL_ID, electronicServicesDTO.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(electronicServicesDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ElectronicServices in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchElectronicServices() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        electronicServices.setId(longCount.incrementAndGet());

        // Create the ElectronicServices
        ElectronicServicesDTO electronicServicesDTO = electronicServicesMapper.toDto(electronicServices);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restElectronicServicesMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(electronicServicesDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ElectronicServices in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamElectronicServices() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        electronicServices.setId(longCount.incrementAndGet());

        // Create the ElectronicServices
        ElectronicServicesDTO electronicServicesDTO = electronicServicesMapper.toDto(electronicServices);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restElectronicServicesMockMvc
            .perform(
                put(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(electronicServicesDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the ElectronicServices in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateElectronicServicesWithPatch() throws Exception {
        // Initialize the database
        electronicServicesRepository.saveAndFlush(electronicServices);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the electronicServices using partial update
        ElectronicServices partialUpdatedElectronicServices = new ElectronicServices();
        partialUpdatedElectronicServices.setId(electronicServices.getId());

        partialUpdatedElectronicServices
            .total_pcs_with_internet(UPDATED_TOTAL_PCS_WITH_INTERNET)
            .pcs_with_internet_for_clients_only(UPDATED_PCS_WITH_INTERNET_FOR_CLIENTS_ONLY);

        restElectronicServicesMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedElectronicServices.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedElectronicServices))
            )
            .andExpect(status().isOk());

        // Validate the ElectronicServices in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertElectronicServicesUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedElectronicServices, electronicServices),
            getPersistedElectronicServices(electronicServices)
        );
    }

    @Test
    @Transactional
    void fullUpdateElectronicServicesWithPatch() throws Exception {
        // Initialize the database
        electronicServicesRepository.saveAndFlush(electronicServices);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the electronicServices using partial update
        ElectronicServices partialUpdatedElectronicServices = new ElectronicServices();
        partialUpdatedElectronicServices.setId(electronicServices.getId());

        partialUpdatedElectronicServices
            .recorded_date(UPDATED_RECORDED_DATE)
            .total_pcs_with_internet(UPDATED_TOTAL_PCS_WITH_INTERNET)
            .pcs_with_internet_for_clients_only(UPDATED_PCS_WITH_INTERNET_FOR_CLIENTS_ONLY);

        restElectronicServicesMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedElectronicServices.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedElectronicServices))
            )
            .andExpect(status().isOk());

        // Validate the ElectronicServices in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertElectronicServicesUpdatableFieldsEquals(
            partialUpdatedElectronicServices,
            getPersistedElectronicServices(partialUpdatedElectronicServices)
        );
    }

    @Test
    @Transactional
    void patchNonExistingElectronicServices() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        electronicServices.setId(longCount.incrementAndGet());

        // Create the ElectronicServices
        ElectronicServicesDTO electronicServicesDTO = electronicServicesMapper.toDto(electronicServices);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restElectronicServicesMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, electronicServicesDTO.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(electronicServicesDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ElectronicServices in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchElectronicServices() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        electronicServices.setId(longCount.incrementAndGet());

        // Create the ElectronicServices
        ElectronicServicesDTO electronicServicesDTO = electronicServicesMapper.toDto(electronicServices);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restElectronicServicesMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(electronicServicesDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ElectronicServices in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamElectronicServices() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        electronicServices.setId(longCount.incrementAndGet());

        // Create the ElectronicServices
        ElectronicServicesDTO electronicServicesDTO = electronicServicesMapper.toDto(electronicServices);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restElectronicServicesMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(electronicServicesDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the ElectronicServices in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteElectronicServices() throws Exception {
        // Initialize the database
        electronicServicesRepository.saveAndFlush(electronicServices);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the electronicServices
        restElectronicServicesMockMvc
            .perform(delete(ENTITY_API_URL_ID, electronicServices.getId()).with(csrf()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return electronicServicesRepository.count();
    }

    protected void assertIncrementedRepositoryCount(long countBefore) {
        assertThat(countBefore + 1).isEqualTo(getRepositoryCount());
    }

    protected void assertDecrementedRepositoryCount(long countBefore) {
        assertThat(countBefore - 1).isEqualTo(getRepositoryCount());
    }

    protected void assertSameRepositoryCount(long countBefore) {
        assertThat(countBefore).isEqualTo(getRepositoryCount());
    }

    protected ElectronicServices getPersistedElectronicServices(ElectronicServices electronicServices) {
        return electronicServicesRepository.findById(electronicServices.getId()).orElseThrow();
    }

    protected void assertPersistedElectronicServicesToMatchAllProperties(ElectronicServices expectedElectronicServices) {
        assertElectronicServicesAllPropertiesEquals(expectedElectronicServices, getPersistedElectronicServices(expectedElectronicServices));
    }

    protected void assertPersistedElectronicServicesToMatchUpdatableProperties(ElectronicServices expectedElectronicServices) {
        assertElectronicServicesAllUpdatablePropertiesEquals(
            expectedElectronicServices,
            getPersistedElectronicServices(expectedElectronicServices)
        );
    }
}
