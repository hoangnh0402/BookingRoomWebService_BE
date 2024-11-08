package com.example.projectbase.domain.mapper;

import com.example.projectbase.domain.dto.ProductCreateDTO;
import com.example.projectbase.domain.dto.ProductDTO;
import com.example.projectbase.domain.dto.ProductUpdateDTO;
import com.example.projectbase.domain.entity.Product;
import com.example.projectbase.domain.entity.User;
import com.example.projectbase.projection.ProductProjection;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.Mappings;

@Mapper(componentModel = "spring")
public interface ProductMapper {


    @Mappings({
            @Mapping(target = "id", source = "product.id"),
            @Mapping(target = "createdDate", source = "product.createdDate"),
            @Mapping(target = "lastModifiedDate", source = "product.lastModifiedDate"),
            @Mapping(target = "createdBy.id", source = "createdBy.id"),
            @Mapping(target = "createdBy.firstName", source = "createdBy.firstName"),
            @Mapping(target = "createdBy.lastName", source = "createdBy.lastName"),
            @Mapping(target = "createdBy.avatar", source = "createdBy.avatar"),
            @Mapping(target = "lastModifiedBy.id", source = "lastModifiedBy.id"),
            @Mapping(target = "lastModifiedBy.firstName", source = "lastModifiedBy.firstName"),
            @Mapping(target = "lastModifiedBy.lastName", source = "lastModifiedBy.lastName"),
            @Mapping(target = "lastModifiedBy.avatar", source = "lastModifiedBy.avatar"),
    })
    ProductDTO toProductDTO(Product product, User createdBy, User lastModifiedBy);

    Product createDtoToProduct(ProductCreateDTO createDTO);

    @Mapping(target = "thumbnail", ignore = true)
    void updateProductFromDTO(ProductUpdateDTO updateDTO, @MappingTarget Product product);

    ProductDTO productProjectionToProductDTO(ProductProjection projection);

}
