package com.kpis.helper.service.mapper;

import com.kpis.helper.domain.Space;
import com.kpis.helper.service.dto.SpaceDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Space} and its DTO {@link SpaceDTO}.
 */
@Mapper(componentModel = "spring")
public interface SpaceMapper extends EntityMapper<SpaceDTO, Space> {}
