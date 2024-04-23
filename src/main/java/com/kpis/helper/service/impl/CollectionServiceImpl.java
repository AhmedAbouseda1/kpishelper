package com.kpis.helper.service.impl;

import com.kpis.helper.domain.Collection;
import com.kpis.helper.repository.CollectionRepository;
import com.kpis.helper.service.CollectionService;
import com.kpis.helper.service.dto.CollectionDTO;
import com.kpis.helper.service.mapper.CollectionMapper;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link com.kpis.helper.domain.Collection}.
 */
@Service
@Transactional
public class CollectionServiceImpl implements CollectionService {

    private final Logger log = LoggerFactory.getLogger(CollectionServiceImpl.class);

    private final CollectionRepository collectionRepository;

    private final CollectionMapper collectionMapper;

    public CollectionServiceImpl(CollectionRepository collectionRepository, CollectionMapper collectionMapper) {
        this.collectionRepository = collectionRepository;
        this.collectionMapper = collectionMapper;
    }

    @Override
    public CollectionDTO save(CollectionDTO collectionDTO) {
        log.debug("Request to save Collection : {}", collectionDTO);
        Collection collection = collectionMapper.toEntity(collectionDTO);
        collection = collectionRepository.save(collection);
        return collectionMapper.toDto(collection);
    }

    @Override
    public CollectionDTO update(CollectionDTO collectionDTO) {
        log.debug("Request to update Collection : {}", collectionDTO);
        Collection collection = collectionMapper.toEntity(collectionDTO);
        collection = collectionRepository.save(collection);
        return collectionMapper.toDto(collection);
    }

    @Override
    public Optional<CollectionDTO> partialUpdate(CollectionDTO collectionDTO) {
        log.debug("Request to partially update Collection : {}", collectionDTO);

        return collectionRepository
            .findById(collectionDTO.getId())
            .map(existingCollection -> {
                collectionMapper.partialUpdate(existingCollection, collectionDTO);

                return existingCollection;
            })
            .map(collectionRepository::save)
            .map(collectionMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public List<CollectionDTO> findAll() {
        log.debug("Request to get all Collections");
        return collectionRepository.findAll().stream().map(collectionMapper::toDto).collect(Collectors.toCollection(LinkedList::new));
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<CollectionDTO> findOne(Long id) {
        log.debug("Request to get Collection : {}", id);
        return collectionRepository.findById(id).map(collectionMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete Collection : {}", id);
        collectionRepository.deleteById(id);
    }
}
