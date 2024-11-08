package com.example.projectbase.domain.mapper;

import com.example.projectbase.domain.dto.ServiceCreateDTO;
import com.example.projectbase.domain.dto.ServiceDTO;
import com.example.projectbase.domain.dto.ServiceUpdateDTO;
import com.example.projectbase.domain.dto.init.ServiceInitJSON;
import com.example.projectbase.domain.entity.Service;
import com.example.projectbase.domain.entity.User;
import com.example.projectbase.projection.ServiceProjection;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.Mappings;

@Mapper(componentModel = "spring")
public interface ServiceMapper {

    @Mappings({
            @Mapping(target = "id", source = "service.id"),
            @Mapping(target = "createdDate", source = "service.createdDate"),
            @Mapping(target = "lastModifiedDate", source = "service.lastModifiedDate"),
            @Mapping(target = "createdBy.id", source = "createdBy.id"),
            @Mapping(target = "createdBy.firstName", source = "createdBy.firstName"),
            @Mapping(target = "createdBy.lastName", source = "createdBy.lastName"),
            @Mapping(target = "createdBy.avatar", source = "createdBy.avatar"),
            @Mapping(target = "lastModifiedBy.id", source = "lastModifiedBy.id"),
            @Mapping(target = "lastModifiedBy.firstName", source = "lastModifiedBy.firstName"),
            @Mapping(target = "lastModifiedBy.lastName", source = "lastModifiedBy.lastName"),
            @Mapping(target = "lastModifiedBy.avatar", source = "lastModifiedBy.avatar"),
    })
    ServiceDTO toServiceDTO(Service service, User createdBy, User lastModifiedBy);

    Service createDtoToProduct(ServiceCreateDTO createDTO);

    @Mapping(target = "thumbnail", ignore = true)
    void updateProductFromDTO(ServiceUpdateDTO updateDTO, @MappingTarget Service service);

    ServiceDTO serviceProjectionToServiceDTO(ServiceProjection projection);

    Service serviceInitToService(ServiceInitJSON initJSON);

}
