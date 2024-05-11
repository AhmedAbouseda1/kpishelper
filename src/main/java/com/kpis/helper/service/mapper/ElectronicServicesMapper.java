package com.kpis.helper.service.mapper;

import com.kpis.helper.domain.ElectronicServices;
import com.kpis.helper.service.dto.ElectronicServicesDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link ElectronicServices} and its DTO {@link ElectronicServicesDTO}.
 */
@Mapper(componentModel = "spring")
public interface ElectronicServicesMapper extends EntityMapper<ElectronicServicesDTO, ElectronicServices> {}
