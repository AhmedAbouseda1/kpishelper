package com.kpis.helper.web.rest;

import static com.kpis.helper.domain.ActivitiesAsserts.*;
import static com.kpis.helper.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.kpis.helper.IntegrationTest;
import com.kpis.helper.domain.Activities;
import com.kpis.helper.repository.ActivitiesRepository;
import com.kpis.helper.service.dto.ActivitiesDTO;
import com.kpis.helper.service.mapper.ActivitiesMapper;
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
 * Integration tests for the {@link ActivitiesResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class ActivitiesResourceIT {

    private static final LocalDate DEFAULT_RECORDED_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_RECORDED_DATE = LocalDate.now(ZoneId.systemDefault());

    private static final Integer DEFAULT_TOTAL_ACTIVITIES = 1;
    private static final Integer UPDATED_TOTAL_ACTIVITIES = 2;

    private static final Integer DEFAULT_TOTAL_PARTICIPANTS = 1;
    private static final Integer UPDATED_TOTAL_PARTICIPANTS = 2;

    private static final String ENTITY_API_URL = "/api/activities";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private ActivitiesRepository activitiesRepository;

    @Autowired
    private ActivitiesMapper activitiesMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restActivitiesMockMvc;

    private Activities activities;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Activities createEntity(EntityManager em) {
        Activities activities = new Activities()
            .recorded_date(DEFAULT_RECORDED_DATE)
            .total_activities(DEFAULT_TOTAL_ACTIVITIES)
            .total_participants(DEFAULT_TOTAL_PARTICIPANTS);
        return activities;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Activities createUpdatedEntity(EntityManager em) {
        Activities activities = new Activities()
            .recorded_date(UPDATED_RECORDED_DATE)
            .total_activities(UPDATED_TOTAL_ACTIVITIES)
            .total_participants(UPDATED_TOTAL_PARTICIPANTS);
        return activities;
    }

    @BeforeEach
    public void initTest() {
        activities = createEntity(em);
    }

    @Test
    @Transactional
    void createActivities() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the Activities
        ActivitiesDTO activitiesDTO = activitiesMapper.toDto(activities);
        var returnedActivitiesDTO = om.readValue(
            restActivitiesMockMvc
                .perform(
                    post(ENTITY_API_URL).with(csrf()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(activitiesDTO))
                )
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            ActivitiesDTO.class
        );

        // Validate the Activities in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedActivities = activitiesMapper.toEntity(returnedActivitiesDTO);
        assertActivitiesUpdatableFieldsEquals(returnedActivities, getPersistedActivities(returnedActivities));
    }

    @Test
    @Transactional
    void createActivitiesWithExistingId() throws Exception {
        // Create the Activities with an existing ID
        activities.setId(1L);
        ActivitiesDTO activitiesDTO = activitiesMapper.toDto(activities);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restActivitiesMockMvc
            .perform(post(ENTITY_API_URL).with(csrf()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(activitiesDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Activities in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkRecorded_dateIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        activities.setRecorded_date(null);

        // Create the Activities, which fails.
        ActivitiesDTO activitiesDTO = activitiesMapper.toDto(activities);

        restActivitiesMockMvc
            .perform(post(ENTITY_API_URL).with(csrf()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(activitiesDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllActivities() throws Exception {
        // Initialize the database
        activitiesRepository.saveAndFlush(activities);

        // Get all the activitiesList
        restActivitiesMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(activities.getId().intValue())))
            .andExpect(jsonPath("$.[*].recorded_date").value(hasItem(DEFAULT_RECORDED_DATE.toString())))
            .andExpect(jsonPath("$.[*].total_activities").value(hasItem(DEFAULT_TOTAL_ACTIVITIES)))
            .andExpect(jsonPath("$.[*].total_participants").value(hasItem(DEFAULT_TOTAL_PARTICIPANTS)));
    }

    @Test
    @Transactional
    void getActivities() throws Exception {
        // Initialize the database
        activitiesRepository.saveAndFlush(activities);

        // Get the activities
        restActivitiesMockMvc
            .perform(get(ENTITY_API_URL_ID, activities.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(activities.getId().intValue()))
            .andExpect(jsonPath("$.recorded_date").value(DEFAULT_RECORDED_DATE.toString()))
            .andExpect(jsonPath("$.total_activities").value(DEFAULT_TOTAL_ACTIVITIES))
            .andExpect(jsonPath("$.total_participants").value(DEFAULT_TOTAL_PARTICIPANTS));
    }

    @Test
    @Transactional
    void getNonExistingActivities() throws Exception {
        // Get the activities
        restActivitiesMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingActivities() throws Exception {
        // Initialize the database
        activitiesRepository.saveAndFlush(activities);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the activities
        Activities updatedActivities = activitiesRepository.findById(activities.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedActivities are not directly saved in db
        em.detach(updatedActivities);
        updatedActivities
            .recorded_date(UPDATED_RECORDED_DATE)
            .total_activities(UPDATED_TOTAL_ACTIVITIES)
            .total_participants(UPDATED_TOTAL_PARTICIPANTS);
        ActivitiesDTO activitiesDTO = activitiesMapper.toDto(updatedActivities);

        restActivitiesMockMvc
            .perform(
                put(ENTITY_API_URL_ID, activitiesDTO.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(activitiesDTO))
            )
            .andExpect(status().isOk());

        // Validate the Activities in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedActivitiesToMatchAllProperties(updatedActivities);
    }

    @Test
    @Transactional
    void putNonExistingActivities() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        activities.setId(longCount.incrementAndGet());

        // Create the Activities
        ActivitiesDTO activitiesDTO = activitiesMapper.toDto(activities);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restActivitiesMockMvc
            .perform(
                put(ENTITY_API_URL_ID, activitiesDTO.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(activitiesDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Activities in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchActivities() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        activities.setId(longCount.incrementAndGet());

        // Create the Activities
        ActivitiesDTO activitiesDTO = activitiesMapper.toDto(activities);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restActivitiesMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(activitiesDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Activities in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamActivities() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        activities.setId(longCount.incrementAndGet());

        // Create the Activities
        ActivitiesDTO activitiesDTO = activitiesMapper.toDto(activities);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restActivitiesMockMvc
            .perform(put(ENTITY_API_URL).with(csrf()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(activitiesDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Activities in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateActivitiesWithPatch() throws Exception {
        // Initialize the database
        activitiesRepository.saveAndFlush(activities);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the activities using partial update
        Activities partialUpdatedActivities = new Activities();
        partialUpdatedActivities.setId(activities.getId());

        restActivitiesMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedActivities.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedActivities))
            )
            .andExpect(status().isOk());

        // Validate the Activities in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertActivitiesUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedActivities, activities),
            getPersistedActivities(activities)
        );
    }

    @Test
    @Transactional
    void fullUpdateActivitiesWithPatch() throws Exception {
        // Initialize the database
        activitiesRepository.saveAndFlush(activities);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the activities using partial update
        Activities partialUpdatedActivities = new Activities();
        partialUpdatedActivities.setId(activities.getId());

        partialUpdatedActivities
            .recorded_date(UPDATED_RECORDED_DATE)
            .total_activities(UPDATED_TOTAL_ACTIVITIES)
            .total_participants(UPDATED_TOTAL_PARTICIPANTS);

        restActivitiesMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedActivities.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedActivities))
            )
            .andExpect(status().isOk());

        // Validate the Activities in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertActivitiesUpdatableFieldsEquals(partialUpdatedActivities, getPersistedActivities(partialUpdatedActivities));
    }

    @Test
    @Transactional
    void patchNonExistingActivities() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        activities.setId(longCount.incrementAndGet());

        // Create the Activities
        ActivitiesDTO activitiesDTO = activitiesMapper.toDto(activities);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restActivitiesMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, activitiesDTO.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(activitiesDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Activities in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchActivities() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        activities.setId(longCount.incrementAndGet());

        // Create the Activities
        ActivitiesDTO activitiesDTO = activitiesMapper.toDto(activities);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restActivitiesMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(activitiesDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Activities in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamActivities() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        activities.setId(longCount.incrementAndGet());

        // Create the Activities
        ActivitiesDTO activitiesDTO = activitiesMapper.toDto(activities);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restActivitiesMockMvc
            .perform(
                patch(ENTITY_API_URL).with(csrf()).contentType("application/merge-patch+json").content(om.writeValueAsBytes(activitiesDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Activities in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteActivities() throws Exception {
        // Initialize the database
        activitiesRepository.saveAndFlush(activities);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the activities
        restActivitiesMockMvc
            .perform(delete(ENTITY_API_URL_ID, activities.getId()).with(csrf()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return activitiesRepository.count();
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

    protected Activities getPersistedActivities(Activities activities) {
        return activitiesRepository.findById(activities.getId()).orElseThrow();
    }

    protected void assertPersistedActivitiesToMatchAllProperties(Activities expectedActivities) {
        assertActivitiesAllPropertiesEquals(expectedActivities, getPersistedActivities(expectedActivities));
    }

    protected void assertPersistedActivitiesToMatchUpdatableProperties(Activities expectedActivities) {
        assertActivitiesAllUpdatablePropertiesEquals(expectedActivities, getPersistedActivities(expectedActivities));
    }
}
