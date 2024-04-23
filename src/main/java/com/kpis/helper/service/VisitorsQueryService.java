package com.kpis.helper.service;

import com.kpis.helper.domain.*; // for static metamodels
import com.kpis.helper.domain.Visitors;
import com.kpis.helper.repository.VisitorsRepository;
import com.kpis.helper.service.criteria.VisitorsCriteria;
import com.kpis.helper.service.dto.VisitorsDTO;
import com.kpis.helper.service.mapper.VisitorsMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tech.jhipster.service.QueryService;

/**
 * Service for executing complex queries for {@link Visitors} entities in the database.
 * The main input is a {@link VisitorsCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link Page} of {@link VisitorsDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class VisitorsQueryService extends QueryService<Visitors> {

    private final Logger log = LoggerFactory.getLogger(VisitorsQueryService.class);

    private final VisitorsRepository visitorsRepository;

    private final VisitorsMapper visitorsMapper;

    public VisitorsQueryService(VisitorsRepository visitorsRepository, VisitorsMapper visitorsMapper) {
        this.visitorsRepository = visitorsRepository;
        this.visitorsMapper = visitorsMapper;
    }

    /**
     * Return a {@link Page} of {@link VisitorsDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<VisitorsDTO> findByCriteria(VisitorsCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<Visitors> specification = createSpecification(criteria);
        return visitorsRepository.findAll(specification, page).map(visitorsMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(VisitorsCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<Visitors> specification = createSpecification(criteria);
        return visitorsRepository.count(specification);
    }

    /**
     * Function to convert {@link VisitorsCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<Visitors> createSpecification(VisitorsCriteria criteria) {
        Specification<Visitors> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), Visitors_.id));
            }
            if (criteria.getTotal_visitors() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getTotal_visitors(), Visitors_.total_visitors));
            }
            if (criteria.getWebsite_visitors() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getWebsite_visitors(), Visitors_.website_visitors));
            }
            if (criteria.getRecorded_date() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getRecorded_date(), Visitors_.recorded_date));
            }
        }
        return specification;
    }
}
