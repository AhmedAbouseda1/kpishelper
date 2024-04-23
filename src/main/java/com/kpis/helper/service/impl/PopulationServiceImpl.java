package com.kpis.helper.service.impl;

import com.kpis.helper.domain.Population;
import com.kpis.helper.repository.PopulationRepository;
import com.kpis.helper.service.PopulationService;
import com.kpis.helper.service.dto.PopulationDTO;
import com.kpis.helper.service.mapper.PopulationMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link com.kpis.helper.domain.Population}.
 */
@Service
@Transactional
public class PopulationServiceImpl implements PopulationService {

    private final Logger log = LoggerFactory.getLogger(PopulationServiceImpl.class);

    private final PopulationRepository populationRepository;

    private final PopulationMapper populationMapper;

    public PopulationServiceImpl(PopulationRepository populationRepository, PopulationMapper populationMapper) {
        this.populationRepository = populationRepository;
        this.populationMapper = populationMapper;
    }

    @Override
    public PopulationDTO save(PopulationDTO populationDTO) {
        log.debug("Request to save Population : {}", populationDTO);
        Population population = populationMapper.toEntity(populationDTO);
        population = populationRepository.save(population);
        return populationMapper.toDto(population);
    }

    @Override
    public PopulationDTO update(PopulationDTO populationDTO) {
        log.debug("Request to update Population : {}", populationDTO);
        Population population = populationMapper.toEntity(populationDTO);
        population = populationRepository.save(population);
        return populationMapper.toDto(population);
    }

    @Override
    public Optional<PopulationDTO> partialUpdate(PopulationDTO populationDTO) {
        log.debug("Request to partially update Population : {}", populationDTO);

        return populationRepository
            .findById(populationDTO.getId())
            .map(existingPopulation -> {
                populationMapper.partialUpdate(existingPopulation, populationDTO);

                return existingPopulation;
            })
            .map(populationRepository::save)
            .map(populationMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<PopulationDTO> findOne(Long id) {
        log.debug("Request to get Population : {}", id);
        return populationRepository.findById(id).map(populationMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete Population : {}", id);
        populationRepository.deleteById(id);
    }
}
