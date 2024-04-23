package com.kpis.helper.service;

import com.kpis.helper.domain.*; // for static metamodels
import com.kpis.helper.domain.Population;
import com.kpis.helper.repository.PopulationRepository;
import com.kpis.helper.service.criteria.PopulationCriteria;
import com.kpis.helper.service.dto.PopulationDTO;
import com.kpis.helper.service.mapper.PopulationMapper;
import jakarta.persistence.criteria.JoinType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tech.jhipster.service.QueryService;

/**
 * Service for executing complex queries for {@link Population} entities in the database.
 * The main input is a {@link PopulationCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link Page} of {@link PopulationDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class PopulationQueryService extends QueryService<Population> {

    private final Logger log = LoggerFactory.getLogger(PopulationQueryService.class);

    private final PopulationRepository populationRepository;

    private final PopulationMapper populationMapper;

    public PopulationQueryService(PopulationRepository populationRepository, PopulationMapper populationMapper) {
        this.populationRepository = populationRepository;
        this.populationMapper = populationMapper;
    }

    /**
     * Return a {@link Page} of {@link PopulationDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<PopulationDTO> findByCriteria(PopulationCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<Population> specification = createSpecification(criteria);
        return populationRepository.findAll(specification, page).map(populationMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(PopulationCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<Population> specification = createSpecification(criteria);
        return populationRepository.count(specification);
    }

    /**
     * Function to convert {@link PopulationCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<Population> createSpecification(PopulationCriteria criteria) {
        Specification<Population> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), Population_.id));
            }
            if (criteria.getDate_recorded() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getDate_recorded(), Population_.date_recorded));
            }
            if (criteria.getPopulation() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getPopulation(), Population_.population));
            }
            if (criteria.getActive_members() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getActive_members(), Population_.active_members));
            }
            if (criteria.getLibraryId() != null) {
                specification = specification.and(
                    buildSpecification(criteria.getLibraryId(), root -> root.join(Population_.library, JoinType.LEFT).get(Library_.id))
                );
            }
        }
        return specification;
    }
}
