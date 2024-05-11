package com.kpis.helper.service.impl;

import com.kpis.helper.domain.ElectronicServices;
import com.kpis.helper.repository.ElectronicServicesRepository;
import com.kpis.helper.service.ElectronicServicesService;
import com.kpis.helper.service.dto.ElectronicServicesDTO;
import com.kpis.helper.service.mapper.ElectronicServicesMapper;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link com.kpis.helper.domain.ElectronicServices}.
 */
@Service
@Transactional
public class ElectronicServicesServiceImpl implements ElectronicServicesService {

    private final Logger log = LoggerFactory.getLogger(ElectronicServicesServiceImpl.class);

    private final ElectronicServicesRepository electronicServicesRepository;

    private final ElectronicServicesMapper electronicServicesMapper;

    public ElectronicServicesServiceImpl(
        ElectronicServicesRepository electronicServicesRepository,
        ElectronicServicesMapper electronicServicesMapper
    ) {
        this.electronicServicesRepository = electronicServicesRepository;
        this.electronicServicesMapper = electronicServicesMapper;
    }

    @Override
    public ElectronicServicesDTO save(ElectronicServicesDTO electronicServicesDTO) {
        log.debug("Request to save ElectronicServices : {}", electronicServicesDTO);
        ElectronicServices electronicServices = electronicServicesMapper.toEntity(electronicServicesDTO);
        electronicServices = electronicServicesRepository.save(electronicServices);
        return electronicServicesMapper.toDto(electronicServices);
    }

    @Override
    public ElectronicServicesDTO update(ElectronicServicesDTO electronicServicesDTO) {
        log.debug("Request to update ElectronicServices : {}", electronicServicesDTO);
        ElectronicServices electronicServices = electronicServicesMapper.toEntity(electronicServicesDTO);
        electronicServices = electronicServicesRepository.save(electronicServices);
        return electronicServicesMapper.toDto(electronicServices);
    }

    @Override
    public Optional<ElectronicServicesDTO> partialUpdate(ElectronicServicesDTO electronicServicesDTO) {
        log.debug("Request to partially update ElectronicServices : {}", electronicServicesDTO);

        return electronicServicesRepository
            .findById(electronicServicesDTO.getId())
            .map(existingElectronicServices -> {
                electronicServicesMapper.partialUpdate(existingElectronicServices, electronicServicesDTO);

                return existingElectronicServices;
            })
            .map(electronicServicesRepository::save)
            .map(electronicServicesMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ElectronicServicesDTO> findAll() {
        log.debug("Request to get all ElectronicServices");
        return electronicServicesRepository
            .findAll()
            .stream()
            .map(electronicServicesMapper::toDto)
            .collect(Collectors.toCollection(LinkedList::new));
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<ElectronicServicesDTO> findOne(Long id) {
        log.debug("Request to get ElectronicServices : {}", id);
        return electronicServicesRepository.findById(id).map(electronicServicesMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete ElectronicServices : {}", id);
        electronicServicesRepository.deleteById(id);
    }
}
