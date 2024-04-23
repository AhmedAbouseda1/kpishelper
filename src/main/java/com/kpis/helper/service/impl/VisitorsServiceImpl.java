package com.kpis.helper.service.impl;

import com.kpis.helper.domain.Visitors;
import com.kpis.helper.repository.VisitorsRepository;
import com.kpis.helper.service.VisitorsService;
import com.kpis.helper.service.dto.VisitorsDTO;
import com.kpis.helper.service.mapper.VisitorsMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link com.kpis.helper.domain.Visitors}.
 */
@Service
@Transactional
public class VisitorsServiceImpl implements VisitorsService {

    private final Logger log = LoggerFactory.getLogger(VisitorsServiceImpl.class);

    private final VisitorsRepository visitorsRepository;

    private final VisitorsMapper visitorsMapper;

    public VisitorsServiceImpl(VisitorsRepository visitorsRepository, VisitorsMapper visitorsMapper) {
        this.visitorsRepository = visitorsRepository;
        this.visitorsMapper = visitorsMapper;
    }

    @Override
    public VisitorsDTO save(VisitorsDTO visitorsDTO) {
        log.debug("Request to save Visitors : {}", visitorsDTO);
        Visitors visitors = visitorsMapper.toEntity(visitorsDTO);
        visitors = visitorsRepository.save(visitors);
        return visitorsMapper.toDto(visitors);
    }

    @Override
    public VisitorsDTO update(VisitorsDTO visitorsDTO) {
        log.debug("Request to update Visitors : {}", visitorsDTO);
        Visitors visitors = visitorsMapper.toEntity(visitorsDTO);
        visitors = visitorsRepository.save(visitors);
        return visitorsMapper.toDto(visitors);
    }

    @Override
    public Optional<VisitorsDTO> partialUpdate(VisitorsDTO visitorsDTO) {
        log.debug("Request to partially update Visitors : {}", visitorsDTO);

        return visitorsRepository
            .findById(visitorsDTO.getId())
            .map(existingVisitors -> {
                visitorsMapper.partialUpdate(existingVisitors, visitorsDTO);

                return existingVisitors;
            })
            .map(visitorsRepository::save)
            .map(visitorsMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<VisitorsDTO> findOne(Long id) {
        log.debug("Request to get Visitors : {}", id);
        return visitorsRepository.findById(id).map(visitorsMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete Visitors : {}", id);
        visitorsRepository.deleteById(id);
    }
}
