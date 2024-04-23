package com.kpis.helper.web.rest;

import static com.kpis.helper.domain.CollectionAsserts.*;
import static com.kpis.helper.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.kpis.helper.IntegrationTest;
import com.kpis.helper.domain.Collection;
import com.kpis.helper.repository.CollectionRepository;
import com.kpis.helper.service.dto.CollectionDTO;
import com.kpis.helper.service.mapper.CollectionMapper;
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
 * Integration tests for the {@link CollectionResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class CollectionResourceIT {

    private static final LocalDate DEFAULT_DATE_RECORDED = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_DATE_RECORDED = LocalDate.now(ZoneId.systemDefault());

    private static final Integer DEFAULT_COLLECTION_SIZE = 1;
    private static final Integer UPDATED_COLLECTION_SIZE = 2;

    private static final Integer DEFAULT_NUMBER_OF_TITLES = 1;
    private static final Integer UPDATED_NUMBER_OF_TITLES = 2;

    private static final Integer DEFAULT_STOCK_FOR_PUBLIC_USAGE = 1;
    private static final Integer UPDATED_STOCK_FOR_PUBLIC_USAGE = 2;

    private static final Integer DEFAULT_TITLES_AVAILABILITY_FOR_POPULATION = 1;
    private static final Integer UPDATED_TITLES_AVAILABILITY_FOR_POPULATION = 2;

    private static final Integer DEFAULT_TITLES_AVAILABILITY_FOR_ACTIVE_MEMBERS = 1;
    private static final Integer UPDATED_TITLES_AVAILABILITY_FOR_ACTIVE_MEMBERS = 2;

    private static final String ENTITY_API_URL = "/api/collections";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private CollectionRepository collectionRepository;

    @Autowired
    private CollectionMapper collectionMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restCollectionMockMvc;

    private Collection collection;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Collection createEntity(EntityManager em) {
        Collection collection = new Collection()
            .date_recorded(DEFAULT_DATE_RECORDED)
            .collection_size(DEFAULT_COLLECTION_SIZE)
            .number_of_titles(DEFAULT_NUMBER_OF_TITLES)
            .stock_for_public_usage(DEFAULT_STOCK_FOR_PUBLIC_USAGE)
            .titles_availability_for_population(DEFAULT_TITLES_AVAILABILITY_FOR_POPULATION)
            .titles_availability_for_active_members(DEFAULT_TITLES_AVAILABILITY_FOR_ACTIVE_MEMBERS);
        return collection;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Collection createUpdatedEntity(EntityManager em) {
        Collection collection = new Collection()
            .date_recorded(UPDATED_DATE_RECORDED)
            .collection_size(UPDATED_COLLECTION_SIZE)
            .number_of_titles(UPDATED_NUMBER_OF_TITLES)
            .stock_for_public_usage(UPDATED_STOCK_FOR_PUBLIC_USAGE)
            .titles_availability_for_population(UPDATED_TITLES_AVAILABILITY_FOR_POPULATION)
            .titles_availability_for_active_members(UPDATED_TITLES_AVAILABILITY_FOR_ACTIVE_MEMBERS);
        return collection;
    }

    @BeforeEach
    public void initTest() {
        collection = createEntity(em);
    }

    @Test
    @Transactional
    void createCollection() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the Collection
        CollectionDTO collectionDTO = collectionMapper.toDto(collection);
        var returnedCollectionDTO = om.readValue(
            restCollectionMockMvc
                .perform(
                    post(ENTITY_API_URL).with(csrf()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(collectionDTO))
                )
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            CollectionDTO.class
        );

        // Validate the Collection in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedCollection = collectionMapper.toEntity(returnedCollectionDTO);
        assertCollectionUpdatableFieldsEquals(returnedCollection, getPersistedCollection(returnedCollection));
    }

    @Test
    @Transactional
    void createCollectionWithExistingId() throws Exception {
        // Create the Collection with an existing ID
        collection.setId(1L);
        CollectionDTO collectionDTO = collectionMapper.toDto(collection);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restCollectionMockMvc
            .perform(post(ENTITY_API_URL).with(csrf()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(collectionDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Collection in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkCollection_sizeIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        collection.setCollection_size(null);

        // Create the Collection, which fails.
        CollectionDTO collectionDTO = collectionMapper.toDto(collection);

        restCollectionMockMvc
            .perform(post(ENTITY_API_URL).with(csrf()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(collectionDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkNumber_of_titlesIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        collection.setNumber_of_titles(null);

        // Create the Collection, which fails.
        CollectionDTO collectionDTO = collectionMapper.toDto(collection);

        restCollectionMockMvc
            .perform(post(ENTITY_API_URL).with(csrf()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(collectionDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllCollections() throws Exception {
        // Initialize the database
        collectionRepository.saveAndFlush(collection);

        // Get all the collectionList
        restCollectionMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(collection.getId().intValue())))
            .andExpect(jsonPath("$.[*].date_recorded").value(hasItem(DEFAULT_DATE_RECORDED.toString())))
            .andExpect(jsonPath("$.[*].collection_size").value(hasItem(DEFAULT_COLLECTION_SIZE)))
            .andExpect(jsonPath("$.[*].number_of_titles").value(hasItem(DEFAULT_NUMBER_OF_TITLES)))
            .andExpect(jsonPath("$.[*].stock_for_public_usage").value(hasItem(DEFAULT_STOCK_FOR_PUBLIC_USAGE)))
            .andExpect(jsonPath("$.[*].titles_availability_for_population").value(hasItem(DEFAULT_TITLES_AVAILABILITY_FOR_POPULATION)))
            .andExpect(
                jsonPath("$.[*].titles_availability_for_active_members").value(hasItem(DEFAULT_TITLES_AVAILABILITY_FOR_ACTIVE_MEMBERS))
            );
    }

    @Test
    @Transactional
    void getCollection() throws Exception {
        // Initialize the database
        collectionRepository.saveAndFlush(collection);

        // Get the collection
        restCollectionMockMvc
            .perform(get(ENTITY_API_URL_ID, collection.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(collection.getId().intValue()))
            .andExpect(jsonPath("$.date_recorded").value(DEFAULT_DATE_RECORDED.toString()))
            .andExpect(jsonPath("$.collection_size").value(DEFAULT_COLLECTION_SIZE))
            .andExpect(jsonPath("$.number_of_titles").value(DEFAULT_NUMBER_OF_TITLES))
            .andExpect(jsonPath("$.stock_for_public_usage").value(DEFAULT_STOCK_FOR_PUBLIC_USAGE))
            .andExpect(jsonPath("$.titles_availability_for_population").value(DEFAULT_TITLES_AVAILABILITY_FOR_POPULATION))
            .andExpect(jsonPath("$.titles_availability_for_active_members").value(DEFAULT_TITLES_AVAILABILITY_FOR_ACTIVE_MEMBERS));
    }

    @Test
    @Transactional
    void getNonExistingCollection() throws Exception {
        // Get the collection
        restCollectionMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingCollection() throws Exception {
        // Initialize the database
        collectionRepository.saveAndFlush(collection);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the collection
        Collection updatedCollection = collectionRepository.findById(collection.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedCollection are not directly saved in db
        em.detach(updatedCollection);
        updatedCollection
            .date_recorded(UPDATED_DATE_RECORDED)
            .collection_size(UPDATED_COLLECTION_SIZE)
            .number_of_titles(UPDATED_NUMBER_OF_TITLES)
            .stock_for_public_usage(UPDATED_STOCK_FOR_PUBLIC_USAGE)
            .titles_availability_for_population(UPDATED_TITLES_AVAILABILITY_FOR_POPULATION)
            .titles_availability_for_active_members(UPDATED_TITLES_AVAILABILITY_FOR_ACTIVE_MEMBERS);
        CollectionDTO collectionDTO = collectionMapper.toDto(updatedCollection);

        restCollectionMockMvc
            .perform(
                put(ENTITY_API_URL_ID, collectionDTO.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(collectionDTO))
            )
            .andExpect(status().isOk());

        // Validate the Collection in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedCollectionToMatchAllProperties(updatedCollection);
    }

    @Test
    @Transactional
    void putNonExistingCollection() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        collection.setId(longCount.incrementAndGet());

        // Create the Collection
        CollectionDTO collectionDTO = collectionMapper.toDto(collection);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restCollectionMockMvc
            .perform(
                put(ENTITY_API_URL_ID, collectionDTO.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(collectionDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Collection in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchCollection() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        collection.setId(longCount.incrementAndGet());

        // Create the Collection
        CollectionDTO collectionDTO = collectionMapper.toDto(collection);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCollectionMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(collectionDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Collection in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamCollection() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        collection.setId(longCount.incrementAndGet());

        // Create the Collection
        CollectionDTO collectionDTO = collectionMapper.toDto(collection);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCollectionMockMvc
            .perform(put(ENTITY_API_URL).with(csrf()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(collectionDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Collection in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateCollectionWithPatch() throws Exception {
        // Initialize the database
        collectionRepository.saveAndFlush(collection);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the collection using partial update
        Collection partialUpdatedCollection = new Collection();
        partialUpdatedCollection.setId(collection.getId());

        partialUpdatedCollection
            .date_recorded(UPDATED_DATE_RECORDED)
            .stock_for_public_usage(UPDATED_STOCK_FOR_PUBLIC_USAGE)
            .titles_availability_for_population(UPDATED_TITLES_AVAILABILITY_FOR_POPULATION);

        restCollectionMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedCollection.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedCollection))
            )
            .andExpect(status().isOk());

        // Validate the Collection in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertCollectionUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedCollection, collection),
            getPersistedCollection(collection)
        );
    }

    @Test
    @Transactional
    void fullUpdateCollectionWithPatch() throws Exception {
        // Initialize the database
        collectionRepository.saveAndFlush(collection);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the collection using partial update
        Collection partialUpdatedCollection = new Collection();
        partialUpdatedCollection.setId(collection.getId());

        partialUpdatedCollection
            .date_recorded(UPDATED_DATE_RECORDED)
            .collection_size(UPDATED_COLLECTION_SIZE)
            .number_of_titles(UPDATED_NUMBER_OF_TITLES)
            .stock_for_public_usage(UPDATED_STOCK_FOR_PUBLIC_USAGE)
            .titles_availability_for_population(UPDATED_TITLES_AVAILABILITY_FOR_POPULATION)
            .titles_availability_for_active_members(UPDATED_TITLES_AVAILABILITY_FOR_ACTIVE_MEMBERS);

        restCollectionMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedCollection.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedCollection))
            )
            .andExpect(status().isOk());

        // Validate the Collection in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertCollectionUpdatableFieldsEquals(partialUpdatedCollection, getPersistedCollection(partialUpdatedCollection));
    }

    @Test
    @Transactional
    void patchNonExistingCollection() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        collection.setId(longCount.incrementAndGet());

        // Create the Collection
        CollectionDTO collectionDTO = collectionMapper.toDto(collection);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restCollectionMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, collectionDTO.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(collectionDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Collection in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchCollection() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        collection.setId(longCount.incrementAndGet());

        // Create the Collection
        CollectionDTO collectionDTO = collectionMapper.toDto(collection);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCollectionMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(collectionDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Collection in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamCollection() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        collection.setId(longCount.incrementAndGet());

        // Create the Collection
        CollectionDTO collectionDTO = collectionMapper.toDto(collection);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCollectionMockMvc
            .perform(
                patch(ENTITY_API_URL).with(csrf()).contentType("application/merge-patch+json").content(om.writeValueAsBytes(collectionDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Collection in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteCollection() throws Exception {
        // Initialize the database
        collectionRepository.saveAndFlush(collection);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the collection
        restCollectionMockMvc
            .perform(delete(ENTITY_API_URL_ID, collection.getId()).with(csrf()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return collectionRepository.count();
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

    protected Collection getPersistedCollection(Collection collection) {
        return collectionRepository.findById(collection.getId()).orElseThrow();
    }

    protected void assertPersistedCollectionToMatchAllProperties(Collection expectedCollection) {
        assertCollectionAllPropertiesEquals(expectedCollection, getPersistedCollection(expectedCollection));
    }

    protected void assertPersistedCollectionToMatchUpdatableProperties(Collection expectedCollection) {
        assertCollectionAllUpdatablePropertiesEquals(expectedCollection, getPersistedCollection(expectedCollection));
    }
}
