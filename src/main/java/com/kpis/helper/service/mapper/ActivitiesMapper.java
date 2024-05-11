package com.kpis.helper.service.mapper;

import com.kpis.helper.domain.Activities;
import com.kpis.helper.service.dto.ActivitiesDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Activities} and its DTO {@link ActivitiesDTO}.
 */
@Mapper(componentModel = "spring")
public interface ActivitiesMapper extends EntityMapper<ActivitiesDTO, Activities> {}
