package com.example.projectbase.domain.mapper;

import com.example.projectbase.domain.dto.RoomRatingCreateDTO;
import com.example.projectbase.domain.dto.RoomRatingDTO;
import com.example.projectbase.domain.dto.RoomRatingUpdateDTO;
import com.example.projectbase.domain.entity.RoomRating;
import com.example.projectbase.domain.entity.User;
import com.example.projectbase.projection.RoomRatingProjection;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.Mappings;

@Mapper(componentModel = "spring")
public interface RoomRatingMapper {

    @Mappings({
            @Mapping(target = "id", source = "roomRating.id"),
            @Mapping(target = "createdDate", source = "roomRating.createdDate"),
            @Mapping(target = "lastModifiedDate", source = "roomRating.lastModifiedDate"),
            @Mapping(target = "roomId", source = "roomRating.room.id"),
            @Mapping(target = "createdBy.id", source = "createdBy.id"),
            @Mapping(target = "createdBy.firstName", source = "createdBy.firstName"),
            @Mapping(target = "createdBy.lastName", source = "createdBy.lastName"),
            @Mapping(target = "createdBy.avatar", source = "createdBy.avatar"),
            @Mapping(target = "lastModifiedBy.id", source = "lastModifiedBy.id"),
            @Mapping(target = "lastModifiedBy.firstName", source = "lastModifiedBy.firstName"),
            @Mapping(target = "lastModifiedBy.lastName", source = "lastModifiedBy.lastName"),
            @Mapping(target = "lastModifiedBy.avatar", source = "lastModifiedBy.avatar"),
    })
    RoomRatingDTO toRoomRatingDTO(RoomRating roomRating, User createdBy, User lastModifiedBy);

    RoomRating createDtoToRoomRating(RoomRatingCreateDTO createDTO);

    void updateRoomRatingFromDTO(RoomRatingUpdateDTO updateDTO, @MappingTarget RoomRating roomRating);

    RoomRatingDTO roomRatingProjectionToRoomRatingDTO(RoomRatingProjection projection);

}
