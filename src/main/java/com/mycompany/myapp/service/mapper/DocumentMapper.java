package com.mycompany.myapp.service.mapper;

import com.mycompany.myapp.domain.Document;
import com.mycompany.myapp.domain.User;
import com.mycompany.myapp.service.dto.DocumentDTO;
import com.mycompany.myapp.service.dto.UserDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Document} and its DTO {@link DocumentDTO}.
 */
@Mapper(componentModel = "spring")
public interface DocumentMapper extends EntityMapper<DocumentDTO, Document> {
    @Mapping(target = "owner", source = "owner", qualifiedByName = "userId")
    DocumentDTO toDto(Document s);

    @Named("userId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    UserDTO toDtoUserId(User user);
}
