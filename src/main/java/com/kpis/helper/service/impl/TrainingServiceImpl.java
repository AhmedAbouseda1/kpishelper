package com.kpis.helper.service.impl;

import com.kpis.helper.domain.Training;
import com.kpis.helper.repository.TrainingRepository;
import com.kpis.helper.service.TrainingService;
import com.kpis.helper.service.dto.TrainingDTO;
import com.kpis.helper.service.mapper.TrainingMapper;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link com.kpis.helper.domain.Training}.
 */
@Service
@Transactional
public class TrainingServiceImpl implements TrainingService {

    private final Logger log = LoggerFactory.getLogger(TrainingServiceImpl.class);

    private final TrainingRepository trainingRepository;

    private final TrainingMapper trainingMapper;

    public TrainingServiceImpl(TrainingRepository trainingRepository, TrainingMapper trainingMapper) {
        this.trainingRepository = trainingRepository;
        this.trainingMapper = trainingMapper;
    }

    @Override
    public TrainingDTO save(TrainingDTO trainingDTO) {
        log.debug("Request to save Training : {}", trainingDTO);
        Training training = trainingMapper.toEntity(trainingDTO);
        training = trainingRepository.save(training);
        return trainingMapper.toDto(training);
    }

    @Override
    public TrainingDTO update(TrainingDTO trainingDTO) {
        log.debug("Request to update Training : {}", trainingDTO);
        Training training = trainingMapper.toEntity(trainingDTO);
        training = trainingRepository.save(training);
        return trainingMapper.toDto(training);
    }

    @Override
    public Optional<TrainingDTO> partialUpdate(TrainingDTO trainingDTO) {
        log.debug("Request to partially update Training : {}", trainingDTO);

        return trainingRepository
            .findById(trainingDTO.getId())
            .map(existingTraining -> {
                trainingMapper.partialUpdate(existingTraining, trainingDTO);

                return existingTraining;
            })
            .map(trainingRepository::save)
            .map(trainingMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public List<TrainingDTO> findAll() {
        log.debug("Request to get all Trainings");
        return trainingRepository.findAll().stream().map(trainingMapper::toDto).collect(Collectors.toCollection(LinkedList::new));
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<TrainingDTO> findOne(Long id) {
        log.debug("Request to get Training : {}", id);
        return trainingRepository.findById(id).map(trainingMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete Training : {}", id);
        trainingRepository.deleteById(id);
    }
}
