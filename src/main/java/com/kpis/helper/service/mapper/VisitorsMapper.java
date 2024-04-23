package com.kpis.helper.service.mapper;

import com.kpis.helper.domain.Visitors;
import com.kpis.helper.service.dto.VisitorsDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Visitors} and its DTO {@link VisitorsDTO}.
 */
@Mapper(componentModel = "spring")
public interface VisitorsMapper extends EntityMapper<VisitorsDTO, Visitors> {}
