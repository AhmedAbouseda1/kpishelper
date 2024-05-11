package com.kpis.helper.web.rest;

import static com.kpis.helper.domain.SpaceAsserts.*;
import static com.kpis.helper.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.kpis.helper.IntegrationTest;
import com.kpis.helper.domain.Space;
import com.kpis.helper.repository.SpaceRepository;
import com.kpis.helper.service.dto.SpaceDTO;
import com.kpis.helper.service.mapper.SpaceMapper;
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
 * Integration tests for the {@link SpaceResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class SpaceResourceIT {

    private static final LocalDate DEFAULT_RECORDED_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_RECORDED_DATE = LocalDate.now(ZoneId.systemDefault());

    private static final Long DEFAULT_SQUARE_METERS_AVAILABLE = 1L;
    private static final Long UPDATED_SQUARE_METERS_AVAILABLE = 2L;

    private static final String ENTITY_API_URL = "/api/spaces";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private SpaceRepository spaceRepository;

    @Autowired
    private SpaceMapper spaceMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restSpaceMockMvc;

    private Space space;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Space createEntity(EntityManager em) {
        Space space = new Space().recorded_date(DEFAULT_RECORDED_DATE).square_meters_available(DEFAULT_SQUARE_METERS_AVAILABLE);
        return space;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Space createUpdatedEntity(EntityManager em) {
        Space space = new Space().recorded_date(UPDATED_RECORDED_DATE).square_meters_available(UPDATED_SQUARE_METERS_AVAILABLE);
        return space;
    }

    @BeforeEach
    public void initTest() {
        space = createEntity(em);
    }

    @Test
    @Transactional
    void createSpace() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the Space
        SpaceDTO spaceDTO = spaceMapper.toDto(space);
        var returnedSpaceDTO = om.readValue(
            restSpaceMockMvc
                .perform(post(ENTITY_API_URL).with(csrf()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(spaceDTO)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            SpaceDTO.class
        );

        // Validate the Space in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedSpace = spaceMapper.toEntity(returnedSpaceDTO);
        assertSpaceUpdatableFieldsEquals(returnedSpace, getPersistedSpace(returnedSpace));
    }

    @Test
    @Transactional
    void createSpaceWithExistingId() throws Exception {
        // Create the Space with an existing ID
        space.setId(1L);
        SpaceDTO spaceDTO = spaceMapper.toDto(space);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restSpaceMockMvc
            .perform(post(ENTITY_API_URL).with(csrf()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(spaceDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Space in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkRecorded_dateIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        space.setRecorded_date(null);

        // Create the Space, which fails.
        SpaceDTO spaceDTO = spaceMapper.toDto(space);

        restSpaceMockMvc
            .perform(post(ENTITY_API_URL).with(csrf()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(spaceDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllSpaces() throws Exception {
        // Initialize the database
        spaceRepository.saveAndFlush(space);

        // Get all the spaceList
        restSpaceMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(space.getId().intValue())))
            .andExpect(jsonPath("$.[*].recorded_date").value(hasItem(DEFAULT_RECORDED_DATE.toString())))
            .andExpect(jsonPath("$.[*].square_meters_available").value(hasItem(DEFAULT_SQUARE_METERS_AVAILABLE.intValue())));
    }

    @Test
    @Transactional
    void getSpace() throws Exception {
        // Initialize the database
        spaceRepository.saveAndFlush(space);

        // Get the space
        restSpaceMockMvc
            .perform(get(ENTITY_API_URL_ID, space.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(space.getId().intValue()))
            .andExpect(jsonPath("$.recorded_date").value(DEFAULT_RECORDED_DATE.toString()))
            .andExpect(jsonPath("$.square_meters_available").value(DEFAULT_SQUARE_METERS_AVAILABLE.intValue()));
    }

    @Test
    @Transactional
    void getNonExistingSpace() throws Exception {
        // Get the space
        restSpaceMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingSpace() throws Exception {
        // Initialize the database
        spaceRepository.saveAndFlush(space);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the space
        Space updatedSpace = spaceRepository.findById(space.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedSpace are not directly saved in db
        em.detach(updatedSpace);
        updatedSpace.recorded_date(UPDATED_RECORDED_DATE).square_meters_available(UPDATED_SQUARE_METERS_AVAILABLE);
        SpaceDTO spaceDTO = spaceMapper.toDto(updatedSpace);

        restSpaceMockMvc
            .perform(
                put(ENTITY_API_URL_ID, spaceDTO.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(spaceDTO))
            )
            .andExpect(status().isOk());

        // Validate the Space in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedSpaceToMatchAllProperties(updatedSpace);
    }

    @Test
    @Transactional
    void putNonExistingSpace() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        space.setId(longCount.incrementAndGet());

        // Create the Space
        SpaceDTO spaceDTO = spaceMapper.toDto(space);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restSpaceMockMvc
            .perform(
                put(ENTITY_API_URL_ID, spaceDTO.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(spaceDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Space in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchSpace() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        space.setId(longCount.incrementAndGet());

        // Create the Space
        SpaceDTO spaceDTO = spaceMapper.toDto(space);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSpaceMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(spaceDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Space in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamSpace() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        space.setId(longCount.incrementAndGet());

        // Create the Space
        SpaceDTO spaceDTO = spaceMapper.toDto(space);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSpaceMockMvc
            .perform(put(ENTITY_API_URL).with(csrf()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(spaceDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Space in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateSpaceWithPatch() throws Exception {
        // Initialize the database
        spaceRepository.saveAndFlush(space);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the space using partial update
        Space partialUpdatedSpace = new Space();
        partialUpdatedSpace.setId(space.getId());

        restSpaceMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedSpace.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedSpace))
            )
            .andExpect(status().isOk());

        // Validate the Space in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertSpaceUpdatableFieldsEquals(createUpdateProxyForBean(partialUpdatedSpace, space), getPersistedSpace(space));
    }

    @Test
    @Transactional
    void fullUpdateSpaceWithPatch() throws Exception {
        // Initialize the database
        spaceRepository.saveAndFlush(space);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the space using partial update
        Space partialUpdatedSpace = new Space();
        partialUpdatedSpace.setId(space.getId());

        partialUpdatedSpace.recorded_date(UPDATED_RECORDED_DATE).square_meters_available(UPDATED_SQUARE_METERS_AVAILABLE);

        restSpaceMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedSpace.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedSpace))
            )
            .andExpect(status().isOk());

        // Validate the Space in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertSpaceUpdatableFieldsEquals(partialUpdatedSpace, getPersistedSpace(partialUpdatedSpace));
    }

    @Test
    @Transactional
    void patchNonExistingSpace() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        space.setId(longCount.incrementAndGet());

        // Create the Space
        SpaceDTO spaceDTO = spaceMapper.toDto(space);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restSpaceMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, spaceDTO.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(spaceDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Space in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchSpace() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        space.setId(longCount.incrementAndGet());

        // Create the Space
        SpaceDTO spaceDTO = spaceMapper.toDto(space);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSpaceMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(spaceDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Space in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamSpace() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        space.setId(longCount.incrementAndGet());

        // Create the Space
        SpaceDTO spaceDTO = spaceMapper.toDto(space);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSpaceMockMvc
            .perform(patch(ENTITY_API_URL).with(csrf()).contentType("application/merge-patch+json").content(om.writeValueAsBytes(spaceDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Space in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteSpace() throws Exception {
        // Initialize the database
        spaceRepository.saveAndFlush(space);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the space
        restSpaceMockMvc
            .perform(delete(ENTITY_API_URL_ID, space.getId()).with(csrf()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return spaceRepository.count();
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

    protected Space getPersistedSpace(Space space) {
        return spaceRepository.findById(space.getId()).orElseThrow();
    }

    protected void assertPersistedSpaceToMatchAllProperties(Space expectedSpace) {
        assertSpaceAllPropertiesEquals(expectedSpace, getPersistedSpace(expectedSpace));
    }

    protected void assertPersistedSpaceToMatchUpdatableProperties(Space expectedSpace) {
        assertSpaceAllUpdatablePropertiesEquals(expectedSpace, getPersistedSpace(expectedSpace));
    }
}
