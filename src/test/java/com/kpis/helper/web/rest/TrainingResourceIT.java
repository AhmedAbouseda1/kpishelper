package com.kpis.helper.web.rest;

import static com.kpis.helper.domain.TrainingAsserts.*;
import static com.kpis.helper.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.kpis.helper.IntegrationTest;
import com.kpis.helper.domain.Training;
import com.kpis.helper.repository.TrainingRepository;
import com.kpis.helper.service.dto.TrainingDTO;
import com.kpis.helper.service.mapper.TrainingMapper;
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
 * Integration tests for the {@link TrainingResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class TrainingResourceIT {

    private static final LocalDate DEFAULT_RECORDED_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_RECORDED_DATE = LocalDate.now(ZoneId.systemDefault());

    private static final Integer DEFAULT_TOTAL_COURSES = 1;
    private static final Integer UPDATED_TOTAL_COURSES = 2;

    private static final Integer DEFAULT_TOTAL_PARTICIPANTS = 1;
    private static final Integer UPDATED_TOTAL_PARTICIPANTS = 2;

    private static final String ENTITY_API_URL = "/api/trainings";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private TrainingRepository trainingRepository;

    @Autowired
    private TrainingMapper trainingMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restTrainingMockMvc;

    private Training training;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Training createEntity(EntityManager em) {
        Training training = new Training()
            .recorded_date(DEFAULT_RECORDED_DATE)
            .total_courses(DEFAULT_TOTAL_COURSES)
            .total_participants(DEFAULT_TOTAL_PARTICIPANTS);
        return training;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Training createUpdatedEntity(EntityManager em) {
        Training training = new Training()
            .recorded_date(UPDATED_RECORDED_DATE)
            .total_courses(UPDATED_TOTAL_COURSES)
            .total_participants(UPDATED_TOTAL_PARTICIPANTS);
        return training;
    }

    @BeforeEach
    public void initTest() {
        training = createEntity(em);
    }

    @Test
    @Transactional
    void createTraining() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the Training
        TrainingDTO trainingDTO = trainingMapper.toDto(training);
        var returnedTrainingDTO = om.readValue(
            restTrainingMockMvc
                .perform(
                    post(ENTITY_API_URL).with(csrf()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(trainingDTO))
                )
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            TrainingDTO.class
        );

        // Validate the Training in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedTraining = trainingMapper.toEntity(returnedTrainingDTO);
        assertTrainingUpdatableFieldsEquals(returnedTraining, getPersistedTraining(returnedTraining));
    }

    @Test
    @Transactional
    void createTrainingWithExistingId() throws Exception {
        // Create the Training with an existing ID
        training.setId(1L);
        TrainingDTO trainingDTO = trainingMapper.toDto(training);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restTrainingMockMvc
            .perform(post(ENTITY_API_URL).with(csrf()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(trainingDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Training in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkRecorded_dateIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        training.setRecorded_date(null);

        // Create the Training, which fails.
        TrainingDTO trainingDTO = trainingMapper.toDto(training);

        restTrainingMockMvc
            .perform(post(ENTITY_API_URL).with(csrf()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(trainingDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllTrainings() throws Exception {
        // Initialize the database
        trainingRepository.saveAndFlush(training);

        // Get all the trainingList
        restTrainingMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(training.getId().intValue())))
            .andExpect(jsonPath("$.[*].recorded_date").value(hasItem(DEFAULT_RECORDED_DATE.toString())))
            .andExpect(jsonPath("$.[*].total_courses").value(hasItem(DEFAULT_TOTAL_COURSES)))
            .andExpect(jsonPath("$.[*].total_participants").value(hasItem(DEFAULT_TOTAL_PARTICIPANTS)));
    }

    @Test
    @Transactional
    void getTraining() throws Exception {
        // Initialize the database
        trainingRepository.saveAndFlush(training);

        // Get the training
        restTrainingMockMvc
            .perform(get(ENTITY_API_URL_ID, training.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(training.getId().intValue()))
            .andExpect(jsonPath("$.recorded_date").value(DEFAULT_RECORDED_DATE.toString()))
            .andExpect(jsonPath("$.total_courses").value(DEFAULT_TOTAL_COURSES))
            .andExpect(jsonPath("$.total_participants").value(DEFAULT_TOTAL_PARTICIPANTS));
    }

    @Test
    @Transactional
    void getNonExistingTraining() throws Exception {
        // Get the training
        restTrainingMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingTraining() throws Exception {
        // Initialize the database
        trainingRepository.saveAndFlush(training);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the training
        Training updatedTraining = trainingRepository.findById(training.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedTraining are not directly saved in db
        em.detach(updatedTraining);
        updatedTraining
            .recorded_date(UPDATED_RECORDED_DATE)
            .total_courses(UPDATED_TOTAL_COURSES)
            .total_participants(UPDATED_TOTAL_PARTICIPANTS);
        TrainingDTO trainingDTO = trainingMapper.toDto(updatedTraining);

        restTrainingMockMvc
            .perform(
                put(ENTITY_API_URL_ID, trainingDTO.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(trainingDTO))
            )
            .andExpect(status().isOk());

        // Validate the Training in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedTrainingToMatchAllProperties(updatedTraining);
    }

    @Test
    @Transactional
    void putNonExistingTraining() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        training.setId(longCount.incrementAndGet());

        // Create the Training
        TrainingDTO trainingDTO = trainingMapper.toDto(training);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restTrainingMockMvc
            .perform(
                put(ENTITY_API_URL_ID, trainingDTO.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(trainingDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Training in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchTraining() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        training.setId(longCount.incrementAndGet());

        // Create the Training
        TrainingDTO trainingDTO = trainingMapper.toDto(training);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTrainingMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(trainingDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Training in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamTraining() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        training.setId(longCount.incrementAndGet());

        // Create the Training
        TrainingDTO trainingDTO = trainingMapper.toDto(training);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTrainingMockMvc
            .perform(put(ENTITY_API_URL).with(csrf()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(trainingDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Training in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateTrainingWithPatch() throws Exception {
        // Initialize the database
        trainingRepository.saveAndFlush(training);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the training using partial update
        Training partialUpdatedTraining = new Training();
        partialUpdatedTraining.setId(training.getId());

        partialUpdatedTraining.total_participants(UPDATED_TOTAL_PARTICIPANTS);

        restTrainingMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedTraining.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedTraining))
            )
            .andExpect(status().isOk());

        // Validate the Training in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertTrainingUpdatableFieldsEquals(createUpdateProxyForBean(partialUpdatedTraining, training), getPersistedTraining(training));
    }

    @Test
    @Transactional
    void fullUpdateTrainingWithPatch() throws Exception {
        // Initialize the database
        trainingRepository.saveAndFlush(training);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the training using partial update
        Training partialUpdatedTraining = new Training();
        partialUpdatedTraining.setId(training.getId());

        partialUpdatedTraining
            .recorded_date(UPDATED_RECORDED_DATE)
            .total_courses(UPDATED_TOTAL_COURSES)
            .total_participants(UPDATED_TOTAL_PARTICIPANTS);

        restTrainingMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedTraining.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedTraining))
            )
            .andExpect(status().isOk());

        // Validate the Training in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertTrainingUpdatableFieldsEquals(partialUpdatedTraining, getPersistedTraining(partialUpdatedTraining));
    }

    @Test
    @Transactional
    void patchNonExistingTraining() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        training.setId(longCount.incrementAndGet());

        // Create the Training
        TrainingDTO trainingDTO = trainingMapper.toDto(training);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restTrainingMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, trainingDTO.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(trainingDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Training in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchTraining() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        training.setId(longCount.incrementAndGet());

        // Create the Training
        TrainingDTO trainingDTO = trainingMapper.toDto(training);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTrainingMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(trainingDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Training in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamTraining() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        training.setId(longCount.incrementAndGet());

        // Create the Training
        TrainingDTO trainingDTO = trainingMapper.toDto(training);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTrainingMockMvc
            .perform(
                patch(ENTITY_API_URL).with(csrf()).contentType("application/merge-patch+json").content(om.writeValueAsBytes(trainingDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Training in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteTraining() throws Exception {
        // Initialize the database
        trainingRepository.saveAndFlush(training);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the training
        restTrainingMockMvc
            .perform(delete(ENTITY_API_URL_ID, training.getId()).with(csrf()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return trainingRepository.count();
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

    protected Training getPersistedTraining(Training training) {
        return trainingRepository.findById(training.getId()).orElseThrow();
    }

    protected void assertPersistedTrainingToMatchAllProperties(Training expectedTraining) {
        assertTrainingAllPropertiesEquals(expectedTraining, getPersistedTraining(expectedTraining));
    }

    protected void assertPersistedTrainingToMatchUpdatableProperties(Training expectedTraining) {
        assertTrainingAllUpdatablePropertiesEquals(expectedTraining, getPersistedTraining(expectedTraining));
    }
}
