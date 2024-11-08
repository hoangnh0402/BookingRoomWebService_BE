package com.example.projectbase.domain.mapper;

import com.example.projectbase.domain.dto.BookingDTO;
import com.example.projectbase.domain.dto.BookingRoomDetailDTO;
import com.example.projectbase.domain.dto.BookingServiceDetailDTO;
import com.example.projectbase.domain.dto.BookingUpdateDTO;
import com.example.projectbase.domain.entity.Booking;
import com.example.projectbase.domain.entity.BookingRoomDetail;
import com.example.projectbase.domain.entity.BookingServiceDetail;
import com.example.projectbase.projection.BookingProjection;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.Mappings;

import java.util.List;
import java.util.Set;

@Mapper(componentModel = "spring")
public interface BookingMapper {

    Booking toBookingCreated(Booking booking);

    @Mappings({
            @Mapping(target = "totalRoomPrice", ignore = true),
            @Mapping(target = "rooms", ignore = true),
            @Mapping(target = "totalServicePrice", ignore = true),
            @Mapping(target = "services", ignore = true),
            @Mapping(target = "surcharges", ignore = true),
            @Mapping(target = "user", ignore = true),
            @Mapping(target = "createdBy", ignore = true),
            @Mapping(target = "lastModifiedBy", ignore = true)
    })
    BookingDTO toBookingDTO(Booking booking);

    @Mappings({
            @Mapping(target = "totalRoomPrice", ignore = true),
            @Mapping(target = "rooms", ignore = true),
            @Mapping(target = "totalServicePrice", ignore = true),
            @Mapping(target = "services", ignore = true),
            @Mapping(target = "surcharges", ignore = true)
    })
    BookingDTO toBookingDTO(BookingProjection booking);

    @Mappings({
            @Mapping(target = "id", source = "bookingRoomDetail.room.id"),
            @Mapping(target = "name", source = "bookingRoomDetail.room.name"),
            @Mapping(target = "type", source = "bookingRoomDetail.room.type"),
            @Mapping(target = "bed", source = "bookingRoomDetail.room.bed"),
            @Mapping(target = "size", source = "bookingRoomDetail.room.size"),
            @Mapping(target = "capacity", source = "bookingRoomDetail.room.capacity"),
            @Mapping(target = "services", source = "bookingRoomDetail.room.services"),
            @Mapping(target = "description", source = "bookingRoomDetail.room.description"),
            @Mapping(target = "price", source = "bookingRoomDetail.price"),
            @Mapping(target = "salePercent", source = "bookingRoomDetail.salePercent"),
            @Mapping(target = "medias", source = "bookingRoomDetail.room.medias")
    })
    BookingRoomDetailDTO toBookingRoomDetailDTO(BookingRoomDetail bookingRoomDetail);

    List<BookingRoomDetailDTO> toBookingRoomDetailDTOs(Set<BookingRoomDetail> bookingRoomDetails);

    @Mappings({
            @Mapping(target = "service.id", source = "bookingServiceDetail.service.id"),
            @Mapping(target = "service.title", source = "bookingServiceDetail.service.title"),
            @Mapping(target = "service.thumbnail", source = "bookingServiceDetail.service.thumbnail"),
            @Mapping(target = "service.price", source = "bookingServiceDetail.price"),
            @Mapping(target = "amount", source = "bookingServiceDetail.amount")
    })
    BookingServiceDetailDTO toBookingServiceDetailDTO(BookingServiceDetail bookingServiceDetail);

    List<BookingServiceDetailDTO> toBookingServiceDetailDTOs(Set<BookingServiceDetail> bookingServiceDetails);

    void updateBookingFromDTO(BookingUpdateDTO updateDTO, @MappingTarget Booking booking);

}
