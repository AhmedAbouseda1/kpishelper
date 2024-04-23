package com.kpis.helper.service.mapper;

import com.kpis.helper.domain.Library;
import com.kpis.helper.domain.Population;
import com.kpis.helper.service.dto.LibraryDTO;
import com.kpis.helper.service.dto.PopulationDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Population} and its DTO {@link PopulationDTO}.
 */
@Mapper(componentModel = "spring")
public interface PopulationMapper extends EntityMapper<PopulationDTO, Population> {
    @Mapping(target = "library", source = "library", qualifiedByName = "libraryId")
    PopulationDTO toDto(Population s);

    @Named("libraryId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    LibraryDTO toDtoLibraryId(Library library);
}
