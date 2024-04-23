package com.kpis.helper.web.rest;

import static com.kpis.helper.domain.VisitorsAsserts.*;
import static com.kpis.helper.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.kpis.helper.IntegrationTest;
import com.kpis.helper.domain.Visitors;
import com.kpis.helper.repository.VisitorsRepository;
import com.kpis.helper.service.dto.VisitorsDTO;
import com.kpis.helper.service.mapper.VisitorsMapper;
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
 * Integration tests for the {@link VisitorsResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class VisitorsResourceIT {

    private static final Integer DEFAULT_TOTAL_VISITORS = 1;
    private static final Integer UPDATED_TOTAL_VISITORS = 2;
    private static final Integer SMALLER_TOTAL_VISITORS = 1 - 1;

    private static final Integer DEFAULT_WEBSITE_VISITORS = 1;
    private static final Integer UPDATED_WEBSITE_VISITORS = 2;
    private static final Integer SMALLER_WEBSITE_VISITORS = 1 - 1;

    private static final LocalDate DEFAULT_RECORDED_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_RECORDED_DATE = LocalDate.now(ZoneId.systemDefault());
    private static final LocalDate SMALLER_RECORDED_DATE = LocalDate.ofEpochDay(-1L);

    private static final String ENTITY_API_URL = "/api/visitors";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private VisitorsRepository visitorsRepository;

    @Autowired
    private VisitorsMapper visitorsMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restVisitorsMockMvc;

    private Visitors visitors;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Visitors createEntity(EntityManager em) {
        Visitors visitors = new Visitors()
            .total_visitors(DEFAULT_TOTAL_VISITORS)
            .website_visitors(DEFAULT_WEBSITE_VISITORS)
            .recorded_date(DEFAULT_RECORDED_DATE);
        return visitors;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Visitors createUpdatedEntity(EntityManager em) {
        Visitors visitors = new Visitors()
            .total_visitors(UPDATED_TOTAL_VISITORS)
            .website_visitors(UPDATED_WEBSITE_VISITORS)
            .recorded_date(UPDATED_RECORDED_DATE);
        return visitors;
    }

    @BeforeEach
    public void initTest() {
        visitors = createEntity(em);
    }

    @Test
    @Transactional
    void createVisitors() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the Visitors
        VisitorsDTO visitorsDTO = visitorsMapper.toDto(visitors);
        var returnedVisitorsDTO = om.readValue(
            restVisitorsMockMvc
                .perform(
                    post(ENTITY_API_URL).with(csrf()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(visitorsDTO))
                )
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            VisitorsDTO.class
        );

        // Validate the Visitors in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedVisitors = visitorsMapper.toEntity(returnedVisitorsDTO);
        assertVisitorsUpdatableFieldsEquals(returnedVisitors, getPersistedVisitors(returnedVisitors));
    }

    @Test
    @Transactional
    void createVisitorsWithExistingId() throws Exception {
        // Create the Visitors with an existing ID
        visitors.setId(1L);
        VisitorsDTO visitorsDTO = visitorsMapper.toDto(visitors);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restVisitorsMockMvc
            .perform(post(ENTITY_API_URL).with(csrf()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(visitorsDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Visitors in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllVisitors() throws Exception {
        // Initialize the database
        visitorsRepository.saveAndFlush(visitors);

        // Get all the visitorsList
        restVisitorsMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(visitors.getId().intValue())))
            .andExpect(jsonPath("$.[*].total_visitors").value(hasItem(DEFAULT_TOTAL_VISITORS)))
            .andExpect(jsonPath("$.[*].website_visitors").value(hasItem(DEFAULT_WEBSITE_VISITORS)))
            .andExpect(jsonPath("$.[*].recorded_date").value(hasItem(DEFAULT_RECORDED_DATE.toString())));
    }

    @Test
    @Transactional
    void getVisitors() throws Exception {
        // Initialize the database
        visitorsRepository.saveAndFlush(visitors);

        // Get the visitors
        restVisitorsMockMvc
            .perform(get(ENTITY_API_URL_ID, visitors.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(visitors.getId().intValue()))
            .andExpect(jsonPath("$.total_visitors").value(DEFAULT_TOTAL_VISITORS))
            .andExpect(jsonPath("$.website_visitors").value(DEFAULT_WEBSITE_VISITORS))
            .andExpect(jsonPath("$.recorded_date").value(DEFAULT_RECORDED_DATE.toString()));
    }

    @Test
    @Transactional
    void getVisitorsByIdFiltering() throws Exception {
        // Initialize the database
        visitorsRepository.saveAndFlush(visitors);

        Long id = visitors.getId();

        defaultVisitorsFiltering("id.equals=" + id, "id.notEquals=" + id);

        defaultVisitorsFiltering("id.greaterThanOrEqual=" + id, "id.greaterThan=" + id);

        defaultVisitorsFiltering("id.lessThanOrEqual=" + id, "id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllVisitorsByTotal_visitorsIsEqualToSomething() throws Exception {
        // Initialize the database
        visitorsRepository.saveAndFlush(visitors);

        // Get all the visitorsList where total_visitors equals to
        defaultVisitorsFiltering("total_visitors.equals=" + DEFAULT_TOTAL_VISITORS, "total_visitors.equals=" + UPDATED_TOTAL_VISITORS);
    }

    @Test
    @Transactional
    void getAllVisitorsByTotal_visitorsIsInShouldWork() throws Exception {
        // Initialize the database
        visitorsRepository.saveAndFlush(visitors);

        // Get all the visitorsList where total_visitors in
        defaultVisitorsFiltering(
            "total_visitors.in=" + DEFAULT_TOTAL_VISITORS + "," + UPDATED_TOTAL_VISITORS,
            "total_visitors.in=" + UPDATED_TOTAL_VISITORS
        );
    }

    @Test
    @Transactional
    void getAllVisitorsByTotal_visitorsIsNullOrNotNull() throws Exception {
        // Initialize the database
        visitorsRepository.saveAndFlush(visitors);

        // Get all the visitorsList where total_visitors is not null
        defaultVisitorsFiltering("total_visitors.specified=true", "total_visitors.specified=false");
    }

    @Test
    @Transactional
    void getAllVisitorsByTotal_visitorsIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        visitorsRepository.saveAndFlush(visitors);

        // Get all the visitorsList where total_visitors is greater than or equal to
        defaultVisitorsFiltering(
            "total_visitors.greaterThanOrEqual=" + DEFAULT_TOTAL_VISITORS,
            "total_visitors.greaterThanOrEqual=" + UPDATED_TOTAL_VISITORS
        );
    }

    @Test
    @Transactional
    void getAllVisitorsByTotal_visitorsIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        visitorsRepository.saveAndFlush(visitors);

        // Get all the visitorsList where total_visitors is less than or equal to
        defaultVisitorsFiltering(
            "total_visitors.lessThanOrEqual=" + DEFAULT_TOTAL_VISITORS,
            "total_visitors.lessThanOrEqual=" + SMALLER_TOTAL_VISITORS
        );
    }

    @Test
    @Transactional
    void getAllVisitorsByTotal_visitorsIsLessThanSomething() throws Exception {
        // Initialize the database
        visitorsRepository.saveAndFlush(visitors);

        // Get all the visitorsList where total_visitors is less than
        defaultVisitorsFiltering("total_visitors.lessThan=" + UPDATED_TOTAL_VISITORS, "total_visitors.lessThan=" + DEFAULT_TOTAL_VISITORS);
    }

    @Test
    @Transactional
    void getAllVisitorsByTotal_visitorsIsGreaterThanSomething() throws Exception {
        // Initialize the database
        visitorsRepository.saveAndFlush(visitors);

        // Get all the visitorsList where total_visitors is greater than
        defaultVisitorsFiltering(
            "total_visitors.greaterThan=" + SMALLER_TOTAL_VISITORS,
            "total_visitors.greaterThan=" + DEFAULT_TOTAL_VISITORS
        );
    }

    @Test
    @Transactional
    void getAllVisitorsByWebsite_visitorsIsEqualToSomething() throws Exception {
        // Initialize the database
        visitorsRepository.saveAndFlush(visitors);

        // Get all the visitorsList where website_visitors equals to
        defaultVisitorsFiltering(
            "website_visitors.equals=" + DEFAULT_WEBSITE_VISITORS,
            "website_visitors.equals=" + UPDATED_WEBSITE_VISITORS
        );
    }

    @Test
    @Transactional
    void getAllVisitorsByWebsite_visitorsIsInShouldWork() throws Exception {
        // Initialize the database
        visitorsRepository.saveAndFlush(visitors);

        // Get all the visitorsList where website_visitors in
        defaultVisitorsFiltering(
            "website_visitors.in=" + DEFAULT_WEBSITE_VISITORS + "," + UPDATED_WEBSITE_VISITORS,
            "website_visitors.in=" + UPDATED_WEBSITE_VISITORS
        );
    }

    @Test
    @Transactional
    void getAllVisitorsByWebsite_visitorsIsNullOrNotNull() throws Exception {
        // Initialize the database
        visitorsRepository.saveAndFlush(visitors);

        // Get all the visitorsList where website_visitors is not null
        defaultVisitorsFiltering("website_visitors.specified=true", "website_visitors.specified=false");
    }

    @Test
    @Transactional
    void getAllVisitorsByWebsite_visitorsIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        visitorsRepository.saveAndFlush(visitors);

        // Get all the visitorsList where website_visitors is greater than or equal to
        defaultVisitorsFiltering(
            "website_visitors.greaterThanOrEqual=" + DEFAULT_WEBSITE_VISITORS,
            "website_visitors.greaterThanOrEqual=" + UPDATED_WEBSITE_VISITORS
        );
    }

    @Test
    @Transactional
    void getAllVisitorsByWebsite_visitorsIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        visitorsRepository.saveAndFlush(visitors);

        // Get all the visitorsList where website_visitors is less than or equal to
        defaultVisitorsFiltering(
            "website_visitors.lessThanOrEqual=" + DEFAULT_WEBSITE_VISITORS,
            "website_visitors.lessThanOrEqual=" + SMALLER_WEBSITE_VISITORS
        );
    }

    @Test
    @Transactional
    void getAllVisitorsByWebsite_visitorsIsLessThanSomething() throws Exception {
        // Initialize the database
        visitorsRepository.saveAndFlush(visitors);

        // Get all the visitorsList where website_visitors is less than
        defaultVisitorsFiltering(
            "website_visitors.lessThan=" + UPDATED_WEBSITE_VISITORS,
            "website_visitors.lessThan=" + DEFAULT_WEBSITE_VISITORS
        );
    }

    @Test
    @Transactional
    void getAllVisitorsByWebsite_visitorsIsGreaterThanSomething() throws Exception {
        // Initialize the database
        visitorsRepository.saveAndFlush(visitors);

        // Get all the visitorsList where website_visitors is greater than
        defaultVisitorsFiltering(
            "website_visitors.greaterThan=" + SMALLER_WEBSITE_VISITORS,
            "website_visitors.greaterThan=" + DEFAULT_WEBSITE_VISITORS
        );
    }

    @Test
    @Transactional
    void getAllVisitorsByRecorded_dateIsEqualToSomething() throws Exception {
        // Initialize the database
        visitorsRepository.saveAndFlush(visitors);

        // Get all the visitorsList where recorded_date equals to
        defaultVisitorsFiltering("recorded_date.equals=" + DEFAULT_RECORDED_DATE, "recorded_date.equals=" + UPDATED_RECORDED_DATE);
    }

    @Test
    @Transactional
    void getAllVisitorsByRecorded_dateIsInShouldWork() throws Exception {
        // Initialize the database
        visitorsRepository.saveAndFlush(visitors);

        // Get all the visitorsList where recorded_date in
        defaultVisitorsFiltering(
            "recorded_date.in=" + DEFAULT_RECORDED_DATE + "," + UPDATED_RECORDED_DATE,
            "recorded_date.in=" + UPDATED_RECORDED_DATE
        );
    }

    @Test
    @Transactional
    void getAllVisitorsByRecorded_dateIsNullOrNotNull() throws Exception {
        // Initialize the database
        visitorsRepository.saveAndFlush(visitors);

        // Get all the visitorsList where recorded_date is not null
        defaultVisitorsFiltering("recorded_date.specified=true", "recorded_date.specified=false");
    }

    @Test
    @Transactional
    void getAllVisitorsByRecorded_dateIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        visitorsRepository.saveAndFlush(visitors);

        // Get all the visitorsList where recorded_date is greater than or equal to
        defaultVisitorsFiltering(
            "recorded_date.greaterThanOrEqual=" + DEFAULT_RECORDED_DATE,
            "recorded_date.greaterThanOrEqual=" + UPDATED_RECORDED_DATE
        );
    }

    @Test
    @Transactional
    void getAllVisitorsByRecorded_dateIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        visitorsRepository.saveAndFlush(visitors);

        // Get all the visitorsList where recorded_date is less than or equal to
        defaultVisitorsFiltering(
            "recorded_date.lessThanOrEqual=" + DEFAULT_RECORDED_DATE,
            "recorded_date.lessThanOrEqual=" + SMALLER_RECORDED_DATE
        );
    }

    @Test
    @Transactional
    void getAllVisitorsByRecorded_dateIsLessThanSomething() throws Exception {
        // Initialize the database
        visitorsRepository.saveAndFlush(visitors);

        // Get all the visitorsList where recorded_date is less than
        defaultVisitorsFiltering("recorded_date.lessThan=" + UPDATED_RECORDED_DATE, "recorded_date.lessThan=" + DEFAULT_RECORDED_DATE);
    }

    @Test
    @Transactional
    void getAllVisitorsByRecorded_dateIsGreaterThanSomething() throws Exception {
        // Initialize the database
        visitorsRepository.saveAndFlush(visitors);

        // Get all the visitorsList where recorded_date is greater than
        defaultVisitorsFiltering(
            "recorded_date.greaterThan=" + SMALLER_RECORDED_DATE,
            "recorded_date.greaterThan=" + DEFAULT_RECORDED_DATE
        );
    }

    private void defaultVisitorsFiltering(String shouldBeFound, String shouldNotBeFound) throws Exception {
        defaultVisitorsShouldBeFound(shouldBeFound);
        defaultVisitorsShouldNotBeFound(shouldNotBeFound);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultVisitorsShouldBeFound(String filter) throws Exception {
        restVisitorsMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(visitors.getId().intValue())))
            .andExpect(jsonPath("$.[*].total_visitors").value(hasItem(DEFAULT_TOTAL_VISITORS)))
            .andExpect(jsonPath("$.[*].website_visitors").value(hasItem(DEFAULT_WEBSITE_VISITORS)))
            .andExpect(jsonPath("$.[*].recorded_date").value(hasItem(DEFAULT_RECORDED_DATE.toString())));

        // Check, that the count call also returns 1
        restVisitorsMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultVisitorsShouldNotBeFound(String filter) throws Exception {
        restVisitorsMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restVisitorsMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingVisitors() throws Exception {
        // Get the visitors
        restVisitorsMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingVisitors() throws Exception {
        // Initialize the database
        visitorsRepository.saveAndFlush(visitors);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the visitors
        Visitors updatedVisitors = visitorsRepository.findById(visitors.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedVisitors are not directly saved in db
        em.detach(updatedVisitors);
        updatedVisitors
            .total_visitors(UPDATED_TOTAL_VISITORS)
            .website_visitors(UPDATED_WEBSITE_VISITORS)
            .recorded_date(UPDATED_RECORDED_DATE);
        VisitorsDTO visitorsDTO = visitorsMapper.toDto(updatedVisitors);

        restVisitorsMockMvc
            .perform(
                put(ENTITY_API_URL_ID, visitorsDTO.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(visitorsDTO))
            )
            .andExpect(status().isOk());

        // Validate the Visitors in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedVisitorsToMatchAllProperties(updatedVisitors);
    }

    @Test
    @Transactional
    void putNonExistingVisitors() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        visitors.setId(longCount.incrementAndGet());

        // Create the Visitors
        VisitorsDTO visitorsDTO = visitorsMapper.toDto(visitors);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restVisitorsMockMvc
            .perform(
                put(ENTITY_API_URL_ID, visitorsDTO.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(visitorsDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Visitors in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchVisitors() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        visitors.setId(longCount.incrementAndGet());

        // Create the Visitors
        VisitorsDTO visitorsDTO = visitorsMapper.toDto(visitors);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restVisitorsMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(visitorsDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Visitors in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamVisitors() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        visitors.setId(longCount.incrementAndGet());

        // Create the Visitors
        VisitorsDTO visitorsDTO = visitorsMapper.toDto(visitors);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restVisitorsMockMvc
            .perform(put(ENTITY_API_URL).with(csrf()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(visitorsDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Visitors in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateVisitorsWithPatch() throws Exception {
        // Initialize the database
        visitorsRepository.saveAndFlush(visitors);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the visitors using partial update
        Visitors partialUpdatedVisitors = new Visitors();
        partialUpdatedVisitors.setId(visitors.getId());

        partialUpdatedVisitors.total_visitors(UPDATED_TOTAL_VISITORS).website_visitors(UPDATED_WEBSITE_VISITORS);

        restVisitorsMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedVisitors.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedVisitors))
            )
            .andExpect(status().isOk());

        // Validate the Visitors in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertVisitorsUpdatableFieldsEquals(createUpdateProxyForBean(partialUpdatedVisitors, visitors), getPersistedVisitors(visitors));
    }

    @Test
    @Transactional
    void fullUpdateVisitorsWithPatch() throws Exception {
        // Initialize the database
        visitorsRepository.saveAndFlush(visitors);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the visitors using partial update
        Visitors partialUpdatedVisitors = new Visitors();
        partialUpdatedVisitors.setId(visitors.getId());

        partialUpdatedVisitors
            .total_visitors(UPDATED_TOTAL_VISITORS)
            .website_visitors(UPDATED_WEBSITE_VISITORS)
            .recorded_date(UPDATED_RECORDED_DATE);

        restVisitorsMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedVisitors.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedVisitors))
            )
            .andExpect(status().isOk());

        // Validate the Visitors in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertVisitorsUpdatableFieldsEquals(partialUpdatedVisitors, getPersistedVisitors(partialUpdatedVisitors));
    }

    @Test
    @Transactional
    void patchNonExistingVisitors() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        visitors.setId(longCount.incrementAndGet());

        // Create the Visitors
        VisitorsDTO visitorsDTO = visitorsMapper.toDto(visitors);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restVisitorsMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, visitorsDTO.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(visitorsDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Visitors in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchVisitors() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        visitors.setId(longCount.incrementAndGet());

        // Create the Visitors
        VisitorsDTO visitorsDTO = visitorsMapper.toDto(visitors);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restVisitorsMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(visitorsDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Visitors in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamVisitors() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        visitors.setId(longCount.incrementAndGet());

        // Create the Visitors
        VisitorsDTO visitorsDTO = visitorsMapper.toDto(visitors);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restVisitorsMockMvc
            .perform(
                patch(ENTITY_API_URL).with(csrf()).contentType("application/merge-patch+json").content(om.writeValueAsBytes(visitorsDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Visitors in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteVisitors() throws Exception {
        // Initialize the database
        visitorsRepository.saveAndFlush(visitors);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the visitors
        restVisitorsMockMvc
            .perform(delete(ENTITY_API_URL_ID, visitors.getId()).with(csrf()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return visitorsRepository.count();
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

    protected Visitors getPersistedVisitors(Visitors visitors) {
        return visitorsRepository.findById(visitors.getId()).orElseThrow();
    }

    protected void assertPersistedVisitorsToMatchAllProperties(Visitors expectedVisitors) {
        assertVisitorsAllPropertiesEquals(expectedVisitors, getPersistedVisitors(expectedVisitors));
    }

    protected void assertPersistedVisitorsToMatchUpdatableProperties(Visitors expectedVisitors) {
        assertVisitorsAllUpdatablePropertiesEquals(expectedVisitors, getPersistedVisitors(expectedVisitors));
    }
}
