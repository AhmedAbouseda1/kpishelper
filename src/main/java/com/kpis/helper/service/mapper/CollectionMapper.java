package com.kpis.helper.service.mapper;

import com.kpis.helper.domain.Collection;
import com.kpis.helper.domain.Library;
import com.kpis.helper.service.dto.CollectionDTO;
import com.kpis.helper.service.dto.LibraryDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Collection} and its DTO {@link CollectionDTO}.
 */
@Mapper(componentModel = "spring")
public interface CollectionMapper extends EntityMapper<CollectionDTO, Collection> {
    @Mapping(target = "library", source = "library", qualifiedByName = "libraryId")
    CollectionDTO toDto(Collection s);

    @Named("libraryId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    LibraryDTO toDtoLibraryId(Library library);
}
