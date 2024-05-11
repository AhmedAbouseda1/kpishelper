package com.kpis.helper.service.mapper;

import com.kpis.helper.domain.Training;
import com.kpis.helper.service.dto.TrainingDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Training} and its DTO {@link TrainingDTO}.
 */
@Mapper(componentModel = "spring")
public interface TrainingMapper extends EntityMapper<TrainingDTO, Training> {}
