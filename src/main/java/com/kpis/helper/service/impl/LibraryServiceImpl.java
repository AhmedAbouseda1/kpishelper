package com.kpis.helper.service.impl;

import com.kpis.helper.domain.Library;
import com.kpis.helper.repository.LibraryRepository;
import com.kpis.helper.service.LibraryService;
import com.kpis.helper.service.dto.LibraryDTO;
import com.kpis.helper.service.mapper.LibraryMapper;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link com.kpis.helper.domain.Library}.
 */
@Service
@Transactional
public class LibraryServiceImpl implements LibraryService {

    private final Logger log = LoggerFactory.getLogger(LibraryServiceImpl.class);

    private final LibraryRepository libraryRepository;

    private final LibraryMapper libraryMapper;

    public LibraryServiceImpl(LibraryRepository libraryRepository, LibraryMapper libraryMapper) {
        this.libraryRepository = libraryRepository;
        this.libraryMapper = libraryMapper;
    }

    @Override
    public LibraryDTO save(LibraryDTO libraryDTO) {
        log.debug("Request to save Library : {}", libraryDTO);
        Library library = libraryMapper.toEntity(libraryDTO);
        library = libraryRepository.save(library);
        return libraryMapper.toDto(library);
    }

    @Override
    public LibraryDTO update(LibraryDTO libraryDTO) {
        log.debug("Request to update Library : {}", libraryDTO);
        Library library = libraryMapper.toEntity(libraryDTO);
        library = libraryRepository.save(library);
        return libraryMapper.toDto(library);
    }

    @Override
    public Optional<LibraryDTO> partialUpdate(LibraryDTO libraryDTO) {
        log.debug("Request to partially update Library : {}", libraryDTO);

        return libraryRepository
            .findById(libraryDTO.getId())
            .map(existingLibrary -> {
                libraryMapper.partialUpdate(existingLibrary, libraryDTO);

                return existingLibrary;
            })
            .map(libraryRepository::save)
            .map(libraryMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public List<LibraryDTO> findAll() {
        log.debug("Request to get all Libraries");
        return libraryRepository.findAll().stream().map(libraryMapper::toDto).collect(Collectors.toCollection(LinkedList::new));
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<LibraryDTO> findOne(Long id) {
        log.debug("Request to get Library : {}", id);
        return libraryRepository.findById(id).map(libraryMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete Library : {}", id);
        libraryRepository.deleteById(id);
    }
}
