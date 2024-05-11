package com.kpis.helper.service.mapper;

import com.kpis.helper.domain.Loans;
import com.kpis.helper.service.dto.LoansDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Loans} and its DTO {@link LoansDTO}.
 */
@Mapper(componentModel = "spring")
public interface LoansMapper extends EntityMapper<LoansDTO, Loans> {}
