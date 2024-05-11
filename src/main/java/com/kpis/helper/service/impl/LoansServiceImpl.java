package com.kpis.helper.service.impl;

import com.kpis.helper.domain.Loans;
import com.kpis.helper.repository.LoansRepository;
import com.kpis.helper.service.LoansService;
import com.kpis.helper.service.dto.LoansDTO;
import com.kpis.helper.service.mapper.LoansMapper;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link com.kpis.helper.domain.Loans}.
 */
@Service
@Transactional
public class LoansServiceImpl implements LoansService {

    private final Logger log = LoggerFactory.getLogger(LoansServiceImpl.class);

    private final LoansRepository loansRepository;

    private final LoansMapper loansMapper;

    public LoansServiceImpl(LoansRepository loansRepository, LoansMapper loansMapper) {
        this.loansRepository = loansRepository;
        this.loansMapper = loansMapper;
    }

    @Override
    public LoansDTO save(LoansDTO loansDTO) {
        log.debug("Request to save Loans : {}", loansDTO);
        Loans loans = loansMapper.toEntity(loansDTO);
        loans = loansRepository.save(loans);
        return loansMapper.toDto(loans);
    }

    @Override
    public LoansDTO update(LoansDTO loansDTO) {
        log.debug("Request to update Loans : {}", loansDTO);
        Loans loans = loansMapper.toEntity(loansDTO);
        loans = loansRepository.save(loans);
        return loansMapper.toDto(loans);
    }

    @Override
    public Optional<LoansDTO> partialUpdate(LoansDTO loansDTO) {
        log.debug("Request to partially update Loans : {}", loansDTO);

        return loansRepository
            .findById(loansDTO.getId())
            .map(existingLoans -> {
                loansMapper.partialUpdate(existingLoans, loansDTO);

                return existingLoans;
            })
            .map(loansRepository::save)
            .map(loansMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public List<LoansDTO> findAll() {
        log.debug("Request to get all Loans");
        return loansRepository.findAll().stream().map(loansMapper::toDto).collect(Collectors.toCollection(LinkedList::new));
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<LoansDTO> findOne(Long id) {
        log.debug("Request to get Loans : {}", id);
        return loansRepository.findById(id).map(loansMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete Loans : {}", id);
        loansRepository.deleteById(id);
    }
}
