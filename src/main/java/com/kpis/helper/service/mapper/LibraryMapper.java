package com.kpis.helper.service.mapper;

import com.kpis.helper.domain.Library;
import com.kpis.helper.service.dto.LibraryDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Library} and its DTO {@link LibraryDTO}.
 */
@Mapper(componentModel = "spring")
public interface LibraryMapper extends EntityMapper<LibraryDTO, Library> {}
