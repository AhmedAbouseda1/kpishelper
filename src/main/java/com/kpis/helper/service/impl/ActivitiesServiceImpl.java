package com.kpis.helper.service.impl;

import com.kpis.helper.domain.Activities;
import com.kpis.helper.repository.ActivitiesRepository;
import com.kpis.helper.service.ActivitiesService;
import com.kpis.helper.service.dto.ActivitiesDTO;
import com.kpis.helper.service.mapper.ActivitiesMapper;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link com.kpis.helper.domain.Activities}.
 */
@Service
@Transactional
public class ActivitiesServiceImpl implements ActivitiesService {

    private final Logger log = LoggerFactory.getLogger(ActivitiesServiceImpl.class);

    private final ActivitiesRepository activitiesRepository;

    private final ActivitiesMapper activitiesMapper;

    public ActivitiesServiceImpl(ActivitiesRepository activitiesRepository, ActivitiesMapper activitiesMapper) {
        this.activitiesRepository = activitiesRepository;
        this.activitiesMapper = activitiesMapper;
    }

    @Override
    public ActivitiesDTO save(ActivitiesDTO activitiesDTO) {
        log.debug("Request to save Activities : {}", activitiesDTO);
        Activities activities = activitiesMapper.toEntity(activitiesDTO);
        activities = activitiesRepository.save(activities);
        return activitiesMapper.toDto(activities);
    }

    @Override
    public ActivitiesDTO update(ActivitiesDTO activitiesDTO) {
        log.debug("Request to update Activities : {}", activitiesDTO);
        Activities activities = activitiesMapper.toEntity(activitiesDTO);
        activities = activitiesRepository.save(activities);
        return activitiesMapper.toDto(activities);
    }

    @Override
    public Optional<ActivitiesDTO> partialUpdate(ActivitiesDTO activitiesDTO) {
        log.debug("Request to partially update Activities : {}", activitiesDTO);

        return activitiesRepository
            .findById(activitiesDTO.getId())
            .map(existingActivities -> {
                activitiesMapper.partialUpdate(existingActivities, activitiesDTO);

                return existingActivities;
            })
            .map(activitiesRepository::save)
            .map(activitiesMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ActivitiesDTO> findAll() {
        log.debug("Request to get all Activities");
        return activitiesRepository.findAll().stream().map(activitiesMapper::toDto).collect(Collectors.toCollection(LinkedList::new));
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<ActivitiesDTO> findOne(Long id) {
        log.debug("Request to get Activities : {}", id);
        return activitiesRepository.findById(id).map(activitiesMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete Activities : {}", id);
        activitiesRepository.deleteById(id);
    }
}
