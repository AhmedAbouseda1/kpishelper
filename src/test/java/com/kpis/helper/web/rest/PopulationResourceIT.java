package com.kpis.helper.web.rest;

import static com.kpis.helper.domain.PopulationAsserts.*;
import static com.kpis.helper.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.kpis.helper.IntegrationTest;
import com.kpis.helper.domain.Library;
import com.kpis.helper.domain.Population;
import com.kpis.helper.repository.PopulationRepository;
import com.kpis.helper.service.dto.PopulationDTO;
import com.kpis.helper.service.mapper.PopulationMapper;
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
 * Integration tests for the {@link PopulationResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class PopulationResourceIT {

    private static final LocalDate DEFAULT_DATE_RECORDED = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_DATE_RECORDED = LocalDate.now(ZoneId.systemDefault());
    private static final LocalDate SMALLER_DATE_RECORDED = LocalDate.ofEpochDay(-1L);

    private static final Integer DEFAULT_POPULATION = 1;
    private static final Integer UPDATED_POPULATION = 2;
    private static final Integer SMALLER_POPULATION = 1 - 1;

    private static final Integer DEFAULT_ACTIVE_MEMBERS = 1;
    private static final Integer UPDATED_ACTIVE_MEMBERS = 2;
    private static final Integer SMALLER_ACTIVE_MEMBERS = 1 - 1;

    private static final String ENTITY_API_URL = "/api/populations";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private PopulationRepository populationRepository;

    @Autowired
    private PopulationMapper populationMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restPopulationMockMvc;

    private Population population;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Population createEntity(EntityManager em) {
        Population population = new Population()
            .date_recorded(DEFAULT_DATE_RECORDED)
            .population(DEFAULT_POPULATION)
            .active_members(DEFAULT_ACTIVE_MEMBERS);
        return population;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Population createUpdatedEntity(EntityManager em) {
        Population population = new Population()
            .date_recorded(UPDATED_DATE_RECORDED)
            .population(UPDATED_POPULATION)
            .active_members(UPDATED_ACTIVE_MEMBERS);
        return population;
    }

    @BeforeEach
    public void initTest() {
        population = createEntity(em);
    }

    @Test
    @Transactional
    void createPopulation() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the Population
        PopulationDTO populationDTO = populationMapper.toDto(population);
        var returnedPopulationDTO = om.readValue(
            restPopulationMockMvc
                .perform(
                    post(ENTITY_API_URL).with(csrf()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(populationDTO))
                )
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            PopulationDTO.class
        );

        // Validate the Population in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedPopulation = populationMapper.toEntity(returnedPopulationDTO);
        assertPopulationUpdatableFieldsEquals(returnedPopulation, getPersistedPopulation(returnedPopulation));
    }

    @Test
    @Transactional
    void createPopulationWithExistingId() throws Exception {
        // Create the Population with an existing ID
        population.setId(1L);
        PopulationDTO populationDTO = populationMapper.toDto(population);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restPopulationMockMvc
            .perform(post(ENTITY_API_URL).with(csrf()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(populationDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Population in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkDate_recordedIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        population.setDate_recorded(null);

        // Create the Population, which fails.
        PopulationDTO populationDTO = populationMapper.toDto(population);

        restPopulationMockMvc
            .perform(post(ENTITY_API_URL).with(csrf()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(populationDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkPopulationIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        population.setPopulation(null);

        // Create the Population, which fails.
        PopulationDTO populationDTO = populationMapper.toDto(population);

        restPopulationMockMvc
            .perform(post(ENTITY_API_URL).with(csrf()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(populationDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllPopulations() throws Exception {
        // Initialize the database
        populationRepository.saveAndFlush(population);

        // Get all the populationList
        restPopulationMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(population.getId().intValue())))
            .andExpect(jsonPath("$.[*].date_recorded").value(hasItem(DEFAULT_DATE_RECORDED.toString())))
            .andExpect(jsonPath("$.[*].population").value(hasItem(DEFAULT_POPULATION)))
            .andExpect(jsonPath("$.[*].active_members").value(hasItem(DEFAULT_ACTIVE_MEMBERS)));
    }

    @Test
    @Transactional
    void getPopulation() throws Exception {
        // Initialize the database
        populationRepository.saveAndFlush(population);

        // Get the population
        restPopulationMockMvc
            .perform(get(ENTITY_API_URL_ID, population.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(population.getId().intValue()))
            .andExpect(jsonPath("$.date_recorded").value(DEFAULT_DATE_RECORDED.toString()))
            .andExpect(jsonPath("$.population").value(DEFAULT_POPULATION))
            .andExpect(jsonPath("$.active_members").value(DEFAULT_ACTIVE_MEMBERS));
    }

    @Test
    @Transactional
    void getPopulationsByIdFiltering() throws Exception {
        // Initialize the database
        populationRepository.saveAndFlush(population);

        Long id = population.getId();

        defaultPopulationFiltering("id.equals=" + id, "id.notEquals=" + id);

        defaultPopulationFiltering("id.greaterThanOrEqual=" + id, "id.greaterThan=" + id);

        defaultPopulationFiltering("id.lessThanOrEqual=" + id, "id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllPopulationsByDate_recordedIsEqualToSomething() throws Exception {
        // Initialize the database
        populationRepository.saveAndFlush(population);

        // Get all the populationList where date_recorded equals to
        defaultPopulationFiltering("date_recorded.equals=" + DEFAULT_DATE_RECORDED, "date_recorded.equals=" + UPDATED_DATE_RECORDED);
    }

    @Test
    @Transactional
    void getAllPopulationsByDate_recordedIsInShouldWork() throws Exception {
        // Initialize the database
        populationRepository.saveAndFlush(population);

        // Get all the populationList where date_recorded in
        defaultPopulationFiltering(
            "date_recorded.in=" + DEFAULT_DATE_RECORDED + "," + UPDATED_DATE_RECORDED,
            "date_recorded.in=" + UPDATED_DATE_RECORDED
        );
    }

    @Test
    @Transactional
    void getAllPopulationsByDate_recordedIsNullOrNotNull() throws Exception {
        // Initialize the database
        populationRepository.saveAndFlush(population);

        // Get all the populationList where date_recorded is not null
        defaultPopulationFiltering("date_recorded.specified=true", "date_recorded.specified=false");
    }

    @Test
    @Transactional
    void getAllPopulationsByDate_recordedIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        populationRepository.saveAndFlush(population);

        // Get all the populationList where date_recorded is greater than or equal to
        defaultPopulationFiltering(
            "date_recorded.greaterThanOrEqual=" + DEFAULT_DATE_RECORDED,
            "date_recorded.greaterThanOrEqual=" + UPDATED_DATE_RECORDED
        );
    }

    @Test
    @Transactional
    void getAllPopulationsByDate_recordedIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        populationRepository.saveAndFlush(population);

        // Get all the populationList where date_recorded is less than or equal to
        defaultPopulationFiltering(
            "date_recorded.lessThanOrEqual=" + DEFAULT_DATE_RECORDED,
            "date_recorded.lessThanOrEqual=" + SMALLER_DATE_RECORDED
        );
    }

    @Test
    @Transactional
    void getAllPopulationsByDate_recordedIsLessThanSomething() throws Exception {
        // Initialize the database
        populationRepository.saveAndFlush(population);

        // Get all the populationList where date_recorded is less than
        defaultPopulationFiltering("date_recorded.lessThan=" + UPDATED_DATE_RECORDED, "date_recorded.lessThan=" + DEFAULT_DATE_RECORDED);
    }

    @Test
    @Transactional
    void getAllPopulationsByDate_recordedIsGreaterThanSomething() throws Exception {
        // Initialize the database
        populationRepository.saveAndFlush(population);

        // Get all the populationList where date_recorded is greater than
        defaultPopulationFiltering(
            "date_recorded.greaterThan=" + SMALLER_DATE_RECORDED,
            "date_recorded.greaterThan=" + DEFAULT_DATE_RECORDED
        );
    }

    @Test
    @Transactional
    void getAllPopulationsByPopulationIsEqualToSomething() throws Exception {
        // Initialize the database
        populationRepository.saveAndFlush(population);

        // Get all the populationList where population equals to
        defaultPopulationFiltering("population.equals=" + DEFAULT_POPULATION, "population.equals=" + UPDATED_POPULATION);
    }

    @Test
    @Transactional
    void getAllPopulationsByPopulationIsInShouldWork() throws Exception {
        // Initialize the database
        populationRepository.saveAndFlush(population);

        // Get all the populationList where population in
        defaultPopulationFiltering("population.in=" + DEFAULT_POPULATION + "," + UPDATED_POPULATION, "population.in=" + UPDATED_POPULATION);
    }

    @Test
    @Transactional
    void getAllPopulationsByPopulationIsNullOrNotNull() throws Exception {
        // Initialize the database
        populationRepository.saveAndFlush(population);

        // Get all the populationList where population is not null
        defaultPopulationFiltering("population.specified=true", "population.specified=false");
    }

    @Test
    @Transactional
    void getAllPopulationsByPopulationIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        populationRepository.saveAndFlush(population);

        // Get all the populationList where population is greater than or equal to
        defaultPopulationFiltering(
            "population.greaterThanOrEqual=" + DEFAULT_POPULATION,
            "population.greaterThanOrEqual=" + UPDATED_POPULATION
        );
    }

    @Test
    @Transactional
    void getAllPopulationsByPopulationIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        populationRepository.saveAndFlush(population);

        // Get all the populationList where population is less than or equal to
        defaultPopulationFiltering("population.lessThanOrEqual=" + DEFAULT_POPULATION, "population.lessThanOrEqual=" + SMALLER_POPULATION);
    }

    @Test
    @Transactional
    void getAllPopulationsByPopulationIsLessThanSomething() throws Exception {
        // Initialize the database
        populationRepository.saveAndFlush(population);

        // Get all the populationList where population is less than
        defaultPopulationFiltering("population.lessThan=" + UPDATED_POPULATION, "population.lessThan=" + DEFAULT_POPULATION);
    }

    @Test
    @Transactional
    void getAllPopulationsByPopulationIsGreaterThanSomething() throws Exception {
        // Initialize the database
        populationRepository.saveAndFlush(population);

        // Get all the populationList where population is greater than
        defaultPopulationFiltering("population.greaterThan=" + SMALLER_POPULATION, "population.greaterThan=" + DEFAULT_POPULATION);
    }

    @Test
    @Transactional
    void getAllPopulationsByActive_membersIsEqualToSomething() throws Exception {
        // Initialize the database
        populationRepository.saveAndFlush(population);

        // Get all the populationList where active_members equals to
        defaultPopulationFiltering("active_members.equals=" + DEFAULT_ACTIVE_MEMBERS, "active_members.equals=" + UPDATED_ACTIVE_MEMBERS);
    }

    @Test
    @Transactional
    void getAllPopulationsByActive_membersIsInShouldWork() throws Exception {
        // Initialize the database
        populationRepository.saveAndFlush(population);

        // Get all the populationList where active_members in
        defaultPopulationFiltering(
            "active_members.in=" + DEFAULT_ACTIVE_MEMBERS + "," + UPDATED_ACTIVE_MEMBERS,
            "active_members.in=" + UPDATED_ACTIVE_MEMBERS
        );
    }

    @Test
    @Transactional
    void getAllPopulationsByActive_membersIsNullOrNotNull() throws Exception {
        // Initialize the database
        populationRepository.saveAndFlush(population);

        // Get all the populationList where active_members is not null
        defaultPopulationFiltering("active_members.specified=true", "active_members.specified=false");
    }

    @Test
    @Transactional
    void getAllPopulationsByActive_membersIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        populationRepository.saveAndFlush(population);

        // Get all the populationList where active_members is greater than or equal to
        defaultPopulationFiltering(
            "active_members.greaterThanOrEqual=" + DEFAULT_ACTIVE_MEMBERS,
            "active_members.greaterThanOrEqual=" + UPDATED_ACTIVE_MEMBERS
        );
    }

    @Test
    @Transactional
    void getAllPopulationsByActive_membersIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        populationRepository.saveAndFlush(population);

        // Get all the populationList where active_members is less than or equal to
        defaultPopulationFiltering(
            "active_members.lessThanOrEqual=" + DEFAULT_ACTIVE_MEMBERS,
            "active_members.lessThanOrEqual=" + SMALLER_ACTIVE_MEMBERS
        );
    }

    @Test
    @Transactional
    void getAllPopulationsByActive_membersIsLessThanSomething() throws Exception {
        // Initialize the database
        populationRepository.saveAndFlush(population);

        // Get all the populationList where active_members is less than
        defaultPopulationFiltering(
            "active_members.lessThan=" + UPDATED_ACTIVE_MEMBERS,
            "active_members.lessThan=" + DEFAULT_ACTIVE_MEMBERS
        );
    }

    @Test
    @Transactional
    void getAllPopulationsByActive_membersIsGreaterThanSomething() throws Exception {
        // Initialize the database
        populationRepository.saveAndFlush(population);

        // Get all the populationList where active_members is greater than
        defaultPopulationFiltering(
            "active_members.greaterThan=" + SMALLER_ACTIVE_MEMBERS,
            "active_members.greaterThan=" + DEFAULT_ACTIVE_MEMBERS
        );
    }

    @Test
    @Transactional
    void getAllPopulationsByLibraryIsEqualToSomething() throws Exception {
        Library library;
        if (TestUtil.findAll(em, Library.class).isEmpty()) {
            populationRepository.saveAndFlush(population);
            library = LibraryResourceIT.createEntity(em);
        } else {
            library = TestUtil.findAll(em, Library.class).get(0);
        }
        em.persist(library);
        em.flush();
        population.setLibrary(library);
        populationRepository.saveAndFlush(population);
        Long libraryId = library.getId();
        // Get all the populationList where library equals to libraryId
        defaultPopulationShouldBeFound("libraryId.equals=" + libraryId);

        // Get all the populationList where library equals to (libraryId + 1)
        defaultPopulationShouldNotBeFound("libraryId.equals=" + (libraryId + 1));
    }

    private void defaultPopulationFiltering(String shouldBeFound, String shouldNotBeFound) throws Exception {
        defaultPopulationShouldBeFound(shouldBeFound);
        defaultPopulationShouldNotBeFound(shouldNotBeFound);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultPopulationShouldBeFound(String filter) throws Exception {
        restPopulationMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(population.getId().intValue())))
            .andExpect(jsonPath("$.[*].date_recorded").value(hasItem(DEFAULT_DATE_RECORDED.toString())))
            .andExpect(jsonPath("$.[*].population").value(hasItem(DEFAULT_POPULATION)))
            .andExpect(jsonPath("$.[*].active_members").value(hasItem(DEFAULT_ACTIVE_MEMBERS)));

        // Check, that the count call also returns 1
        restPopulationMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultPopulationShouldNotBeFound(String filter) throws Exception {
        restPopulationMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restPopulationMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingPopulation() throws Exception {
        // Get the population
        restPopulationMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingPopulation() throws Exception {
        // Initialize the database
        populationRepository.saveAndFlush(population);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the population
        Population updatedPopulation = populationRepository.findById(population.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedPopulation are not directly saved in db
        em.detach(updatedPopulation);
        updatedPopulation.date_recorded(UPDATED_DATE_RECORDED).population(UPDATED_POPULATION).active_members(UPDATED_ACTIVE_MEMBERS);
        PopulationDTO populationDTO = populationMapper.toDto(updatedPopulation);

        restPopulationMockMvc
            .perform(
                put(ENTITY_API_URL_ID, populationDTO.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(populationDTO))
            )
            .andExpect(status().isOk());

        // Validate the Population in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedPopulationToMatchAllProperties(updatedPopulation);
    }

    @Test
    @Transactional
    void putNonExistingPopulation() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        population.setId(longCount.incrementAndGet());

        // Create the Population
        PopulationDTO populationDTO = populationMapper.toDto(population);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restPopulationMockMvc
            .perform(
                put(ENTITY_API_URL_ID, populationDTO.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(populationDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Population in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchPopulation() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        population.setId(longCount.incrementAndGet());

        // Create the Population
        PopulationDTO populationDTO = populationMapper.toDto(population);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPopulationMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(populationDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Population in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamPopulation() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        population.setId(longCount.incrementAndGet());

        // Create the Population
        PopulationDTO populationDTO = populationMapper.toDto(population);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPopulationMockMvc
            .perform(put(ENTITY_API_URL).with(csrf()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(populationDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Population in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdatePopulationWithPatch() throws Exception {
        // Initialize the database
        populationRepository.saveAndFlush(population);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the population using partial update
        Population partialUpdatedPopulation = new Population();
        partialUpdatedPopulation.setId(population.getId());

        partialUpdatedPopulation.population(UPDATED_POPULATION).active_members(UPDATED_ACTIVE_MEMBERS);

        restPopulationMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedPopulation.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedPopulation))
            )
            .andExpect(status().isOk());

        // Validate the Population in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPopulationUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedPopulation, population),
            getPersistedPopulation(population)
        );
    }

    @Test
    @Transactional
    void fullUpdatePopulationWithPatch() throws Exception {
        // Initialize the database
        populationRepository.saveAndFlush(population);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the population using partial update
        Population partialUpdatedPopulation = new Population();
        partialUpdatedPopulation.setId(population.getId());

        partialUpdatedPopulation.date_recorded(UPDATED_DATE_RECORDED).population(UPDATED_POPULATION).active_members(UPDATED_ACTIVE_MEMBERS);

        restPopulationMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedPopulation.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedPopulation))
            )
            .andExpect(status().isOk());

        // Validate the Population in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPopulationUpdatableFieldsEquals(partialUpdatedPopulation, getPersistedPopulation(partialUpdatedPopulation));
    }

    @Test
    @Transactional
    void patchNonExistingPopulation() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        population.setId(longCount.incrementAndGet());

        // Create the Population
        PopulationDTO populationDTO = populationMapper.toDto(population);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restPopulationMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, populationDTO.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(populationDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Population in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchPopulation() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        population.setId(longCount.incrementAndGet());

        // Create the Population
        PopulationDTO populationDTO = populationMapper.toDto(population);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPopulationMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(populationDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Population in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamPopulation() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        population.setId(longCount.incrementAndGet());

        // Create the Population
        PopulationDTO populationDTO = populationMapper.toDto(population);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPopulationMockMvc
            .perform(
                patch(ENTITY_API_URL).with(csrf()).contentType("application/merge-patch+json").content(om.writeValueAsBytes(populationDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Population in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deletePopulation() throws Exception {
        // Initialize the database
        populationRepository.saveAndFlush(population);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the population
        restPopulationMockMvc
            .perform(delete(ENTITY_API_URL_ID, population.getId()).with(csrf()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return populationRepository.count();
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

    protected Population getPersistedPopulation(Population population) {
        return populationRepository.findById(population.getId()).orElseThrow();
    }

    protected void assertPersistedPopulationToMatchAllProperties(Population expectedPopulation) {
        assertPopulationAllPropertiesEquals(expectedPopulation, getPersistedPopulation(expectedPopulation));
    }

    protected void assertPersistedPopulationToMatchUpdatableProperties(Population expectedPopulation) {
        assertPopulationAllUpdatablePropertiesEquals(expectedPopulation, getPersistedPopulation(expectedPopulation));
    }
}
