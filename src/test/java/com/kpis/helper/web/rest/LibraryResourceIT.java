package com.kpis.helper.web.rest;

import static com.kpis.helper.domain.LibraryAsserts.*;
import static com.kpis.helper.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.kpis.helper.IntegrationTest;
import com.kpis.helper.domain.Library;
import com.kpis.helper.repository.LibraryRepository;
import com.kpis.helper.service.dto.LibraryDTO;
import com.kpis.helper.service.mapper.LibraryMapper;
import jakarta.persistence.EntityManager;
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
 * Integration tests for the {@link LibraryResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class LibraryResourceIT {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_LOCATION = "AAAAAAAAAA";
    private static final String UPDATED_LOCATION = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/libraries";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private LibraryRepository libraryRepository;

    @Autowired
    private LibraryMapper libraryMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restLibraryMockMvc;

    private Library library;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Library createEntity(EntityManager em) {
        Library library = new Library().name(DEFAULT_NAME).location(DEFAULT_LOCATION);
        return library;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Library createUpdatedEntity(EntityManager em) {
        Library library = new Library().name(UPDATED_NAME).location(UPDATED_LOCATION);
        return library;
    }

    @BeforeEach
    public void initTest() {
        library = createEntity(em);
    }

    @Test
    @Transactional
    void createLibrary() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the Library
        LibraryDTO libraryDTO = libraryMapper.toDto(library);
        var returnedLibraryDTO = om.readValue(
            restLibraryMockMvc
                .perform(
                    post(ENTITY_API_URL).with(csrf()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(libraryDTO))
                )
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            LibraryDTO.class
        );

        // Validate the Library in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedLibrary = libraryMapper.toEntity(returnedLibraryDTO);
        assertLibraryUpdatableFieldsEquals(returnedLibrary, getPersistedLibrary(returnedLibrary));
    }

    @Test
    @Transactional
    void createLibraryWithExistingId() throws Exception {
        // Create the Library with an existing ID
        library.setId(1L);
        LibraryDTO libraryDTO = libraryMapper.toDto(library);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restLibraryMockMvc
            .perform(post(ENTITY_API_URL).with(csrf()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(libraryDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Library in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkNameIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        library.setName(null);

        // Create the Library, which fails.
        LibraryDTO libraryDTO = libraryMapper.toDto(library);

        restLibraryMockMvc
            .perform(post(ENTITY_API_URL).with(csrf()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(libraryDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllLibraries() throws Exception {
        // Initialize the database
        libraryRepository.saveAndFlush(library);

        // Get all the libraryList
        restLibraryMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(library.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].location").value(hasItem(DEFAULT_LOCATION)));
    }

    @Test
    @Transactional
    void getLibrary() throws Exception {
        // Initialize the database
        libraryRepository.saveAndFlush(library);

        // Get the library
        restLibraryMockMvc
            .perform(get(ENTITY_API_URL_ID, library.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(library.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME))
            .andExpect(jsonPath("$.location").value(DEFAULT_LOCATION));
    }

    @Test
    @Transactional
    void getNonExistingLibrary() throws Exception {
        // Get the library
        restLibraryMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingLibrary() throws Exception {
        // Initialize the database
        libraryRepository.saveAndFlush(library);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the library
        Library updatedLibrary = libraryRepository.findById(library.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedLibrary are not directly saved in db
        em.detach(updatedLibrary);
        updatedLibrary.name(UPDATED_NAME).location(UPDATED_LOCATION);
        LibraryDTO libraryDTO = libraryMapper.toDto(updatedLibrary);

        restLibraryMockMvc
            .perform(
                put(ENTITY_API_URL_ID, libraryDTO.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(libraryDTO))
            )
            .andExpect(status().isOk());

        // Validate the Library in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedLibraryToMatchAllProperties(updatedLibrary);
    }

    @Test
    @Transactional
    void putNonExistingLibrary() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        library.setId(longCount.incrementAndGet());

        // Create the Library
        LibraryDTO libraryDTO = libraryMapper.toDto(library);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restLibraryMockMvc
            .perform(
                put(ENTITY_API_URL_ID, libraryDTO.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(libraryDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Library in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchLibrary() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        library.setId(longCount.incrementAndGet());

        // Create the Library
        LibraryDTO libraryDTO = libraryMapper.toDto(library);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restLibraryMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(libraryDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Library in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamLibrary() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        library.setId(longCount.incrementAndGet());

        // Create the Library
        LibraryDTO libraryDTO = libraryMapper.toDto(library);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restLibraryMockMvc
            .perform(put(ENTITY_API_URL).with(csrf()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(libraryDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Library in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateLibraryWithPatch() throws Exception {
        // Initialize the database
        libraryRepository.saveAndFlush(library);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the library using partial update
        Library partialUpdatedLibrary = new Library();
        partialUpdatedLibrary.setId(library.getId());

        partialUpdatedLibrary.name(UPDATED_NAME);

        restLibraryMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedLibrary.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedLibrary))
            )
            .andExpect(status().isOk());

        // Validate the Library in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertLibraryUpdatableFieldsEquals(createUpdateProxyForBean(partialUpdatedLibrary, library), getPersistedLibrary(library));
    }

    @Test
    @Transactional
    void fullUpdateLibraryWithPatch() throws Exception {
        // Initialize the database
        libraryRepository.saveAndFlush(library);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the library using partial update
        Library partialUpdatedLibrary = new Library();
        partialUpdatedLibrary.setId(library.getId());

        partialUpdatedLibrary.name(UPDATED_NAME).location(UPDATED_LOCATION);

        restLibraryMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedLibrary.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedLibrary))
            )
            .andExpect(status().isOk());

        // Validate the Library in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertLibraryUpdatableFieldsEquals(partialUpdatedLibrary, getPersistedLibrary(partialUpdatedLibrary));
    }

    @Test
    @Transactional
    void patchNonExistingLibrary() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        library.setId(longCount.incrementAndGet());

        // Create the Library
        LibraryDTO libraryDTO = libraryMapper.toDto(library);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restLibraryMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, libraryDTO.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(libraryDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Library in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchLibrary() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        library.setId(longCount.incrementAndGet());

        // Create the Library
        LibraryDTO libraryDTO = libraryMapper.toDto(library);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restLibraryMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(libraryDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Library in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamLibrary() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        library.setId(longCount.incrementAndGet());

        // Create the Library
        LibraryDTO libraryDTO = libraryMapper.toDto(library);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restLibraryMockMvc
            .perform(
                patch(ENTITY_API_URL).with(csrf()).contentType("application/merge-patch+json").content(om.writeValueAsBytes(libraryDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Library in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteLibrary() throws Exception {
        // Initialize the database
        libraryRepository.saveAndFlush(library);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the library
        restLibraryMockMvc
            .perform(delete(ENTITY_API_URL_ID, library.getId()).with(csrf()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return libraryRepository.count();
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

    protected Library getPersistedLibrary(Library library) {
        return libraryRepository.findById(library.getId()).orElseThrow();
    }

    protected void assertPersistedLibraryToMatchAllProperties(Library expectedLibrary) {
        assertLibraryAllPropertiesEquals(expectedLibrary, getPersistedLibrary(expectedLibrary));
    }

    protected void assertPersistedLibraryToMatchUpdatableProperties(Library expectedLibrary) {
        assertLibraryAllUpdatablePropertiesEquals(expectedLibrary, getPersistedLibrary(expectedLibrary));
    }
}
