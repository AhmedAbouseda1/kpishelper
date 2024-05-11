package com.kpis.helper.web.rest;

import static com.kpis.helper.domain.LoansAsserts.*;
import static com.kpis.helper.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.kpis.helper.IntegrationTest;
import com.kpis.helper.domain.Loans;
import com.kpis.helper.repository.LoansRepository;
import com.kpis.helper.service.dto.LoansDTO;
import com.kpis.helper.service.mapper.LoansMapper;
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
 * Integration tests for the {@link LoansResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class LoansResourceIT {

    private static final LocalDate DEFAULT_RECORDED_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_RECORDED_DATE = LocalDate.now(ZoneId.systemDefault());

    private static final Integer DEFAULT_TOTAL_ITEMS_BORROWED = 1;
    private static final Integer UPDATED_TOTAL_ITEMS_BORROWED = 2;

    private static final Long DEFAULT_TURNOVER_RATE = 1L;
    private static final Long UPDATED_TURNOVER_RATE = 2L;

    private static final Integer DEFAULT_MEDIA_BORROWED_AT_LEAST_ONCE_PERCENTAGE = 1;
    private static final Integer UPDATED_MEDIA_BORROWED_AT_LEAST_ONCE_PERCENTAGE = 2;

    private static final String ENTITY_API_URL = "/api/loans";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private LoansRepository loansRepository;

    @Autowired
    private LoansMapper loansMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restLoansMockMvc;

    private Loans loans;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Loans createEntity(EntityManager em) {
        Loans loans = new Loans()
            .recorded_date(DEFAULT_RECORDED_DATE)
            .total_items_borrowed(DEFAULT_TOTAL_ITEMS_BORROWED)
            .turnover_rate(DEFAULT_TURNOVER_RATE)
            .media_borrowed_at_least_once_percentage(DEFAULT_MEDIA_BORROWED_AT_LEAST_ONCE_PERCENTAGE);
        return loans;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Loans createUpdatedEntity(EntityManager em) {
        Loans loans = new Loans()
            .recorded_date(UPDATED_RECORDED_DATE)
            .total_items_borrowed(UPDATED_TOTAL_ITEMS_BORROWED)
            .turnover_rate(UPDATED_TURNOVER_RATE)
            .media_borrowed_at_least_once_percentage(UPDATED_MEDIA_BORROWED_AT_LEAST_ONCE_PERCENTAGE);
        return loans;
    }

    @BeforeEach
    public void initTest() {
        loans = createEntity(em);
    }

    @Test
    @Transactional
    void createLoans() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the Loans
        LoansDTO loansDTO = loansMapper.toDto(loans);
        var returnedLoansDTO = om.readValue(
            restLoansMockMvc
                .perform(post(ENTITY_API_URL).with(csrf()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(loansDTO)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            LoansDTO.class
        );

        // Validate the Loans in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedLoans = loansMapper.toEntity(returnedLoansDTO);
        assertLoansUpdatableFieldsEquals(returnedLoans, getPersistedLoans(returnedLoans));
    }

    @Test
    @Transactional
    void createLoansWithExistingId() throws Exception {
        // Create the Loans with an existing ID
        loans.setId(1L);
        LoansDTO loansDTO = loansMapper.toDto(loans);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restLoansMockMvc
            .perform(post(ENTITY_API_URL).with(csrf()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(loansDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Loans in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkRecorded_dateIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        loans.setRecorded_date(null);

        // Create the Loans, which fails.
        LoansDTO loansDTO = loansMapper.toDto(loans);

        restLoansMockMvc
            .perform(post(ENTITY_API_URL).with(csrf()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(loansDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllLoans() throws Exception {
        // Initialize the database
        loansRepository.saveAndFlush(loans);

        // Get all the loansList
        restLoansMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(loans.getId().intValue())))
            .andExpect(jsonPath("$.[*].recorded_date").value(hasItem(DEFAULT_RECORDED_DATE.toString())))
            .andExpect(jsonPath("$.[*].total_items_borrowed").value(hasItem(DEFAULT_TOTAL_ITEMS_BORROWED)))
            .andExpect(jsonPath("$.[*].turnover_rate").value(hasItem(DEFAULT_TURNOVER_RATE.intValue())))
            .andExpect(
                jsonPath("$.[*].media_borrowed_at_least_once_percentage").value(hasItem(DEFAULT_MEDIA_BORROWED_AT_LEAST_ONCE_PERCENTAGE))
            );
    }

    @Test
    @Transactional
    void getLoans() throws Exception {
        // Initialize the database
        loansRepository.saveAndFlush(loans);

        // Get the loans
        restLoansMockMvc
            .perform(get(ENTITY_API_URL_ID, loans.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(loans.getId().intValue()))
            .andExpect(jsonPath("$.recorded_date").value(DEFAULT_RECORDED_DATE.toString()))
            .andExpect(jsonPath("$.total_items_borrowed").value(DEFAULT_TOTAL_ITEMS_BORROWED))
            .andExpect(jsonPath("$.turnover_rate").value(DEFAULT_TURNOVER_RATE.intValue()))
            .andExpect(jsonPath("$.media_borrowed_at_least_once_percentage").value(DEFAULT_MEDIA_BORROWED_AT_LEAST_ONCE_PERCENTAGE));
    }

    @Test
    @Transactional
    void getNonExistingLoans() throws Exception {
        // Get the loans
        restLoansMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingLoans() throws Exception {
        // Initialize the database
        loansRepository.saveAndFlush(loans);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the loans
        Loans updatedLoans = loansRepository.findById(loans.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedLoans are not directly saved in db
        em.detach(updatedLoans);
        updatedLoans
            .recorded_date(UPDATED_RECORDED_DATE)
            .total_items_borrowed(UPDATED_TOTAL_ITEMS_BORROWED)
            .turnover_rate(UPDATED_TURNOVER_RATE)
            .media_borrowed_at_least_once_percentage(UPDATED_MEDIA_BORROWED_AT_LEAST_ONCE_PERCENTAGE);
        LoansDTO loansDTO = loansMapper.toDto(updatedLoans);

        restLoansMockMvc
            .perform(
                put(ENTITY_API_URL_ID, loansDTO.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(loansDTO))
            )
            .andExpect(status().isOk());

        // Validate the Loans in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedLoansToMatchAllProperties(updatedLoans);
    }

    @Test
    @Transactional
    void putNonExistingLoans() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        loans.setId(longCount.incrementAndGet());

        // Create the Loans
        LoansDTO loansDTO = loansMapper.toDto(loans);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restLoansMockMvc
            .perform(
                put(ENTITY_API_URL_ID, loansDTO.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(loansDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Loans in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchLoans() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        loans.setId(longCount.incrementAndGet());

        // Create the Loans
        LoansDTO loansDTO = loansMapper.toDto(loans);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restLoansMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(loansDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Loans in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamLoans() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        loans.setId(longCount.incrementAndGet());

        // Create the Loans
        LoansDTO loansDTO = loansMapper.toDto(loans);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restLoansMockMvc
            .perform(put(ENTITY_API_URL).with(csrf()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(loansDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Loans in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateLoansWithPatch() throws Exception {
        // Initialize the database
        loansRepository.saveAndFlush(loans);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the loans using partial update
        Loans partialUpdatedLoans = new Loans();
        partialUpdatedLoans.setId(loans.getId());

        partialUpdatedLoans.media_borrowed_at_least_once_percentage(UPDATED_MEDIA_BORROWED_AT_LEAST_ONCE_PERCENTAGE);

        restLoansMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedLoans.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedLoans))
            )
            .andExpect(status().isOk());

        // Validate the Loans in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertLoansUpdatableFieldsEquals(createUpdateProxyForBean(partialUpdatedLoans, loans), getPersistedLoans(loans));
    }

    @Test
    @Transactional
    void fullUpdateLoansWithPatch() throws Exception {
        // Initialize the database
        loansRepository.saveAndFlush(loans);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the loans using partial update
        Loans partialUpdatedLoans = new Loans();
        partialUpdatedLoans.setId(loans.getId());

        partialUpdatedLoans
            .recorded_date(UPDATED_RECORDED_DATE)
            .total_items_borrowed(UPDATED_TOTAL_ITEMS_BORROWED)
            .turnover_rate(UPDATED_TURNOVER_RATE)
            .media_borrowed_at_least_once_percentage(UPDATED_MEDIA_BORROWED_AT_LEAST_ONCE_PERCENTAGE);

        restLoansMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedLoans.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedLoans))
            )
            .andExpect(status().isOk());

        // Validate the Loans in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertLoansUpdatableFieldsEquals(partialUpdatedLoans, getPersistedLoans(partialUpdatedLoans));
    }

    @Test
    @Transactional
    void patchNonExistingLoans() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        loans.setId(longCount.incrementAndGet());

        // Create the Loans
        LoansDTO loansDTO = loansMapper.toDto(loans);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restLoansMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, loansDTO.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(loansDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Loans in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchLoans() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        loans.setId(longCount.incrementAndGet());

        // Create the Loans
        LoansDTO loansDTO = loansMapper.toDto(loans);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restLoansMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(loansDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Loans in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamLoans() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        loans.setId(longCount.incrementAndGet());

        // Create the Loans
        LoansDTO loansDTO = loansMapper.toDto(loans);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restLoansMockMvc
            .perform(patch(ENTITY_API_URL).with(csrf()).contentType("application/merge-patch+json").content(om.writeValueAsBytes(loansDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Loans in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteLoans() throws Exception {
        // Initialize the database
        loansRepository.saveAndFlush(loans);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the loans
        restLoansMockMvc
            .perform(delete(ENTITY_API_URL_ID, loans.getId()).with(csrf()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return loansRepository.count();
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

    protected Loans getPersistedLoans(Loans loans) {
        return loansRepository.findById(loans.getId()).orElseThrow();
    }

    protected void assertPersistedLoansToMatchAllProperties(Loans expectedLoans) {
        assertLoansAllPropertiesEquals(expectedLoans, getPersistedLoans(expectedLoans));
    }

    protected void assertPersistedLoansToMatchUpdatableProperties(Loans expectedLoans) {
        assertLoansAllUpdatablePropertiesEquals(expectedLoans, getPersistedLoans(expectedLoans));
    }
}
