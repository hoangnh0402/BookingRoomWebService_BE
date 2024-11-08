package com.example.projectbase.domain.mapper;

import com.example.projectbase.domain.dto.PostCreateDTO;
import com.example.projectbase.domain.dto.PostDTO;
import com.example.projectbase.domain.dto.PostUpdateDTO;
import com.example.projectbase.domain.entity.Post;
import com.example.projectbase.domain.entity.User;
import com.example.projectbase.projection.PostProjection;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.Mappings;

@Mapper(componentModel = "spring")
public interface PostMapper {

    @Mappings({
            @Mapping(target = "id", source = "post.id"),
            @Mapping(target = "createdDate", source = "post.createdDate"),
            @Mapping(target = "lastModifiedDate", source = "post.lastModifiedDate"),
            @Mapping(target = "createdBy.id", source = "createdBy.id"),
            @Mapping(target = "createdBy.firstName", source = "createdBy.firstName"),
            @Mapping(target = "createdBy.lastName", source = "createdBy.lastName"),
            @Mapping(target = "createdBy.avatar", source = "createdBy.avatar"),
            @Mapping(target = "lastModifiedBy.id", source = "lastModifiedBy.id"),
            @Mapping(target = "lastModifiedBy.firstName", source = "lastModifiedBy.firstName"),
            @Mapping(target = "lastModifiedBy.lastName", source = "lastModifiedBy.lastName"),
            @Mapping(target = "lastModifiedBy.avatar", source = "lastModifiedBy.avatar"),
    })
    PostDTO toPostDTO(Post post, User createdBy, User lastModifiedBy);

    @Mappings({
            @Mapping(target = "createdBy", ignore = true),
            @Mapping(target = "lastModifiedBy", ignore = true)
    })
    PostDTO toPostDTO(Post post);

    Post createDtoToPost(PostCreateDTO createDTO);

    @Mapping(target = "medias", ignore = true)
    void updatePostFromDTO(PostUpdateDTO updateDTO, @MappingTarget Post post);

    PostDTO postProjectionToPostDTO(PostProjection projection);

}
