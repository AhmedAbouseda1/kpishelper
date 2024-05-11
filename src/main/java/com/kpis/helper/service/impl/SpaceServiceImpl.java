package com.kpis.helper.service.impl;

import com.kpis.helper.domain.Space;
import com.kpis.helper.repository.SpaceRepository;
import com.kpis.helper.service.SpaceService;
import com.kpis.helper.service.dto.SpaceDTO;
import com.kpis.helper.service.mapper.SpaceMapper;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link com.kpis.helper.domain.Space}.
 */
@Service
@Transactional
public class SpaceServiceImpl implements SpaceService {

    private final Logger log = LoggerFactory.getLogger(SpaceServiceImpl.class);

    private final SpaceRepository spaceRepository;

    private final SpaceMapper spaceMapper;

    public SpaceServiceImpl(SpaceRepository spaceRepository, SpaceMapper spaceMapper) {
        this.spaceRepository = spaceRepository;
        this.spaceMapper = spaceMapper;
    }

    @Override
    public SpaceDTO save(SpaceDTO spaceDTO) {
        log.debug("Request to save Space : {}", spaceDTO);
        Space space = spaceMapper.toEntity(spaceDTO);
        space = spaceRepository.save(space);
        return spaceMapper.toDto(space);
    }

    @Override
    public SpaceDTO update(SpaceDTO spaceDTO) {
        log.debug("Request to update Space : {}", spaceDTO);
        Space space = spaceMapper.toEntity(spaceDTO);
        space = spaceRepository.save(space);
        return spaceMapper.toDto(space);
    }

    @Override
    public Optional<SpaceDTO> partialUpdate(SpaceDTO spaceDTO) {
        log.debug("Request to partially update Space : {}", spaceDTO);

        return spaceRepository
            .findById(spaceDTO.getId())
            .map(existingSpace -> {
                spaceMapper.partialUpdate(existingSpace, spaceDTO);

                return existingSpace;
            })
            .map(spaceRepository::save)
            .map(spaceMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public List<SpaceDTO> findAll() {
        log.debug("Request to get all Spaces");
        return spaceRepository.findAll().stream().map(spaceMapper::toDto).collect(Collectors.toCollection(LinkedList::new));
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<SpaceDTO> findOne(Long id) {
        log.debug("Request to get Space : {}", id);
        return spaceRepository.findById(id).map(spaceMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete Space : {}", id);
        spaceRepository.deleteById(id);
    }
}
