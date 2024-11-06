package com.example.projectbase.domain.mapper;

import com.example.projectbase.domain.dto.*;
import com.example.projectbase.domain.dto.init.RoomInitJSON;
import com.example.projectbase.domain.entity.Room;
import com.example.projectbase.domain.entity.User;
import com.example.projectbase.projection.RoomProjection;
import com.example.projectbase.projection.StatisticRoomBookedProjection;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.Mappings;

@Mapper(componentModel = "spring")
public interface RoomMapper {

    @Mappings({
            @Mapping(target = "id", source = "room.id"),
            @Mapping(target = "createdDate", source = "room.createdDate"),
            @Mapping(target = "lastModifiedDate", source = "room.lastModifiedDate"),
            @Mapping(target = "createdBy.id", source = "createdBy.id"),
            @Mapping(target = "createdBy.firstName", source = "createdBy.firstName"),
            @Mapping(target = "createdBy.lastName", source = "createdBy.lastName"),
            @Mapping(target = "createdBy.avatar", source = "createdBy.avatar"),
            @Mapping(target = "lastModifiedBy.id", source = "lastModifiedBy.id"),
            @Mapping(target = "lastModifiedBy.firstName", source = "lastModifiedBy.firstName"),
            @Mapping(target = "lastModifiedBy.lastName", source = "lastModifiedBy.lastName"),
            @Mapping(target = "lastModifiedBy.avatar", source = "lastModifiedBy.avatar"),
    })
    RoomDTO toRoomDTO(Room room, User createdBy, User lastModifiedBy);

    Room createDtoToRoom(RoomCreateDTO createDTO);

    @Mapping(target = "medias", ignore = true)
    void updateRoomFromDTO(RoomUpdateDTO updateDTO, @MappingTarget Room room);

    RoomDTO roomProjectionToRoomDTO(RoomProjection projection);

    @Mapping(target = "isAvailable", ignore = true)
    RoomAvailableDTO roomProjectionToRoomAvailableDTO(RoomProjection projection);

    RoomSummaryDTO statisticRoomToRoomDTO(StatisticRoomBookedProjection projection);

    Room roomInitToRoom(RoomInitJSON initJSON);

}
